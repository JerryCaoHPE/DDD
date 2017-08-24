package ddd.simple.entity.tree;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;
import ddd.simple.entity.listview.DataSource;

@ddd.base.annotation.Entity(name="viewTreeNode",label="树节点")
public class ViewTreeNode extends Entity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="nodeIndex",label="节点层级")
	private Integer nodeIndex;
	
	@Column(name="idColumn",label="ID字段")
	private String idColumn;
	
	@Column(name="titleColumn",label="TITLE字段")
	private String titleColumn;
	
	@Column(name="viewTreeId",composition=true,FKName="VTN_FK_VT")
	private ViewTree viewTree;
	
	@Column(name="issHierachical",label="是否为级联")
	private String issHierachical;
	
	@Column(name="hierColumn",label="级联字段名")
	private String hierachicalColumn;
	
	@Column(name="isLoad",label="是否加载")
	private String isLoad;
	
	@Column(name="icon",label="图标")
	private String icon;
	
	@Column(name="template",label="模板")
	private String template;
	
	@Column(name="nodeSql",label="节点sql语句")
	private String nodeSql;
	
	@Column(name="nodeChildSql",label="节点孩子sql语句")
	private String nodeChildSql;
	
	public String getIsLoad() {
		return isLoad;
	}

	public void setIsLoad(String isLoad) {
		this.isLoad = isLoad;
	}

	public String getIssHierachical() {
		return issHierachical;
	}
	
	public Integer getNodeIndex() {
		return nodeIndex;
	}

	public void setNodeIndex(Integer nodeIndex) {
		this.nodeIndex = nodeIndex;
	}

	public String getIdColumn() {
		return idColumn;
	}

	public void setIdColumn(String idColumn) {
		this.idColumn = idColumn;
	}

	public String getTitleColumn() {
		return titleColumn;
	}

	public void setTitleColumn(String titleColumn) {
		this.titleColumn = titleColumn;
	}

	public ViewTree getViewTree() {
		return viewTree;
	}

	public void setViewTree(ViewTree viewTree) {
		this.viewTree = viewTree;
	}


	public String isIssHierachical() {
		return issHierachical;
	}

	public void setIssHierachical(String issHierachical) {
		this.issHierachical = issHierachical;
	}



	public String getHierachicalColumn() {
		return hierachicalColumn;
	}

	public void setHierachicalColumn(String hierachicalColumn) {
		this.hierachicalColumn = hierachicalColumn;
	}

	public String isLoad() {
		return isLoad;
	}

	public void setLoad(String isLoad) {
		this.isLoad = isLoad;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getNodeSql() {
		return nodeSql;
	}

	public void setNodeSql(String nodeSql) {
		this.nodeSql = nodeSql;
	}

	public String getNodeChildSql() {
		return nodeChildSql;
	}

	public void setNodeChildSql(String nodeChildSql) {
		this.nodeChildSql = nodeChildSql;
	}
	
	
}
