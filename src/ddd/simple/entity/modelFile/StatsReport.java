/**
* @Title: StatsReport.java
* @Package ddd.simple.entity.modelFile
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年12月26日 下午3:36:52
* @version V1.0
*/
package ddd.simple.entity.modelFile;

import java.util.List;

import ddd.base.persistence.EntitySet;

/**
 * 项目名称：INVITE
 * 类名称：StatsReport
 * 类描述：   
 * 创建人：AnotherTen
 * 创建时间：2015年12月26日 下午3:36:52
 * 修改人：胡均
 * 修改时间：2015年12月26日 下午3:36:52
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public class StatsReport
{
	private String name;
	private EntitySet<ModelFile> modelFiles;
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public EntitySet<ModelFile> getModelFiles()
	{
		return modelFiles;
	}
	public void setModelFiles(EntitySet<ModelFile> modelFiles)
	{
		this.modelFiles = modelFiles;
	}
	
}
