package ddd.base.codegenerator.generator.impl;

import ddd.base.codegenerator.entity.GenerationAttribute;
import ddd.base.codegenerator.generator.GeneratorService;

public class GeneratorServiceBean implements GeneratorService {
	
	private PrimaryGenerator primaryGenerator;

	@Override
	public void startGenerator(GenerationAttribute generationAttribute) {
		//generationAttribute合法性检查
		primaryGenerator = new PrimaryGenerator(generationAttribute);
		primaryGenerator.generate();
	}

}
