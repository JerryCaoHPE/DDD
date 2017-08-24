/**
 * @Title: CodeTableConfig.java
 * @Package ddd.base.codegenerator.generator
 * @Description: TODO(用一句话描述该文件做什么)
 * @author matao@cqrainbowsoft.com
 * @date 2015年11月4日 上午2:40:46
 * @version V1.0
 */
package ddd.base.codegenerator.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：DDD3 类名称：CodeTableConfig 类描述： 创建人：AnotherTen 创建时间：2015年11月4日 上午2:40:46
 * 修改人：胡均 修改时间：2015年11月4日 上午2:40:46 修改备注：
 * 
 * @version 1.0 Copyright (c) 2015 DDD
 */
public class CodeTableConfig
{
	private String 				codeTypeName;
	private String				codeTypeKey;
	private List<String>	codeTables	= new ArrayList<String>();
	private boolean				isExist;
	public String getCodeTypeKey()
	{
		return codeTypeKey;
	}
	public void setCodeTypeKey(String codeTypeKey)
	{
		this.codeTypeKey = codeTypeKey;
	}
	public List<String> getCodeTables()
	{
		return codeTables;
	}
	public void setCodeTables(List<String> codeTables)
	{
		this.codeTables = codeTables;
	}
	public boolean isExist()
	{
		return isExist;
	}
	public void setExist(boolean isExist)
	{
		this.isExist = isExist;
	}
	/** 
	* @return codeTypeName 
	*/ 
	
	public String getCodeTypeName()
	{
		return codeTypeName;
	}
	/** 
	* @param codeTypeName 要设置的 codeTypeName 
	*/
	
	public void setCodeTypeName(String codeTypeName)
	{
		this.codeTypeName = codeTypeName;
	}
}
