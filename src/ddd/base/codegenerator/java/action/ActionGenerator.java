package ddd.base.codegenerator.java.action;

import org.apache.velocity.VelocityContext;

import ddd.base.Config;
import ddd.base.codegenerator.Generator;
import ddd.base.codegenerator.IGenerator;
import ddd.base.persistence.baseEntity.EntityClass;

public class ActionGenerator implements IGenerator {
	private String templateFile=basePath+"src/frameResource/java/Action.vm";
	private VelocityContext velocityContext=null;
	public ActionGenerator(EntityClass<?> entityClass) {
		velocityContext = new VelocityContext();
		velocityContext.put("entityClass", entityClass);
		velocityContext.put("shortCode", Config.shortCode);
	}

	@Override
	public void generate() {
		Generator.generate(velocityContext, templateFile);
	}

}
