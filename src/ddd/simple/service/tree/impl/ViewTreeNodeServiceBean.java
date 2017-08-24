package ddd.simple.service.tree.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.simple.dao.tree.ViewTreeNodeDao;
import ddd.simple.entity.tree.ViewTreeNode;
import ddd.simple.service.tree.ViewTreeNodeService;

@Service
public class ViewTreeNodeServiceBean implements ViewTreeNodeService {
    
	@Resource(name="viewTreeNodeDaoBean")
	private ViewTreeNodeDao viewTreeNodeDao;
	
	public ViewTreeNode saveViewTreeNode(ViewTreeNode viewTreeNode) {
		
		try {
			return this.viewTreeNodeDao.saveViewTreeNode(viewTreeNode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("saveViewTreeNode", e.getMessage(), e);
		}
	}

	public void deleteViewTreeNode(Long viewTreeNodeId) {
 
		try {
			this.viewTreeNodeDao.deleteViewTreeNode(viewTreeNodeId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("deleteViewTreeNode", e.getMessage(), e);
		}
	}

	public ViewTreeNode updateViewTreeNode(ViewTreeNode viewTreeNode) {
		
		try {
			return this.viewTreeNodeDao.updateViewTreeNode(viewTreeNode);
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new DDDException("updateViewTreeNode", e.getMessage(), e);

		}
	}

	@Override
	public ViewTreeNode findViewTreeNodeById(Long viewTreeNodeId) {
		
		try {
			ViewTreeNode viewTreeNode = this.viewTreeNodeDao.findViewTreeNodeById(viewTreeNodeId);
			return viewTreeNode;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findViewTreeNodeById", e.getMessage(), e);

		}
	}

	
	public ViewTreeNode findViewTreeNodeByViewTree(Long viewTreeId,
			int nodeIndex) {
		try {
			ViewTreeNode viewTreeNode = this.viewTreeNodeDao.findViewTreeNodeByViewTree(viewTreeId,nodeIndex);
			return viewTreeNode;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("findViewTreeNodeByViewTree", e.getMessage(), e);

		}
	}

	

}
