package ddd.base.dynamicSql;

public interface SqlHandler {
	
	public String toDate(String dateString);
	
	
	public String limit(String sql,String[] properties,String start,String size);
}
