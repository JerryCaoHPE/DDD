package ddd.base.codegenerator.generator;

import java.util.List;

import ddd.base.Config;
import ddd.base.codegenerator.entity.FieldAttribute;
import ddd.base.codegenerator.entity.GenerationAttribute;


public class GenerationParameter {
	
	public final static String PROJECT_PATH = Config.applicationPath;
	public final static String SRC_PATH = "src/";
	public final static String WEBROOT_PATH = "WebRoot/";
	public final static String BASE_PACKAGE = Config.basePackage;
	
	public static String srcBasePath;
	
	public static GenerationAttribute ga;
	public static String entityNameUp;//首写字母为大写的类名，不包含包名
	public static String entityNameLow;//首写字母为小写的类名，不包含包名
	public static String shortPackageNameLow;//精简的小写包名，例如ddd.simple.dao.base则为base
	public static String shortPackageNameUp;//精简的大写包名，例如ddd.simple.dao.base则为Base
	public static String fullClassName;//ddd.cam.entity.student.Student
	public static String packageName;//包名，例如ddd.simple.dao.base
	public static String packagePath; //包的路径
	public static String fullPackagePath; //包的本地路径
	public static String basePackageName; //基本包名，如：cn.simple.entity.student.Student则为cn.simple
	public static String basePackagePath; //基本包的本地路径
	
	public static List<FieldAttribute> fieldAttributeList; //字段信息列表
	public static String entityTips; //类上的注释
	public static String entityAnnotation; //类上的标注
	public static String entityLabel;
	public static String importString = "";
	public static String classNameUpperCase = "";
	public static String currentEntityLabel = ""; //EntityAttribute entityLabel实体中文名称

	public static String javaCode;
}
