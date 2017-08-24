package ddd.base.codegenerator.java.dao;

import org.apache.velocity.VelocityContext;

import ddd.base.codegenerator.Generator;
import ddd.base.codegenerator.IGenerator;
import ddd.base.persistence.baseEntity.EntityClass;

public class DaoPyGenerator implements IGenerator {

	private String templateFile=basePath+"src/frameResource/py/Dao.py";
	private VelocityContext velocityContext=null;
	public DaoPyGenerator(EntityClass<?> entityClass) {
		velocityContext = new VelocityContext();
		velocityContext.put("entityClass", entityClass);
	}
	
	@Override
	public void generate() {
		Generator.generate(velocityContext, templateFile);
	}

}
