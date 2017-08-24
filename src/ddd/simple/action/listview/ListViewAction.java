package ddd.simple.action.listview;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.exception.DDDException;
import ddd.base.persistence.EntitySet;
import ddd.simple.entity.listview.Page;
import ddd.simple.entity.listview.ReportView;
import ddd.simple.service.listview.ListViewService;
import ddd.simple.service.reportForm.ReportTestService;

@Action
@RequestMapping("/ListView")
@Controller
public class ListViewAction
{
	@Resource(name = "listViewServiceBean")
	private ListViewService		listViewService;
	
	@Resource(name = "reportTestServiceBean")
	private ReportTestService	reportTestService;
	
	/*
	 * public Map<String ,Object> findListViewById(Long listViewId) { try {
	 * Map<String ,Object> listview =
	 * this.listViewService.findListViewById(listViewId); return listview; }
	 * catch (DDDException e) { throw e; } }
	 */
	
	public ReportView findListViewByKey(String listViewKey)
	{
		try
		{
			ReportView  listview = this.listViewService.findListViewByKey(listViewKey);
			return listview; 
		} catch (DDDException e)
		{
			throw e;
		}
	}
	
	public Set<Map<String, Object>> findEntityByKey(String listViewKey,String eIds){
		return this.listViewService.findEntityByKey(listViewKey,eIds);
	}
	
	@SuppressWarnings("unchecked")
	public Set<Map<String, Object>> getResult(Integer currentPage, Integer pageSize, String listViewKey, String filterCondition,
			String params, HashMap<String, String> initFilter, String sorting)
	{
		try
		{
			Map<String, Object> datas = getResult1(currentPage, pageSize, listViewKey, filterCondition, params, initFilter, sorting);
			Set<Map<String, Object>> set = new HashSet<Map<String, Object>>();
			set.add(datas);
			return set;
		} catch (DDDException e)
		{
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getResult1(Integer currentPage, Integer pageSize, String listViewKey, String filterCondition, String params,
			HashMap<String, String> iniFilter, String sorting)
	{
		Page page = new Page();
		page.setCurrentPage(currentPage);
		page.setEveryPage(pageSize);
		page.setBeginIndex((page.getCurrentPage() - 1) * page.getEveryPage());
		
		Map<String, String> sortingMap = (Map<String, String>) JSON.parseObject(sorting, HashMap.class);
		
		Map<String, Object> paramsMap = (Map<String, Object>) JSON.parseObject(params, HashMap.class);
		Map<String, String> conditionMap = (Map<String, String>) JSON.parseObject(filterCondition, HashMap.class);
		
		Map<String, Object> datas = this.listViewService.getResult(page, listViewKey, conditionMap, paramsMap, iniFilter, sortingMap);
		
		return datas;
		
	}
	
	@SuppressWarnings("unchecked")
	public void export2Excel(HttpServletRequest request, HttpServletResponse response, String listViewKey, String filterCondition,
			String params, HashMap<String, String> iniFilter, String sorting) throws IOException
	{
		Map<String, Object> datas = this.getResult1(1, Integer.MAX_VALUE, listViewKey, filterCondition, params, iniFilter, sorting);
		ReportView reportView = this.listViewService.findListViewByKey(listViewKey);
		
		response.setHeader("Connection", "close");
		response.setContentType("octets/stream");
		response.setHeader("Content-Type", "application/vnd.ms-excel;charset=UTF-8");
		String filename = encodeFileName(request, reportView.getReportViewName() + ".xls");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);
		
		reportTestService.export2Excel((Set<Map<String, Object>>) datas.get("result"), reportView, response.getOutputStream());
	}
	
	private String encodeFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException
	{
		String agent = request.getHeader("USER-AGENT");
		if (null != agent && -1 != agent.indexOf("MSIE"))
		{
			return URLEncoder.encode(fileName, "UTF-8");
		} else if (null != agent && -1 != agent.indexOf("Mozilla"))
		{
			return "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
		} else
		{
			return fileName;
		}
	}
}
