package ddd.simple.service.ddd3util;

import java.util.List;
import java.util.Map;

import ddd.simple.action.ddd3util.TabInfo;
import ddd.simple.service.base.BaseServiceInterface;

public interface Ddd3utilService extends BaseServiceInterface
{
	public String generatorFileds(List<TabInfo> tabs) ;
}