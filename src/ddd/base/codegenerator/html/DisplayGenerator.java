package ddd.base.codegenerator.html;

import org.apache.velocity.VelocityContext;

import ddd.base.codegenerator.Generator;
import ddd.base.codegenerator.IGenerator;
import ddd.base.persistence.baseEntity.EntityClass;

public class DisplayGenerator implements IGenerator {
	private String templateFile=basePath+"src/frameResource/html/Display.html";
	private VelocityContext velocityContext=null;
	public DisplayGenerator(EntityClass<?> entityClass) {
		velocityContext = new VelocityContext();
		velocityContext.put("entityClass", entityClass);
	}

	@Override
	public void generate() {
		Generator.generate(velocityContext, templateFile);
	}

}
