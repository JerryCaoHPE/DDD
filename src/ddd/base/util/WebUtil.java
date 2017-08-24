package ddd.base.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import ddd.base.Config;
import ddd.base.helper.Img;
import ddd.base.helper.VMPage;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.util.json.JSONResult;

//以指定格式将返回值写入响应
public class WebUtil {

	// 字符串分隔符
    public static final String SEPARATOR = String.valueOf((char) 29);
	
    //private static final Logger logger = LoggerFactory.getLogger(WebUtil.class);

    // 将数据以 JSON 格式写入响应中
    public static void writeJSON(HttpServletResponse response, Object data) {
        try {
      
			JSONResult jsonResult = JSONUtil.entityToJSON(data, false);
			if(jsonResult.isHasReference())
			{
				response.setHeader("datatype", "objectWithReference");
			}
			else
			{
				response.setHeader("datatype", "object");
			}
			WebUtil.writeJSONString(response,jsonResult.getJson());
 
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void writeJSONString(HttpServletResponse response, String data) {
        try {
            // 设置响应头
            response.setContentType("application/json"); // 指定内容类型为 JSON 格式
            response.setCharacterEncoding(Config.CHARSET_UTF8); // 防止中文乱码

            // 向响应中写入数据
            PrintWriter writer = response.getWriter();
 
        	writer.write(data); // 转为 JSON 字符串
            writer.flush();
            writer.close();
        } catch (Exception e) {
            //logger.error("在响应中写数据出错！", e);
            throw new RuntimeException(e);
        }
    }    
    public static void writeText(HttpServletResponse response, String data) {
        try {
            // 设置响应头
            response.setContentType("text/plain"); // 以文本的形式写到前台
            response.setCharacterEncoding(Config.CHARSET_UTF8); // 防止中文乱码

            //data=JSONUtil.toJSON(data);
            // 向响应中写入数据
            PrintWriter writer = response.getWriter();
            writer.write(data); // 
            writer.flush();
            writer.close();
        } catch (Exception e) {
            //logger.error("在响应中写数据出错！", e);
            throw new RuntimeException(e);
        }
    }

    // 将数据以 HTML 格式写入响应中（在 JS 中获取的是 JSON 字符串，而不是 JSON 对象）
    public static void writeHTML(HttpServletResponse response, Object data) {
        try {
            // 设置响应头
            response.setContentType("text/html"); // 指定内容类型为 HTML 格式
            response.setCharacterEncoding(Config.CHARSET_UTF8); // 防止中文乱码

            // 向响应中写入数据
            PrintWriter writer = response.getWriter();
            writer.write(data.toString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            //logger.error("在响应中写数据出错！", e);
            throw new RuntimeException(e);
        }
    }
    // 将数据以 HTML 格式写入响应中（在 JS 中获取的是 JSON 字符串，而不是 JSON 对象）
    public static void writeVMPage(HttpServletResponse response, VMPage vmPage) {
        try {
            // 设置响应头
            response.setContentType("text/html"); // 指定内容类型为 HTML 格式
            response.setCharacterEncoding(Config.CHARSET_UTF8); // 防止中文乱码

            // 向响应中写入数据
            PrintWriter writer = response.getWriter();
            
           // String stringTemplate = FileUtil.readeString(new File(vmPage.getVmPath()));
            
            //VelocityUtil.generate(stringTemplate, writer, vmPage.getData());
            VelocityUtil.generateStream(vmPage.getVmPath(), vmPage.getData(), writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            //logger.error("在响应中写数据出错！", e);
            throw new RuntimeException(e);
        }
    }
    
    public static void writeImage(HttpServletResponse response,Img img) {
        try
        {
    		// 设置响应头
    		response.setContentType("image/jpeg");
			OutputStream os = response.getOutputStream();
			if(img.getBytes()!=null){
				os.write(img.getBytes());  
			}
			if(img.getInputStream()!=null){
				InputStream inputStream = img.getInputStream();
				byte[] bytes = new byte[1024];
				int size = 0;
				while((size=inputStream.read(bytes))!=-1){
					os.write(bytes, 0, size);
				}
				inputStream.close();
			}
		    os.flush();
		    os.close();
        } catch (Exception e) {
            //logger.error("在响应中写数据出错！", e);
            throw new RuntimeException(e);
        }
    }
    
    public static void writeBytes(HttpServletResponse response,byte[] bytes) {
        try
        {
    		// 设置响应头
    		response.setContentType("application/octet-stream");
			OutputStream os = response.getOutputStream();
			os.write(bytes);  
		    os.flush();
		    os.close();
        } catch (Exception e) {
            //logger.error("在响应中写数据出错！", e);
            throw new RuntimeException(e);
        }
    }
    public static void writeStream(HttpServletResponse response,InputStream inputStream) {
        try
        {
    		// 设置响应头
    		response.setContentType("application/octet-stream");
			OutputStream os = response.getOutputStream();
			if(inputStream!=null){
				byte[] bytes = new byte[1024];
				int size = 0;
				while((size=inputStream.read(bytes))!=-1){
					os.write(bytes, 0, size);
				}
				inputStream.close();
			}
		    os.flush();
		    os.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // 从请求中获取所有参数（当参数名重复时，用后者覆盖前者）
    public static Map<String, String> getRequestParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new LinkedHashMap<String, String>();
        try {
            String method = request.getMethod();
            if (method.equalsIgnoreCase("put") || method.equalsIgnoreCase("delete")) {
                String queryString = CodecUtil.urlDecode(StreamUtil.getString(request.getInputStream()));
                if (StringUtils.isNotEmpty(queryString)) {
                    String[] qsArray = queryString.split("&");
                    if (ArrayUtils.isNotEmpty(qsArray)) {
                        for (String qs : qsArray) {
                            String[] array = qs.split("=");
                            if (ArrayUtils.isNotEmpty(array) && array.length == 2) {
                                String paramName = array[0];
                                String paramValue = array[1];
                                if (checkParamName(paramName)) {
                                    paramMap.put(paramName, paramValue);
                                }
                            }
                        }
                    }
                }
            } else {
                Enumeration<String> paramNames = request.getParameterNames();
                while (paramNames.hasMoreElements()) {
                    String paramName = paramNames.nextElement();
                    if (checkParamName(paramName)) {
                        String[] paramValues = request.getParameterValues(paramName);
                        if (ArrayUtils.isNotEmpty(paramValues)) {
                            if (paramValues.length == 1) {
                                paramMap.put(paramName, paramValues[0]);
                            } else {
                                StringBuilder paramValue = new StringBuilder("");
                                for (int i = 0; i < paramValues.length; i++) {
                                    paramValue.append(paramValues[i]);
                                    if (i != paramValues.length - 1) {
                                        paramValue.append(SEPARATOR);
                                    }
                                }
                                paramMap.put(paramName, paramValue.toString());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            //logger.error("获取请求参数出错！", e);
            System.err.println("获取请求参数出错！");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return paramMap;
    }

    private static boolean checkParamName(String paramName) {
        return !paramName.equals("_"); // 忽略 jQuery 缓存参数
    }

    // 转发请求
    public static void forwardRequest(String path, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher(path).forward(request, response);
        } catch (Exception e) {
            //logger.error("转发请求出错！", e);
        	System.err.println("转发请求出错！"+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // 重定向请求
    public static void redirectRequest(String path, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(request.getContextPath() + path);
        } catch (Exception e) {
            //logger.error("重定向请求出错！", e);
            System.err.println("重定向请求出错！"+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // 发送错误代码
    public static void sendError(int code, HttpServletResponse response) {
        try {
            response.sendError(code);
        } catch (Exception e) {
            //logger.error("发送错误代码出错！", e);
            System.err.println("发送错误代码出错！"+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // 判断是否为 AJAX 请求
    public static boolean isAJAX(HttpServletRequest request) {
        return request.getHeader("X-Requested-With") != null;
    }

    // 获取请求路径
    public static String getRequestPath(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        String pathInfo = StringUtils.defaultIfEmpty(request.getPathInfo(), "");
        return servletPath + pathInfo;
    }

    // 从 Cookie 中获取数据
    public static String getCookie(HttpServletRequest request, String name) {
        String value = "";
        try {
            Cookie[] cookieArray = request.getCookies();
            if (cookieArray != null) {
                for (Cookie cookie : cookieArray) {
                    if (StringUtils.isNotEmpty(name) && name.equals(cookie.getName())) {
                        value = CodecUtil.urlDecode(cookie.getValue());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            //logger.error("获取 Cookie 出错！", e);
        	
            throw new RuntimeException(e);
        }
        return value;
    }

    // 下载文件
    public static void downloadFile(HttpServletResponse response, String filePath) {
        try {
            String originalFileName = FilenameUtils.getName(filePath);
            //String originalFileName = filePath;
            String downloadedFileName = new String(originalFileName.getBytes("GBK"), Config.CHARSET_ISO);

            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=\"" + downloadedFileName + "\"");

            InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            StreamUtil.copyStream(inputStream, outputStream);
        } catch (Exception e) {
            //logger.error("下载文件出错！", e);
        	System.err.println("下载文件出错！");
        	e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // 设置 Redirect URL 到 Session 中
    public static void setRedirectURL(HttpServletRequest request, String sessionKey) {
        if (!isAJAX(request)) {
            String requestPath = getRequestPath(request);
            request.getSession().setAttribute(sessionKey, requestPath);
        }
    }

    // 创建验证码
    public static String createCaptcha(HttpServletResponse response) {
        StringBuilder captcha = new StringBuilder();
        try {
            // 参数初始化
            int width = 60;                      // 验证码图片的宽度
            int height = 25;                     // 验证码图片的高度
            int codeCount = 4;                   // 验证码字符个数
            int codeX = width / (codeCount + 1); // 字符横向间距
            int codeY = height - 4;              // 字符纵向间距
            int fontHeight = height - 2;         // 字体高度
            int randomSeed = 10;                 // 随机数种子
            char[] codeSequence = {              // 验证码中可出现的字符
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
            };
            // 创建图像
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bi.createGraphics();
            // 将图像填充为白色
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            // 设置字体
            g.setFont(new Font("Courier New", Font.BOLD, fontHeight));
            // 绘制边框
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, width - 1, height - 1);
            // 产生随机干扰线（160条）
            g.setColor(Color.WHITE);
            // 创建随机数生成器
            Random random = new Random();
            for (int i = 0; i < 160; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g.drawLine(x, y, x + xl, y + yl);
            }
            // 生成随机验证码
            int red, green, blue;
            for (int i = 0; i < codeCount; i++) {
                // 获取随机验证码
                String validateCode = String.valueOf(codeSequence[random.nextInt(randomSeed)]);
                // 随机构造颜色值
                red = random.nextInt(255);
                green = random.nextInt(255);
                blue = random.nextInt(255);
                // 将带有颜色的验证码绘制到图像中
                g.setColor(new Color(red, green, blue));
                g.drawString(validateCode, (i + 1) * codeX - 6, codeY);
                // 将产生的随机数拼接起来
                captcha.append(validateCode);
            }
            // 禁止图像缓存
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            // 设置响应类型为 JPEG 图片
            response.setContentType("image/jpeg");
            // 将缓冲图像写到 Servlet 输出流中
            ServletOutputStream sos = response.getOutputStream();
            ImageIO.write(bi, "jpeg", sos);
            sos.close();
        } catch (Exception e) {
            //logger.error("创建验证码出错！", e);
        	System.err.println("创建验证码出错！");
        	e.printStackTrace();
            throw new RuntimeException(e);
        }
        return captcha.toString();
    }

    // 是否为 IE 浏览器
    public boolean isIE(HttpServletRequest request) {
        String agent = request.getHeader("User-Agent");
        return agent != null && agent.contains("MSIE");
    }
    
    /**
     * 从request对象里面取得前台传递的参数，如果是get方式传递的就进行转码，防止中文乱码的问题
     * @param request
     * @param parameterName
     * @return
     */
    public static String getParamter(HttpServletRequest request,String parameterName,Map<String,String> bodyParameters){
    	String method  = request.getMethod();
    	String value = request.getParameter(parameterName);  
    	if(value!=null && method.toLowerCase().equals("get")){
    		try {
				return new String(value.getBytes(Config.CHARSET_ISO),Config.CHARSET_UTF8);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
    	}
    	if(value == null)
    	{
    		if(bodyParameters.get(parameterName) == null){
    			value = null;
    		}else{
    		    value = bodyParameters.get(parameterName);
    		}
    	}
    	return value;
    }
    
    /**
     * 将request对象里面传递的参数反射生成一个对象
     * @param request
     * @param t   //需要被反射生产的对象
     * @return 
     * @return
     */
    public static <T> T parseParametersWithBean(Class<T> t,String paramenterValue){

    	try {
			if(paramenterValue == null)
			{
				return null;
			}
			//T newT = JSON.parseObject(paramenterValue, t);
			T newT = JSONUtil.fromJSON(paramenterValue, t);
			if(Entity.class.isAssignableFrom(t) && JSONObject.parseObject(paramenterValue).get("newEntity") ==null ){
				((Entity) newT).setNewEntity(true);
			}
			return newT;
// 2015-09-01 xcy注释，不支持通过查询参数名为对象的属性			
//			T newT = (T) t.newInstance();
//			Map<String,String[]> paramerMap = request.getParameterMap();
//			
//			for(Entry<String,String[]> entry : paramerMap.entrySet()){
//				String key = entry.getKey();
//				
//				String []value = entry.getValue();
//				
//				Field field = null;
//				try {
//					field = t.getDeclaredField(key);
//					if(field!=null){
//						setValue(newT,field,value);
//					}
//				} catch (NoSuchFieldException e) {
//					System.err.println(t.getName()+" not has field "+ key);
//					continue;
//				}
//			}
//			
//			return newT;
			
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
    
    //根据字段的类型给对象赋值
  	private static void setValue(Object newT,Field field, String value[]){
  		
  		try {
  			field.setAccessible(true);
  			Class<?> fieldType = field.getType();
  			String typeName = fieldType.getName(); 
  			
  			//Integer 类型的数据
  			if(typeName.equals("int") || typeName.equals("java.lang.Integer")){
  				field.set(newT, Integer.parseInt(value[0].equals("")?"0":value[0]));
  			}
  			//Long 类型的数据
  			else if(typeName.equals("long") || typeName.equals("java.lang.Long")){
  				field.set(newT, Long.parseLong(value[0].equals("")?"0":value[0]));
  			}
  			//Float 类型的数据
  			else if(typeName.equals("float") || typeName.equals("java.lang.Float")){
  				field.set(newT, Float.parseFloat(value[0].equals("")?"0":value[0]));
  			}
  			//Double 类型的数据
  			else if(typeName.equals("double") || typeName.equals("java.lang.Double")){
  				field.set(newT, Double.parseDouble(value[0].equals("")?"0":value[0]));
  			}
  			//String 类型的对象
  			else if(typeName.equals("java.lang.String")){
  				field.set(newT, value[0]);
  			}
  			//解析为时间的格式
  			else if(typeName.equals("java.util.Date")){
  				if(value[0].toString().equals("")){
  					field.set(newT, null);
  				}else {
  					Date tempDate = SysUtil.praseDate(value[0]);
  	  				field.set(newT, tempDate);
  				}
  				
  			}else {
  				field.set(newT, value[0]);
  			}
  			//Parse end
  		} catch (Exception e) {
  			e.printStackTrace();
  			System.err.println("can not reflact parameter:"+ field.getName() +"; value:"+value[0] + " to " + newT.getClass().getName()+",type:"+field.getType().getName()); 
  		}
  		
  	}
  	
  	 public static Collection<?> getGenericParamter(Type type,String paramenterValue)
  	 {
			if(paramenterValue == null )
			{
				return null;
			}
			else
			{ 
				return (Collection<?>) JSON.parseObject(paramenterValue, type);
			}
  	 }
  	 
  	 public static Map getHashMapParamter(Type type,String paramenterValue)
  	 {
			if(paramenterValue == null )
			{
				return null;
			}
			else
			{ 
				return  JSON.parseObject(paramenterValue, type);
			}
  	 }
    
}

