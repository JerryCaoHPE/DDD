package ddd.base.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public interface ORMapper<T> {
	
	/**
	 * 映射结果集的当前行为对象 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public T map(ResultSet rs) throws SQLException;

	/**
	 * 映射结果集的所有行为对象 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public Collection<T> maps(ResultSet rs) throws SQLException;
}
