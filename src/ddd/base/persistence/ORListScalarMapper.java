package ddd.base.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ddd.base.persistence.baseEntity.EntityClass;

/**
* 类名称：ORListScalarMapper
* 类描述： 实现<code>ORMapper</code>接口,把结果集中第一个字段存入List中。
 */
public class ORListScalarMapper<T> implements ORMapper<T> {

	private Class<T> clazz;
	public ORListScalarMapper(Class<T> clazz) {
		this.clazz = clazz;
	}
	@Override
	public List<T> maps(ResultSet rs) throws SQLException {
		List<T> result = new ArrayList<T>();
        while(rs.next()){
        	result.add(this.map(rs));
        }
		return result;
	}

	@Override
	public T map(ResultSet rs) throws SQLException {
		return (T) EntityClass.getRealTypeData(clazz, rs, 1);
	}

}
