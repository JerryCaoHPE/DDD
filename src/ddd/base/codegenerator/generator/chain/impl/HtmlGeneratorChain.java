package ddd.base.codegenerator.generator.chain.impl;

import java.util.ArrayList;
import java.util.List;

import ddd.base.codegenerator.generator.Generator;
import ddd.base.codegenerator.generator.chain.GeneratorChain;

public class HtmlGeneratorChain implements GeneratorChain {
	
	private List<Generator> chain = new ArrayList<Generator>();

	@Override
	public void execute() {
		for(Generator generator : chain) {
			generator.generate();
		}
	}

	@Override
	public void addGenerator(Generator htmlGenerator) {
		chain.add(htmlGenerator);
	}

}
