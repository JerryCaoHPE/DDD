package ddd.simple.dao.exportAndImport;

import java.util.Map;

import ddd.simple.dao.base.BaseDaoInterface;

public interface ExportAndImportDao extends BaseDaoInterface {
	public int insertOneEntity(String tableName,Map<String, Object> insertInfoMap) throws Exception;
	
	public boolean isUpdateOneEntity(String tableName,Map<String, Object> insertInfoMap) throws Exception;
	
	public int updateOneEntity(String tableName,Map<String, Object> updateInfoMap,String where) throws Exception;
	
	public int insertEntities(String tableName, Map<String, Object>[] maps) throws Exception;
	
	public int updateEntities(String tableName, Map<String, Object>[] maps, String [] allUpdateWhere) throws Exception;

	public int deleteOneEntity(String tableName, String where) throws Exception;
	
}
