package ddd.simple.service.tree;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ddd.simple.entity.tree.ViewTree;

public interface ViewTreeService {
	
    public ViewTree saveViewTree(ViewTree viewTree) ;
	
	public int deleteViewTree(Long viewTreeId);
	
	public ViewTree updateViewTree(ViewTree viewTree);
	
	public ViewTree findViewTreeById(Long viewTreeId);

	public ViewTree findViewTreeByKey(String viewTreeKey);
	
	public Set<Map<String ,Object>> findDataByViewTreeNodeIndex(Long viewTreeId,HashMap node);

}
