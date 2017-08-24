package ddd.simple.dao.ddd3util;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Set;

import ddd.simple.dao.base.BaseDaoInterface;

public interface Ddd3utilDao extends BaseDaoInterface
{
	public ResultSet querryRSbySql(String sql) throws Exception;
}
