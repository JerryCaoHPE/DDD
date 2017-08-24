package ddd.simple.action.ddd3util;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import ddd.base.annotation.Action;
import ddd.base.annotation.RequestMapping;
import ddd.simple.service.ddd3util.Ddd3utilService;

@Action
@RequestMapping("/Ddd3util")
@Controller
public class Ddd3utilAction
{ 
	@Resource(name="ddd3utilServiceBean")
	private Ddd3utilService ddd3utilService;
	
	public String generatorFileds(List<TabInfo> tabs)
	{
		return this.ddd3utilService.generatorFileds(tabs);
	}
}