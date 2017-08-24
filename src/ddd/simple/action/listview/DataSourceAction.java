package ddd.simple.action.listview;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.simple.entity.listview.DataSource;
import ddd.simple.service.listview.DataSourceService;

@Action
@RequestMapping("/DataSource")
@Controller
public class DataSourceAction {

	@Resource(name = "dataSourceServiceBean")
	private DataSourceService dataSourceService;

	public int deleteDataSource(Long dataSourceId) {

		try {
			return this.dataSourceService.deleteDataSource(dataSourceId);
		} catch (DDDException e) {
			throw e;
		}
	}

	public DataSource saveDataSource(DataSource dataSource) {
		try {
			DataSource saveDataSource =this.dataSourceService.saveDataSource(dataSource);
			return  saveDataSource;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public DataSource updateDataSource(DataSource dataSource) {
		try {
			DataSource updateDataSource =this.dataSourceService.updateDataSource(dataSource);
			return updateDataSource;
		} catch (DDDException e) {
			throw e;
		}
	}

	public DataSource findDataSourceById( Long dataSourceId) {
		try {
			DataSource dataSource =this.dataSourceService.findDataSourceById(dataSourceId);
			return dataSource;
		} catch (DDDException e) {
			throw e;
		}
	}
	
	public DataSource findDataSourceByCode( String dataSourceCode) {
		try {
			DataSource dataSource =this.dataSourceService.findDataSourceByCode(dataSourceCode);
			return dataSource;
		} catch (DDDException e) {
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public String checkReportSql(String checkSql,String params,HttpServletRequest request) {
		try {
			Map<String,Object> paramsMap = (Map<String,Object> ) JSON.parseObject(params,HashMap.class);
			String checkReportSql =this.dataSourceService.checkReportSql(checkSql,paramsMap,request);
			return checkReportSql;
		} catch (DDDException e) {
			return JSONObject.toJSONString(e);
		}
	}
	
	public DataSource copyDataSource(Long dataSourceId,String dataSourceCode)
	{
		return this.dataSourceService.copyDataSource(dataSourceId,dataSourceCode);
	}
	public Map<String,String> genSQLs(String tableName)
	{
		return this.dataSourceService.genSQLs(tableName);
	}
		
	
}
