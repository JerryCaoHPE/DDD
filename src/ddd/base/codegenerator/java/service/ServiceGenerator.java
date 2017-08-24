package ddd.base.codegenerator.java.service;

import org.apache.velocity.VelocityContext;

import ddd.base.codegenerator.Generator;
import ddd.base.codegenerator.IGenerator;
import ddd.base.persistence.baseEntity.EntityClass;

public class ServiceGenerator implements IGenerator {
	private String templateFile=basePath+"src/frameResource/java/Service.vm";
	private VelocityContext velocityContext=null;
	public ServiceGenerator(EntityClass<?> entityClass) {
		velocityContext = new VelocityContext();
		velocityContext.put("entityClass", entityClass);
	}
	@Override
	public void generate() {
		Generator.generate(velocityContext, templateFile);
		
	}

}
