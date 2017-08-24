/**
* @Title: RouteMerge.java
* @package  ddd.develop
* @Description: TODO(用一句话描述该文件做什么)
* @author matao@cqrainbowsoft.com
* @date 2015年11月15日 上午2:11:59
* @version V1.0
*/
package  ddd.develop;

/**
 * 项目名称：DDD3
 * 类名称：RouteMerge
 * 类描述：   
 * 创建人：DDD
 * 创建时间：2015年11月15日 上午2:11:59
 * 修改人：DDD
 * 修改时间：2015年11月15日 上午2:11:59
 * 修改备注：   
 * @version 1.0
 * Copyright (c) 2015  DDD
 */
public class RouteMerge
{
	
	/** 
	 * @Title: main 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param args 
	 * @return void 
	 */
	public static void main(String[] args)
	{
		 ddd.simple.util.RouteMerge.merge("src\\ddd\\develop\\mergeConfig\\ddd\\routerList.txt","src\\ddd\\develop\\mergeConfig\\ddd\\whiteRouterList.txt","WebContent\\ddd\\extends\\js\\appRouter.js");
		 ddd.simple.util.RouteMerge.merge("src\\ddd\\develop\\mergeConfig\\vip\\routerList.txt","src\\ddd\\develop\\mergeConfig\\vip\\whiteRouterList.txt","WebContent\\ddd\\extends\\vip\\js\\appRouter.js");
		 ProjectCompile.merge();
	}
	
}
