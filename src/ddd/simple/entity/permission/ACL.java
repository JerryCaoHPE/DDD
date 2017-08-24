package ddd.simple.entity.permission;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="acl",label="")
public class ACL extends Entity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name="moduleId",label="对应模块",FKName="CAL_FK_M")
	private Module module;
	
	@Column(name="roleId",label="对应角色",FKName="ACL_FK_R")
	private Role role;
	
	@Column(label="模块状态")
	private Long state;

	public Module getModule() {
		lazyLoad();
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public Role getRole() {
		lazyLoad();
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Long getState() {
		lazyLoad();
		return state;
	}

	public void setState(long state) {
		this.state = state;
	}
	
	
}
