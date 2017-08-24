package ddd.simple.dao.tree.impl;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.tree.ViewTreeNodeDao;
import ddd.simple.entity.tree.ViewTreeNode;

@Service
public class ViewTreeNodeDaoBean extends BaseDao implements ViewTreeNodeDao {

	public ViewTreeNode saveViewTreeNode(ViewTreeNode viewTreeNode) throws Exception {
		
		return this.save(viewTreeNode);
	}

	public void deleteViewTreeNode(Long viewTreeNodeId) throws Exception {
		
		this.deleteById(viewTreeNodeId, ViewTreeNode.class);

	}

	public ViewTreeNode updateViewTreeNode(ViewTreeNode viewTreeNode) throws Exception {
		
		return this.update(viewTreeNode);
	}

	public ViewTreeNode findViewTreeNodeById(Long viewTreeNodeId) throws Exception {
		
		return this.query(viewTreeNodeId, ViewTreeNode.class);
	}

	public ViewTreeNode findViewTreeNodeByViewTree(Long viewTreeId,int nodeIndex) throws Exception 
	{
		String whereSql = "and viewTreeId = "+viewTreeId+" and nodeIndex = "+nodeIndex+"";
		EntitySet<ViewTreeNode> viewTreeNodes = this.query(whereSql,ViewTreeNode.class);
		if(viewTreeNodes != null && viewTreeNodes.size() > 0)
		{
			return (ViewTreeNode)viewTreeNodes.toArray()[0];
		}
		return null;
	}

}
