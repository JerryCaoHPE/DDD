/**
* @Title: TestOutInterface.java
* @Package ddd.simple.service.exportAndImport.impl
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年9月25日 上午10:55:26
* @version V1.0
*/
package ddd.simple.service.exportAndImport.impl;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 项目名称：DDD3
 * 类名称：TestOutInterface
 * 类描述：   
 * 创建人：AnotherTen
 * 创建时间：2015年9月25日 上午10:55:26
 * 修改人：胡均
 * 修改时间：2015年9月25日 上午10:55:26
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public class TestOutInterface
{
	public void test(String tableName,ArrayList<HashMap<String,String>> datas){
		System.out.println("调用外部接口成功");
	}
}
