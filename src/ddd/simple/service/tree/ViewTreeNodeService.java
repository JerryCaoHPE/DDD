package ddd.simple.service.tree;


import java.util.HashMap;

import ddd.simple.entity.tree.ViewTreeNode;

public interface ViewTreeNodeService {
	
    public ViewTreeNode saveViewTreeNode(ViewTreeNode viewTree) ;
	
	public void deleteViewTreeNode(Long viewTreeId);
	
	public ViewTreeNode updateViewTreeNode(ViewTreeNode viewTree);
	
	public ViewTreeNode findViewTreeNodeById(Long viewTreeId);

	public ViewTreeNode findViewTreeNodeByViewTree(Long viewTreeId,int nodeIndex);

}
