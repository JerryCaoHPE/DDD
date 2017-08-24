/**
* @Title: GeneratorDeletSql.java
* @Package ddd.base.util
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年12月15日 下午4:46:55
* @version V1.0
*/
package ddd.base.util;

import java.util.List;
import java.util.Map;

import ddd.base.Config;
import ddd.base.annotation.ColumnInfo;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;

/**
 * 项目名称：DDD3
 * 类名称：GeneratorDeletSql
 * 类描述：   扫描所有的实体类，并生成删除对应表的sql语句
 * 创建人：AnotherTen
 * 创建时间：2015年12月15日 下午4:46:55
 * 修改人：胡均
 * 修改时间：2015年12月15日 下午4:46:55
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public class GeneratorDeletSql
{
	public static void generatorDeletSql()
	{
		//获取所有的带有Entity注解的类
		List<Class<? extends Entity>> classes = ClassUtil.getClassListByAnnotation("ddd", ddd.base.annotation.Entity.class);
		for(int i =0;i<classes.size();i++){
			Class<? extends Entity> temp = classes.get(i);
			EntityClass<? extends Entity> entityClass = new EntityClass(temp);
			
			
			Map<String, ColumnInfo> one2many = entityClass.getOne2ManyFieldColumnInfos();
			for(ColumnInfo cloumnInfo : one2many.values()){
				System.out.println("DROP TABLE "+cloumnInfo.getJoinTableName());
			}
			String tableName = entityClass.getEntityInfo().getName();
			System.out.println("DROP TABLE "+tableName);
		}
	}
}
