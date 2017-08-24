package ddd.simple.entity.operationManual;

import java.io.Serializable;
import java.util.Collection;
import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="Catalog",label="目录")
public class Catalog extends Entity implements Serializable{
	/**
	 * 目录
	 * Catalog
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(label="目录编码")
	private String code;
	
	@Column(label="目录名称")
	private String name;
	
	@Column(label="图标")
	private String iconClass;

	@Column(label="内容集合",joinColumn="catalogId",composition=true)
    private Collection<Manual> manuals;

	@Column(label="上级目录",name="parentId",FKName="C_FK_C")
	private Catalog parent;
	
	@Column(label="下级目录", joinColumn="parentId",composition=true)
	private Collection<Catalog> children;
	
	@Column(label="是否为一级目录")
	private String catalogType;

	@Column(label="是否在用")
	private String isInUse;
	
	public String getIconClass() {
		lazyLoad();
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}
	
	public String getCatalogType() {
		lazyLoad();
		return catalogType;
	}

	public void setCatalogType(String catalogType) {
		this.catalogType = catalogType;
	}

	public String getIsInUse() {
		lazyLoad();
		return isInUse;
	}

	public void setIsInUse(String isInUse) {
		this.isInUse = isInUse;
	}

	public String getCode() {
		lazyLoad();
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		lazyLoad();
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Manual> getManuals() {
		lazyLoad("manuals");
		return manuals;
	}

	public void setManuals(Collection<Manual> manuals) {
		this.manuals = manuals;
	}

	public Catalog getParent() {
		lazyLoad();
		return parent;
	}

	public void setParent(Catalog parent) {
		this.parent = parent;
	}

	public Collection<Catalog> getChildren() {
		lazyLoad("children");
		return children;
	}

	public void setChildren(Collection<Catalog> children) {
		this.children = children;
	}

	
	
}
