package ddd.base.annotation;
/**
 *@author 作者:吴桥伟
 *@version 创建时间：2015年1月28日 下午3:45:42
 * 注解的信息
 */
public class EntityInfo {
	private String label=""; 

	private String name="";

	private String comment="";

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
