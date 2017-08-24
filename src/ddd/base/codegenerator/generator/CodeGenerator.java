package ddd.base.codegenerator.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import ddd.base.codegenerator.entity.FieldAttribute;
import ddd.base.codegenerator.entity.GenerationAttribute;
import ddd.base.codegenerator.generator.impl.PrimaryGenerator;
import ddd.base.codegenerator.generator.impl.java.entity.EntityGenerator;
import ddd.base.codegenerator.util.GeneratorUtil;
import ddd.base.util.SpringContextUtil;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.codeTable.CodeTypeService;

public class CodeGenerator {

	@Resource(name="codeTypeServiceBean")
	private static CodeTypeService codeTypeService = (CodeTypeService) SpringContextUtil.getBean("codeTypeServiceBean");
	public static void main(String[] args) {
		String text = GeneratorUtil.readFile("C:/Users/Administrator/Desktop/javaE.txt");
		
	}
	
	public synchronized static Map<String,String> generatorEntity (String text){
		GenerationAttribute ga = new GenerationAttribute();
		ArrayList<CodeTableConfig> CodeTableConfigs = new ArrayList<CodeTableConfig>();
		List<FieldAttribute> attris = new ArrayList<FieldAttribute>();
		Map<String, String> result = new HashMap<String, String>();
		result.put("successMsg", "");
		result.put("errorMsg", "");
		String[] rows = text.split("\n");
		for (int i = 0; i < rows.length; i++) {
			if((i+1)==rows.length){
				int tabNum=0;
				for (int k = 0; k < rows[i].length(); k++) {
					if(rows[i].charAt(k)=='\t'){
						tabNum++;
					}
				}
				if(tabNum<3){
					result.put("errorMsg", "第"+(i+1)+"行缺少数据，只有"+(tabNum)+"列");
					return result;
				}
				for (int j = 0; j < 6-tabNum; j++) {
					rows[i]=rows[i]+'\t';
				}
			}
			rows[i] = rows[i].replaceAll("\t", "\t ");
			String[] cols = rows[i].split("[\t\r]");
			int j = 0;
			for (; j < cols.length; j++) {
				cols[j] = cols[j].replaceFirst(" ", "");
				//System.out.print(cols[j]+"|");
			}
			//System.out.println();
			if(j<7){
				result.put("errorMsg", "第"+(i+1)+"行缺少数据，只有"+(j)+"列");
				return result;
			}
			
			if(i==0){
				if("".equals(cols[1])){
					result.put("errorMsg", "请写上实体名");
					return result;
				}
				if("".equals(cols[3])){
					result.put("errorMsg", "请写上中文名");
					return result;
				}
				if("".equals(cols[5])){
					result.put("errorMsg", "请写上模块名");
					return result;
				}
				ga.setClassNameUp(cols[1]);
				ga.getEntityInfo().setLabel(cols[3]);
				ga.setFullPackageName(GenerationParameter.BASE_PACKAGE+".entity."+cols[5]);
			}else if(i>=2){
				if(cols[6].equals("")){
					result.put("errorMsg", "字段编号为"+(i-1)+"，字段名称为"+cols[1]+",的备注为空");
					return result;
				}
				FieldAttribute fa = new FieldAttribute();
				fa.setVisitModifier("private");
				fa.setAttributeModifier("");
				String type = cols[2].replaceAll(" ", "");
				if(type.charAt(0)>'Z'||type.charAt(0)<'A'){
					result.put("errorMsg", "属性："+cols[1]+"的类型："+type+"应该使用包装类型");
					return result;
				}
				if(type.contains("(")){
					int startIndex = type.indexOf("(");
					fa.setType(type.substring(0,startIndex));
					fa.getColumnInfo().setLength(Integer.parseInt(type.substring(startIndex+1,type.length()-1)));
				}else{
					fa.setType(type);
				}
				fa.setFieldName(cols[1].replaceAll(" ", ""));
				fa.getColumnInfo().setLabel(cols[3]);
				if(cols[4].toLowerCase().contains("非空")){
					fa.getColumnInfo().setNullable(false);
				}
				if(cols[4].toLowerCase().contains("唯一")){
					fa.getColumnInfo().setUnique(true);
				}
				if(cols[5].toLowerCase().contains("true")){
					fa.getColumnInfo().setComposition(true);
				}
				if( type.contains("CodeTable")){
					int start = type.indexOf("<");
					int end = type.indexOf(">");
					String codeTypeKey = type.substring(start+1,end);
					List<String> codeTables = Arrays.asList(cols[5].split(","));
					CodeTableConfig codeTableConfig =new CodeTableConfig();
					codeTableConfig.setCodeTypeKey(codeTypeKey);
					codeTableConfig.setCodeTypeName(cols[3]);
					codeTableConfig.setCodeTables(codeTables);
					CodeTableConfigs.add(codeTableConfig);
				}
				fa.getColumnInfo().setComment(cols[6]);
				attris.add(fa);
			}
		}
		ga.setFieldAttributeList(attris);
		PrimaryGenerator pg = new PrimaryGenerator(ga);
		try {
			Class.forName(GenerationParameter.fullClassName);
			result.put("errorMsg", "已存在实体："+GenerationParameter.fullClassName);
			result.put("javaCode", GenerationParameter.javaCode);
			return result;
		} catch (ClassNotFoundException e) {
		}
		pg.generate();
		//实体生成成功之后去处理码表
		codeTypeService.createCodeTable(CodeTableConfigs);
		result.put("successMsg", GenerationParameter.fullClassName);
		result.put("javaCode", GenerationParameter.javaCode);
		return result;
	}
	
	public synchronized static Map<String, String> generatorEntityCode (String text){
		GenerationAttribute ga = new GenerationAttribute();
		List<FieldAttribute> attris = new ArrayList<FieldAttribute>();
		String[] rows = text.split("\n");
		for (int i = 0; i < rows.length; i++) {
			if((i+1)==rows.length){
				int tabNum=0;
				for (int k = 0; k < rows[i].length(); k++) {
					if(rows[i].charAt(k)=='\t'){
						tabNum++;
					}
				}
				for (int j = 0; j < 6-tabNum; j++) {
					rows[i]=rows[i]+'\t';
				}
			}
			rows[i] = rows[i].replaceAll("\t", "\t ");
			String[] cols = rows[i].split("[\t\r]");
			int j = 0;
			for (; j < cols.length; j++) {
				cols[j] = cols[j].replaceFirst(" ", "");
				//System.out.print(cols[j]+"|");
			}
			//System.out.println();
			if(i==0){
				ga.setClassNameUp(cols[1]);
				ga.getEntityInfo().setLabel(cols[3]);
				ga.setFullPackageName(GenerationParameter.BASE_PACKAGE+".entity."+cols[5]);
			}else if(i>=2){
				FieldAttribute fa = new FieldAttribute();
				fa.setVisitModifier("private");
				fa.setAttributeModifier("");
				String type = cols[2];
				if(type.contains("(")){
					int startIndex = type.indexOf("(");
					fa.setType(type.substring(0,startIndex));
					fa.getColumnInfo().setLength(Integer.parseInt(type.substring(startIndex+1,type.length()-1)));
				}else{
					fa.setType(type);
				}
				fa.setFieldName(cols[1]);
				fa.getColumnInfo().setLabel(cols[3]);
				if(cols[4].toLowerCase().contains("非空")){
					fa.getColumnInfo().setNullable(false);
				}
				if(cols[4].toLowerCase().contains("唯一")){
					fa.getColumnInfo().setUnique(true);
				}
				if(cols[5].toLowerCase().contains("true")){
					fa.getColumnInfo().setComposition(true);
				}
				if(!cols[6].equals("")){
					fa.getColumnInfo().setComment(cols[6]);
				}
				attris.add(fa);
			}
		}
		ga.setFieldAttributeList(attris);
		PrimaryGenerator pg = new PrimaryGenerator(ga);
		//pg.generate();
		EntityGenerator entityGenerator = new EntityGenerator();
		entityGenerator.setCoverWrite(null);
		entityGenerator.generate();
		Map<String, String> result= new HashMap<String, String>();
		result.put("fullClassName", GenerationParameter.fullClassName);
		result.put("javaCode", GenerationParameter.javaCode);
		return result;
	}
	
	

}
