package ddd.base.exporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**	
{
	"entityName":"department",
	"forbidFields":[],
	"idFieldName":"EId",
	"onlyExportPk":false,
	"pkFieldName":"code",
	"entityFields":{
		"organization":{
			"entityName":"department",
			"forbidFields":[],
			"idFieldName":"EId",
			"onlyExportPk":false,
			"pkFieldName":"fullCode",
			"entityFields":{}
		},
		"employees":{
			"entityName":"employee",
			"forbidFields":[],
			"idFieldName":"EId",
			"onlyExportPk":false,
			"pkFieldName":"code",
			"entityFields":{}
		},
	}
}	
	
*/
public class Config {
	/**
	 * 实体注解名
	 */
	private String entityName;
	/**
	 * 唯一标识字段
	 */
	private String pkFieldName;
	/**
	 * 主键
	 */
	private String idFieldName ="EId";
	
	/**
	 * 是否只导出唯一标识字段
	 */
	private Boolean onlyExportPk = false;
	/**
	 * 外键字段配置
	 */
	private Map<String,Config> entityFields = new HashMap<String, Config>();
	/**
	 * 只对导入有用 设置是否导入哪些字段
	 */
	private List<String> forbidFields = new ArrayList<String>();

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getPkFieldName() {
		return pkFieldName;
	}

	public void setPkFieldName(String pkFieldName) {
		this.pkFieldName = pkFieldName;
	}

	public String getIdFieldName() {
		return idFieldName;
	}

	public void setIdFieldName(String idFieldName) {
		this.idFieldName = idFieldName;
	}

	public Boolean getOnlyExportPk() {
		return onlyExportPk;
	}

	public void setOnlyExportPk(Boolean onlyExportPk) {
		this.onlyExportPk = onlyExportPk;
	}

 

	public List<String> getForbidFields() {
		return forbidFields;
	}

	public void setForbidFields(List<String> forbidFields) {
		this.forbidFields = forbidFields;
	}

	public Map<String,Config> getEntityFields() {
		return entityFields;
	}

	public void setEntityFields(Map<String,Config> entityFields) {
		this.entityFields = entityFields;
	}
	
}
