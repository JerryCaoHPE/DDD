package ddd.simple.entity.billCode;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;
/**
 * 编码类型
 * @author xcy
 *
 */
@ddd.base.annotation.Entity(name="billCode",label="编码定义")
public class BillCode extends Entity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="billCodeTypeName",nullable=false,label="编码类型名称")
	private String billCodeTypeName;
	
	@Column(name="billCodeRule",nullable=false,label="编码规则",comment="编码的规则中包含常量、变量、序列号变量三种元素组成,每种元素可以多次出现，"
			+ "例如：N-${学院编号,3,0}-$[学生序列号,5,0],"
			+ "学院编号是变量，在生成编码时提供，3 是在编码中的长度，0 是指如果长度不足3位，用0在左边填充。"
			+ "学生序列号是序列号变量，在每个学院都会独立编号")
	private String billCodeRule;

	@Column(name="variables",label="变量",comment="编码规则中的变量，在编码中用${}标示")
	private String variables;
	
	@Column(name="seqences",label="序列号变量",comment="编码规则中的序列号变量，在编码中用$[]标示")
	private String seqences;

//	@Column(name="baseCodeRule",label="序列号变量的基本规则",comment="编码规则中序列号变量的基本规则，即去除序列变量后的规则，"
//			+ "例如规则“N-${学院编号,3,0}-$[学生序列号,5,0]”的序列号变量的基本名称为“N-${学院编号,3,0}-”")
//	private String baseCodeRule;
	
	@Column(name="sequenceStartIndex",label="序列号变量的开始位置",comment="序列号变量的开始位置，即$[]的开始位置")
	private Integer sequenceStartIndex;

	@Column(name="sequenceEndIndex",label="序列号变量的结束位置",comment="序列号变量的结束位置，即$[]的结束位置")
	private Integer sequenceEndIndex;
	
	
	@Column(name="entityName",label="实体名称",comment="使用编码规则的实体名称")
	private String entityName;
	
	@Column(name="fieldName",label="属性名称",comment="使用编码规则的实体的属性名称")
	private String fieldName;
	
	@Column(name="tableName",label="数据库表名",comment="使用编码规则的数据库表名")
	private String tableName;
	
	@Column(name="columnName",label="字段名",comment="使用编码规则的数据库表的字段名")
	private String columnName;
	
	
	public String getBillCodeTypeName() {
		return billCodeTypeName;
	}

	public void setBillCodeTypeName(String billCodeTypeName) {
		this.billCodeTypeName = billCodeTypeName;
	}

	public String getBillCodeRule() {
		return billCodeRule;
	}

	public void setBillCodeRule(String billCodeRule) {
		this.billCodeRule = billCodeRule;
	}

	public String getVariables()
	{
		return variables;
	}

	public void setVariables(String variables)
	{
		this.variables = variables;
	}

	public String getSeqences()
	{
		return seqences;
	}

	public void setSeqences(String seqences)
	{
		this.seqences = seqences;
	}

	public Integer getSequenceStartIndex()
	{
		return sequenceStartIndex;
	}

	public void setSequenceStartIndex(Integer sequenceStartIndex)
	{
		this.sequenceStartIndex = sequenceStartIndex;
	}

	public Integer getSequenceEndIndex()
	{
		return sequenceEndIndex;
	}

	public void setSequenceEndIndex(Integer sequenceEndIndex)
	{
		this.sequenceEndIndex = sequenceEndIndex;
	}

	/** 
	* @return baseCodeRule 
	*/ 
	
//	public String getBaseCodeRule()
//	{
//		return baseCodeRule;
//	}
//
//	/** 
//	* @param baseCodeRule 要设置的 baseCodeRule 
//	*/
//	
//	public void setBaseCodeRule(String baseCodeRule)
//	{
//		this.baseCodeRule = baseCodeRule;
//	}

	/** 
	* @return entityName 
	*/ 
	
	public String getEntityName()
	{
		return entityName;
	}

	/** 
	* @param entityName 要设置的 entityName 
	*/
	
	public void setEntityName(String entityName)
	{
		this.entityName = entityName;
	}

	/** 
	* @return fieldName 
	*/ 
	
	public String getFieldName()
	{
		return fieldName;
	}

	/** 
	* @param fieldName 要设置的 fieldName 
	*/
	
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	/** 
	* @return tableName 
	*/ 
	
	public String getTableName()
	{
		return tableName;
	}

	/** 
	* @param tableName 要设置的 tableName 
	*/
	
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	/** 
	* @return columnName 
	*/ 
	
	public String getColumnName()
	{
		return columnName;
	}

	/** 
	* @param columnName 要设置的 columnName 
	*/
	
	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}

}
