package ddd.simple.entity.permission;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="permission",label="权限")
public class Permission extends Entity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(label="名称")
	private String name;
	
	@Column(label="编码")
	private String code;
	
	@Column(name="moduleId",label="对应模块",FKName="P_FK_M")
	private Module module;
	
	@Column(label="前台展示用")
	private Boolean isAssigned;
	
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

	public Module getModule() {
		lazyLoad();
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public Boolean getIsAssigned() {
		lazyLoad();
		return isAssigned;
	}

	public void setIsAssigned(Boolean isAssigned) {
		this.isAssigned = isAssigned;
	}
	
}
