package ddd.simple.entity.listview;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="reportcategory")
public class ReportCategory extends Entity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -576749192147794728L;
	
	@Column
	private String text;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
	
}
