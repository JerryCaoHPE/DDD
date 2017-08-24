package ddd.simple.service.importData.impl;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import ddd.base.exception.DDDException;
import ddd.base.persistence.SessionFactory;
import ddd.base.util.CastUtil;
import ddd.base.util.DateUtil;
import ddd.base.util.JSONUtil;
import ddd.base.util.SpringContextUtil;
import ddd.base.util.SysUtil;
import ddd.simple.dao.exportAndImport.ExportAndImportDao;
import ddd.simple.entity.importConfigs.CellError;
import ddd.simple.entity.importConfigs.Config;
import ddd.simple.entity.importConfigs.ConfigItem;
import ddd.simple.entity.importConfigs.ImportParam;
import ddd.simple.entity.importConfigs.RecordItem;
import ddd.simple.service.billCode.BillCodeService;
import ddd.simple.service.importData.ImportDataService;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFeatures;
import net.sf.json.JSONObject;

public class ImportDataServiceImpl implements ImportDataService {
	private String separator = ";";
	private Config config;
	private String dataFilePath;
	private List<String> columnTitles;
	private List<String> fieldNames;
	private List<ConfigItem> configItems;
	private Map<String, Object> baseInfo;
	private BillCodeService billCodeService;
	private ExportAndImportDao exportAndImportDao;

	private List<CellError> cellErrors = new ArrayList<CellError>();
	private List<CellError> tempCellErrors = new ArrayList<CellError>();
	private Map<Integer, RecordItem> oneToMany = new HashMap<Integer, RecordItem>();
	private List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

	private Map<Integer, String> oneToManyTemp = new HashMap<Integer, String>();

	/**
	 * @param baseInfo 内置信息
	 * @param exportAndImportDao
	 * @param configContext  配置信息
	 * @param dataFilePath 导入文件路径
	 * @param separator 一对多分割符
	 */
	public ImportDataServiceImpl(Map<String, Object> baseInfo, ExportAndImportDao exportAndImportDao,
			BillCodeService billCodeService, String configContext, String dataFilePath, String separator) {
		this.baseInfo = baseInfo;
		this.separator = separator;
		this.dataFilePath = dataFilePath;
		this.billCodeService = billCodeService;
		this.exportAndImportDao = exportAndImportDao;

		configContext = configContext.replace("\n\r", " ").trim();
		config = JSONUtil.fromJSON(configContext, Config.class);

		config.init();
	}

	@Override
	public List<CellError> importData() {
		String errorMessage = null;
		try
		{
			this.readFromExcel();
			this.addTempToError();
			
			List<Map<String, Object>> allUpdates = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> allInserts = new ArrayList<Map<String, Object>>();
	
			String[] allUpdateWhere = new String[datas.size()];
			int updateIndex = 0;
			
			String outInterface=this.config.getOutInterface();
		//	ImportHandler importHandler = this.config.getImportHandler();
	
			ImportParam importParam=new ImportParam();
			if (outInterface != null && this.cellErrors.size() == 0) {// 外部参数
				String[] param=outInterface.split("[@]");
				String beanName = param[0];
				String methodName = param[1];
				
				importParam.setData(datas);
				importParam.setConfig(config);
				
				importParam = (ImportParam) SpringContextUtil.invoke(beanName, methodName, importParam);
				
				this.datas=importParam.getData();
				//this.cellErrors.addAll(importParam.getErrorMessage());
			}
	
			for (Map<String, Object> data : this.datas) {
				data.putAll(baseInfo);
	
				Long eId = (Long) data.get("EId");
				if (eId == null) {
					allInserts.add(data);
				} else {
					allUpdates.add(data);
					allUpdateWhere[updateIndex] = "EId = " + eId;
					updateIndex++;
				}
			}
	
			Map<String, Object>[] allInsertMaps = this.initInsertMaps(allInserts);
			Map<String, Object>[] allUpdateMaps = this.initUpdateMaps(allUpdates);
	
			

			if (importParam.isAutoSave()) {
				persistence(allInsertMaps, allUpdateMaps, allUpdateWhere);
			}else{
				System.out.println("调用外部接口保存数据成功");
			}
		} catch(Exception e)
		{
			e.printStackTrace();
			errorMessage = e.getMessage();
		}
		if(errorMessage != null)
		{
			this.cellErrors.add(0,new CellError(0,0,"执行出错："+errorMessage));
		}
		return this.cellErrors;
	}

	/**
	 * 
	 * @param allUpdates
	 * @return
	 */
	private Map<String, Object>[] initUpdateMaps(List<Map<String, Object>> allUpdates) {
		Map<String, Object>[] allUpdateMaps = new HashMap[allUpdates.size()];
		for (int i = 0; i < allUpdates.size(); i++) {
			allUpdateMaps[i] = allUpdates.get(i);
		}
		return allUpdateMaps;
	}

	/**
	 * 
	 * @param allInserts
	 * @param allInsertMaps
	 */
	private Map<String, Object>[] initInsertMaps(List<Map<String, Object>> allInserts) {
		Map<String, Object>[] allInsertMaps = new HashMap[allInserts.size()];

		for (int i = 0; i < allInserts.size(); i++) {
			Map<String, Object> tempMap = allInserts.get(i);

			if (this.config.getAutomaticEncoding() != null && this.config.getAutomaticEncoding()) {// 自动添加单据的编码
				String billCode = this.billCodeService.genNewBillCode(this.config.getEncodingKey());
				tempMap.put(config.getEncodingFieldName(), billCode);
			}

			allInsertMaps[i] = tempMap;
		}

		return allInsertMaps;
	}

	/**
	 * 
	 * @param allInsertMaps
	 * @param allUpdateMaps
	 * @param allUpdateWhere
	 */
	private void persistence(Map<String, Object>[] allInsertMaps, Map<String, Object>[] allUpdateMaps,
			String[] allUpdateWhere) {
		try {
			if (cellErrors.size() > 0) {
				return;
			}

			if (allInsertMaps.length > 0) {
				this.exportAndImportDao.insertEntities(this.config.getTableName(), allInsertMaps);
			}
			if (allUpdateMaps.length > 0) {
				this.exportAndImportDao.updateEntities(this.config.getTableName(), allUpdateMaps, allUpdateWhere);
			}

			insertOneToMany();

			System.out.println("保存数据成功");
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("在保存需要插入和新增的数据时出错,原因：" + e.getMessage());
		}
	}

	/**
	 * 添加中间表记录
	 * 
	 * @throws Exception
	 */
	public void insertOneToMany() throws Exception {
		if (oneToMany.size() == 0 || oneToManyTemp.size() == 0)
			return;

		for (Map.Entry<Integer, String> entry : oneToManyTemp.entrySet()) {
			int key = entry.getKey();

			if (oneToMany.containsKey(key)) {// 添加记录
				String sql = "select EId from " + config.getTableName() + " where " + entry.getValue();

				Set<Map<String, Object>> rs = this.exportAndImportDao.query(sql);

				if (rs.size() == 0) {
					this.recordError(0, 0, "在关联表 " + config.getTableName() + " 中的未查询到条件为" + entry.getValue() + "的记录");
					continue;
				}
				if (rs.size() > 1) {
					this.recordError(0, 0, "在关联表 " + config.getTableName() + " 中的数据时查询到" + rs.size()+ " 条，应该只有一条,且必须有一条，条件：" + entry.getValue());
					continue;
				}

				Iterator<Map<String, Object>> ite1 = rs.iterator();
				ite1.hasNext();
				Long eid = new BigDecimal(ite1.next().get("EId").toString()).longValue();

				RecordItem recordItem = oneToMany.get(key);

				String where = config.getTableName() + "EId" + "=" + eid;// 删除中间表数据
				this.exportAndImportDao.deleteOneEntity(recordItem.getJoinTableName(), where);

				List<Map<String, Object>> maps = recordItem.getValues();

				for (Map<String, Object> map : maps) {
					map.put(config.getTableName() + "EId", eid);
					this.exportAndImportDao.insertOneEntity(recordItem.getJoinTableName(), map);
				}
			}
		}
	}

	
	/**
	 * 读取表头
	 * @param sheet
	 */
	private Map<Integer, String> readHead(Sheet sheet){
		Map<Integer, String> emptyIndexs=new HashMap<Integer, String>();
		if (this.columnTitles == null) {
			this.columnTitles = new ArrayList<String>();
			this.configItems = new ArrayList<ConfigItem>();
			this.fieldNames = new ArrayList<String>();
			
			Map<String, String> notEmpty=new HashMap<String, String>();
			
			for (int i = 0; i < config.getConfigItems().size(); i++) {
				ConfigItem configItem=config.getConfigItems().get(i);
				
				if(configItem.getIsNotNull()){
					notEmpty.put(configItem.getColumnTitle(), configItem.getFieldName());
				}
			}
			
			for (int j = 0; j < sheet.getColumns(); j++)// 读取表头
			{
				String title = sheet.getCell(j, 0).getContents();

				if ("".equals(title) || title.trim().length() == 0) {
					continue;
				}

				if(notEmpty.containsKey(title)){
					notEmpty.remove(title);
				}
				
				ConfigItem configItem = this.config.getConfigItemByTitle(title);
				String fieldName = null;
				
				if(configItem == null){
				//	System.out.println("警告：在实体配置中未找到对应（" + title + "） 的列的配置 ，该列不会导入");
					emptyIndexs.put(j, title);
					continue;
				}else if (configItem.getColumnInfo() == null) {
					emptyIndexs.put(j, title);
					//System.out.println("警告：在实体配置中未找到名为" + configItem.getFieldName() + " 的列 （" + title + "） ，该列不会导入");
					//this.recordTempError(0, 0,"警告：在实体配置中未找到名为" + configItem.getFieldName() + " 的列 （" + title + "） ，该列不会导入");
					configItem = null;
				} else {
					fieldName = configItem.getColumnInfo().getName();
				}
				this.configItems.add(configItem);
				this.columnTitles.add(title);
				this.fieldNames.add(fieldName);
			}
			
			if(notEmpty.size()>0){
				String message="";
				for (Map.Entry<String, String> entry : notEmpty.entrySet()) {
					message+=entry.getKey()+",";
				}
				this.recordError(0, 0, "未找到非空列："+message);
			}
			
			this.addTempToError();
		}
		return emptyIndexs;
	}
	
	private int readBody(Sheet sheet,Map<Integer, String> emptyIndexs) throws Exception{
		int rowIndex = 0;
		// 循环进行读写
		for (int i = 1; i < sheet.getRows(); i++) {
			Map<String, Object> data = new HashMap<String, Object>();

			int emptys = 0,cellIndex=0;// 记录每一行空单元格的个数
			for (int j = 0; j < configItems.size(); j++,cellIndex++) {
				if(emptyIndexs.containsKey(cellIndex)){
					emptyIndexs.remove(cellIndex);
					j--;
					continue;
				}
				ConfigItem configItem = this.configItems.get(j);
				if (configItem != null) {
					Object value = this.getContents(sheet.getCell(cellIndex, i), configItem, rowIndex);

					if (value == null) {
						emptys++;
						if(configItem.getIsNotNull()){
							this.recordTempError(i, j, configItem.getColumnTitle()+" 不能为空");
						}
					}
					
					if (configItem.getIsEntity()) {
						String className = configItem.getRelationEntityName();
						if (!className.contains("EntitySet")) {
							data.put(this.fieldNames.get(j), value);
						}
					} else {
						data.put(this.fieldNames.get(j), value);
					}
				}
			}

			if (emptys == configItems.size()) {// 该行为空,结束读取
				this.tempCellErrors=new ArrayList<CellError>();
				break;
			}else{
				this.addTempToError();
			}

			Long eId = this.getExistEid(data, i);
			if (eId != null) {
				data.put("EId", eId);
			}
			this.datas.add(data);
			rowIndex++;
		}
		return rowIndex;
	}
	
	/**
	 * 
	 * @param path  Excel路径
	 * @param needImportFieldsIndex  只读取列号在该arrayList中的列
	 * @param startIndex  从该行开始读值
	 * @param count  都多少个值
	 * @param getRows 是否只是得到数据的行数
	 * @return
	 */
	private void readFromExcel() {
		this.datas = new ArrayList<Map<String, Object>>();
		try {
			Workbook book = Workbook.getWorkbook(new File(this.dataFilePath));

			// 获得所有工作表对象
			Sheet[] sheets = book.getSheets();
			for (Sheet sheet : sheets) {
				if (sheet.getColumns() == 0 || sheet.getRows() == 0){
					continue;
				}

				Map<Integer, String> emptyIndexs=readHead(sheet);
				
				readBody(sheet,emptyIndexs);
			}
			book.close();
		} catch (BiffException e) {
			throw new DDDException("读取Excel出错(请确认excel文件的扩展名是xls)，原因是：" + e.getMessage(), e);
		} catch (IndexOutOfBoundsException e) {
			throw new DDDException("读取Excel出错，请检查导入模板是否正确", e);
		} catch (Exception e) {
			throw new DDDException("读取Excel出错，原因是：" + e.getMessage(), e);
		}
	}

	/**
	 * 获取正真的值
	 * @param cell
	 * @param configItem
	 * @param value
	 * @param rowIndex
	 * @return
	 * @throws Exception
	 */
	private Object getValue(jxl.Cell cell,ConfigItem configItem,String value, int rowIndex) throws Exception{
		String typeName = configItem.getColumnInfo().getFieldType().getName();
		if (StringUtils.isEmpty(value)) {
			if (configItem.getIsNotNull()) {
				this.recordTempError(cell, configItem.getColumnTitle() + " : 值不能为空");
			}
			return null;
		}

		Object valuerObject = null;
		if (configItem.getIsEntity())// 外键)
		{
			valuerObject = value;
		} else {
			valuerObject = CastUtil.parseValue(value, typeName);
		}

		if (!StringUtils.isEmpty(configItem.getValidate()))// 进行正则表达式验证
		{
			Pattern pattern = Pattern.compile(configItem.getValidate());
			Matcher matcher = pattern.matcher(value);

			if (!matcher.matches()) {
				this.recordTempError(cell, configItem.getColumnTitle() + ":" + valuerObject.toString() + "验证不通过，不符合"+ configItem.getValidate() + "规则!");
				return null;
			}
		}

		if (configItem.getIsEntity())// 外键
		{
			String className = configItem.getRelationEntityName();

			if (className.contains("EntitySet")) {// 一对多
				getRelationEntitys(configItem, cell, value, rowIndex);
			} else {
				valuerObject = this.getRelationEntityId(configItem, cell, value);
			}
		}

		if (configItem.getIsUnique()) {// 唯一
			String where = oneToManyTemp.get(rowIndex);
			if (where != null && where.length() > 0) {
				where += " and " + configItem.getFieldName() + "= '" + value + "'";
			} else {
				where = configItem.getFieldName() + "= '" + value + "'";
			}
			oneToManyTemp.put(rowIndex, where);// 将唯一值加入temp中，以备添加一对多记录时使用
		}
		return valuerObject;
	}
	
	/**
	 * 获取单元格的数据
	 * 
	 * @param cell
	 * @return Object 单元格数据
	 */
	private Object getContents(jxl.Cell cell, ConfigItem configItem, int rowIndex) {
		String value = "";
		String typeName = configItem.getColumnInfo().getFieldType().getName();

		if (cell.getType().equals(CellType.EMPTY)) {
			return null;
		} else if (cell.getType().equals(CellType.DATE)){// 转型失败
			DateCell dateRecord = (DateCell) cell;
			if (typeName.equals("java.util.Date")) {
				return dateRecord.getDate();
			} else if (typeName.equals("java.sql.Timestamp")) {
				return new Timestamp(dateRecord.getDate().getTime());
			} else {
				return SysUtil.formatDate(dateRecord.getDate());
			}
		}

		value = cell.getContents();
		if (value != null) {
			value = value.trim();
		}

		try {
			return this.getValue(cell, configItem, value, rowIndex);
		} catch (Exception e) {
			this.recordTempError(cell, e.getMessage());
		}
		return null;
	}

	/**
	 * 获取中间表名
	 * 
	 * @param configItem
	 * @return
	 */
	private String getJoinTableName(ConfigItem configItem) {
		Field field = configItem.getColumnInfo().getField();
		Type genType = field.getGenericType();

		Class<?> genericClazz;
		if (genType instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) genType;
			genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
			return SessionFactory.getTableName(genericClazz.getName());
		}else{
			this.recordTempError(0, 0,configItem.getColumnTitle() + "(" + configItem.getFieldName() + ")" + "处导入配置出错，请于管理员联系！");
			return "";
		}
	}

	/**
	 * 一对多
	 * 
	 * @param configItem
	 * @param cell
	 * @param value
	 * @throws Exception
	 */
	private void getRelationEntitys(ConfigItem configItem, jxl.Cell cell, String value, int rowIndex) throws Exception {
		String[] values = value.split("[" + this.separator + "]");

		RecordItem recordItem = new RecordItem();

		recordItem.setJoinTableName(configItem.getColumnInfo().getJoinTableName());
		recordItem.setRelationTableName(getJoinTableName(configItem));

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < values.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();

			Long eid = getRelationEntityId(configItem, cell, values[i], recordItem.getRelationTableName());
			map.put(recordItem.getRelationTableName() + "EID", eid);

			list.add(map);
		}
		recordItem.setValues(list);
		this.oneToMany.put(rowIndex, recordItem);
	}

	/**
	 * 查询关联实体的Id
	 * 
	 * @param configItem
	 * @param cell
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public Long getRelationEntityId(ConfigItem configItem, jxl.Cell cell, String value, String tableName)
			throws Exception {
		String sql = "select EId from " + tableName + " where " + configItem.getRelationEntityKey() + " = '" + value+ "' ";

		Set<Map<String, Object>> rs = this.exportAndImportDao.query(sql);

		if (rs.size() == 0) {
			this.recordTempError(cell, "在关联表 " + tableName + " 中的未查询到" + configItem.getRelationEntityKey() + "=" + value+ "的记录,请检查" + value + "列是否填写正确！");
			return null;
		}

		if (rs.size() > 1) {
			this.recordTempError(cell, "在关联表 " + tableName + " 中的数据时查询到" + rs.size() + " 条，应该只有一条,且必须有一条，条件："+ configItem.getRelationEntityKey() + "=" + value);
			return null;
		}
		Iterator<Map<String, Object>> ite1 = rs.iterator();
		ite1.hasNext();
		Long eid = new BigDecimal(ite1.next().get("EId").toString()).longValue();
		return eid;
	}

	/**
	 * 查询关联实体的Id
	 * 
	 * @param configItem
	 * @param cell
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public Long getRelationEntityId(ConfigItem configItem, jxl.Cell cell, String value) throws Exception {
		String tableName = SessionFactory.getTableName(configItem.getRelationEntityName());
		return this.getRelationEntityId(configItem, cell, value, tableName);
	}

	/**
	 * 给单元格加注释
	 * 
	 * @param label
	 * @param comments
	 */
	private void setCellComments(jxl.Cell label, String comments) {
		WritableCellFeatures cellFeatures = new WritableCellFeatures();
		cellFeatures.setComment(comments);
		if (label instanceof jxl.write.Number) {
			jxl.write.Number num = (jxl.write.Number) label;
			num.setCellFeatures(cellFeatures);
		} else if (label instanceof jxl.write.Boolean) {
			jxl.write.Boolean bool = (jxl.write.Boolean) label;
			bool.setCellFeatures(cellFeatures);
		} else if (label instanceof jxl.write.DateTime) {
			jxl.write.DateTime dt = (jxl.write.DateTime) label;
			dt.setCellFeatures(cellFeatures);
		} else {
			Label _label = (Label) label;
			_label.setCellFeatures(cellFeatures);
		}
	}

	/**
	 * 
	 * @param data
	 * @param rowIndex
	 * @return
	 * @throws Exception
	 */
	public Long getExistEid(Map<String, Object> data, int rowIndex) throws Exception {
		String where = "";

		for (ConfigItem configItem : config.getConfigItems()) {
			if (configItem.getIsUnique()) {
				String name=configItem.getColumnInfo().getName();
				
				if(data.containsKey(name)){
					Class<?> fieldType=configItem.getColumnInfo().getFieldType();
					if(fieldType.equals(String.class)){
						where += " and " + name+ "= '"+ data.get(name) + "'";
					}else if(fieldType.equals(Date.class)){//bug,仅满足日期格式
						where += " and TRUNC(" + name+ ") = TRUNC(to_date('"+ DateUtil.formatDate((Date) data.get(name)) + "','yyyy-MM-dd')) ";
					}else{
						where += " and " + name+ " = "+ data.get(name) + " ";
					}
				}
			}
		}
		
		if ("".equals(where)) {
			return null;
		}

		String sql = "select EId from " + config.getTableName() + " where 1=1 " + where;

		Set<Map<String, Object>> rs = this.exportAndImportDao.query(sql);

		if (rs.size() > 1) {
			this.recordTempError(rowIndex, 0, "根据条件找到多行可更新的数据,请检查配置的 唯一 条件是否正确" + where);
			return null;
		}

		Iterator<Map<String, Object>> ite1 = rs.iterator();
		if (ite1.hasNext()) {
			Long eid = new BigDecimal(ite1.next().get("EId").toString()).longValue();
			return eid;
		} else {
			return null;
		}
	}

	/**
	 * 获取外部接口的类名和方法名
	 * 
	 * @param outInterfaceInfo
	 * @return
	 */
	public String[] getNames(String outInterfaceInfo) {
		if (!outInterfaceInfo.equals("") && !outInterfaceInfo.contains("@")) {
			System.out.println("外部接口配置错误");
			return null;
		}
		if (outInterfaceInfo.equals("")) {
			return null;
		}
		String[] temp = outInterfaceInfo.split("@");
		String beanName = temp[0];
		String methodName = temp[1];

		return new String[] { beanName, methodName };
	}

	/**
	 * 
	 * @param importFields
	 * @param fieldNameMap 导入的数据对应的下标
	 * @param datas  导入的数据
	 * @return
	 */
	private ArrayList<HashMap<Object, Object>> capsulationDataForOutInterface(JSONObject importFields,
			HashMap<Object, Integer> importDataMap, ArrayList<ArrayList<Object>> datas) {
		ArrayList<HashMap<Object, Object>> capsulationdatas = new ArrayList<HashMap<Object, Object>>();

		for (int i = 0; i < datas.size(); i++) {
			HashMap<Object, Object> temp = new HashMap<Object, Object>();
			ArrayList<Object> oneRow = datas.get(i);

			Iterator iterator = importDataMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				String key = (String) entry.getKey();

				if (importFields.containsKey(key)) {
					String fieldName = (String) importFields.get(key);
					int index = Integer.parseInt(entry.getValue().toString());

					if (index < oneRow.size()) {
						Object value = oneRow.get(index);
						temp.put(fieldName, value);
					} else {
						throw new DDDException("java.lang.ArrayIndexOutOfBoundsException", "请保证导入的字段在配置中都存在");
					}
				}
			}
			capsulationdatas.add(temp);
		}
		return capsulationdatas;
	}

	/**
	 * 
	 * @param outInterfaceInfo 外部接口的信息（完整类名，方法名）
	 * @param method 外部接口的方法实体
	 * @param datas 交由外部接口处理的数据集
	 * @return 返回一个ArrayList<ArrayList<String>>的由外部接口处理过的数据集
	 */
	public Class<?> getOutInterfaceClass(String outInterfaceInfo) {
		if (!outInterfaceInfo.equals("") && !outInterfaceInfo.contains("@")) {
			System.out.println("外部接口配置错误");
			return null;
		}
		if (outInterfaceInfo.equals("")) {
			return null;
		}
		String[] temp = outInterfaceInfo.split("@");
		String outInterfaceClassName = temp[0];

		try {
			Class<?> clazz = Class.forName(outInterfaceClassName);
			return clazz;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("getOutInterfaceClass", e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param outInterfaceInfo 外部接口的信息（完整类名，方法名）
	 * @return 返回一个指定的Method对象
	 */
	public Method dealWithOutInterface(String outInterfaceInfo) {
		if (!outInterfaceInfo.equals("") && !outInterfaceInfo.contains("@")) {
			System.out.println("外部接口配置错误");
			return null;
		}
		if (outInterfaceInfo.equals("")) {
			return null;
		}

		String[] temp = outInterfaceInfo.split("@");
		String outInterfaceClassName = temp[0];
		String outInterfaceMethodName = temp[1];

		try {
			Class<?> clazz = Class.forName(outInterfaceClassName);
			Method[] outInterfaceMethod = clazz.getMethods();
			for (Method method : outInterfaceMethod) {
				if (method.getName().equals(outInterfaceMethodName)) {
					return method;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DDDException("dealWithOutInterface", e.getMessage(), e);
		}
		return null;
	}

	// 错误信息处理
	private void recordError(jxl.Cell cell, String error) {
		this.cellErrors.add(new CellError(cell.getRow(), cell.getColumn(), error));
	}

	private void recordError(int row, int column, String error) {
		this.cellErrors.add(new CellError(row, column, error));
	}
	
	private void recordTempError(jxl.Cell cell, String error) {
		this.tempCellErrors.add(new CellError(cell.getRow(), cell.getColumn(), error));
	}
	
	private void recordTempError(int row, int column, String error) {
		this.tempCellErrors.add(new CellError(row, column, error));
	}
	
	private void addTempToError(){
		if(this.tempCellErrors.size() > 0){
			this.cellErrors.addAll(this.tempCellErrors);
			this.tempCellErrors=new ArrayList<CellError>();
		}
	}
}
