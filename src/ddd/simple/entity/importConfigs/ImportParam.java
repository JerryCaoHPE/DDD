package ddd.simple.entity.importConfigs;

import java.util.List;
import java.util.Map;

public class ImportParam {
	private List<Map<String, Object>> data;// excel数据
	private boolean autoSave;// 是否自动保存
	private String errorMessage;// 错误信息
	private Config config;
	
	
	
	public ImportParam() {
		this.autoSave=true;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	public boolean isAutoSave() {
		return autoSave;
	}

	public void setAutoSave(boolean autoSave) {
		this.autoSave = autoSave;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
