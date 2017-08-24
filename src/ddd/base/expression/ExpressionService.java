package ddd.base.expression;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
@Service
public class ExpressionService {
	private Map<String, ExpressionHandler> beans = new HashMap<String, ExpressionHandler>();

	public Object execute(String expression, Map<String, Object> params) {
		expression = expression.trim();
		expression = expression.substring(2, expression.length() - 1);
		String[] operates = expression.trim().split("[.]");
		ExpressionHandler handler = beans.get(operates[0]);// 获取表达式中的处理者

		if (operates.length > 1) { // 带方法的表达式
			Class handlerClass = handler.getClass();
			String method = operates[1].trim();
			int left_ = method.indexOf('(');
			String methodName = method.substring(0, left_);
			String paramsString = method.substring(left_ + 1,method.length() - 1);
			Object result = null;
			if (paramsString.startsWith("$")) {
				String[] paramNames = paramsString.split("[,]");
				List paramsList = new ArrayList();
				for (int i = 0; i < paramNames.length; i++) {
					paramsList.add(paramNames[i].substring(2,paramNames[i].length() - 1));
				}
				Object[] args = new Object[paramsList.size()];
				for (int i = 0; i < paramsList.size(); i++) {
					args[i] = params.get(paramsList.get(i));
				}
				Method handlerMethod = getMethod(handlerClass, methodName);

				try {
					result = handlerMethod.invoke(handler, args);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
			} else {
                 System.err.println("请输入正确的格式 eg：$服务名.(${参数名})");
			}
			return result;
		} else {
			return handler.execute(); // 不带方法(.)的表达式默认执行execute方法
		}

	}
	
	public Object execute(String expression) {
		if(expression == null){
			return null;
		}
		expression = expression.trim();
		expression = expression.substring(2, expression.length()-1);
		String[] operates = expression.trim().split("[.]");
		ExpressionHandler handler = beans.get(operates[0]);
		
		if(operates.length>1){
			Class handlerClass = handler.getClass();
			String method = operates[1].trim();
			int left_ = method.indexOf('(');
			String methodName = method.substring(0, left_);
			String paramsString = method.substring(left_+1,	 method.length()-1);
			Object[] paramNames = paramsString.split("[,]");
		
			Method handlerMethod = getMethod(handlerClass, methodName);
			Object result = null;
			try {
				result = handlerMethod.invoke(handler, paramNames);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return result;
		}else{
			return handler.execute();
		}
	}

	private Method getMethod(Class handlerClass, String methodName) {
		Method[] methods = handlerClass.getMethods();

		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				return method;
			}
		}
		return null;
	}
 
	public Map<String, ExpressionHandler> getBeans() {
		return beans;
	}

	public void setBeans(Map<String, ExpressionHandler> beans) {
		this.beans = beans;
	}

}
