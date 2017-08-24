package ddd.simple.entity.codeTable;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.baseEntity.Entity;
@ddd.base.annotation.Entity(name="codeType",label="码表类型")
public class CodeType extends Entity implements Serializable{
   
	
	private static final long serialVersionUID = 1392524698899L;

	@Column(label = "类型名称")
	private String name;

	@Column(label = "类型编码")
	private String code;
	
	@Column(label = "类型唯一编码")
	private String codeTypeKey;
	
	@Column(label="编码集合",joinColumn="codeTypeId",composition=true)
    private EntitySet<CodeTable> codeTable;
	
	public String getName() {
		lazyLoad();
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		lazyLoad();
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
  
	public String getCodeTypeKey() {
		lazyLoad();
		return codeTypeKey;
	}

	public void setCodeTypeKey(String codeTypeKey) {
		this.codeTypeKey = codeTypeKey;
	}
	
	public EntitySet<CodeTable> getCodeTable() {
		lazyLoad("codeTable");
		return codeTable;
	}

	public void setCodeTable(EntitySet<CodeTable> codeTable) {
		this.codeTable = codeTable;
	}

}
