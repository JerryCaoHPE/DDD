package ddd.base.codegenerator;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import ddd.base.Config;
import ddd.base.codegenerator.html.FormGenerator;
import ddd.base.codegenerator.html.ListGenerator;
import ddd.base.codegenerator.html.MainGenerator;
import ddd.base.codegenerator.html.DisplayGenerator;
import ddd.base.codegenerator.java.action.ActionGenerator;
import ddd.base.codegenerator.java.dao.DaoGenerator;
import ddd.base.codegenerator.java.dao.DaoImplGenerator;
import ddd.base.codegenerator.java.dao.DaoPyGenerator;
import ddd.base.codegenerator.java.service.ServiceGenerator;
import ddd.base.codegenerator.java.service.ServiceImplGenerator;
import ddd.base.codegenerator.js.JsDisplayGenerator;
import ddd.base.codegenerator.js.JsFromGenerator;
import ddd.base.codegenerator.js.JsListGenerator;
import ddd.base.codegenerator.js.JsRouteGenerator;
import ddd.base.codegenerator.js.JsServiceGenerator;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;

public class Generator {

	public static void main(String[] args) {
		//String className="ddd.simple.entity.school.Course";
		//new Generator().generatorBaseCode(className);
		Map map = new HashMap();
		map.put("date", new Date().toLocaleString());
		String result = generate("D://angular/workspace/DDD3/src/ddd/test1.vm", map);
		System.out.println(result);
	}
	
	public Generator() {
		
	}
	
	public Map<String,String> generatorBaseCode(String className){
		Map<String, String> result = new HashMap<String, String>();
		result.put("successMsg", "");
		result.put("errorMsg", "");
		
		if(className==null||"".equals(className)){
			result.put("errorMsg", "请输入完整类名");
			return result;
		}
		
		Class<?> clazz = getClazz(className);
		List<IGenerator> generators = new ArrayList<IGenerator>();
		if(clazz!=null&&clazz.getSuperclass().equals(Entity.class)){
			EntityClass<?> entityClass = SessionFactory.getEntityClass(clazz);
			//java代码生成
			generators.add(new DaoGenerator(entityClass));
			generators.add(new DaoImplGenerator(entityClass));
			generators.add(new ServiceGenerator(entityClass));
			generators.add(new ServiceImplGenerator(entityClass));
			generators.add(new ActionGenerator(entityClass));
			//dao中的py文件生成
			generators.add(new DaoPyGenerator(entityClass));
			//HTML代码生成
			//generators.add(new MainGenerator(entityClass));
			generators.add(new ListGenerator(entityClass));
			generators.add(new FormGenerator(entityClass));
			generators.add(new DisplayGenerator(entityClass));
			//js代码生成
			generators.add(new JsServiceGenerator(entityClass));
			generators.add(new JsFromGenerator(entityClass));
			generators.add(new JsListGenerator(entityClass));
			generators.add(new JsDisplayGenerator(entityClass));
			generators.add(new JsRouteGenerator(entityClass));
			result.put("successMsg", "生成代码成功");
		}else if(clazz==null){
			result.put("errorMsg", "不存在实体："+className);
		}else {
			result.put("errorMsg", "实体："+className+"非Entity的子类");
		}
		for (IGenerator generator : generators) {
			generator.generate();
		}
		return result;
	}
	
	
	public static void generate(VelocityContext velocityContext,String templateFile){
		velocityContext.put("htmlAndjsCodeGenPath", Config.htmlAndjsCodeGenPath);
		velocityContext.put("applicationName", Config.applicationName);
		velocityContext.put("sbuSystem", Config.sbuSystem);
		try {
			StringWriter stringWriter = new StringWriter();
			VelocityEngine velocityEngine = new VelocityEngine();
			Properties p = new Properties();
			p.put(VelocityEngine.FILE_RESOURCE_LOADER_PATH, SessionFactory.getConfig().get("applicationPath"));
			velocityEngine.init(p);
			velocityEngine.mergeTemplate(templateFile, Config.CHARSET_UTF8, velocityContext, stringWriter);
			String outputFile = velocityContext.get("outputFile").toString();
			outputFile=SessionFactory.getConfig().get("applicationPath") + outputFile ;
			File file = new File(outputFile);
			if(file.exists()){
				System.err.println("文件已存在："+file.getAbsolutePath());
				return;
			}
			File pDirec = new File(file.getParent());
			if(!pDirec.exists()) pDirec.mkdirs();
			Template template = velocityEngine.getTemplate(templateFile, Config.CHARSET_UTF8);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,Config.CHARSET_UTF8);
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
			template.merge(velocityContext,bufferedWriter);
			bufferedWriter.flush();
			bufferedWriter.close();
			System.out.println("生成文件："+file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String generate(String absoluteTemplatePath,Map replaceMap){
		VelocityEngine velocityEngine = new VelocityEngine();
		Properties p = new Properties();
		p.put(VelocityEngine.FILE_RESOURCE_LOADER_PATH,"");
		velocityEngine.init(p);
		File file = new File(absoluteTemplatePath);
		if(!file.exists()){
			System.err.println("模板不存在："+file.getAbsolutePath());
			return null;
		}
		Template template = velocityEngine.getTemplate(absoluteTemplatePath, Config.CHARSET_UTF8);
		VelocityContext velocityContext = new VelocityContext(replaceMap);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);
		template.merge(velocityContext, outputStreamWriter);
		try {
			outputStreamWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(byteArrayOutputStream.toByteArray());
	}
	
	private static Class<?> getClazz(String className){
		Class<?> clazz=null;
		try {
			clazz = Class.forName(className);
			if(SessionFactory.getEntityClass(clazz)==null&&clazz.getSuperclass().equals(Entity.class)){
				EntityClass entityClass = new EntityClass(clazz);
				entityClass.preInit();
				synchronized (SessionFactory.getEntityClasses()) {
					SessionFactory.getEntityClasses().put((Class<? extends Entity>)clazz, entityClass);
				}
				entityClass.postInit();
			}
			
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			System.err.println("类不存在："+className);
		}
		return clazz;
	}
	

}
