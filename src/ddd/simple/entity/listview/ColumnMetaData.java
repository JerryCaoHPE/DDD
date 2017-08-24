package ddd.simple.entity.listview;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;
import ddd.simple.util.HtmlReportUtil;

@ddd.base.annotation.Entity(name="columnmetadata")
public class ColumnMetaData extends Entity implements Serializable,Cloneable
{
	private static final long serialVersionUID = 1L;
	
	@Column
	private String columnKey="";

	@Column
	private String columnValue="";

	@Column
	private String type="text";

	@Column
	private Integer columnIndex = 0 ;
	
	@Column(name="dataSourceId",composition=true,FKName="CMD_FK_DS")
	private DataSource dataSource;
	
	
	public DataSource getDataSource() {
		lazyLoad();
		return this.dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ColumnMetaData(){}
	
	public ColumnMetaData(String columnKey ,String columnValue ,Integer columnIndex ,String type){
		this.columnIndex = columnIndex;
		this.columnKey = columnKey;
		this.columnValue = columnKey;
		
		this.transformColumnType(type);
	}
	
	public void transformColumnType(String type){
		if(type == null)return ;
		
		if(type.equalsIgnoreCase("BIGINT")||type.equalsIgnoreCase("LONG")){//long
			this.type = HtmlReportUtil.COLUMN_TYPE_DIGIT;
		}if(type.equalsIgnoreCase("INT")||type.equalsIgnoreCase("NUMBER")){//int
			this.type = HtmlReportUtil.COLUMN_TYPE_DIGIT;
		}else if(type.equalsIgnoreCase("DATETIME")||type.equalsIgnoreCase("DATE")){//date
			this.type = HtmlReportUtil.COLUMN_TYPE_DATE;
		}else if(type.equalsIgnoreCase("VARCHAR")||type.equalsIgnoreCase("VARCHAR2")
				||type.equalsIgnoreCase("LONGTEXT")||type.equalsIgnoreCase("CLOB")){//string
			this.type = HtmlReportUtil.COLUMN_TYPE_TEXT;
		}
	}

	public String getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}

	public String getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(Integer columnIndex) {
		this.columnIndex = columnIndex;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override  
    public Object clone() throws CloneNotSupportedException  
    {  
        return super.clone();  
    }  
	

}