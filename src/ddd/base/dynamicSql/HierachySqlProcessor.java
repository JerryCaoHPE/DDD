package ddd.base.dynamicSql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import ddd.base.exception.DDDException;
import ddd.base.persistence.SessionFactory;
import ddd.base.util.FileUtil;

public class HierachySqlProcessor
{
	
	public static void main(String[] args) throws IOException
	{
		
		// Matcher matcher =
		// ParaParamPattern.matcher("select * from orderItem where orderId = #[order.Id]");
		// while(matcher.find())
		// {
		// System.out.println(matcher.group());
		// for(int i=0;i<=matcher.groupCount();i++)
		// {
		// System.out.println(i+":"+matcher.group(i));
		// }
		// }
		
		// "D:\\angular\\workspace\\SWP\\src\\test\\regex\\sql.sql"
		
		HierachySqlProcessor test1 = new HierachySqlProcessor();
		Map<String, HierachySql> hierachySqlsParsed = test1
				.parseNamedSqlsFromFile("D:\\angular\\workspace\\SWP\\src\\test\\regex\\sql.sql");
		
		System.out.println(hierachySqlsParsed);
	}
	
	public Map<String, Object> loadData(String sqlFileName, Map<String, Object> params)
	{
		Map<String, HierachySql> hierachySqlsParsed = this.parseNamedSqlsFromFile(sqlFileName);
		
		Map<String,Object> datas = new HashMap<String, Object>();
		Iterator<HierachySql> hierachySqlsParsedIterator = hierachySqlsParsed.values().iterator();
		while (hierachySqlsParsedIterator.hasNext())
		{
			HierachySql hierachySql = hierachySqlsParsedIterator.next();
			
			String hierachyCompliedSql = DynamicService.getDynamicSql(hierachySql.getHierachySql(), params);
			hierachySql.setHierachyCompliedSql(hierachyCompliedSql);
			
			try
			{
				Set<Map<String, Object>> data = SessionFactory.getThreadSession().get(hierachySql.getHierachyCompliedSql());
				hierachySql.setData(data);
				
				datas.put(hierachySql.getShortName(), hierachySql.getData());
			} catch (Exception e)
			{
				DDDException dddException = new DDDException("加载数据出错，原因是：" + e.getMessage() + ",sql:"
						+ hierachySql.getHierachyCompliedSql(), e);
				throw dddException;
			}  
		}
		hierachySqlsParsedIterator = hierachySqlsParsed.values().iterator();
		while (hierachySqlsParsedIterator.hasNext())
		{
			HierachySql hierachySql = hierachySqlsParsedIterator.next();
			if (hierachySql.getChildren().size() > 0)
			{
				Iterator<HierachySql> childIterator = hierachySql.getChildren().values().iterator();
				while (childIterator.hasNext())
				{
					HierachySql childHierachySql = childIterator.next();
					Iterator<Map<String,Object>> dataIterator = hierachySql.getData().iterator();
					
					Map<Object,Map<String,Object>> dataMap = new HashMap<Object, Map<String,Object>>();
					while(dataIterator.hasNext())
					{
						Map<String,Object> row = dataIterator.next();
						ArrayList<Map<String,Object>> groupData = new ArrayList<Map<String,Object>>();
						row.put(childHierachySql.getShortName(),groupData);
						dataMap.put(row.get(childHierachySql.getParentFieldName()),row);
					}
					
					Iterator<Map<String,Object>> childDataIterator = childHierachySql.getData().iterator();
					while(childDataIterator.hasNext())
					{
						Map<String,Object> row = childDataIterator.next();
						Object key = row.get(childHierachySql.getFieldName());
						Map<String,Object> parentRow = (Map<String,Object>)dataMap.get(key);
						@SuppressWarnings("unchecked")
						List<Map<String,Object>> groupData = (List<Map<String,Object>>)parentRow.get(childHierachySql.getShortName());
						groupData.add(row);
					}
					
				}
			}
		}
		return datas;
	}
	
	public Map<String, String> parseNamedSqls(String sqlText)
	{
		Pattern pattern = Pattern.compile("(^\\s*\\[.+\\]\\s*$)", Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(sqlText);
		
		List<Integer> partIndexs = new ArrayList<Integer>();
		while (matcher.find())
		{
			partIndexs.add(matcher.start());
			partIndexs.add(matcher.end());
		}
		partIndexs.add(sqlText.length() - 1);
		
		Map<String, String> namedSqls = new LinkedHashMap<String, String>();
		for (int i = 0; i < partIndexs.size() - 1;)
		{
			String name = sqlText.substring(partIndexs.get(i), partIndexs.get(++i));
			name = name.trim();
			name = name.substring(1, name.length() - 1).trim();
			
			String sql = sqlText.substring(partIndexs.get(i) + 2, partIndexs.get(++i)+1);
			
			namedSqls.put(name, sql);
		}
		System.out.println(namedSqls);
		
		return namedSqls;
	}
	
	public Map<String, HierachySql> parseNamedSqlsFromFile(String sqlFileName)
	{
		String sqlText = "";
		try
		{
			sqlText = FileUtils.readFileToString(new File(sqlFileName));
			
		} catch (IOException e)
		{
			System.out.println("加载动态sql文件出错，原因是：" + e.getMessage() + ",文件：" + sqlFileName);
			e.printStackTrace();
		}
		Map<String, String> namedSqls = parseNamedSqls(sqlText);
		Map<String, HierachySql> hierachySqlsParsed = this.parseNamedHierachySqls(namedSqls);
		
		return hierachySqlsParsed;
	}
	
	public Map<String, HierachySqlProcessor.HierachySql> parseNamedHierachySqls(Map<String, String> namedSqls)
	{
		Map<String, HierachySql> hierachySqls = new LinkedHashMap<String, HierachySqlProcessor.HierachySql>();
		
		Iterator<Map.Entry<String, String>> iterator = namedSqls.entrySet().iterator();
		while (iterator.hasNext())
		{
			Map.Entry<String, String> entry = iterator.next();
			HierachySql hierachySql = new HierachySql(entry.getKey(), entry.getValue());
			hierachySqls.put(hierachySql.getName(), hierachySql);
		}
		
		Map<String, HierachySql> hierachySqlsParsed = new LinkedHashMap<String, HierachySqlProcessor.HierachySql>();
		
		Iterator<Map.Entry<String, HierachySql>> sqlIterator0 = hierachySqls.entrySet().iterator();
		while (sqlIterator0.hasNext())
		{
			Map.Entry<String, HierachySql> entry = sqlIterator0.next();
			if (entry.getValue().getLevel() == 0)
			{
				HierachySql child = entry.getValue();
				hierachySqlsParsed.put(child.getName(), child);
				sqlIterator0.remove();
			}
		}
		
		// 组织层次结构，最大支持10层
		for (int i = 1; i < 10; i++)
		{
			if (hierachySqls.size() == 0)
				break;
			
			Iterator<Map.Entry<String, HierachySql>> sqlIterator = hierachySqls.entrySet().iterator();
			while (sqlIterator.hasNext())
			{
				Map.Entry<String, HierachySql> entry = sqlIterator.next();
				if (entry.getValue().getLevel() == i)
				{
					Iterator<Map.Entry<String, HierachySql>> sqlParsedIterator = hierachySqlsParsed.entrySet().iterator();
					while (sqlParsedIterator.hasNext())
					{
						HierachySql parent = sqlParsedIterator.next().getValue();
						if (parent.addChild(entry.getValue()))
						{
							hierachySqlsParsed.put(entry.getValue().getName(), entry.getValue());
							sqlIterator.remove();
							break;
						}
						
					}
				}
			}
		}
		System.out.println(hierachySqlsParsed);
		return hierachySqlsParsed;
	}
	
	private static Pattern	ParaParamPattern	= Pattern
														.compile("((\\s*\\w+\\s*\\.\\s*)|(\\s*))(\\w+)(\\s*in\\s*\\(\\s*)(\\$\\{\\s*)(\\W*)(\\w+)(\\s*\\.\\s*)(\\w+)(\\W*)(\\s*\\})(\\s*\\)\\s*)");
	private static Pattern fromPattern = Pattern.compile("from",Pattern.CASE_INSENSITIVE);	
	public class HierachySql
	{
		private String						name;
		private String						sql;
		private String						hierachySql;
		private String						hierachyCompliedSql;
		private String						shortName;
		private String						parentName;
		private int							level;
		private String						parentFieldName;
		private String						fieldName;
		private Set<Map<String, Object>>	data;
		
		Map<String, HierachySql>			children	= new LinkedHashMap<String, HierachySqlProcessor.HierachySql>();
		HierachySql							parent;
		
		public String getShortName()
		{
			return shortName;
		}
		
		public String getParentName()
		{
			return parentName;
		}
		
		public HierachySql(String name, String sql)
		{
			super();
			this.name = name;
			this.sql = sql;
			this.hierachySql = sql;
			
			int lastDotIndex = this.name.lastIndexOf('.');
			if (lastDotIndex >= 0)
			{
				this.parentName = this.name.substring(0, lastDotIndex);
				this.shortName = this.name.substring(lastDotIndex + 1);
				this.level = StringUtils.countMatches(this.name, ".");
			} else
			{
				this.parentName = "";
				this.shortName = this.name;
				this.level = 0;
			}
			
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getSql()
		{
			return sql;
		}
		
		public boolean hasChildren()
		{
			return children.size() == 0;
		}
		
		public HierachySql getParent()
		{
			return parent;
		}
		
		public void setParent(HierachySql parent)
		{
			this.parent = parent;
		}
		
		public boolean addChild(HierachySql child)
		{
			String name = child.getName();
			String sql = child.getSql();
			
			if (this.name.equals(child.getParentName()))
			{
				child.setParent(this);
				try
				{
					this.constructHierarcySql(child);
				} catch (Exception e)          
				{                              
					System.out.println("构造层交动态s交动态sql出错，原因是：" + e.getMessage() + "\n" + child.getSql() + "\n" + child.getParent().getSql());
					e.printStackTrace();       
				}                              
				this.children.put(name, child);
				return true;                   
			}
			return false;
		}
		
		private void constructHierarcySql(HierachySql hierachySql) throws Exception
		{
			Matcher matcher = ParaParamPattern.matcher(hierachySql.getSql());
			String paraName = null;
			while (matcher.find())
			{
				if (paraName != null)
				{
					String message = "Sql:" + hierachySql.getSql() + " 存在两个形如: in (${ })的参数，这是不允许的，请检查后重试";
					throw new Exception(message);
				}
				paraName = matcher.group();
				
				String fieldName = matcher.group(4).trim();
				String fieldAliasName = this.getSqlFieldAliasName(hierachySql.getSql(), hierachySql.getSql().substring(matcher.start(1), matcher.end(4)));
				if(fieldAliasName != null)
				{
					fieldName = fieldAliasName;
				}
				hierachySql.setFieldName(fieldName);
				
				hierachySql.setParentFieldName(matcher.group(10));
				String extendedWhere = matcher.group(1) + matcher.group(4) + " in (select " + hierachySql.getParentFieldName() + "  "
						+ this.getSqlFrom(hierachySql.getParent().getHierachySql()) + ")";
				
				String hSql = hierachySql.getSql().substring(0, matcher.start()) + " ( " + extendedWhere + " ) "
						+ hierachySql.getSql().substring(matcher.end());
				
				hierachySql.setHierachySql(hSql);
				
				System.out.println(hierachySql.getName() + "=" + hierachySql.getHierachySql());
			}
			if (paraName == null)
			{
				hierachySql.setHierachySql(hierachySql.getSql());
				return;
			}
		}
		
		
		private String getSqlFrom(String sql)
		{
			Matcher matcher = fromPattern.matcher(sql);
			if(matcher.find() == false)
			{
				throw new DDDException("在sql中没有找到from关键字\n"+sql);
			}
			int fromIndex =  matcher.start();
			return sql.substring(fromIndex);
		}
		/**
		 * 
		* @Title: getSqlFieldAliasName 
		* @Description: 取出字段中形如vtc.EId as viewTreeConditionEid中的viewTreeConditionEid
		* @param sql
		* @param fieldName
		* @return 
		* @return String
		 */
		private String getSqlFieldAliasName(String sql,String fieldName)
		{
			sql = sql.trim();
			fieldName = fieldName.trim();
			Matcher matcher = fromPattern.matcher(sql);
			if(matcher.find() == false)
			{
				throw new DDDException("在sql中没有找到from关键字\n"+sql);
			}
			int fromIndex =  matcher.start();
			
			String sqlSelectPart = sql.substring(7,fromIndex);//去掉前面的 select关键字
			String[] fieldNames = StringUtils.split(sqlSelectPart, ',');
			for (int fieldIndex = 0; fieldIndex < fieldNames.length; fieldIndex++)
			{
				String fieldNamePart = fieldNames[fieldIndex].trim();
				int startIndex = fieldNamePart.indexOf(fieldName+" ");
				if( fieldNamePart.startsWith(fieldName+" "))
				{
					int asEndIndex = fieldNamePart.lastIndexOf(" ");
					if(asEndIndex >= 0 & asEndIndex > fieldName.length())
					{
						String as = fieldNamePart.substring(fieldName.length(),asEndIndex).trim().toLowerCase();
						if("as".equals(as))
						{
							return fieldNamePart.substring(asEndIndex+1).trim();
						}
					}
				}
				
			}
			return null;
		}		
		public int getLevel()
		{
			return level;
		}
		
		public String getHierachySql()
		{
			return hierachySql;
		}
		
		public void setHierachySql(String hierachySql)
		{
			this.hierachySql = hierachySql;
		}
		
		public String getParentFieldName()
		{
			return parentFieldName;
		}
		
		public void setParentFieldName(String parentFieldName)
		{
			this.parentFieldName = parentFieldName;
		}
		
		public String getHierachyCompliedSql()
		{
			return hierachyCompliedSql;
		}
		
		public void setHierachyCompliedSql(String hierachyCompliedSql)
		{
			this.hierachyCompliedSql = hierachyCompliedSql;
		}
		
		/**
		 * @return data
		 */
		
		public Set<Map<String, Object>> getData()
		{
			return data;
		}
		
		/**
		 * @param data
		 *            要设置的 data
		 */
		
		public void setData(Set<Map<String, Object>> data)
		{
			this.data = data;
		}
		
		public Map<String, HierachySql> getChildren()
		{
			return children;
		}
		
		public void setChildren(Map<String, HierachySql> children)
		{
			this.children = children;
		}

		public String getFieldName()
		{
			return fieldName;
		}

		public void setFieldName(String fieldName)
		{
			if(fieldName != null)
			{
				this.fieldName = fieldName.trim();
			}
			else
			{
				this.fieldName = null;
			}
			
		}
	}
	
}
