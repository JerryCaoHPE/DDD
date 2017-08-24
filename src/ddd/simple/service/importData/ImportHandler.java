package ddd.simple.service.importData;

import java.util.List;
import java.util.Map;

import ddd.simple.entity.importConfigs.Config;

public interface ImportHandler {

	/**
	 *  处理一行数据，如果有错，则返回返回错误信息
	 * @param config
	 * @param data
	 * @return
	 */
	public String processData(Config config,Map<String, Object> data);
	
	/**
	 * 处理所有数据，如果有错，则返回返回错误信息
	 * @param config
	 * @param datas
	 * @return
	 */
	public String processDatas(Config config,List<Map<String, Object>> datas);
	
	/**
	 * 保存数据
	 * @param config
	 * @param datas
	 * @return 如果已保存，则返回 true
	 */
	public boolean save(Config config,List<Map<String, Object>> datas);
}
