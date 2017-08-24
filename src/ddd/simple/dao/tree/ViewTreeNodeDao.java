package ddd.simple.dao.tree;

import java.util.HashMap;

import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.tree.ViewTreeNode;


    public interface ViewTreeNodeDao extends BaseDaoInterface{
	

    public ViewTreeNode saveViewTreeNode(ViewTreeNode viewTreeNode) throws Exception;
	
	public void deleteViewTreeNode(Long viewTreeNodeId) throws Exception;
	
	public ViewTreeNode updateViewTreeNode(ViewTreeNode viewTreeNode) throws Exception;
	
	public ViewTreeNode findViewTreeNodeById(Long viewTreeNodeId) throws Exception;

	public ViewTreeNode findViewTreeNodeByViewTree(Long viewTreeId,int nodeIndex) throws Exception;


}
