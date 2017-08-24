package ddd.simple.entity.permission;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name = "module", label = "模块")
public class Module extends Entity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(label = "模块编码")
	private String code;

	@Column(label = "模块名称")
	private String name;

	@Column(label = "路径", length = 1000)
	private String url;

	@Column(label = "路由")
	private String route;

	@Column(label = "图标")
	private String iconClass;

	@Column(label = "显示顺序")
	private Long displayIndex;

	@Column(name = "parentId", label = "父级模块", FKName = "M_FK_M")
	private Module parent;

	@Column(joinColumn = "parentId", composition = true)
	private EntitySet<Module> children;

	@Column(label = "是否为系统模块")
	private String moduleType;

	@Column(label = "是否在用")
	private String isInUse;

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

	public String getUrl() {
		lazyLoad();
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRoute() {
		lazyLoad();
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public Long getDisplayIndex() {
		return displayIndex;
	}

	public void setDisplayIndex(Long displayIndex) {
		this.displayIndex = displayIndex;
	}

	public String getIconClass() {
		lazyLoad();
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

	public Module getParent() {
		lazyLoad();
		return parent;
	}

	public void setParent(Module parent) {
		this.parent = parent;
	}

	public EntitySet<Module> getChildren() {
		lazyLoad("children");
		return children;
	}

	public void setChildren(EntitySet<Module> children) {
		super.LazyFieidsLoaded.put("children", true);
		this.children = children;
	}

	public String getModuleType() {
		lazyLoad();
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
}
