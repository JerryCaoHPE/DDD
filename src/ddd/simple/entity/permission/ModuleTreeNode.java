package ddd.simple.entity.permission;

import java.util.List;

/**
 * 项目名称：DDD3 类名称：Node 类描述： 创建人：AnotherTen 创建时间：2015年12月24日 下午4:27:34 修改人：胡均
 * 修改时间：2015年12月24日 下午4:27:34 修改备注：
 * 
 * @version 1.0 Copyright (c) 2015 DDD
 */
public class ModuleTreeNode
{
	// id text name img nodes
	private String		id;
	private String		text;
	private String		name;
	private String		img;
	private String		icon;
	private String		routeData;
	private List<ModuleTreeNode>	nodes;
	
	
	
	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public String getRouteData()
	{
		return routeData;
	}
	
	public void setRouteData(String routeData)
	{
		this.routeData = routeData;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getImg()
	{
		return img;
	}
	
	public void setImg(String img)
	{
		this.img = img;
	}
	
	public List<ModuleTreeNode> getNodes()
	{
		return nodes;
	}
	
	public void setNodes(List<ModuleTreeNode> nodes)
	{
		this.nodes = nodes;
	}
	
	public void addNode(ModuleTreeNode node)
	{
		this.nodes.add(node);
	}
}
