package ddd.base.dynamicSql.mysql;

import ddd.base.dynamicSql.SqlHandler;

public class MysqlSqlHandler implements SqlHandler {

	@Override
	public String toDate(String dateString) {
		if(dateString.trim().startsWith("?")){
			return dateString;
		}
		return "'"+dateString+"'";
	}

	@Override
	public String limit(String sql,String[] properties, String start, String size) {
		StringBuffer sb = new StringBuffer(sql);
		sb.append(" limit ");
		sb.append(start);
		sb.append(",");
		sb.append(size);
		return sb.toString();
	}

}
