/**
* @Title: Student.java
* @Package ddd.base.util
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年11月13日 下午10:08:04
* @version V1.0
*/
package ddd.base.util;

import java.util.Date;

/**
 * 项目名称：DDD3
 * 类名称：Student
 * 类描述：   
 * 创建人：DDD
 * 创建时间：2015年11月13日 下午10:08:04
 * 修改人：DDD
 * 修改时间：2015年11月13日 下午10:08:04
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public class Student
{
	private String name;
	private Date date;
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Date getDate()
	{
		return date;
	}
	public void setDate(Date date)
	{
		this.date = date;
	}
}
