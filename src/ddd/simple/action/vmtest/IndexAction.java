package ddd.simple.action.vmtest;


import java.util.Date;

import org.springframework.stereotype.Controller;

import ddd.base.Config;
import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.base.helper.VMPage;

@Action
@RequestMapping("/Index")
@Controller
public class IndexAction
{
	
	public VMPage index(){
		VMPage vmPage = new VMPage(); 
		vmPage.setVmPath("/index.html");
		vmPage.putData("title", "新闻题目");
		vmPage.putData("content", "11223454646");
		vmPage.putData("time", new Date().toLocaleString());
		return vmPage;
	}
	
}