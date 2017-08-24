package ddd.simple.util.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import ddd.base.annotation.ColumnInfo;
import ddd.base.annotation.EntityInfo;
import ddd.base.persistence.EntitySet;
import ddd.base.persistence.baseEntity.Entity;
import ddd.base.persistence.baseEntity.EntityClass;
import ddd.simple.entity.model.Model;
import ddd.simple.entity.model.ModelItem;

public class CreateModelEntityClass {
	
	public static EntityClass<Entity> create(Model model) throws ClassNotFoundException{
		EntityClass<Entity> entityClass = new EntityClass<Entity>(Entity.class);
		
		EntityInfo entityInfo = new EntityInfo();
		entityInfo.setName(model.getModelEnglishName().trim());
		entityInfo.setLabel(model.getModelName());
		entityInfo.setComment(model.getRemark());
		
		entityClass.setEntityInfo(entityInfo);
		entityClass.preInit();
		
		LinkedHashMap<String, ColumnInfo> columnInfos = entityClass.getColumnInfos();
		LinkedHashMap<String, ColumnInfo> classColumnInfos = entityClass.getClassColumnInfos();
		EntitySet<ModelItem> modelItems = model.getModelItems();
		
		List<String> properties =new ArrayList<String>(Arrays.asList(entityClass.getProperties()));
		for (ModelItem modelItem : modelItems) {
			ColumnInfo columnInfo = new ColumnInfo();
			columnInfo.setName(modelItem.getModelItemEnglishName().trim());
			columnInfo.setLabel(modelItem.getModelItemName());
			
			if("subTable".equals(modelItem.getDatatype())){
				columnInfo.setJoinTableName(modelItem.getJoinTableName());
				columnInfo.setJoinTableOneSide("oneSide");
				columnInfo.setJoinTableManySide("manySide");
				columnInfo.setType(ColumnInfo.COLUMN_TYPE_ONE2MANY);
				
				entityClass.getOne2ManyFieldColumnInfos().put(columnInfo.getName(), columnInfo);
			}else{
				
				
				columnInfo.setLength(modelItem.getTextSize()==null?255:modelItem.getTextSize());
				
				columnInfo.setClazz(Class.forName(modelItem.getJavatype()));
				
				columnInfo.setType(ColumnInfo.COLUMN_TYPE_PRIMARY);
				
				columnInfos.put(columnInfo.getName(), columnInfo);
				
				classColumnInfos.put(columnInfo.getName(), columnInfo);
				
				properties.add(columnInfo.getName());
			}
		}
		String[] p = new String[properties.size()];
		properties.toArray(p);
		entityClass.setProperties(p);
		return entityClass;
	}
	public static void main(String[] args) throws ClassNotFoundException {
		Model model = new Model();
		model.setModelEnglishName("News");
		model.setModelName("新闻");
		
		EntitySet< ModelItem> modelItems = new EntitySet<ModelItem>();
		ModelItem modelItem = new ModelItem();
		modelItem.setModelItemEnglishName("title");
		modelItem.setModelItemName("题目");
		modelItem.setDatatype("文本");
		modelItem.setJavatype(ModelItemTypeTojavaType.getJavaType(modelItem.getDatatype()));
		modelItem.setTextSize(20);
		modelItems.add(modelItem);
		
		modelItem = new ModelItem();
		modelItem.setModelItemEnglishName("time");
		modelItem.setModelItemName("时间");
		modelItem.setDatatype("时间");
		modelItem.setJavatype(ModelItemTypeTojavaType.getJavaType(modelItem.getDatatype()));
		modelItems.add(modelItem);
		
		model.setModelItems(modelItems);
		create(model);
	}
}
