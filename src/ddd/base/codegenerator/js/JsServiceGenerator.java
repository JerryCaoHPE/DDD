package ddd.base.codegenerator.js;

import org.apache.velocity.VelocityContext;

import ddd.base.Config;
import ddd.base.codegenerator.Generator;
import ddd.base.codegenerator.IGenerator;
import ddd.base.persistence.baseEntity.EntityClass;

public class JsServiceGenerator implements IGenerator {
	private String templateFile=basePath+"src/frameResource/js/Service.js";
	private VelocityContext velocityContext=null;
	public JsServiceGenerator(EntityClass<?> entityClass) {
		velocityContext = new VelocityContext();
		velocityContext.put("entityClass", entityClass);
		velocityContext.put("shortCode", Config.shortCode);
	}

	@Override
	public void generate() {
		Generator.generate(velocityContext, templateFile);
	}

}
