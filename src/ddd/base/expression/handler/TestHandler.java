package ddd.base.expression.handler;

import java.util.Date;

import org.springframework.stereotype.Service;

import ddd.base.expression.ExpressionHandler;

@Service
public class TestHandler implements ExpressionHandler{

	@Override
	public Object execute() {
		
		System.out.println("ddddd");
		return null;
	}
	
	
	public Object 哈哈(String name,Date date){
		System.out.println(name+":"+date.toLocaleString());
		return null;
	}
	public Object 哈哈2(String name,String age,String hh){
		System.out.println(name+":"+age+hh);
		return null;
	}
}
