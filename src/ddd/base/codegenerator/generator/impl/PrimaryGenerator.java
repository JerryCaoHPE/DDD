package ddd.base.codegenerator.generator.impl;

import ddd.base.codegenerator.entity.GenerationAttribute;
import ddd.base.codegenerator.generator.GenerationParameter;
import ddd.base.codegenerator.generator.Generator;
import ddd.base.codegenerator.generator.chain.GeneratorChain;
import ddd.base.codegenerator.generator.chain.impl.HtmlGeneratorChain;
import ddd.base.codegenerator.generator.chain.impl.JavaGeneratorChain;
import ddd.base.codegenerator.generator.impl.java.entity.EntityGenerator;
import ddd.base.codegenerator.util.GeneratorUtil;

public class PrimaryGenerator implements Generator {
	
	public static GenerationAttribute generationAttribute;
	
	private GeneratorChain javaGeneratorChain;
	
	private GeneratorChain htmlGeneratorChain;
	
	public PrimaryGenerator(GenerationAttribute generationAttribute) {
		PrimaryGenerator.generationAttribute = generationAttribute;
		this.initParameters();
		
		//初始化生成链
		javaGeneratorChain = new JavaGeneratorChain();
		javaGeneratorChain.addGenerator(new EntityGenerator());
		/*javaGeneratorChain.addGenerator(new DAOGenerator());
		javaGeneratorChain.addGenerator(new DAOImplGenerator());
		javaGeneratorChain.addGenerator(new ServiceGenerator());
		javaGeneratorChain.addGenerator(new ServiceImplGenerator());
		javaGeneratorChain.addGenerator(new ActionGenerator());*/
		
		htmlGeneratorChain = new HtmlGeneratorChain();
	}

	@Override
	public void generate() {
		javaGeneratorChain.execute();
		htmlGeneratorChain.execute();

	}
	
	/**
	 * 生成器参数初始化
	 */
	private void initParameters() {
		GenerationParameter.ga = generationAttribute;
		String classNameUp = generationAttribute.getClassNameUp();
		String fullPackageName = generationAttribute.getFullPackageName();
		GenerationParameter.entityNameLow = GeneratorUtil.subUptoLow(classNameUp, 0, 1);
		GenerationParameter.entityNameUp = GeneratorUtil.subLowToUp(
				GenerationParameter.entityNameLow, 0, 1);
		int lastDotIndex = fullPackageName.lastIndexOf(".");
		GenerationParameter.shortPackageNameLow = fullPackageName.substring(
				lastDotIndex, fullPackageName.length());
		GenerationParameter.shortPackageNameUp = GeneratorUtil.subLowToUp(
				GenerationParameter.shortPackageNameLow, 0, 1);
		GenerationParameter.fullClassName = fullPackageName + "." + GenerationParameter.entityNameUp;
		GenerationParameter.packageName = fullPackageName;
		//包的路径
		GenerationParameter.packagePath = GenerationParameter.packageName.replace(".", "/") + "/";
		//包的本地路径
		GenerationParameter.srcBasePath = GenerationParameter.PROJECT_PATH + GenerationParameter.SRC_PATH;
		GenerationParameter.fullPackagePath = GenerationParameter.srcBasePath + GenerationParameter.packagePath;
		//基本包名
		int bpi = fullPackageName.indexOf("entity") - 1;
		GenerationParameter.basePackageName = fullPackageName.substring(0, bpi);
		//基本包本地路径
		GenerationParameter.basePackagePath = GenerationParameter.srcBasePath + 
				GenerationParameter.basePackageName.replace(".", "/");
		GenerationParameter.fieldAttributeList = generationAttribute.getFieldAttributeList();
	}

}
