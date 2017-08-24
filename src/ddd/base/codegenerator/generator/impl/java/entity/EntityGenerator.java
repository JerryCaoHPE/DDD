package ddd.base.codegenerator.generator.impl.java.entity;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import ddd.base.Config;
import ddd.base.annotation.Column; 
import java.util.Date;

import ddd.base.codegenerator.entity.FieldAttribute;
import ddd.base.codegenerator.generator.GenerationParameter;
import ddd.base.codegenerator.generator.impl.PrimaryGenerator;
import ddd.base.codegenerator.generator.impl.java.JavaGenerator;
import ddd.base.codegenerator.util.GeneratorUtil;
import ddd.base.util.SpringContextUtil;
import ddd.simple.service.codeTable.CodeTypeService;
import ddd.simple.service.systemConfig.SystemConfigService;

public class EntityGenerator extends JavaGenerator
{
	
	private SystemConfigService systemConfigService = (SystemConfigService) SpringContextUtil.getBean("systemConfigServiceBean");
	
	private Boolean coverWrite = false;
	
	private String filePath;
	
	private String daoTemplateLoc = "frameResource/java/Entity.txt";
	
	private String daoTemplateAbsPath;
	
	private Map<String, String> replacement;
	
	private Set<String> importArea = new HashSet<String>();
	
	private static String gsfunc = "\tpublic {fieldType} get{fieldNameUp}() {\n" + "\t\t{lazyLoad};\n" + "\t\treturn this.{fieldName};\n"
			+ "\t}\n\n" + "\tpublic void set{fieldNameUp}({fieldType} {fieldName}) {\n" + "{setLazyLoad}"
			+ "\t\tthis.{fieldName} = {fieldName};\n" + "\t}\n\n";
			
	public EntityGenerator()
	{
		initParameters();
	}
	
	private void initParameters()
	{
		filePath = GenerationParameter.fullPackagePath + GenerationParameter.entityNameUp + ".java";
		daoTemplateAbsPath = GenerationParameter.srcBasePath + daoTemplateLoc;
		
		replacement = new HashMap<String, String>();
		replacement.put("{packageName}", GenerationParameter.packageName);
		replacement.put("{className}", GenerationParameter.entityNameUp);
		String tablePrefix = Config.shortCode+"_";
		replacement.put("{EntityAnnotation}", "label=\""+GenerationParameter.ga.getEntityInfo().getLabel()+"\",name=\""+tablePrefix+GenerationParameter.entityNameLow+"\"");
		generateEntityCode();
	}
	
	private void generateEntityCode()
	{
		StringBuffer field = new StringBuffer();
		StringBuffer funcs = new StringBuffer();
		List<FieldAttribute> fattris = PrimaryGenerator.generationAttribute.getFieldAttributeList();
		for (FieldAttribute fa : fattris)
		{
			field.append(generateFieldCode(fa));
			funcs.append(generateFuncCode(fa));
		}
		replacement.put("{fieldArea}", field.toString());
		replacement.put("{funcArea}", funcs.toString());
	}
	
	private String generateFieldCode(FieldAttribute fa)
	{
		StringBuffer sb = new StringBuffer("\t@Column(");
		sb.append("label=\"" + fa.getColumnInfo().getLabel() + "\"");
		
		String type = GeneratorUtil.isBaseDataType(fa.getType());
		if (type != null)
		{
			fa.setType(type);
			if (fa.getType().equals("Date"))
			{
				importArea.add("\nimport java.util.Date;");
			} else if (fa.getType().equals("Timestamp"))
			{
				importArea.add("\nimport java.util.Date;");
			} else if (fa.getType().equals("BigDecimal"))
			{
				importArea.add("\nimport java.math.BigDecimal;");
			}
			
			sb.append(",name=\"" + GeneratorUtil.subUptoLow(fa.getFieldName(), 0, 1) + "\"");
			if (fa.getColumnInfo().getLength() > 0)
			{
				sb.append(",length=" + fa.getColumnInfo().getLength());
			} else
			{
				sb.append(",length=250");
			}
		} else if (fa.getType().startsWith("EntitySet<"))
		{
			importArea.add("\nimport ddd.base.persistence.EntitySet;");
			sb.append(",joinColumn=\"" + GenerationParameter.entityNameLow + "Id\"");
			type = fa.getType().substring(fa.getType().indexOf("<") + 1, fa.getType().indexOf(">"));
			importArea.add("\n" + GeneratorUtil.getImport(new File(GenerationParameter.basePackagePath + "/entity"), type));
			
			if (fa.getColumnInfo().isComposition())
			{
				sb.append(",composition=true");
			} else
			{
				sb.append(",composition=false");
			}
		} else if (fa.getType().startsWith("Set<"))
		{
			importArea.add("\nimport java.util.Set;");
			sb.append(",joinColumn=\"" + GenerationParameter.entityNameLow + "Id\"");
			type = fa.getType().substring(fa.getType().indexOf("<") + 1, fa.getType().indexOf(">"));
			importArea.add("\n" + GeneratorUtil.getImport(new File(GenerationParameter.basePackagePath + "/entity"), type));
		} else if (fa.getType().startsWith("CodeTable<"))
		{
			type = fa.getType().substring(fa.getType().indexOf("<") + 1, fa.getType().indexOf(">"));
			sb.append(",codeTable=\"" + type + "\"");
			fa.setType("String");
		} else
		{
			importArea.add("\n" + GeneratorUtil.getImport(new File(GenerationParameter.basePackagePath + "/entity"), fa.getType()));
			sb.append(",name=\"" + GeneratorUtil.subUptoLow(fa.getType(), 0, 1) + "Id\"");
		}
		
		if (!fa.getColumnInfo().isNullable())
		{
			sb.append(",nullable=false");
		} else
		{
			sb.append(",nullable=true");
		}
		
		if (fa.getColumnInfo().isUnique())
		{
			sb.append(",unique=true");
		} else
		{
			sb.append(",unique=false");
		}
		
		if (fa.getColumnInfo().getComment() != null)
		{
			sb.append(",comment=\"" + fa.getColumnInfo().getComment() + "\"");
		} else
		{
			fa.getColumnInfo().setComment("");
			sb.append(",comment=\"\"");
		}
		
		sb.append(")\n");
		
		sb.append("\t" + fa.getVisitModifier() + " " + fa.getType() + " " + fa.getFieldName() + ";\n\n");
		
		return sb.toString();
	}
	
	private String generateFuncCode(FieldAttribute fa)
	{
		String res = EntityGenerator.gsfunc;
		String fieldNameUp = GeneratorUtil.subLowToUp(fa.getFieldName(), 0, 1);
		
		if (fa.getType().startsWith("EntitySet<") || fa.getType().startsWith("Set<"))
		{
			res = res.replace("{lazyLoad}", "lazyLoad(\"" + fa.getFieldName() + "\")");
			res = res.replace("{setLazyLoad}", "\t\tsuper.LazyFieidsLoaded.put(\"" + fa.getFieldName() + "\", true);\n");
		} else
		{
			res = res.replace("{lazyLoad}", "lazyLoad()");
			res = res.replace("{setLazyLoad}", "");
		}
		res = res.replace("{fieldType}", fa.getType());
		res = res.replace("{fieldNameUp}", fieldNameUp);
		res = res.replace("{fieldName}", fa.getFieldName());
		return res;
	}
	
	@Override
	public void generate()
	{
		StringBuffer sb = new StringBuffer();
		for (String string : importArea)
		{
			sb.append(string);
		}
		replacement.put("{importArea}", sb.toString());
		GeneratorUtil.generateFromTemplate(daoTemplateAbsPath, filePath, replacement, coverWrite);
	}
	
	public Boolean getCoverWrite()
	{
		return coverWrite;
	}
	
	public void setCoverWrite(Boolean coverWrite)
	{
		this.coverWrite = coverWrite;
	}
	
}
