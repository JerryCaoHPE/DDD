package ddd.simple.entity.importConfigs;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import ddd.base.annotation.ColumnInfo;
import ddd.base.persistence.SessionFactory;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.base.util.SpringContextUtil;
import ddd.simple.service.importData.ImportHandler;

/**
 * @author Administrator
 *
 */
public class Config {
	private String outInterface;
	private String simpleEntityName;
	private String entityName;
	private String tableName;
	private Boolean automaticEncoding;// 自动编码
	private String encodingFieldName;// 编码字段
	private String encodingKey;//编码规则key
	private List<ConfigItem> configItems;
	private ImportHandler importHandler;;

	private EntityClass<?> entityClass;

	
	public String getSimpleEntityName() {
		return simpleEntityName;
	}

	public void setSimpleEntityName(String simpleEntityName) {
		this.simpleEntityName = simpleEntityName;
	}

	public String getOutInterface() {
		return outInterface;
	}

	public void setOutInterface(String outInterface) {
		this.outInterface = outInterface;
	}

	public List<ConfigItem> getConfigItems() {
		return configItems;
	}

	public void setConfigItems(List<ConfigItem> configItems) {
		this.configItems = configItems;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public EntityClass<?> getEntityClass() {
		if (entityClass == null) {
			this.init();
		}
		return entityClass;
	}

	public void init() {
		if (entityClass == null) {
			try {
				this.entityClass = SessionFactory.getEntityClass(Class.forName(this.entityName));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			this.tableName = this.entityClass.getEntityInfo().getName();
			for (ConfigItem configItem : this.configItems) {
				ColumnInfo columnInfo = this.entityClass.getColumnInfo(configItem.getFieldName());
				configItem.setColumnInfo(columnInfo);
			}
			int index = this.entityName.lastIndexOf(".");
			
			this.simpleEntityName = this.entityName.substring(index + 1, this.entityName.length());
			//StringUtils.endsWith(str, suffix);
			/*if (this.outInterface != null && this.outInterface.trim().length() != 0) {
				this.importHandler = (ImportHandler) SpringContextUtil.getBean(this.outInterface);
			}*/
		}
	}

	public ConfigItem getConfigItemByTitle(String columnTitle) {
		for (ConfigItem configItem : this.configItems) {
			if (configItem.getColumnTitle().equals(columnTitle)) {
				return configItem;
			}
		}
		return null;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public ImportHandler getImportHandler() {
		return importHandler;
	}

	public void setImportHandler(ImportHandler importHandler) {
		this.importHandler = importHandler;
	}

	public Boolean getAutomaticEncoding() {
		return automaticEncoding;
	}

	public void setAutomaticEncoding(Boolean automaticEncoding) {
		this.automaticEncoding = automaticEncoding;
	}

	public String getEncodingFieldName() {
		return encodingFieldName;
	}

	public void setEncodingFieldName(String encodingFieldName) {
		this.encodingFieldName = encodingFieldName;
	}

	public String getEncodingKey() {
		return encodingKey;
	}

	public void setEncodingKey(String encodingKey) {
		this.encodingKey = encodingKey;
	}

	public void setEntityClass(EntityClass<?> entityClass) {
		this.entityClass = entityClass;
	}
	
	
}
