package ddd.simple.dao.exportAndImport.impl;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.exportAndImport.ExportAndImportDao;

@Service
public class ExportAndImportDaoBean  extends BaseDao implements ExportAndImportDao{
	@Override
	public int insertEntities (String tableName ,Map<String,Object>[] maps) throws Exception{
		return this.save(tableName, maps).length;
	}
	
	@Override
	public int updateEntities(String tableName ,Map<String,Object> [] maps,String [] allUpdateWhere) throws Exception{
		int result=0;
		
		for(int i =0;i<maps.length;i++){
			result+=this.update(tableName, maps[i], allUpdateWhere[i]);
		}
		
		return result;
	}
	
	@Override
	public int insertOneEntity(String tableName,Map<String, Object> insertInfos) throws Exception {
		return this.saveMiddleTable(tableName, insertInfos);
	}

	@Override
	public int deleteOneEntity(String tableName,String where) throws Exception{
		return this.delete(tableName, where);
	}
	
	@Override
	public int updateOneEntity(String tableName,Map<String, Object> updateInfoMap,String where) throws Exception {
		return this.update(tableName, updateInfoMap,where);
	}

	@Override
	public boolean isUpdateOneEntity(String tableName,
			Map<String, Object> insertInfos) throws Exception {
		String select="select ";
		
		String where=" from "+tableName +" where 1=1 ";
		
		for (Map.Entry<String, Object> entry : insertInfos.entrySet()) {
			select+=entry.getKey()+",";
			where+=" and "+entry.getKey()+" = " + entry.getValue() ;
		}
		
		select=select.substring(0, select.length()-1);
		
		return this.query(select+where).size()>0?true:false;
	}
}
