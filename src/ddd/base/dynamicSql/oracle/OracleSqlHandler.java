package ddd.base.dynamicSql.oracle;

import ddd.base.dynamicSql.SqlHandler;

public class OracleSqlHandler implements SqlHandler {

	@Override
	public String toDate(String dateString) {
		if(dateString.trim().startsWith("?")){
			return "TO_DATE("+dateString+", 'SYYYY-MM-DD HH24:MI:SS')";
		}
		return "TO_DATE('"+dateString+"', 'SYYYY-MM-DD HH24:MI:SS')";
	}

	@Override
	public String limit(String sql,String[] properties,String start, String size) {
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		for (String p : properties) {
			sb.append(p);
			sb.append(',');
		}
		sb.deleteCharAt(sb.length()-1);
		
		sb.append(" from (select * from (select t_t_t.*, row_number() OVER(ORDER BY null) AS row_number from (");
		sb.append(sql);
		sb.append(") t_t_t) t_t_t where t_t_t.row_number >");
		sb.append(start);
		sb.append(")where rownum <=");
		sb.append(size);
		return sb.toString();
	}

}
