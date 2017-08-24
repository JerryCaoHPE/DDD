package ddd.simple.service.scheduleTask;

import org.springframework.web.context.ContextLoaderListener;

import ddd.simple.service.scheduleTask.impl.ScheduleServiceBean;

public class ScheduleTaskExecutor {
	private static ScheduleService scheduleService=ContextLoaderListener.getCurrentWebApplicationContext().getBean(ScheduleServiceBean.class);
	public static void execute() {
		new Thread(){
			public void run() {
				scheduleService.executeScheduleTask();
			};
		}.start();
	}
	
}
