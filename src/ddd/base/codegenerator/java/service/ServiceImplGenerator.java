package ddd.base.codegenerator.java.service;

import org.apache.velocity.VelocityContext;

import ddd.base.codegenerator.Generator;
import ddd.base.codegenerator.IGenerator;
import ddd.base.persistence.baseEntity.EntityClass;

public class ServiceImplGenerator implements IGenerator {
	private String templateFile=basePath+"src/frameResource/java/ServiceImpl.vm";
	private VelocityContext velocityContext=null;
	public ServiceImplGenerator(EntityClass<?> entityClass) {
		velocityContext = new VelocityContext();
		velocityContext.put("entityClass", entityClass);
	}

	@Override
	public void generate() {
		Generator.generate(velocityContext, templateFile);
	}

}
