package ddd.simple.service.listview.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import ddd.base.Config;
import ddd.base.annotation.ColumnInfo;
import ddd.base.dynamicSql.DynamicService;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.base.util.EntityUtil;
import ddd.simple.dao.listview.ListViewDao;
import ddd.simple.entity.listview.DataSource;
import ddd.simple.entity.listview.DisplayAttribute;
import ddd.simple.entity.listview.Page;
import ddd.simple.entity.listview.ReportView;
import ddd.simple.service.listview.DataSourceService;
import ddd.simple.service.listview.ListViewService;
import ddd.simple.service.listview.ReportViewService;
import ddd.simple.util.Strings;

@Service
public class ListViewServiceBean implements ListViewService
{
	@Resource(name = "listViewDaoBean")
	private ListViewDao			listViewDao;
	
	@Resource(name = "dataSourceServiceBean")
	private DataSourceService	dataSourceService;
	
	@Resource(name = "reportViewServiceBean")
	private ReportViewService	reportViewService;
	
	private String getFinalSql(ReportView reportView, Map<String, Object> paramsMap, Map<String, String> condition,
			Map<String, String> initFilter, Map<String, String> sortingMap)
	{
		try
		{
			DataSource dataSource = this.dataSourceService.findDataSourceById(reportView.getDataSource().getEId());
			
			String reportSql = dataSource.getReportSql();
			
			String dataSourceSql = reportView.getFinalSql();
			
			String filterCondition = condition == null || condition.isEmpty() ? "" : this.getFilterCondition(condition);
			// 解决多表查询字段名重复导致过滤的问题
			if (!"".equals(filterCondition))
			{
				dataSourceSql += "  where " + filterCondition;
			}
			
			dataSourceSql = this.appendOrderBy(dataSourceSql, sortingMap);
			
			dataSourceSql = dataSourceSql.replace(Strings.BASESQL, reportSql);
			
			if(initFilter != null){
				paramsMap.putAll(initFilter);
			}
			String finalSql = DynamicService.getDynamicSql(dataSourceSql, paramsMap);
			
			finalSql = finalSql.replaceAll("[$]", "");
			// 处理sql语句中的没有被替换的{field}表达式，把他们的全部值都置为' '
			Pattern pattern = Pattern.compile("\\{[^}]*\\}");
			Matcher matcher = pattern.matcher(finalSql);
			finalSql = matcher.replaceAll("' '");
			
			return finalSql;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("getFinalSql", e.getMessage(), e);
		}
	}
	
	private String appendOrderBy(String sql, Map<String, String> sortingMap)
	{
		if (sortingMap == null || sortingMap.size() == 0)
		{
			return sql;
		}
		
		String sqlLowerCase = sql.toLowerCase();
		
		String orderBy = null;
		String str1 = StringUtils.substringBetween(sqlLowerCase, " order ", "by ");
		
		if (str1 != null)
		{
			if ("".equals(StringUtils.trimToEmpty(str1)))
			{
				orderBy = " order " + str1 + "by ";
			}
		}
		
		StringBuilder sqlBuilder = new StringBuilder(sql);
		if (orderBy != null)
		{
			sqlBuilder.append(",");
		} else
		{
			sqlBuilder.append(" order by ");
		}
		
		for (Map.Entry entity : sortingMap.entrySet())
		{
			sqlBuilder.append(" ").append(entity.getKey()).append(" ").append(entity.getValue()).append(",");
		}
		
		sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
		
		return sqlBuilder.toString();
	}
	
	private boolean beginWithOperator(String str)
	{
		return str.startsWith("=") || str.startsWith(">") || str.startsWith("<") ? true : false;
	}
	
	private String getFilterCondition(Map<String, String> condition)
	{
		return this.getSearchFilterCondition(condition);
	}
	
	private String getSearchFilterCondition(Map<String, String> condition)
	{
		try
		{
			String attributeName = condition.get("attributeName");
			String fileterText = condition.get("fileterText");
			String operator = "";
			// 以操作符开始
			if (this.beginWithOperator(fileterText))
			{
				if (fileterText.length() > 1)
				{
					operator = fileterText.trim().substring(0, 2);
					
					if ("<=".equals(operator) || ">=".equals(operator) || "<>".equals(operator))
					{
						if (fileterText.length() > 3)
							return attributeName + " " + operator + " '" + fileterText.substring(2).trim() + "'";
						else
							return "";
					}
					
					operator = fileterText.trim().substring(0, 1);
					if (">".equals(operator) || "<".equals(operator))
					{
						return attributeName + " " + operator + " '" + fileterText.substring(1).trim() + "'";
					} else if ("=".equals(operator))
					{
						String[] conditions = fileterText.substring(1).split(" ");
						String conditionString = " in (";
						for (int i = 0; i < conditions.length; i++)
						{
							conditionString += "'" + conditions[i] + "',";
						}
						conditionString += "'" + fileterText.substring(1) + "')";
						return attributeName + conditionString;
					}
				} else if (fileterText.length() > 0)
				{
					operator = fileterText.trim().substring(0, 1);
					if (">".equals(operator) || "<".equals(operator))
					{
						if (fileterText.length() > 1)
							return attributeName + " " + operator + " '" + fileterText.substring(1).trim() + "'";
						else
							return "";
					} else if ("=".equals(operator))
					{
						if (fileterText.length() > 1)
						{
							String[] conditions = fileterText.substring(1).split(" ");
							String conditionString = " in (";
							for (int i = 0; i < conditions.length; i++)
							{
								conditionString += "'" + conditions[i] + "',";
							}
							conditionString += "'" + fileterText.substring(1) + "')";
							return attributeName + conditionString;
						} else
							return "";
					}
				} else
					return "";
			} else if (fileterText.trim().length() > 0)
			{
				String[] conditions = fileterText.trim().split(" ");
				String conditionString = "(";
				for (int i = 0; i < conditions.length; i++)
				{
					conditionString += attributeName + " like '%" + conditions[i] + "%' or ";
				}
				conditionString += attributeName + " like '%" + fileterText.trim() + "%')";
				return conditionString;
			} else
				return "";
		} catch (Exception e)
		{
			throw new DDDException("getFilterCondition", e.getMessage(), e);
		}
		return "";
	}
	
	public Map<String, Object> getResult(Page page, String listViewKey, Map<String, String> filterCondition, Map<String, Object> paramsMap,
			Map<String, String> initFilter, Map<String, String> sortingMap)
	{
		try
		{
			Map<String, Object> returnMap = new HashMap<String, Object>();
			
			ReportView reportView = this.listViewDao.findListViewByKey(listViewKey);
			String findSQL = this.getFinalSql(reportView, paramsMap, filterCondition, initFilter, sortingMap);
			
			long totalCount = this.listViewDao.getTotalCount(findSQL);
			if (page != null)
			{
				findSQL += " #Limit( " + page.getBeginIndex() + "," + page.getEveryPage() + ")";
			}
			
			returnMap.put("totalCount", totalCount);
			returnMap.put("result", this.listViewDao.getReportViewData(findSQL));
			
			return returnMap;
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new DDDException("getResult", e.getMessage(), e);
		}
	}
	
	@Override
	public Set<Map<String, Object>> findEntityByKey(String listViewKey,String eIds){
		ReportView listView = this.findListViewByKey(listViewKey);
		Set<Map<String, Object>> result;
		Set<Map<String,Object>> realResult = new HashSet<Map<String,Object>>();
		try
		{
			String findSQL = this.getFinalSql(listView, null, null, null, null);
			result = this.listViewDao.querryEntity(findSQL);
			Iterator<Map<String,Object>> ite = result.iterator();
			while(ite.hasNext()){
				Map<String,Object> temp = ite.next();
				if(eIds.contains(String.valueOf(temp.get("EId")))){
					realResult.add(temp);
				}
			}
		} catch (Exception e)
		{
			throw new DDDException("findEntityByKey", e.getMessage(), e);
		}
		
		return realResult;
	}
	
	@Override
	public ReportView findListViewByKey(String listViewkey)
	{
		try
		{
			Map<String, Object> ListViewMap;
			ReportView reportView = this.listViewDao.findListViewByKey(listViewkey);
			return reportView;
//			if (reportView == null)
//			{
//				return null;
//			}
//			
//			String findSQL = this.getFinalSql(reportView, new HashMap<String, Object>(), null, initFilter, null);
//			
//			ListViewMap = this.listViewDao.getSqlResultByDao(findSQL, 0, 10, null);
//			ListViewMap.put("reportView", reportView);
//			return ListViewMap;
		} catch (Exception e)
		{
			throw new DDDException("findListViewByKey", e.getMessage(), e);
		}
	}
	
	@Override
	public boolean createDataSourceWithState(String className) throws ClassNotFoundException
	{
		return (this.createDataSourceWithEntity(className) != null) ? true : false;
	}
	
	@Override
	public DataSource createDataSourceWithEntity(String className) throws ClassNotFoundException
	{
		EntityClass<?> entityClass = SessionFactory.getEntityClass(Class.forName(className));
		String entityNameLow = entityClass.getClassName().substring(0, 1).toLowerCase() + entityClass.getClassName().substring(1);
		String lable = entityClass.getEntityInfo().getLabel();
		
		DataSource dataSource = dataSourceService.findDataSourceByCode(entityNameLow);
		if (dataSource == null)
		{
			dataSource = createDataSource(entityNameLow, lable);
			dataSourceService.saveDataSource(dataSource);
		}
		
		return dataSource;
	}
	
	@Override
	public boolean createReportViewWithState(String className) throws ClassNotFoundException
	{
		return (this.createReportViewWithEntity(className).size() > 0) ? true : false;
	}
	
	@Override
	public List<ReportView> createReportViewWithEntity(String className) throws ClassNotFoundException
	{
		EntityClass<?> entityClass = SessionFactory.getEntityClass(Class.forName(className));
		String entityNameLow = entityClass.getClassName().substring(0, 1).toLowerCase() + entityClass.getClassName().substring(1);
		String lable = entityClass.getEntityInfo().getLabel();
		
		DataSource dataSource = dataSourceService.findDataSourceByCode(entityNameLow);
		
		ReportView reportView = createReportView(entityNameLow, lable, entityClass);
		
		ReportView listReportView = this.addListReportView(reportView, dataSource, entityNameLow, lable);
		ReportView referenceReportView = this.addReferenceReportView(reportView, dataSource, entityNameLow, lable);
		
		List<ReportView> reportViewList = new ArrayList<ReportView>();
		reportViewList.add(listReportView);
		reportViewList.add(referenceReportView);
		return reportViewList;
	}
	
	public boolean createDataSourceAndReportView(String className) throws ClassNotFoundException
	{
		return (this.createDataSourceAndReportViewWithEntity(className).size() > 0) ? true : false;
	}
	
	public List<Object> createDataSourceAndReportViewWithEntity(String className) throws ClassNotFoundException
	{
		EntityClass<?> entityClass = SessionFactory.getEntityClass(Class.forName(className));
		String entityNameLow = entityClass.getClassName().substring(0, 1).toLowerCase() + entityClass.getClassName().substring(1);
		String lable = entityClass.getEntityInfo().getLabel();
		
		DataSource dataSource = dataSourceService.findDataSourceByCode(entityNameLow);
		if (dataSource == null)
		{
			dataSource = createDataSource(entityNameLow, lable);
			
			//在这里处理实体默认的left join语句
			this.addReferenceSearch(dataSource,className);
			
			dataSourceService.saveDataSource(dataSource);
		}
		
		ReportView reportView = createReportView(entityNameLow, lable, entityClass);
		
		ReportView listReportView = this.addListReportView(reportView, dataSource, entityNameLow, lable);
		ReportView referenceReportView = this.addReferenceReportView(reportView, dataSource, entityNameLow, lable);
		
		List<Object> dataSourceReportViewList = new ArrayList<Object>();
		dataSourceReportViewList.add(dataSource);
		dataSourceReportViewList.add(listReportView);
		dataSourceReportViewList.add(referenceReportView);
		return dataSourceReportViewList;
	}
	
	private void addReferenceSearch(DataSource dataSource,String className){
		try{
			//查出该实体中的所有外键
			EntityClass<?> entityClass = SessionFactory.getEntityClass(Class.forName(className));
			//找到外键实体对应的表名
		}catch(Exception e){
			throw new DDDException("addReferenceSearch");
		}
	}
	
	
	private ReportView addListReportView(ReportView reportView, DataSource dataSource, String entityNameLow, String lable)
	{
		ReportView listReportView = reportViewService.findReportViewByKey(entityNameLow + "List");
		if (listReportView == null)
		{
			listReportView = (ReportView) EntityUtil.clone(reportView);
			listReportView.setDataSource(dataSource);
			listReportView.setReportViewName(lable + "基础视图");
			listReportView.setReportViewKey(entityNameLow + "List");
			reportViewService.addReportView(listReportView);
		}
		return listReportView;
	}
	
	private ReportView addReferenceReportView(ReportView reportView, DataSource dataSource, String entityNameLow, String lable)
	{
		ReportView referenceReportView = reportViewService.findReportViewByKey(entityNameLow + "Reference");
		if (referenceReportView == null)
		{
			referenceReportView = (ReportView) EntityUtil.clone(reportView);
			referenceReportView.setDataSource(dataSource);
			referenceReportView.setReportViewName(lable + "外键视图");
			referenceReportView.setReportViewKey(entityNameLow + "Reference");
			reportViewService.addReportView(referenceReportView);
		}
		return referenceReportView;
	}
	
	private ReportView createReportView(String entityNameLow, String lable, EntityClass<?> entityClass)
	{
		ReportView reportView = new ReportView();
		
		EntitySet<DisplayAttribute> attributes = new EntitySet<DisplayAttribute>();
		
		Set<Entry<String, ColumnInfo>> columnSet = entityClass.getClassColumnInfos().entrySet();
		
		int i = 0;
		for (Map.Entry<String, ColumnInfo> entry : columnSet)
		{
			ColumnInfo columnInfo = entry.getValue();
			if (columnInfo.getType() == 1)
			{
				DisplayAttribute attribute = new DisplayAttribute();
				attribute.setAttributeName(entry.getKey());
				attribute.setAttributeValue(columnInfo.getLabel());
				attribute.setColumnIndex(i);
				attribute.setCssArrtribute("width:100,text-align:'left'");
				if (columnInfo.getClazz().equals(Date.class))
					attribute.setShowType("%Y/%m/%d");
				else if (columnInfo.getClazz().equals(Long.class))
					attribute.setShowType("#,###");
				else
					attribute.setShowType("文本");
				
				attributes.add(attribute);
				i++;
			}
		}
		reportView.setDisplayAttributes(attributes);
		
		return reportView;
	}
	
	private DataSource createDataSource(String entityNameLow, String lable)
	{
		DataSource dataSource = new DataSource();
		dataSource.setDataSourceCode(entityNameLow);
		dataSource.setDataSourceName(lable + "数据源");
		String shortCode = "";
		if(!("".equals(Config.shortCode))){
			shortCode = Config.shortCode+"_";
		}
		dataSource.setReportSql("select * from " + shortCode +entityNameLow);
		return dataSource;
	}
}
