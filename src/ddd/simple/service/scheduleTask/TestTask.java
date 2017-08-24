package ddd.simple.service.scheduleTask;

import java.util.Date;

public class TestTask {
	
	public void say(){
		System.out.println("hello ,"+new Date().toLocaleString());
	}
	
	public void say(String name){
		System.out.println("hello ,"+name+","+new Date().toLocaleString());
	}

}
