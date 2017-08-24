package ddd.simple.entity.operationManual;

import java.io.Serializable;
import java.util.Collection;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;
import ddd.simple.entity.permission.Module;

@ddd.base.annotation.Entity(name="Manual",label="操作内容")
public class Manual extends Entity implements Serializable{

	/**
	 * 操作内容
	 * Manual
	 */
	private static final long serialVersionUID = 1L;

	@Column(label="内容标题")
	private String title;
	
	@Column(label="内容编码")
	private String code;
	
	@Column(label="内容")
	private String content;
	/*
	@Column(label="目录集合",joinColumn="manualId",composition=true)
    private Collection<Catalog> catalogs;*/
	
	@Column(name="catalogId",label="对应目录",FKName="M_FK_C")
	private  Catalog catalog;

	@Column(label="上级内容",name="parentId",FKName="M_FK_M")
	private Manual parent;
	
	@Column(label="下级内容", joinColumn="parentId",composition=true)
	private Collection<Manual> children;
	
	@Column(label="是否为一级内容")
	private String manualType;

	@Column(label="是否在用")
	private String isInUse;

	public String getTitle() {
		lazyLoad();
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		lazyLoad();
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContent() {
		lazyLoad();
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public Catalog getCatalog() {
		lazyLoad();
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
/*	public Collection<Catalog> getCatalogs() {
		lazyLoad("catalogs");
		return catalogs;
	}

	public void setCatalogs(Collection<Catalog> catalogs) {
		this.catalogs = catalogs;
	}*/

	public Manual getParent() {
		lazyLoad();
		return parent;
	}
	
	public void setParent(Manual parent) {
		this.parent = parent;
	}

	public Collection<Manual> getChildren() {
		lazyLoad("children");
		return children;
	}

	public void setChildren(Collection<Manual> children) {
		this.children = children;
	}
	
	public String getManualType() {
		lazyLoad();
		return manualType;
	}

	public void setManualType(String manualType) {
		this.manualType = manualType;
	}

	public String getIsInUse() {
		lazyLoad();
		return isInUse;
	}

	public void setIsInUse(String isInUse) {
		this.isInUse = isInUse;
	}
	
}
