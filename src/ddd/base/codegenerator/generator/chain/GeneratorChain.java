package ddd.base.codegenerator.generator.chain;

import ddd.base.codegenerator.generator.Generator;

public interface GeneratorChain {
	
	public void execute();
	
	public void addGenerator(Generator generator);

}
