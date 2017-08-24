package ddd.simple.service.model.extend;

import java.util.Map;

import ddd.simple.entity.model.Model;

public interface ModelDataExtInterface
{
	public String submitModelData(Map<String, Object> processVar,Model model,Map<String,Object> modelData);
}
