package ddd.simple.dao.tree;

import java.util.Map;
import java.util.Set;

import ddd.simple.dao.base.BaseDaoInterface;
import ddd.simple.entity.tree.ViewTree;


    public interface ViewTreeDao  extends BaseDaoInterface{
	
    public ViewTree saveViewTree(ViewTree viewTree) throws Exception;
	
	public int deleteViewTree(Long viewTreeId) throws Exception;
	
	public ViewTree updateViewTree(ViewTree viewTree) throws Exception;
	
	public ViewTree findViewTreeById(Long viewTreeId) throws Exception;

	public ViewTree findViewTreeByKey(String viewTreeKey) throws Exception;
}
