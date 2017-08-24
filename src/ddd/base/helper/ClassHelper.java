package ddd.base.helper;

import java.lang.annotation.Annotation;
import java.util.List;

import ddd.base.persistence.baseEntity.Entity;
import ddd.base.util.ClassUtil;

public class ClassHelper {

	private static final String packageNames = "ddd";//ConfigHelper.getConfigString("app.package");
	
	public static List<Class<? extends Entity>> getClassListByAnnotation(Class<? extends Annotation> annotationClass) {
		return ClassUtil.getClassListByAnnotation(packageNames, annotationClass);
	}

}
