/**
* @Title: MethodGenerate.java
* @Package ddd.develop
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年11月22日 下午5:18:49
* @version V1.0
*/
package ddd.develop;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ddd.base.util.FileUtil;
import ddd.base.util.VelocityUtil;

/**
 * 项目名称：DDD3
 * 类名称：MethodGenerate
 * 类描述：   为一个实体添加一个方法（前台、后台）
 * 创建人：AnotherTen
 * 创建时间：2015年11月22日 下午5:18:49
 * 修改人：胡均
 * 修改时间：2015年11月22日 下午5:18:49
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public class MethodGenerator
{
	public static void main(String[] args)
	{
		Map<String,Object> map = new HashMap<String, Object>();
		List<String> argments = Arrays.asList("arg1","arg2","arg3");
		map.put("EntityNameLow", "organization");
		map.put("methodName", "autoMethodGenerator");
		generateServiceJs(map,argments);
	}
	
	public static void generateServiceJs(Map<String,Object> map,List<String> argments){
		try{
			
			String fR = FileUtil.readeString(new File("D:\\angular\\workspace\\DDD3\\src\\ddd\\develop\\methodGeneratorFR\\service.js.txt"));
			String result = VelocityUtil.generateString(fR, map);
			String arg1 =  "";
			String arg2 = "";
			for(int i =0;i<argments.size();i++){
				String temp = argments.get(i);
				arg1 += temp+",";
				arg2 += temp +":"+temp+",";
			}
			arg1 = arg1.substring(0, arg1.length()-1);
			arg2 = arg2.substring(0, arg2.length()-1);
			result = result.replace("dddarg1", arg1);
			result = result.replace("dddarg2", arg2);
			System.out.println(result);
			
			//替换文件
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
