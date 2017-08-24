package ddd.simple.entity.tree;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="viewTree",label="树")
public class ViewTree extends Entity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name="viewTreeName",label="名称")
	private String viewTreeName ; //名称
	
	@Column(name="viewTreeKey",label="唯一标示")
	private String viewTreeKey ; //唯一标示
	
	@Column(joinColumn="viewTreeId",composition=true)
	private EntitySet<ViewTreeNode> viewTreeNodes;
	
	public String getViewTreeName() {
		return viewTreeName;
	}

	public void setViewTreeName(String viewTreeName) {
		this.viewTreeName = viewTreeName;
	}

	public String getViewTreeKey() {
		return viewTreeKey;
	}

	public void setViewTreeKey(String viewTreeKey) {
		this.viewTreeKey = viewTreeKey;
	}



	public EntitySet<ViewTreeNode> getViewTreeNodes() {
		lazyLoad("viewTreeNodes");
		return viewTreeNodes;
	}

	public void setViewTreeNodes(EntitySet<ViewTreeNode> viewTreeNodes) {
		super.LazyFieidsLoaded.put("viewTreeNodes", true);
		this.viewTreeNodes = viewTreeNodes;
	}
}
