package ddd.simple.dao.tree.impl;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import ddd.base.persistence.EntitySet;
import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.tree.ViewTreeDao;
import ddd.simple.entity.listview.ReportView;
import ddd.simple.entity.tree.ViewTree;

@Service
public class ViewTreeDaoBean extends BaseDao implements ViewTreeDao {

	public ViewTree saveViewTree(ViewTree viewTree) throws Exception {
		
		return this.save(viewTree);
	}

	public int deleteViewTree(Long viewTreeId) throws Exception {
		
		return this.deleteById(viewTreeId, ViewTree.class);

	}

	public ViewTree updateViewTree(ViewTree viewTree) throws Exception {
		
		return this.update(viewTree);
	}

	public ViewTree findViewTreeById(Long viewTreeId) throws Exception {
		
		return this.query(viewTreeId, ViewTree.class);
	}

	public ViewTree findViewTreeByKey(String viewTreeKey) throws Exception 
	{
		String whereSql = "and viewTreeKey = '"+viewTreeKey+"'";
		EntitySet<ViewTree> viewTrees = this.query(whereSql,ViewTree.class);
		if(viewTrees != null && viewTrees.size() > 0)
		{
			return (ViewTree)viewTrees.toArray()[0];
		}
		return null;
	}
}
