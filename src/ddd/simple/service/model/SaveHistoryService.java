package ddd.simple.service.model;

import java.util.Map;

import ddd.base.expression.ExpressionHandler;

public interface SaveHistoryService extends ExpressionHandler{
	public String 保存模型历史版本(String tableName,Map<String,Object> modelData);
}
