package ddd.simple.service.billCode.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.stereotype.Service;

import ddd.base.annotation.ColumnInfo;
import ddd.base.annotation.Entity;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.simple.dao.billCode.BillCodeDao;
import ddd.simple.entity.billCode.BillCode;
import ddd.simple.service.base.BaseService;
import ddd.simple.service.base.InternalParamHandler;
import ddd.simple.service.billCode.BillCodeService;

@Service
public class BillCodeServiceBean extends BaseService implements BillCodeService
{	
	@Resource(name="billCodeDaoBean")
	private BillCodeDao billCodeDao;
	
	@Resource(name="internalParamHandler")
	private InternalParamHandler internalParamHandler;
	
	@Override
	public BillCode saveBillCode(BillCode billCode) 
	{
		try {
			this.parseBillCode(billCode);
			return this.billCodeDao.saveBillCode(billCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveBillCode", e.getMessage(), e);
		}
	}

	@Override
	public int deleteBillCode(Long billCodeId) {
		try {
			return this.billCodeDao.deleteBillCode(billCodeId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteBillCode", e.getMessage(), e);
		}
		
	}

	@Override
	public BillCode updateBillCode(BillCode billCode) {
		try {
			this.parseBillCode(billCode);
			return this.billCodeDao.updateBillCode(billCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("updateBillCode", e.getMessage(), e);
		}
	}
	
	private void parseBillCode(BillCode billCode)
	{
		Pattern pattern =  null;
		Matcher matcher = null;
		int sequenceStartIndex = -1;
		int sequenceEndIndex = -1;

		//N-${学院编号,3,0}-$[学生序列号,5,0],
		pattern = Pattern.compile("\\$\\[.*?\\]");
		matcher = pattern.matcher(billCode.getBillCodeRule());
		List<String> seqences = new ArrayList<String>();
		while(matcher.find())
		{
			String seqence = matcher.group();
			sequenceStartIndex = matcher.start();
			sequenceEndIndex = matcher.end();
			billCode.setSequenceStartIndex(sequenceStartIndex);
			billCode.setSequenceEndIndex(sequenceEndIndex);
			seqences.add(seqence);
		}
		if(seqences.size() != 1 )
		{
			DDDException dddException = new DDDException("编码规则中只能有一个序列变量，即$[]只能出现一次,且必须出现一次，编码："+billCode.getBillCodeRule());
			dddException.putExtendedData("编码名称：", billCode.getBillCodeTypeName());
			dddException.putExtendedData("编码规则：", billCode.getBillCodeRule());
			throw dddException;
		}
		else
		{
			billCode.setSeqences(seqences.get(0));
			Variable variable = new Variable(billCode.getSeqences());
			//计算在实际生成的编码中，序列在编码中的结束位置
			billCode.setSequenceEndIndex(billCode.getSequenceEndIndex()-billCode.getSeqences().length()+variable.length);
		}
 
		String baseCodeRule = billCode.getBillCodeRule();//.replace(billCode.getSeqences(), "序列");

		//N-${学院编号,3,0}-$[学生序列号,5,0],
		pattern = Pattern.compile("\\$\\{.*?\\}");
		matcher = pattern.matcher(billCode.getBillCodeRule());
		String variables = "";
		while(matcher.find())
		{
			String variableStr = matcher.group();
			variables+=variableStr+"\n";
			
			Variable variable = new Variable(variableStr);
			
			//计算在实际生成的编码中，序列在编码中的开始位置，请注意，以下算法中变量和序列在编码中的长度必须是固定的值
			if(matcher.start() < sequenceStartIndex)
			{
				billCode.setSequenceStartIndex(billCode.getSequenceStartIndex()-variableStr.length()+variable.length);
				billCode.setSequenceEndIndex(billCode.getSequenceEndIndex()-variableStr.length()+variable.length);
			}	
		}
		billCode.setVariables(variables.trim());
		
		//把实体和属性的名称转换为表名和字段名
		String tableName = ""; 
		String columnName = "";
		if(billCode.getEntityName() != null && !"".equals(billCode.getEntityName().trim()))
		{
			@SuppressWarnings("unchecked")
			EntityClass<? extends Entity>  entityClass = (EntityClass<? extends Entity> )SessionFactory.getEntityClassByName(billCode.getEntityName());
			if(entityClass != null)
			{
				tableName = entityClass.getEntityInfo().getName();
				ColumnInfo columnInfo = entityClass.getFieldColumnInfo(billCode.getFieldName());
				if(columnInfo == null)
				{
					throw new DDDException("replaceSequenceVariable",
							"在查找编码所对应的实体属性定义时出错，实体名为："+billCode.getEntityName()+",属性名为："+billCode.getFieldName());
				}
				columnName = columnInfo.getName();
			}
			else
			{
				throw new DDDException("replaceSequenceVariable", "在查找编码所对应的数据库表名出错，实体名为："+billCode.getEntityName());
			}
		}
		billCode.setTableName(tableName);
		billCode.setColumnName(columnName);
	}

	@Override
	public BillCode findBillCodeById(Long billCodeId) {
		try {
			return this.billCodeDao.findBillCodeById(billCodeId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findBillCodeById", e.getMessage(), e);
		}
	}
	
	@Override
	public EntitySet<BillCode> findAllBillCode() {
		try{
			return this.billCodeDao.findAllBillCode();
		}catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findAllBillCode", e.getMessage(), e);
		}
	}
	@Override
	public String genNewBillCode(String billCodeTypeName)
	{
		Map<String,Object>  variables = new HashMap<String, Object>();
		return this.genNewBillCode(billCodeTypeName, variables);
	}
	@Override
	public String genNewBillCode(String billCodeTypeName,Map<String,Object> variables)
	{
		BillCode billCode = null;
		try
		{
			billCode = this.billCodeDao.findBillCodeByName(billCodeTypeName);
		} catch (Exception e)
		{
			throw new DDDException("查找编码类型:"+billCodeTypeName+"出错，原因是："+e.getMessage(), e);
		}
		if(billCode == null)
		{
			throw new DDDException("没有找到编码类型:"+billCodeTypeName+"，请确认其是否存在");
		}
		
		if(variables == null)
		{
			variables = new HashMap<String, Object>();
		}

		variables = this.internalParamHandler.addInternalParam(variables);
		
		String code = this.replaceVariables(billCode,variables);
		
		return code;
	}
	
	private String replaceVariables(BillCode billCode,Map<String,Object> variables)
	{
		String billCodeRule = billCode.getBillCodeRule();
		String ruleVariables = billCode.getVariables();
		String seqenceVariable = billCode.getSeqences();
		
		String[]  ruleVariablesArray = ruleVariables.split("\\Q\n\\E");
		Map<String,Object> ruleVariablesList = new HashMap<String, Object>();
		for(String ruleVariable:ruleVariablesArray)
		{
			if(ruleVariable != null && ruleVariable.length() >3)
			{
				Variable variable = new Variable(ruleVariable);
				String value = "";
				if(variables.get(variable.name) != null)
				{
					value = variables.get(variable.name).toString();
				}
				else
				{
					throw new DDDException("replaceVariables", "在变量中找不名称为："+variable.name+" 的变量，请检查");
				}
				value = StringUtils.leftPad(value,variable.length, variable.padding);
				if(value.length() > variable.length)
				{
					throw new DDDException("replaceVariables", "在生成编码时出错，原因是变量 "+variable.name+" 的值为 "
							+variables.get(variable.name).toString()+",其长度大于变量规定的长度 "+variable.length+",这是不允许的，请修改编码规则。");
				}
				ruleVariablesList.put(variable.variableStr, value);
				
			}
		}
		StrSubstitutor strSubstitutor = new StrSubstitutor(ruleVariablesList, "${", "}");
		String baseCode = strSubstitutor.replace(billCodeRule);
		
		String sequenceCode = this.replaceSequenceVariable(billCode, baseCode);
		
		//String code = strSubstitutor.replace(sequenceCode);
		
		return sequenceCode;
	}
	 
	private String replaceSequenceVariable(BillCode billCode,String baseCode)
	{
	
		Long sequence = SessionFactory.getNewPrimarykey(baseCode,billCode);
		
	 
		return this.replaceSequenceVariable(billCode, baseCode, sequence);
	}
	public String replaceSequenceVariable(BillCode billCode,String baseCode,Long sequence)
	{
	
		String seqenceVariable = billCode.getSeqences();
		
		Map<String,Object> ruleVariablesList = new HashMap<String, Object>();
		if(seqenceVariable == null || seqenceVariable.trim().length() <= 3)
		{
			return null;
		}
		Variable seqenceVariable1 = new Variable(seqenceVariable);
		String value =sequence.toString();//
		value = StringUtils.leftPad(value,seqenceVariable1.length, seqenceVariable1.padding);
		ruleVariablesList.put(seqenceVariable1.variableStr, value);
		
		StrSubstitutor strSubstitutor = new StrSubstitutor(ruleVariablesList, "$[", "]");
		String code = strSubstitutor.replace(baseCode);
		
		return code;
	}
	public class Variable
	{
		public String name;
		public int length;
		public String padding;
		public String variableStr ;
		public Variable(String variableStr)
		{
			//2是因为要去掉前面的：${
			variableStr = variableStr.trim().substring(2,variableStr.length()-1);
			this.variableStr = variableStr;
			String[] parts = variableStr.split(",");
			if(parts.length ==2)
			{
				this.padding ="0";
			}
			else if(parts.length ==3)
			{
				this.name = parts[0].trim();
				this.length = Integer.parseInt(parts[1]);
				this.padding =parts[2];
			}
		}
	}
}
