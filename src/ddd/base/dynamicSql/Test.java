package ddd.base.dynamicSql;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ddd.base.util.VelocityUtil;

public class Test {

	public static void main(String[] args) {
		/*String temp="select * from test where birthday > #ToDate(\"1992-10-12\") and birthday < #ToDate(\"${date}\") #if(${num}!=0) and eid<${num} #end";

		temp="select t.eid,t.name,t.birthday from test t "
				+ "where birthday > #ToDate('1992-10-12') "
				+ "and birthday < #ToDate($date) "
				+ "#if($num!=0) and eid<${num} #end #Limit(' ? ', $num) order by t.eid";
		/*
		temp="update test set birthday=#ToDate('1992-10-12'),name='ddd2' where eid=6";*/
		
		String temp = "select e.eid as EId,e.code,e.name,e.property,e.jobname,e.workDayTemplate,e.graduateSchool,"
				+ "e.major,e.linkTel, e.faxNumber,e.email,e.specialAbility,e.enable,e.description,e.workPosition,"
				+ "e.workMode,e.employeePosition,e.employeeType, e.dimissionReason,e.sex from employee e,department "
				+ "d,organization o where  1=1  #if($param.notEmpty($organizationId)) and d.organizationId= ${organizationId}"
				+ " #end #if(${param.notEmpty(${departmentId})}) "
				+ "and e.departmentId = ${departmentId} #end and e.departmentId = d.eid";
		System.out.println(Charset.defaultCharset());
		System.out.println(temp);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("date",new Date().toLocaleString());
		//map.put("organizationId", 1);
		map.put("departmentId", 1);
		
		String dysql = VelocityUtil.generateString("ss中文", map);
		System.out.println(dysql);
		System.exit(0);;
		long start = System.currentTimeMillis();
		dysql = DynamicService.getDynamicSql(temp, map);
		long time = System.currentTimeMillis()-start;
		System.out.println(time+"ms："+dysql);
		System.out.println(DynamicService.getDynamicSql(dysql, map));
		/*map = new HashMap<String, Object>();
		map.put("date",new Date().toLocaleString());
		map.put("num", 0);
		start = System.currentTimeMillis();
		dysql = DynamicService.getDynamicSql(temp, map);
		time = System.currentTimeMillis()-start;
		System.out.println(time+"ms："+dysql);*/
		
		System.out.println(DynamicService.getDynamicSqlParams(temp).toString());
	}

}
