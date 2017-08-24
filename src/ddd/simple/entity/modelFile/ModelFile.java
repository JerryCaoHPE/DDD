package ddd.simple.entity.modelFile;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;


@ddd.base.annotation.Entity(label="模板文件",name="modelFile")
public class ModelFile extends Entity
{
	private static final long serialVersionUID = 1L;
	
	@Column(label="唯一标识",comment="")
	private String modelKey;

	@Column(label="类型",comment="")
	private String type;

	@Column(label="类别",comment="")
	private String category;

	@Column(label="名称",comment="")
	private String name;
	
	

	public String getName()
	{
		lazyLoad();
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getModelKey() {
		lazyLoad();
		return this.modelKey;
	}

	public void setModelKey(String modelKey) {
		this.modelKey = modelKey;
	}

	public String getType() {
		lazyLoad();
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		lazyLoad();
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}


	
}