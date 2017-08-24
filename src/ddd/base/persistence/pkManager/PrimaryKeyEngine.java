package ddd.base.persistence.pkManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

import ddd.base.Config;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.Session;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.base.util.SpringContextUtil;
import ddd.simple.dao.billCode.BillCodeDao;
import ddd.simple.entity.billCode.BillCode;
import ddd.simple.service.billCode.impl.BillCodeServiceBean;

/**
 * 主键服务
 * 
 * @author Administrator
 *
 */
public class PrimaryKeyEngine
{
	
	// private static Connection connection ;
	/**
	 * String是app, String 是表名, PrimaryKey主键信息
	 */
	private static Map<String, PrimaryKey>	primaryKeys;
	
	public static void init()
	{
		loadPrimaryKeyInfo();
		resynchronizeTableCurrentKey();
		resynchronizeBillCodeCurrentKey();
	}
 
	private static void loadPrimaryKeyInfo()
	{
		String where = "serverId='" + Config.serverId + "'";
		Session session = SessionFactory.getThreadSession();
		try
		{
			EntitySet<PrimaryKey> primaryKeysRs = (EntitySet<PrimaryKey>) session.getByWhere(where, null, PrimaryKey.class);
			setPrimaryKeys(new HashMap<String, PrimaryKey>());
			if (primaryKeysRs != null && primaryKeysRs.size() > 0)
			{
				Iterator<PrimaryKey> iterable = primaryKeysRs.iterator();
				while (iterable.hasNext())
				{
					PrimaryKey primaryKey = iterable.next();
					getPrimaryKeys().put(primaryKey.getName(), primaryKey);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("从数据库加载序列号配置出错（PrimaryKey）出错，原因是：" + e.getMessage());
		} finally
		{
		}
	}
	
	/**
	 * 检查是否有没被维护主键的表，如果有没被维护主键的表，就添加到主键表中
	 * 	
	 * @param singleServerId
	 */
	private static void resynchronizeTableCurrentKey()
	{
		Iterator<PrimaryKey>  primaryKeyIterator = primaryKeys.values().iterator();
		while(primaryKeyIterator.hasNext())
		{
			PrimaryKey primaryKey = primaryKeyIterator.next();
			if(!"table".equals(primaryKey.getKeyType())) continue;
			primaryKey.getTableName();
			long maxKey = getTableMaxKey(primaryKey);
			if(maxKey == 0)
			{
				maxKey = primaryKey.getStartKey();
			}
			primaryKey.setCurrentKey(maxKey);
			updatePrimarykey(primaryKey);
		}
		
		Map<Class<? extends Entity>, EntityClass<? extends Entity>> entityClasses = SessionFactory.getEntityClasses();
		for (Class<?> key : entityClasses.keySet())
		{
			EntityClass<?> entityClass = entityClasses.get(key);
			String name = entityClass.getEntityInfo().getName().toLowerCase();
			
			if (primaryKeys.get(name) == null)
			{
				PrimaryKey primaryKey = createPrimaryKey(name);
				
				synchronized (primaryKeys)
				{
					primaryKeys.put(primaryKey.getName(), primaryKey);
				}
			} else
			{
				PrimaryKey primaryKey = primaryKeys.get(name);
				long maxKey = getTableMaxKey(primaryKey);
				if(maxKey == 0)
				{
					maxKey = primaryKey.getStartKey();
				}
				primaryKey.setCurrentKey(maxKey);
				updatePrimarykey(primaryKey);
			}
		}
	}
	
	
	private static void resynchronizeBillCodeCurrentKey()
	{
		for (PrimaryKey primaryKey : primaryKeys.values())
		{
			if( !"billcode".equals(primaryKey.getKeyType()))
			{
				continue;
			}
			resynchronizeBillCodeCurrentKey(primaryKey);
		}
		
	}
	private static boolean resynchronizeBillCodeCurrentKey(PrimaryKey primaryKey)
	{
		BillCodeDao billCodeDao = (BillCodeDao)SpringContextUtil.getBean("billCodeDaoBean");
		BillCodeServiceBean  billCodeService = (BillCodeServiceBean)SpringContextUtil.getBean("billCodeServiceBean");

		BillCode billCode = null;
		try
		{
			billCode = billCodeDao.findBillCodeByName(primaryKey.getBillCodeTypeName());
		} catch (Exception e)
		{
			System.out.println("同步编码的当前序列号出错误，原因是："+e.getMessage());
			e.printStackTrace();
			return false;
		}
		String baseCode = StringUtils.substringAfterLast(primaryKey.getName(),"$$");
		String minCode = billCodeService.replaceSequenceVariable(billCode, baseCode,primaryKey.getMinKey());
		String maxCode = billCodeService.replaceSequenceVariable(billCode, baseCode,primaryKey.getMaxKey());
		
		String sql ="select max(substr("+primaryKey.getColumnName()+","+(billCode.getSequenceStartIndex()+1)+","
				+(billCode.getSequenceEndIndex()-billCode.getSequenceStartIndex()+1)
				+")) as code from "+ primaryKey.getTableName() + " where " + primaryKey.getColumnName()
				+" between '"+minCode+"' and '"+maxCode+"'";
		
		Set<Map<String, Object>> codes = null;
		try
		{
			codes = SessionFactory.getThreadSession().get(sql,new String[]{},new String[]{"code"});
		} catch (Exception e)
		{
			System.out.println("同步编码的当前序列号出错误，原因是："+e.getMessage());
			e.printStackTrace();
			return false;		 
		}
		if(codes == null || codes.size() ==0)
		{
			primaryKey.setCurrentKey(primaryKey.getStartKey()-1);
			return true;
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Object> codeMap = (Map<String, Object>)(codes.toArray()[0]);
		if(codeMap == null || codeMap.get("code") == null)
		{
			primaryKey.setCurrentKey(primaryKey.getStartKey()-1);
			return true;
		}
		@SuppressWarnings("unchecked")
		String code =  ((Map<String, Object>)(codes.toArray()[0])).get("code").toString();
		BillCodeServiceBean.Variable variable = billCodeService.new Variable(billCode.getSeqences());
		//移出前面的所有填充字符
		for(int i=1;i<variable.length;i++)
		{
			code = StringUtils.removeStart(code, variable.padding);
		}
		primaryKey.setCurrentKey(Long.parseLong(code));
		
		return true;
	}
 
	private static PrimaryKey createPrimaryKey(String name)
	{
		return createPrimaryKey(name,name ,"EId","table");
	}
	
	private static PrimaryKey createPrimaryKey(String name,String tableName, String columnName,String keyType)
	{
		PrimaryKey primaryKey =createPrimaryKey(name,tableName,columnName,keyType," ");
		
		return primaryKey;
	}
	private static PrimaryKey createPrimaryKey(String name,String tableName, String columnName,String keyType,String billCodeTypeName)
	{
		PrimaryKey primaryKey = new PrimaryKey();
		primaryKey.setServerId(Config.serverId);
		primaryKey.setName(name);
		primaryKey.setTableName(tableName);
		primaryKey.setColumnName(columnName);
		primaryKey.setKeyType(keyType);
		primaryKey.setBillCodeTypeName(billCodeTypeName);
		
		primaryKey.setCachedSize(Config.primaryKeyCachedSize);
		Long minKey = getMaxKey(name) + 1;
		primaryKey.setMinKey(minKey);
		primaryKey.setMaxKey(minKey + Config.primaryKeySegmentSize);
		primaryKey.setStartKey(primaryKey.getMinKey());
		primaryKey.setEndKey(primaryKey.getMinKey() + primaryKey.getCachedSize());
		Long currentKey = minKey;
		if("table".equals(primaryKey.getKeyType()))
		{
			currentKey = getTableMaxKey(primaryKey);
			if(currentKey == 0)
			{
				currentKey = minKey;
			}
		}
	 
		
		primaryKey.setCurrentKey(currentKey);
		
		Long eId = getMaxPrimaryKeyEId()+1;
		primaryKey.setEId(eId);
		
		savePrimarykey(primaryKey);
		
		if("billcode".equals(primaryKey.getKeyType()))
		{
			resynchronizeBillCodeCurrentKey(primaryKey);
		}
		return primaryKey;
	}	
	private static Long getTableMaxKey(PrimaryKey primaryKey)
	{
		if(! "table".equals(primaryKey.getKeyType())) return 0l;
		if(primaryKey.getName() != null && primaryKey.getName().contains("错误")){
			return 0l;
		}
		
		String sql = "select MAX(" + primaryKey.getColumnName() + ") max from " + primaryKey.getName() + " where "
				+ primaryKey.getColumnName() + ">=" + primaryKey.getMinKey() + " and " + primaryKey.getColumnName() + "<="
				+ primaryKey.getMaxKey();
		Session session = SessionFactory.getNewSession();
		try
		{
			Object maxObject = session.getScalar(sql);
			Long maxKey = 0l;
			if (maxObject != null)
			{
				maxKey = Long.parseLong(session.getScalar(sql).toString());
			}
			return maxKey;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("取表中字段的最大值出错，原因是：" + e.getMessage() + ",sql:" + sql, e);
		} finally
		{
			session.releaseSession();
		}
	}
	private static Long getMaxPrimaryKeyEId()
	{
		String sql = "select MAX(eid) max from  primaryKey";
		Session session = SessionFactory.getNewSession();
		try
		{
			Object maxObject = session.getScalar(sql);
			Long maxKey = 0l;
			if (maxObject != null)
			{
				maxKey = Long.parseLong(session.getScalar(sql).toString());
			}
			return maxKey;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("取表中字段的最大值出错，原因是：" + e.getMessage() + ",sql:" + sql, e);
		} finally
		{
			session.releaseSession();
		}
	}	
	private static Long getMaxKey(String name)
	{
		String sql = "select max(maxKey) as maxKey from primaryKey where name='" + name + "'";
		Session session = SessionFactory.getNewSession();
		try
		{
			Object maxObject = session.getScalar(sql);
			Long maxKey = 0l;
			if (maxObject != null)
			{
				maxKey = Long.parseLong(session.getScalar(sql).toString());
			}
			return maxKey;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("取序列号的最大值出错，原因是：" + e.getMessage() + ",sql:" + sql, e);
		} finally
		{
			session.releaseSession();
		}
		
	}
	
	public static <T> Long getNewPrimaryKey(Class<T> clazz)
	{
		EntityClass<?> entityClass = SessionFactory.getEntityClass(clazz);
		String name = entityClass.getEntityInfo().getName();
		return getNewPrimaryKey(name);
	}
	
	public static Long getNewPrimaryKey(String name)
	{
		return getNewPrimaryKey(name, "EId");
	}
	public static Long getNewPrimaryKey(String name, String fieldName)
	{
		return getNewPrimaryKey(name,name,fieldName);
	}
	
	
	public static Long getNewPrimaryKey(String baseCode,BillCode billCode)
	{
		String name = "billcode_"+billCode.getTableName()+"_"+billCode.getColumnName()+"$$" +baseCode;
		PrimaryKey primaryKey = primaryKeys.get(name);
		if (primaryKey == null)
		{
			primaryKey = createPrimaryKey(name,billCode.getTableName(), billCode.getColumnName(),"billcode",billCode.getBillCodeTypeName());
			synchronized (primaryKeys)
			{
				primaryKeys.put(primaryKey.getName(), primaryKey);
			}
		}
		return getNewPrimaryKey(primaryKey); 		
	}
	public static Long getNewPrimaryKey(String name,String tableName, String fieldName)
	{
		name = name.toLowerCase();
		PrimaryKey primaryKey = primaryKeys.get(name);
		if (primaryKey == null)
		{
			primaryKey = createPrimaryKey(name,tableName, fieldName,"table");
			synchronized (primaryKeys)
			{
				primaryKeys.put(primaryKey.getName(), primaryKey);
			}
		}
		return getNewPrimaryKey(primaryKey); 
	}
	private static Long getNewPrimaryKey(PrimaryKey primaryKey)
	{
		String name = primaryKey.getName().toLowerCase();
		synchronized (primaryKey)
		{
			// 如果有缓存的序列号，则取新号返加
			if (primaryKey.getCurrentKey() < primaryKey.getEndKey())
			{
				primaryKey.setCurrentKey(primaryKey.getCurrentKey() + 1);
			} else
			{
				// 以下为如果没有缓存的序列号，则重新生成缓存序列号
				
				// 如果当前服务器的分配的段没有使用完，则在当前段上生成新的缓存
				if (primaryKey.getCurrentKey() < primaryKey.getMaxKey())
				{
					primaryKey.setCurrentKey(primaryKey.getCurrentKey() + 1);
					primaryKey.setStartKey(primaryKey.getCurrentKey());
					Long endKey = Math.min(primaryKey.getStartKey() + primaryKey.getCachedSize(), primaryKey.getMaxKey());
					primaryKey.setEndKey(endKey);
				} else
				{
					// 如果当前服务器的分配的段已经使用完，则在需为当前服务器生成新的段
					Long minKey = getMaxKey(name) + 1;
					
					primaryKey.setCurrentKey(minKey);
					primaryKey.setMinKey(primaryKey.getCurrentKey());
					primaryKey.setMaxKey(primaryKey.getCurrentKey() + Config.primaryKeySegmentSize-1);
					primaryKey.setStartKey(primaryKey.getMinKey());
					Long endKey = Math.min(primaryKey.getStartKey() + primaryKey.getCachedSize(), primaryKey.getMaxKey());
					primaryKey.setEndKey(endKey);
				}
				
				updatePrimarykey(primaryKey);
			}
			return primaryKey.getCurrentKey();
		}
	}	
	private static void updatePrimarykey(PrimaryKey primaryKey)
	{
		String sql ="update PrimaryKey set serverId='${serverId}',name='${name}',tableName='${tableName}',"
				+ "columnName='${columnName}',currentKey=${currentKey},startKey=${startKey},endKey=${endKey},"
				+ "maxKey=${maxKey},minKey=${minKey},banKey='${banKey}',keyType='${keyType}',cachedSize=${cachedSize},"
				+ "billCodeTypeName='${billCodeTypeName}'"
				+ " where EId=${EId}";
		Map<String,Object>  valuesMap = new HashMap<String, Object>();
		
		valuesMap.put("EId",primaryKey.getEId());
		valuesMap.put("serverId",primaryKey.getServerId());
		valuesMap.put("name",primaryKey.getName());
		valuesMap.put("columnName",primaryKey.getColumnName());
		valuesMap.put("tableName",primaryKey.getTableName());
		valuesMap.put("currentKey",primaryKey.getCurrentKey());
		valuesMap.put("startKey",primaryKey.getStartKey());
		valuesMap.put("endKey",primaryKey.getEndKey());
		valuesMap.put("maxKey",primaryKey.getMaxKey());
		valuesMap.put("minKey",primaryKey.getMinKey());
		valuesMap.put("banKey",primaryKey.getBanKey());
		valuesMap.put("keyType",primaryKey.getKeyType());
		valuesMap.put("cachedSize",primaryKey.getCachedSize());
		valuesMap.put("billCodeTypeName", primaryKey.getBillCodeTypeName());

		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		sql = sub.replace(sql);
		SessionFactory.executeSqlWithoutTransaction(sql);
	}
	
	private static void savePrimarykey(PrimaryKey primaryKey)
	{
		String sql ="insert into PrimaryKey (EId,serverId,name,tableName,columnName,currentKey,startKey,"
				+ "endKey,maxKey,minKey,banKey,keyType,cachedSize,billCodeTypeName) values(${EId},'${serverId}',"
				+ "'${name}','${tableName}','${columnName}',${currentKey},${startKey},${endKey},${maxKey},${minKey},"
				+ "'${banKey}','${keyType}',${cachedSize},'${billCodeTypeName}')";
		Map<String,Object>  valuesMap = new HashMap<String, Object>();
		
		valuesMap.put("EId",primaryKey.getEId());
		valuesMap.put("serverId",primaryKey.getServerId());
		valuesMap.put("name",primaryKey.getName());
		valuesMap.put("columnName",primaryKey.getColumnName());
		valuesMap.put("tableName",primaryKey.getTableName());
		valuesMap.put("currentKey",primaryKey.getCurrentKey());
		valuesMap.put("startKey",primaryKey.getStartKey());
		valuesMap.put("endKey",primaryKey.getEndKey());
		valuesMap.put("maxKey",primaryKey.getMaxKey());
		valuesMap.put("minKey",primaryKey.getMinKey());
		valuesMap.put("banKey",primaryKey.getBanKey());
		valuesMap.put("keyType",primaryKey.getKeyType());
		valuesMap.put("cachedSize",primaryKey.getCachedSize());
		valuesMap.put("billCodeTypeName", primaryKey.getBillCodeTypeName());

		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		sql = sub.replace(sql);
		SessionFactory.executeSqlWithoutTransaction(sql);
	}
	
	/**
	 * @return primaryKeys
	 */
	
	public static Map<String, PrimaryKey> getPrimaryKeys()
	{
		return primaryKeys;
	}
	
	/**
	 * @param primaryKeys
	 *            要设置的 primaryKeys
	 */
	
	public static void setPrimaryKeys(Map<String, PrimaryKey> primaryKeys)
	{
		PrimaryKeyEngine.primaryKeys = primaryKeys;
	}
}
