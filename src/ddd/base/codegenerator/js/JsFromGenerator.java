package ddd.base.codegenerator.js;

import org.apache.velocity.VelocityContext;

import ddd.base.codegenerator.Generator;
import ddd.base.codegenerator.IGenerator;
import ddd.base.persistence.baseEntity.EntityClass;

public class JsFromGenerator implements IGenerator {
	private String templateFile=basePath+"src/frameResource/js/Form.js";
	private VelocityContext velocityContext=null;
	public JsFromGenerator(EntityClass<?> entityClass) {
		velocityContext = new VelocityContext();
		velocityContext.put("entityClass", entityClass);
	}

	@Override
	public void generate() {
		Generator.generate(velocityContext, templateFile);
	}

}
