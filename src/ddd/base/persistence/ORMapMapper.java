package ddd.base.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import ddd.base.util.SQLUtil;

/**
* 类名称：ListMapHander
* 类描述： 实现<code>ResultSetHander</code>接口,用于将结果集每一行里面的的放入Map<String,Object>中，然后在将这个Map存入LinkedHashSet中。
 */
public class ORMapMapper implements ORMapper<Map<String, Object>> {
	private String[] properties;
	public ORMapMapper(String sql,String[] properties) throws SQLException {
		String temp = sql.toLowerCase().trim();
		if(temp.startsWith("select * from")&&(properties==null || properties.length==0)){
			throw new SQLException("sql语句中使用select * ,请将*号指明具体字段名:"+sql);
		}
		if(properties!=null && properties.length!=0){
			this.properties = properties;
		}else{
			this.properties = SQLUtil.getProperties(sql);
		}
	}
	
	@Override
	public Set<Map<String, Object>> maps(ResultSet rs) throws SQLException {
		Set<Map<String, Object>> resultList = new LinkedHashSet<Map<String, Object>>(); 
        
        Map<String,Object> m = null;
        while(rs.next()){
        	m = this.map(rs);
        	resultList.add(m);
        }
		return resultList;
	}

	@Override
	public Map<String, Object> map(ResultSet rs) throws SQLException {
		Map<String,Object> m = new HashMap<String,Object>();
    	for (String string : properties) {
			m.put(string, rs.getObject(string));
		}
		return m;
	}

}
