package ddd.base;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import ddd.base.exception.ClientError;
import ddd.base.exception.DDDException;
import ddd.base.helper.ActionHelper;
import ddd.base.helper.ErrorPage;
import ddd.base.helper.ForwardPage;
import ddd.base.helper.HTMLPage;
import ddd.base.helper.Img;
import ddd.base.helper.RedirectPage;
import ddd.base.helper.VMPage;
import ddd.base.helper.bean.ActionBean;
import ddd.base.persistence.SessionFactory;
import ddd.base.util.CastUtil;
import ddd.base.util.JSONUtil;
import ddd.base.util.SpringContextUtil;
import ddd.base.util.WebUtil;
import ddd.base.util.json.JSONResult;
import ddd.base.web.WebContext;
import ddd.simple.entity.modelFile.ModelFile;
import ddd.simple.entity.modelFile.Report;
import ddd.simple.service.cache.JSONCacheEngine;
import ddd.simple.service.scheduleTask.ScheduleTaskExecutor;
import ddd.simple.util.DispatcherProperty;

public class DispatcherFilter implements Filter
{
	
	/**
	 * Default constructor.
	 */
	public DispatcherFilter()
	{
	}
	
	/**
	 * @see Filter#destroy()
	 */
	public void destroy()
	{
	}
	
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		// 设置编码
		httpServletRequest.setCharacterEncoding(Config.CHARSET_UTF8);
		// 获取请求方法
		String currentRequestMethod = httpServletRequest.getMethod();
		// 获取请求路径
		String currentRequestPath = getRequestPath(httpServletRequest);
		
		// Operator operator =
		// (Operator)httpServletRequest.getSession().getAttribute("user");
		// boolean isAuthenticate =
		// authenticateFactory.authenticate(operator,currentRequestPath);
		
		// 打印请求信息，方便调试
		System.out.println("-----------------请求信息----------------");
		System.out.println("请求路径：" + currentRequestPath);
		System.out.println(""
				+ new String(JSON.toJSONString(request.getParameterMap(), true).getBytes(Config.CHARSET_ISO), Config.CHARSET_UTF8));
		System.out.println("--------------------------------------");
		
		// 取得session. 如果没有session则自动会创建一个, 我们用false表示没有取得到session则设置为session为空.
		HttpSession session = httpServletRequest.getSession(false);
		// 如果session中没有任何东西.
		
		// 静态资源直接放行
		if (isStaticResource(currentRequestPath) || "/FacadeServlet".equals(currentRequestPath))
		{
			// System.out.println(" is mathced ok!");
			chain.doFilter(request, response);
		} else
		{
			// 执行action
			// 创建请求字符串 get:/user/list
			// String requestPath =
			// currentRequestMethod.toLowerCase()+":"+currentRequestPath;
			String requestPath = currentRequestPath;
			
			boolean isMapped = actionHandler(httpServletRequest, httpServletResponse, chain, requestPath);// 默认为映射失败
			
			// 如果映射失败就执行默认的操作
			if (!isMapped)
			{
				chain.doFilter(request, response);
			}
		}
	}
	//创建web上下文，
//	private void createWebContext(HttpServletRequest httpServletRequest)
//	{
//		LoginUser loginUser = null;
//		if (httpServletRequest.getSession() != null) {
//			loginUser = (LoginUser) ((HashMap)httpServletRequest.getSession().getAttribute("loginUser")).get("loginUser");
//		}
//		
//		WebContext.getWebContext().setLoginUser(loginUser);
//	}
	private void releaseWebContext()
	{
		WebContext.getWebContext().release();
	}
	// 处理action请求
	private boolean actionHandler(HttpServletRequest request, HttpServletResponse response, FilterChain chain, String requestPath)
	{
		
		// 获取并遍历 Action 映射
		// Map<String, ActionBean> actionMap = ActionHelper.getActionMap();
		
		// 请求方法和请求的action方法
		ActionBean actionBean = ActionHelper.getActionMap().get(requestPath);
		if (actionBean == null)
		{
			System.err.println(requestPath + "不存在");
			return false;
		}
		
		// Class<?> actionClass = actionBean.getActionClass();
		Method actionMethod = actionBean.getActionMethod();
		
		// 设置可访问性
		actionMethod.setAccessible(true);
		
		// Object instance = actionClass.newInstance();
		Object actionInstance = SpringContextUtil.getBean(actionBean.getBean());
		System.out.println("调用方法:" + actionMethod.toGenericString());
		// 创建参数列表
		Object[] actionMethodParams = null;
		try
		{
			actionMethodParams = createActionMethodParamList(request, response, actionBean);
		} catch (Exception e)
		{
			ClientError clientError = ClientError.create(e, requestPath, ClientError.createNewId());
			clientError.setMessage("提取查询参数出错，" + clientError.getMessage());
			DDDException.logInFile(e, clientError.getId());
			response.setStatus(500);
			WebUtil.writeJSON(response, clientError);
			if(Config.isDeveloping)
			{
				e.printStackTrace();
			}
			return true;
		}
		
		Object actionMethodResult = null;
		
		try
		{
			actionMethodResult = actionMethod.invoke(actionInstance, actionMethodParams);
			SessionFactory.commitTransaction();
		} catch (InvocationTargetException e)
		{
			SessionFactory.rollbackTransaction();
			
			ClientError clientError = ClientError.create(e.getCause(), requestPath, ClientError.createNewId());
			DDDException.logInFile(e, clientError.getId());
			DDDException.printException(e);
			response.setStatus(500);
			WebUtil.writeJSON(response, clientError);
			if(Config.isDeveloping)
			{
				e.printStackTrace();
			}
			return true;
		} catch (IllegalAccessException e)
		{
			SessionFactory.rollbackTransaction();
			
			ClientError clientError = ClientError.create(e, requestPath, ClientError.createNewId());
			DDDException.logInFile(e, clientError.getId());
			DDDException.printException(e);
			response.setStatus(500);
			WebUtil.writeJSON(response, clientError);
			if(Config.isDeveloping)
			{
				e.printStackTrace();
			}
			return true;
		} catch (IllegalArgumentException e)
		{
			SessionFactory.rollbackTransaction();
			
			ClientError clientError = ClientError.create(e.getCause(), requestPath, ClientError.createNewId());
			clientError.getExtendedData().put("参数是：", JSONUtil.toJSON(actionMethodParams));
			DDDException.logInFile(e, clientError.getId());
			DDDException.printException(e);
			response.setStatus(500);
			WebUtil.writeJSON(response, clientError);
			if(Config.isDeveloping)
			{
				e.printStackTrace();
			}
			return true;
		}
		
		try
		{
			// 处理放回结果
			handleActionMethodReturn(request, response, chain, actionMethodResult);
			return true;
		} catch (Exception e)
		{
			ClientError clientError = ClientError.create(e.getCause(), requestPath, ClientError.createNewId());
			clientError.setMessage("处理返回结果出错，" + clientError.getMessage());
			DDDException.logInFile(e, clientError.getId());
			DDDException.printException(e);
			response.setStatus(500);
			WebUtil.writeJSON(response, clientError);
			if(Config.isDeveloping)
			{
				e.printStackTrace();
			}
			return true;
		}
		
	}
	
	private Map<String, String> getBodyParameters(HttpServletRequest request)
	{
		StringBuffer body = new StringBuffer();
		String line;
		try
		{
			line = request.getReader().readLine();
			while (line != null)
			{
				body.append(line + "\n");
				line = request.getReader().readLine();
			}
			if (body.length() == 0)
			{
				return new HashMap<String, String>();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
			return new HashMap<String, String>();
		}
		Map<String, String> bodyParameters = JSON.parseObject(body.toString(), new TypeReference<Map<String, String>>()
		{});
		return bodyParameters;
	}
	
	private Object[] createActionMethodParamList(HttpServletRequest request, HttpServletResponse response, ActionBean actionBean)
	{
		Map<String, Map<String, Type>> methodParamsAndType = actionBean.getMethodParamsAndType();
		
		Map<String, String> bodyParameters = null;
		
		Object[] objs = new Object[methodParamsAndType.size()];
		int i = 0;
		for (String parameterName : methodParamsAndType.keySet())
		{
			Map<String, Type> parameterTypeMap = methodParamsAndType.get(parameterName);
			Class<?> parameterType = (Class<?>) parameterTypeMap.get("parameterType");
			
			if (parameterType.equals(HttpServletRequest.class))
			{
				objs[i] = request;
				i++;
				continue;
			} else if (parameterType.equals(HttpServletResponse.class))
			{
				objs[i] = response;
				i++;
				continue;
			}
			
			if(bodyParameters == null){
				bodyParameters=this.getBodyParameters(request);
			}
			
			// 根据参数名称查找请求参数，如果找不到，则根据参数类型名取参数
			String parameterValue = WebUtil.getParamter(request, parameterName, bodyParameters);
			if (parameterValue == null)
			{
				parameterName = parameterType.getSimpleName();
				parameterName = parameterName.substring(0, 1).toLowerCase() + parameterName.substring(1);
				parameterValue = WebUtil.getParamter(request, parameterName, bodyParameters);
			}
			
			if (parameterValue == null)
			{
				objs[i] = null;
			}
			
			else if (parameterType.equals(String.class))
			{
				objs[i] = parameterValue;
			} else if (parameterType.equals(int.class) || parameterType.equals(Integer.class))
			{
				objs[i] = CastUtil.castInt(parameterValue);
			} else if (parameterType.equals(long.class) || parameterType.equals(Long.class))
			{
				objs[i] = CastUtil.castLong(parameterValue);
			} else if (parameterType.equals(double.class) || parameterType.equals(Double.class))
			{
				objs[i] = CastUtil.castDouble(parameterValue);
			} else if (parameterType.equals(float.class) || parameterType.equals(Float.class))
			{
				objs[i] = CastUtil.castFloat(parameterValue);
			} else if (parameterType.equals(boolean.class) || parameterType.equals(Boolean.class))
			{
				objs[i] = CastUtil.castBoolean(parameterValue);
			}
			
			else if (Collection.class.isAssignableFrom(parameterType))
			{
				Type type = parameterTypeMap.get("paraType");
				objs[i] = WebUtil.getGenericParamter(type, parameterValue);
			} else if (Map.class.isAssignableFrom(parameterType))
			{
				Type type = parameterTypeMap.get("paraType");
				objs[i] = WebUtil.getHashMapParamter(type, parameterValue);
				
			} else
			{
				objs[i] = WebUtil.parseParametersWithBean(parameterType, parameterValue);
			}
			
			i++;
		}
		return objs;
	}
	
	// 判断是否为基本数据类型
	private boolean isBaseType(Class<?> clazz)
	{
		return clazz.equals(int.class) || clazz.equals(Integer.class) || clazz.equals(long.class) || clazz.equals(Long.class)
				|| clazz.equals(double.class) || clazz.equals(Double.class) || clazz.equals(String.class);
	}
	
	private void handleActionMethodReturn(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Object actionMethodResult)
	{
		if (actionMethodResult == null)
		{
			response.setHeader("datatype", "null");
			WebUtil.writeText(response, "null");
		}
		else if (Collection.class.isAssignableFrom(actionMethodResult.getClass()))
		{
			response.setHeader("datatype", "array");
			
			JSONResult jsonResult = JSONUtil.entityToJSON(actionMethodResult, false);
			if(jsonResult.isHasReference())
			{
				response.setHeader("datatype", "arrayWithReference");
			}
			else
			{
				response.setHeader("datatype", "array");
			}
			WebUtil.writeJSONString(response,jsonResult.getJson());
			return;
		}		
		else if (actionMethodResult instanceof String )
		{
			// 返回text数据
			response.setHeader("datatype", "string");
			WebUtil.writeText(response, actionMethodResult.toString()); 
			return;
		}
		else if ( actionMethodResult instanceof Integer)
		{
			response.setHeader("datatype", "integer");
			WebUtil.writeText(response, actionMethodResult.toString()); 
			return;
		}
		else if (actionMethodResult instanceof Boolean)
		{
			response.setHeader("datatype", "boolean");
			WebUtil.writeText(response, actionMethodResult.toString()); 
			return;
		}
		else if (actionMethodResult instanceof Float || actionMethodResult instanceof Double)
		{
			response.setHeader("datatype", "float");
			WebUtil.writeText(response, actionMethodResult.toString()); 
			return;
		}		
		else if (actionMethodResult instanceof HTMLPage)
		{
			WebUtil.writeHTML(response, (HTMLPage) actionMethodResult);
		} else if (actionMethodResult instanceof VMPage)
		{
			WebUtil.writeVMPage(response, (VMPage) actionMethodResult);
		} else if (actionMethodResult instanceof Img)
		{
			WebUtil.writeImage(response, (Img) actionMethodResult);
		} else if (actionMethodResult instanceof byte[])
		{
			WebUtil.writeBytes(response, (byte[]) actionMethodResult);
		} else if (InputStream.class.isAssignableFrom(actionMethodResult.getClass()))
		{
			WebUtil.writeStream(response, (InputStream) actionMethodResult);
		} else if (actionMethodResult instanceof RedirectPage)
		{
			// 若为 Page 类型，则 转发 或 重定向 到相应的页面中
			RedirectPage page = (RedirectPage) actionMethodResult;
			
			// 获取路径
			String path = ((RedirectPage) actionMethodResult).genURL();
			// 让浏览器重定向到页面
			WebUtil.redirectRequest(path, request, response);
		} else if (actionMethodResult instanceof ForwardPage)
		{
			ForwardPage page = (ForwardPage) actionMethodResult;
			// 获取路径
			String jspPath = "";
			String path = jspPath + page.getPath();
			// 初始化请求属性
			Map<String, Object> data = page.getData();
			if (data != null && data.size() > 0)
			{
				for (Map.Entry<String, Object> entry : data.entrySet())
				{
					request.setAttribute(entry.getKey(), entry.getValue());
				}
			}
			// 转发请求
			WebUtil.forwardRequest(path, request, response);
		} else if (actionMethodResult instanceof ErrorPage)
		{// 错误页面的处理
			ErrorPage page = (ErrorPage) actionMethodResult;
			// 获取路径
			String jspPath = "";
			String path = jspPath + "/ErrorPage.jsp";
			request.setAttribute("exception", page.getException());
			Map<String, Object> data = page.getData();
			if (data != null && data.size() > 0)
			{
				for (Map.Entry<String, Object> entry : data.entrySet())
				{
					request.setAttribute(entry.getKey(), entry.getValue());
				}
			}
			// 跳转到错误页面
			WebUtil.forwardRequest(path, request, response);
		} else if(actionMethodResult instanceof Report){
			//报表不做任何处理
		}else
		{
			// 返回JSON数据
 
			JSONResult jsonResult = JSONUtil.entityToJSON(actionMethodResult, false);
			if(jsonResult.isHasReference())
			{
				response.setHeader("datatype", "objectWithReference");
			}
			else
			{
				response.setHeader("datatype", "object");
			}
			WebUtil.writeJSONString(response,jsonResult.getJson());
		 
		}
		
	}
	
	private void printActionMapping(Map<String, ActionBean> actionMapping)
	{
		Set<Entry<String, ActionBean>> set = actionMapping.entrySet();
		
		for (Entry<String, ActionBean> entry : set)
		{
			ActionBean actionBean = entry.getValue();
			System.out.println("path:" + entry.getKey() + ";class:" + actionBean.getActionClass() + ";method:"
					+ actionBean.getActionMethod().getName());
		}
		
	}
	
	// 判断是否为静态资源，.html,.jsp,.png,.gif,.jpg,.swf
	private boolean isStaticResource(String currentRequestPath)
	{
		String staticResourceReg = ".+\\.((html)|(htm)|(jsp)|(png)|(gif)|(jpg)|(swf)|(css)|(js))$";
		Matcher requestPathMatcher = Pattern.compile(staticResourceReg).matcher(currentRequestPath);
		// int count = requestPathMatcher.groupCount();
		boolean ismatcher = requestPathMatcher.matches();
		
		// System.out.println("是否匹配:"+ismatcher);
		// System.err.println(count);
		// for(int i=0;i<count;i++){
		// if(ismatcher)
		// System.out.println(requestPathMatcher.group(i));
		// }
		
		// //得到匹配的静态资源类型
		// if(ismatcher){
		// String staticResourceType = requestPathMatcher.group(1);
		// System.out.println("静态资源类型为："+staticResourceType);
		// }
		
		return ismatcher;
	}
	
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException
	{
		Config.put("tomcatWebContentPath", fConfig.getServletContext().getRealPath(""));
		// 初始化上下文，维护表
		SessionFactory.initContext();
		System.out.println("actionMap 初始化:");
		
		ActionHelper.init();
		/*
		 * DynamicSql.initDynamicSql(fConfig.getServletContext().getRealPath(
		 * "WEB-INF/classes/")); DynamicSql.initDataSourceDynamicSql();
		 */
		
		// printActionMapping(actionMap);
		ScheduleTaskExecutor.execute();
		
		//初始化Json缓存
		JSONCacheEngine.initCache();
		
		System.out.println("初始化完毕");
		
	}
	
	// 得到请求路径
	public static String getRequestPath(HttpServletRequest request)
	{
		String servletPath = request.getServletPath();
		String pathInfo = StringUtils.defaultIfEmpty(request.getPathInfo(), "");
		return servletPath + pathInfo;
	}
	
	private boolean noLoginFilter(HttpServletRequest request, String currentRequestPath)
	{
		HttpSession session = request.getSession(false);
		
		List<String> paths = DispatcherProperty.getDispatcherProperties();
		
		if (session == null || (session.getAttribute("loginUser") == null && session.getAttribute("vipLogin") == null))
		{
			/**
			 * date:2015-8-8 ope:zhouxue reason:为了在帐号输入完成之后查询该帐号所在单位而为查询单位方法添加放行
			 * */
			for (int i = 0; i < paths.size(); i++)
			{
				if (currentRequestPath.equals(paths.get(i)))
				{
					return true;
				}
				
			}
			
		} else
		{
			return true;
		}
		return false;
		
	}
	
}
