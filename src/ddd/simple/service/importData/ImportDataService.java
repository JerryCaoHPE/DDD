package ddd.simple.service.importData;

import java.util.List;

import ddd.simple.entity.importConfigs.CellError;

public interface ImportDataService  {
	
	public List<CellError> importData();
	
}
