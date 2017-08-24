package ddd.base.persistence;

import ddd.base.persistence.baseEntity.EntityClass;

public class JoinTableInfo {
	
	private String joinTableName;
	
	private String joinTableOneSide;
	
	private String joinTableManySide;
	
	private EntityClass<?> manySideEntityClass;

	public String getJoinTableName() {
		return joinTableName;
	}

	public void setJoinTableName(String joinTableName) {
		this.joinTableName = joinTableName;
	}

	public String getJoinTableOneSide() {
		return joinTableOneSide;
	}

	public void setJoinTableOneSide(String joinTableOneSide) {
		this.joinTableOneSide = joinTableOneSide;
	}

	public String getJoinTableManySide() {
		return joinTableManySide;
	}

	public void setJoinTableManySide(String joinTableManySide) {
		this.joinTableManySide = joinTableManySide;
	}

	public EntityClass<?> getManySideEntityClass() {
		return manySideEntityClass;
	}

	public void setManySideEntityClass(EntityClass<?> manySideEntityClass) {
		this.manySideEntityClass = manySideEntityClass;
	}
	
	

}
