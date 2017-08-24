package ddd.simple.service.model.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Service;

import ddd.base.exception.DDDException;
import ddd.base.expression.ExpressionHandler;
import ddd.simple.service.model.SaveHistoryService;
import ddd.simple.dao.base.BaseDao;
 

@Service
public class SaveHistoryServiceBean   implements SaveHistoryService{

	@Resource(name="baseDao")
	private BaseDao baseDao;
	
	@Override
	public Object execute() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String 保存模型历史版本(String tableName,Map<String,Object> modelData) {
		try {
			Map<String,Object> newHistory = new HashMap<String,Object>();
			
			String MODEL_SQL = "select EId from model where modelEnglishName = '"+tableName+"'";
			Set<Map<String, Object>> model = this.baseDao.query(MODEL_SQL);
			Object modelId = null;
			for(Map<String, Object> m:model){
				modelId =  m.get("EId");
			}
			
			String HISTORY_SQL = "select max(version) as version from history where modelId ='"+modelId+"' and dataId='"+modelData.get("EId")+"'";
			Set<Map<String,Object>> oldHistory = this.baseDao.query(HISTORY_SQL);
			for(Map<String,Object> oh:oldHistory){
				newHistory.put("version",Integer.parseInt(oh.get("version").toString())+1);
			}
			
			newHistory.put("modelId", modelId);
			newHistory.put("dataId", modelData.get("EId"));
			newHistory.put("modelData", JSONArray.fromObject(modelData).toString());
			newHistory.put("modifiedTime",new Date());
			
			this.baseDao.save("history", newHistory);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("保存新闻历史版本", e.getMessage(), e);
		}
		return "success";
	}
}
