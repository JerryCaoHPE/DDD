package ddd.base.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("serial")
public class DateUtil  
{	
	
	public static String getNowDate()
	{
		String datetime = (getSimpleDateFormat("yyyy-MM-dd")).format(new java.util.Date());
		return datetime;
	}
	
	public static String getNowTime()
	{
		String datetime = getSimpleDateFormat("HH:mm:ss").format(new java.util.Date());
		return datetime;
	}
	
	public static String getNowDateTime()
	{
        String datetime = getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
		return datetime;
	}

	public static String getNowDateTimeAsFileName() {
		String datetime = getSimpleDateFormat("yyyy-MM-dd HH mm ss S").format(
				new java.util.Date());
		return datetime;
	}

	public static String formatDate(Date date)
	{
        String datetime = getSimpleDateFormat("yyyy-MM-dd").format(date);
		return datetime;
	}	
	public static String formatDateTime(Date date)
	{
		if( date == null)
		{
			return ""; 
		}
        String datetime = getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return datetime;
	}	
	
	private static SimpleDateFormat getSimpleDateFormat(String format)
	{
	    SimpleDateFormat tempDate = new SimpleDateFormat(format);
		return tempDate;
	}
	
	
	@SuppressWarnings("deprecation")
	public static int getYearFromString(String date)
	{
		 java.sql.Date dt= java.sql.Date.valueOf(date);
		 int year=1900+ dt.getYear();
		 return year;
	}
	
	@SuppressWarnings("deprecation")
	public static int getMonthFromString(String date)
	{
		 java.sql.Date dt= java.sql.Date.valueOf(date);
		 int month=1+dt.getMonth();
		 return month;
	}
	
	
	@SuppressWarnings("deprecation")
	public static int getDateFromString(String date)
	{
		 java.sql.Date dt= java.sql.Date.valueOf(date);
		 int myDate=dt.getDate();
		 return myDate;
	}
	
	public static String getYearAndMonthFromString(String date)
	{
		String myDate = date.substring(0, date.lastIndexOf("-"));
		return myDate;
	}
	
	//传入“2009-09-05”则返回从5日到30日的所有日期（不包含周日）
	public static List<String> getDatesInMonthNotIncludeSUNDAY(String nowDate)
	{
		List<String> dates=new ArrayList<String>();
		String tempDate=null;
		
		int year = DateUtil.getYearFromString(nowDate);
		int month = DateUtil.getMonthFromString(nowDate);
		int date = DateUtil.getDateFromString(nowDate);
		
		Calendar start = Calendar.getInstance();
		//设置开始时间
		start.set(year, month-1, date);
		
		Calendar end = Calendar.getInstance();
		//设置完成时间 为下一个月的1号
		end.set(Calendar.YEAR, year);
		end.set(Calendar.MONTH, month);
		end.set(Calendar.DATE, 1);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		//循环，把日期加入List里
		while (start.compareTo(end) < 0) 
		{
            int w = start.get(Calendar.DAY_OF_WEEK);
            if(w != Calendar.SUNDAY)//周日不添加进日期列表
            {
            	tempDate=format.format(start.getTime());
    			dates.add(tempDate);
    			System.out.println(tempDate);
            }
            start.set(Calendar.DATE, start.get(Calendar.DATE) + 1);	// 循环，每次天数加1
		}
		return dates;
	}
}
