package ddd.simple.entity.importConfigs;

import java.util.List;
import java.util.Map;

public class RecordItem {
	private String code;
	
	private List<Map<String, Object>> values;
	
	private String joinTableName;//中间表

	private String relationTableName;//关联表
	
	private ConfigItem configItem;
	
	public RecordItem(){
	}
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	

	public List<Map<String, Object>> getValues() {
		return values;
	}


	public void setValues(List<Map<String, Object>> values) {
		this.values = values;
	}


	public String getJoinTableName() {
		return joinTableName;
	}

	public void setJoinTableName(String joinTableName) {
		this.joinTableName = joinTableName;
	}

	public ConfigItem getConfigItem() {
		return configItem;
	}

	public void setConfigItem(ConfigItem configItem) {
		this.configItem = configItem;
	}

	public String getRelationTableName() {
		return relationTableName;
	}

	public void setRelationTableName(String relationTableName) {
		this.relationTableName = relationTableName;
	}
}
