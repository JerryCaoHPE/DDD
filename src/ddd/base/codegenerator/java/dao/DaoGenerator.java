package ddd.base.codegenerator.java.dao;

import org.apache.velocity.VelocityContext;

import ddd.base.codegenerator.Generator;
import ddd.base.codegenerator.IGenerator;
import ddd.base.persistence.baseEntity.EntityClass;

public class DaoGenerator implements IGenerator {

	private String templateFile=basePath+"src/frameResource/java/Dao.vm";
	private VelocityContext velocityContext=null;
	public DaoGenerator(EntityClass<?> entityClass) {
		velocityContext = new VelocityContext();
		velocityContext.put("entityClass", entityClass);
	}
	
	@Override
	public void generate() {
		Generator.generate(velocityContext, templateFile);
	}

}
