package ddd.simple.action.util;


import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;

@Action
@RequestMapping("/UniqueAction")
@Controller
public class UniqueAction {

	
	public Boolean test(String username,String dbField) throws Exception{
		return false;
	}

}
