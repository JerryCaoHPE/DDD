/**
* @Title: DeveloperFacade.java
* @Package ddd.develop
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年11月15日 下午2:52:04
* @version V1.0
*/
package ddd.develop;

import ddd.simple.util.RouteMerge;

/**
 * 项目名称：QOSMS
 * 类名称：DeveloperFacade
 * 类描述：   
 * 创建人：DDD
 * 创建时间：2015年11月15日 下午2:52:04
 * 修改人：DDD
 * 修改时间：2015年11月15日 下午2:52:04
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public class DeveloperFacade
{
	
	/** 
	 * @Title: main 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param args 
	 * @return void 
	 */
	public static void main(String[] args)
	{
		 ProjectFileSynchro.synchro();
		 ddd.simple.util.RouteMerge.merge("src\\ddd\\develop\\mergeConfig\\ddd\\routerList.txt","src\\ddd\\develop\\mergeConfig\\ddd\\whiteRouterList.txt","WebContent\\ddd\\extends\\js\\appRouter.js");
		 ProjectCompile.merge();
		 ProjectDDD3Jar.start();
		
	}
	
}
