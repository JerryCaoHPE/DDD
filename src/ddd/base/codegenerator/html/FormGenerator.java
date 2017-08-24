package ddd.base.codegenerator.html;

import java.util.Date;

import org.apache.velocity.VelocityContext;

import ddd.base.codegenerator.Generator;
import ddd.base.codegenerator.IGenerator;
import ddd.base.persistence.baseEntity.EntityClass;

public class FormGenerator implements IGenerator {
	private String templateFile=basePath+"src/frameResource/html/Form.html";
	private VelocityContext velocityContext=null;
	public FormGenerator(EntityClass<?> entityClass) {
		velocityContext = new VelocityContext();
		velocityContext.put("entityClass", entityClass);
		velocityContext.put("date", new Date().getClass());
	}

	@Override
	public void generate() {
		Generator.generate(velocityContext, templateFile);
	}

}
