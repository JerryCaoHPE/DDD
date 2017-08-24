
package ddd.simple.util;

public class HtmlReportUtil {
	public static final int IS_TABLE = 1;
	public static final int IS_VIEW = 2;
	public static final int IS_NOT_TBALEORVIEW = 3;
	
	public static final String FLAG_REPORTVIEW ="reportview";
	public static final String FLAG_QUERYDATA ="date";
	
	
	
	
	public static final String COLUMN_TYPE_DIGIT = "digit" ; 
	public static final String COLUMN_TYPE_TEXT = "text" ;
	public static final String COLUMN_TYPE_DATE = "date" ;
	
	public static String getNameFromSql(String sql) throws Exception{
		if(sql.startsWith("select") && sql.contains("from")
				&& sql.lastIndexOf("select") == sql.indexOf("select")
				&& sql.lastIndexOf("from") == sql.indexOf("from")
		){
			int frist = sql.lastIndexOf(" ");
			int end =  sql.length();
			String name = sql.substring(frist, end);
			name = name.replaceAll("\\s*", "");
			return name;
		}else{
			throw new Exception("sql语句包含子查询，请先建立视图!");
		}
	}

}
