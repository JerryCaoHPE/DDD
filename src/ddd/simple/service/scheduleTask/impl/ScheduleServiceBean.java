package ddd.simple.service.scheduleTask.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.SessionFactory;
import ddd.base.util.SpringContextUtil;
import ddd.simple.dao.scheduleTask.ScheduleTaskDao;
import ddd.simple.entity.scheduleTask.ScheduleTask;
import ddd.simple.service.scheduleTask.ScheduleService;
import ddd.simple.service.scheduleTask.ScheduleTaskService;
@Service
public class ScheduleServiceBean implements ScheduleService {

	@Resource(name="scheduleTaskDaoBean")
	private ScheduleTaskDao scheduleTaskDao;
	
	private static Thread currentThread;
	
	private static Queue<ScheduleTask> taskQueue = new PriorityBlockingQueue<ScheduleTask>(10,new Comparator<ScheduleTask>() {
		public int compare(ScheduleTask s1, ScheduleTask s2) {
			if(s1.getNextStartTime().before(s2.getNextStartTime())){
				return -1;
			}else if(s1.getNextStartTime().after(s2.getNextStartTime())){
				return 1;
			}
			return 0;
		};
	});
	
	private static ExecutorService executorService =  Executors.newCachedThreadPool();

	/**
	 * 维护过期执行的任务的下次启动时间 让他的下次启动时间跟原本正确执行的下次时间相等
	 * 
	 * @param task
	 * @param now
	 */
	public void dealNextTime(ScheduleTask task, Date now) {
		boolean needAdd = true;
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(now);

		GregorianCalendar calendar1 = new GregorianCalendar();
		calendar1.setTime(task.getStartTime());

		String type = task.getScheduledTaskType();

		task.setLastStartTime(now);
		
		if(type.equals("startWithServer")){
			needAdd=false;
		}else if(type.equals("oneTime")){
			task.setState("停用");
			needAdd = false;
		}else if (type.equals("everyDay")) {
			if (now.before(task.getStartTime())) {
				
			}
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.get(Calendar.DAY_OF_MONTH) + 1);
			calendar.set(Calendar.HOUR_OF_DAY,
					calendar1.get(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar1.get(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar1.get(Calendar.SECOND));

		} else if (type.equals("everyWeek")) {
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.get(Calendar.DAY_OF_MONTH) + 7);
			calendar.set(Calendar.HOUR_OF_DAY,
					calendar1.get(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar1.get(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar1.get(Calendar.SECOND));
		} else if (type.equals("everyMonth")) {
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
			calendar.set(Calendar.HOUR_OF_DAY,
					calendar1.get(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar1.get(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar1.get(Calendar.SECOND));
		}else if(type.equals("period")){
			String periodUnit=task.getPeriodUnit();
			int period=task.getPeriod();
			if(periodUnit.equals("秒")){
				calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND)+period);
			}else if(periodUnit.equals("分钟")){
				calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+period);
				calendar.set(Calendar.SECOND, calendar1.get(Calendar.SECOND));
			}else if(periodUnit.equals("小时")){
				calendar.set(Calendar.HOUR_OF_DAY,
						calendar.get(Calendar.HOUR_OF_DAY)+period);
				calendar.set(Calendar.MINUTE, calendar1.get(Calendar.MINUTE));
				calendar.set(Calendar.SECOND, calendar1.get(Calendar.SECOND));
			}else if(periodUnit.equals("周")){
				calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+(period*7));
//				calendar.set(Calendar.MONTH, calendar1.get(Calendar.MONTH));
				calendar.set(Calendar.HOUR_OF_DAY,
						calendar1.get(Calendar.HOUR_OF_DAY));
				calendar.set(Calendar.MINUTE, calendar1.get(Calendar.MINUTE));
				calendar.set(Calendar.SECOND, calendar1.get(Calendar.SECOND));
			}else if(periodUnit.equals("天")){
				calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+period);
//				calendar.set(Calendar.MONTH, calendar1.get(Calendar.MONTH));
				calendar.set(Calendar.HOUR_OF_DAY,
						calendar1.get(Calendar.HOUR_OF_DAY));
				calendar.set(Calendar.MINUTE, calendar1.get(Calendar.MINUTE));
				calendar.set(Calendar.SECOND, calendar1.get(Calendar.SECOND));
			}
		}
		task.setNextStartTime(calendar.getTime());
		if(needAdd){
			taskQueue.add(task);
		}
	}
	/**
	 * 停用过期任务
	 */
	public void stopScheduleTask(final ScheduleTask task) {
		
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				task.setState("停用");
				updateScheduleTask(task);
			}
		});
	}
	/**
	 * 打开bat类型的文件 或执行window命令
	 */
	public void openBatFile(final ScheduleTask task) {
		
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				String filePath = task.getScheduleTaskPath();
				Process process; // 创建一个本机进程
				try {
					// 执行命令
					process = Runtime.getRuntime().exec(filePath);
					// 取得命令结果的输出流
					InputStream fis = process.getInputStream();
					// 用一个读输出流类去读
					InputStreamReader isr = new InputStreamReader(fis,"gbk");
					// 用缓冲器读行
					BufferedReader br = new BufferedReader(isr);
					String line = null;
					// 直到读完为止
					while ((line = br.readLine()) != null) {
						System.out.println(line); 
					}
					task.setExecuteResult("成功执行");

				} catch (IOException e) {
					task.setExecuteResult("执行失败");
					//this.logService.logging("计划任务执行失败", e.toString());
					e.printStackTrace();
				}
				updateScheduleTask(task);
			}
		});
	}

	/**
	 * 打开一个Exe文件
	 * 
	 */
	public void openExeFile(final ScheduleTask task) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				String filePath = task.getScheduleTaskPath();
				try {
					Runtime.getRuntime().exec(filePath);
					task.setExecuteResult("成功执行");
				} catch (Exception e) {
					task.setExecuteResult("执行失败");
					//this.logService.logging("计划任务执行失败", e.toString());
					e.printStackTrace();
				}
				updateScheduleTask(task);
			}
		});
	}

	public void openMethod(ScheduleTask task) throws InstantiationException,
			IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		String methodPath = task.getScheduleTaskPath();

		int classIndex = methodPath.lastIndexOf(".");

		int methodIndex = methodPath.indexOf("(");

		String _className = methodPath.substring(0, classIndex);
		
		int beanIndex = _className.lastIndexOf(".");
		
		String _beanName = _className.substring(beanIndex+1,_className.length());
		_beanName = _beanName.substring(0, 1).toLowerCase() + _beanName.substring(1);

		String _methodName = methodPath.substring(classIndex + 1, methodIndex);

		String paramater = methodPath.substring(methodIndex + 1,
				methodPath.length() - 1);
		
		if(paramater.length() > 0)
		{
			paramater = paramater.substring(1, paramater.length() - 1);
		}
		Object paramaters[] = this.split(paramater, ",");
		int paraCount = paramaters.length;
		
		Object obj;
			if(SpringContextUtil.containsBean(_beanName)){
				obj = SpringContextUtil.getBean(_beanName);
			}
			else{
				Class<?> _class;
				_class = Class.forName(_className);
				obj = _class.newInstance();
			}
			if (paraCount == 0) {
				Method _method = obj.getClass().getDeclaredMethod(_methodName);
				_method.invoke(obj);
				task.setExecuteResult("成功执行");
			} else {
				Method _method = null;
				Method methods[] = obj.getClass().getMethods();
				for (int i = 0; i < methods.length; i++) {

					if (methods[i].getName().equals(_methodName)
							&& methods[i].getParameterTypes().length == paraCount) {
						_method = methods[i];
						break;
					} 

				}
				_method.invoke(obj, paramaters);
				task.setExecuteResult("成功执行");
			}
		
	}

	public String[] split(String str, String sign) {
		String[] strData = null;
		StringTokenizer st1 = new StringTokenizer(str, sign);
		strData = new String[st1.countTokens()];
		int i = 0;
		while (st1.hasMoreTokens()) {
			strData[i] = st1.nextToken().trim();
			i++;
		}
		return strData;
	}

	public void openScheduleTask(final ScheduleTask task) {

		if (task.getFileType().equals("exe")) {
			this.openExeFile(task);
		} else if (task.getFileType().equals("bat")) {
			this.openBatFile(task);
		} else if (task.getFileType().equals("method")) {
			
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					try {
						openMethod(task);
						task.setExecuteResult("成功执行");
					} catch (Exception e) {
						e.printStackTrace();
						task.setExecuteResult("执行失败");
						throw new DDDException("openScheduleTask", e.getMessage(), e);
					}
					updateScheduleTask(task);
				}
			});
		}
	}

	public boolean compareTime(Date time1, Date time2) // 比较两个时间
	{
		GregorianCalendar calendar1 = new GregorianCalendar();
		GregorianCalendar calendar2 = new GregorianCalendar();
		calendar1.setTime(time1);
		calendar2.setTime(time2);

		int month1 = calendar1.get(Calendar.MONTH);// 月份
		int month2 = calendar2.get(Calendar.MONTH);

		int dayOfMonth1 = calendar1.get(Calendar.DAY_OF_MONTH);// 日期
		int dayOfMonth2 = calendar2.get(Calendar.DAY_OF_MONTH);

		int hour1 = calendar1.get(Calendar.HOUR_OF_DAY);// 时
		int hour2 = calendar2.get(Calendar.HOUR_OF_DAY);

		int minute1 = calendar1.get(Calendar.MINUTE);// 分
		int minute2 = calendar2.get(Calendar.MINUTE);

		if ((month1 == month2) && (dayOfMonth1 == dayOfMonth2)
				&& (hour1 == hour2) && (minute1 == minute2))

		{

			return true;

		} else {
			return false;
		}
	}

	@Override
	public void executeScheduleTask() {
		currentThread = Thread.currentThread();
		rebuildScheduleTask();
		ScheduleTask task;
		Date now;
		while(true){
			while((task = taskQueue.peek()) == null){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			now = new Date();
			
			if(task.getEndTime()!=null && task.getEndTime().before(now) || task.getState().equals("停用")){
				taskQueue.poll();
				this.stopScheduleTask(task);
			}else{
				long sleepTime = task.getNextStartTime().getTime() - now.getTime();
				if(sleepTime <= 0){
					taskQueue.poll();	
					this.openScheduleTask(task);
					this.dealNextTime(task, now);
				}else{
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						
					}
				}
			}
		}
		
	}
	private void updateScheduleTask(ScheduleTask task){
		try {
			scheduleTaskDao.updateScheduleTask(task);
			SessionFactory.getThreadSession().commitTransaction();
		} catch (Exception e) {
			SessionFactory.getThreadSession().rollbackTransaction();
			e.printStackTrace();
			throw new DDDException("updateScheduleTask", e.getMessage(), e);
		}
	}
	public void addScheduleTask(ScheduleTask task){
		taskQueue.add(task);
		currentThread.interrupt();
	}
	public void removeScheduleTask(ScheduleTask task){
		for (ScheduleTask scheduleTask : taskQueue) {
			if(scheduleTask.equals(task)){
				taskQueue.remove(scheduleTask);
			}
		}
	}
	public void rebuildScheduleTask(ScheduleTask task){
		removeScheduleTask(task);
		addScheduleTask(task);
	}
	public void rebuildScheduleTask(){
		EntitySet<ScheduleTask> scheduleTasks;
		try {
			scheduleTasks = this.scheduleTaskDao.findStartScheduleTasks();
			taskQueue.clear();
			taskQueue.addAll(scheduleTasks);
			currentThread.interrupt();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("rebuildScheduleTask", e.getMessage(), e);
		}
	}
}
