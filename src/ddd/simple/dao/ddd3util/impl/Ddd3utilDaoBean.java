package ddd.simple.dao.ddd3util.impl;

import java.sql.ResultSet;

import org.springframework.stereotype.Service;

import ddd.simple.dao.base.BaseDao;
import ddd.simple.dao.ddd3util.Ddd3utilDao;

@Service
public class Ddd3utilDaoBean extends BaseDao implements Ddd3utilDao
{

	@Override
	public ResultSet querryRSbySql(String sql) throws Exception
	{
		
		return this.getResultSet(sql);
	}


}
