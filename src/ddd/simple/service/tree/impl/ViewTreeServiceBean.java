package ddd.simple.service.tree.impl;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.dynamicSql.DynamicService;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.dao.tree.ViewTreeDao;
import ddd.simple.entity.tree.ViewTree;
import ddd.simple.entity.tree.ViewTreeNode;
import ddd.simple.service.listview.DataSourceService;
import ddd.simple.service.tree.ViewTreeNodeService;
import ddd.simple.service.tree.ViewTreeService;

@Service
public class ViewTreeServiceBean implements ViewTreeService {
    
	@Resource(name="viewTreeDaoBean")
	private ViewTreeDao viewTreeDao;
	
	@Resource(name="viewTreeNodeServiceBean")
	private ViewTreeNodeService viewTreeNodeService;
	
	@Resource(name="dataSourceServiceBean")
	private DataSourceService dataSourceService;
	
	public ViewTree saveViewTree(ViewTree viewTree) {
		
		try {
			return this.viewTreeDao.saveViewTree(viewTree);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveViewTree", e.getMessage(), e);
		}
	}

	public int deleteViewTree(Long viewTreeId) {
 
		try {
			return this.viewTreeDao.deleteViewTree(viewTreeId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteViewTree", e.getMessage(), e);
		}
	}

	public ViewTree updateViewTree(ViewTree viewTree) {
		
		try {
			return this.viewTreeDao.updateViewTree(viewTree);
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new DDDException("updateViewTree", e.getMessage(), e);

		}
	}

	public ViewTree findViewTreeById(Long viewTreeId) {
		
		try {
			ViewTree viewTree = this.viewTreeDao.findViewTreeById(viewTreeId);
			viewTree.getViewTreeNodes();
			return viewTree;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findViewTreeById", e.getMessage(), e);

		}
	}
	
	public ViewTree findViewTreeByKey(String viewTreeKey) {
		
		try {
			ViewTree viewTree = this.viewTreeDao.findViewTreeByKey(viewTreeKey);
			if(viewTree != null)
			{
				viewTree.getViewTreeNodes();
			}
			return viewTree;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findViewTreeByKey", e.getMessage(), e);

		}
	}
	
	public Set<Map<String ,Object>> findDataByViewTreeNodeIndex(Long viewTreeId,HashMap node)
	{
		try 
		{
			String issHierachical = node.get("issHierachical") == null ? "" : node.get("issHierachical").toString();
			Set<Map<String, Object>> results = this.getDataFromViewTreeNode(viewTreeId, node);
			if(results == null && issHierachical.equals("true"))
			{
				node.put("issHierachical", "false");
				results = this.getDataFromViewTreeNode(viewTreeId, node);
			}
			return results;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findViewTreeByKey", e.getMessage(), e);

		}
	}
	
	private Set<Map<String, Object>> getDataFromViewTreeNode(Long viewTreeId,HashMap node)
	{
		try
		{
			int nodeIndex = 0;
			String issHierachical = node.get("issHierachical") == null ? "" : node.get("issHierachical").toString();
			if(issHierachical.equals("true"))
			{
				nodeIndex = Integer.parseInt(node.get("nodeIndex").toString());
			}
			else
			{
				nodeIndex = Integer.parseInt(node.get("nodeIndex").toString()) + 1;
			}
			ViewTreeNode viewTreeNode = this.viewTreeNodeService.findViewTreeNodeByViewTree(viewTreeId,nodeIndex);
			if(viewTreeNode == null)
			{
				return null;
			}
			
			//原始sql语句
			String nodeSql = viewTreeNode.getNodeSql();
			//如果是级次节点，则加载级次节点数据
			if(issHierachical.equals("true"))
			{
				nodeSql = viewTreeNode.getNodeChildSql();
			}
			
			//解析后的sql语句
			nodeSql = DynamicService.getDynamicSql(nodeSql,node);
					//this.dataSourceService.getDynamicSQL("ViewTreeDisplay", nodeSql, node);
			Set<Map<String, Object>> results = this.viewTreeDao.query(nodeSql);
			if(results.size() == 0 || results == null)
			{
				return null;
			}
			else
			{
				return toTreeNodes(results,viewTreeNode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("getDataFromViewTreeNode", e.getMessage(), e);
	
		}
	}
	
	private Set<Map<String ,Object>> toTreeNodes(Set<Map<String, Object>> datas,
			ViewTreeNode viewTreeNode)
	{
		for(Map<String ,Object> object : datas)
		{
			object.put("Id", object.get("EId"));
			object.put("name", object.get(viewTreeNode.getTitleColumn()));
			object.put("isLoad", false);
			object.put("nodeIndex", viewTreeNode.getNodeIndex());
			object.put("issHierachical",viewTreeNode.getIssHierachical());
			object.put("idColumn", viewTreeNode.getIdColumn());
		}
		return datas;
	}
}
