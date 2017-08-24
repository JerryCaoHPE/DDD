package ddd.base.codegenerator.js;

import org.apache.velocity.VelocityContext;

import ddd.base.codegenerator.Generator;
import ddd.base.codegenerator.IGenerator;
import ddd.base.persistence.baseEntity.EntityClass;

public class JsRouteGenerator implements IGenerator {
	private String templateFile=basePath+"src/frameResource/js/Route.js";
	private VelocityContext velocityContext=null;
	public JsRouteGenerator(EntityClass<?> entityClass) {
		velocityContext = new VelocityContext();
		velocityContext.put("entityClass", entityClass);
	}

	@Override
	public void generate() {
		Generator.generate(velocityContext, templateFile);
	}

}
