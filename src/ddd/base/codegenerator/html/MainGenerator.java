package ddd.base.codegenerator.html;

import org.apache.velocity.VelocityContext;

import ddd.base.codegenerator.Generator;
import ddd.base.codegenerator.IGenerator;
import ddd.base.persistence.baseEntity.EntityClass;

public class MainGenerator implements IGenerator {
	private String templateFile=basePath+"src/frameResource/html/Module.html";
	private VelocityContext velocityContext=null;
	public MainGenerator(EntityClass<?> entityClass) {
		velocityContext = new VelocityContext();
		velocityContext.put("entityClass", entityClass);
	}

	@Override
	public void generate() {
		Generator.generate(velocityContext, templateFile);
	}

}
