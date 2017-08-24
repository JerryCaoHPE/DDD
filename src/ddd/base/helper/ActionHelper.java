package ddd.base.helper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.helper.bean.ActionBean;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.util.ClassAssist;

//遍历action，将所有类和对应的方法存入ActionMap
public class ActionHelper {
	
	//创建一个ActionMap用于存放所有的ActionBean（把每个action的全类名和方法封装成一个bean）
	private static final Map<String,ActionBean> actionMap = new HashMap<String,ActionBean>();
	
	//ActionHelper初始化时，在actionMap中遍历带有@Action注解的类
	public static void init() {
		List<Class<? extends Entity>> actionClassList = ClassHelper.getClassListByAnnotation(Action.class);
		
		if(CollectionUtils.isNotEmpty(actionClassList)){
			Map<String,ActionBean> actionPathMap = new HashMap<String,ActionBean>();
			// 遍历 Action 类
			for (Class<?> actionClass : actionClassList) {
				// 获取并遍历该 Action 类中所有的方法（不包括父类中的方法）
				Method[] actionMethods = actionClass.getDeclaredMethods();

				ClassAssist classAssist = new ClassAssist(actionClass);
				
				if (!ArrayUtils.isEmpty(actionMethods)) {
					for (Method actionMethod : actionMethods) {
						// 遍历获取方法请求方式和请求二级路径
						String requestMethod = "post";
						String requestPath = '/' + actionMethod.getName();
						ActionBean actionBean = new ActionBean(actionClass,
								actionMethod,classAssist);
						// 通过类上的@RequestMapping获取请求路径（一级地址）
						String classRequestMappingStr = "";
						if (actionClass.isAnnotationPresent(RequestMapping.class)) {
							RequestMapping rm = actionClass.getAnnotation(RequestMapping.class);
							String value = rm.value();
							if (!value.equals("/")) {
								classRequestMappingStr = value;
							}
						}
						if(actionClass.getAnnotation(Controller.class)!=null){
							String beanName = actionClass.getAnnotation(Controller.class).value();
			                if("".equals(beanName.trim())){
			                	beanName = actionClass.getSimpleName().substring(0,1).toLowerCase()+actionClass.getSimpleName().substring(1);
			                }
			                actionBean.setBean(beanName);
		                }else{
		                	//error
		                }
		                
						if (requestMethod.equals("")) {
							actionPathMap.put(classRequestMappingStr+ requestPath, actionBean);
							actionPathMap.put(classRequestMappingStr+ requestPath, actionBean);
						} else {
							actionPathMap.put(classRequestMappingStr+ requestPath, actionBean);
						}
					}
				}

			}
			actionMap.putAll(actionPathMap);
		}
	}
	
	public static Map<String, ActionBean> getActionMap() {
        return actionMap;
    }
	
}
