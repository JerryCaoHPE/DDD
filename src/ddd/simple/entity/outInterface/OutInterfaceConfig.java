package ddd.simple.entity.outInterface;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="outInterfaceConfig",label="外部接口配置")
public class OutInterfaceConfig extends Entity implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** 备注信息 */
	@Column(label = "名字", comment = "配置名字", nullable = true)
	private String configName;
	
	@Column(label = "数据库链接", comment = "数据库链接信息", nullable = true,length=200)
	private String connectInfo= " outDriverName==oracle.jdbc.driver.OracleDriver ;;\n"+
			 " outDbURL==jdbc:oracle:thin:@localhost:1521:cqaz ;;"+
			 " outUserName== ;;\n"+
			 " outPassword== ;;\n"+
			 " \n"+
			 " localDriverName==oracle.jdbc.driver.OracleDriver ;;\n"+
			 " localDbURL==jdbc:oracle:thin:@localhost:CCM ;;\n"+
			 " localUserName== ;;\n"+
			 " ;localPassword== ;;;\n";
	
	@Column(label = "源库查询语句", comment = "源库查询数据的语句", nullable = true,length=3000)
	private String outQuerySql;
	
	@Column(label = "目标库查询语句", comment = "目标数据库查询的语句", nullable = true,length=3000)
	private String localQuerySql;
	
	@Column(label = "目标库更新语句", comment = "目标库更新的语句", nullable = true,length=3000)
	private String localUpdateSql;
	
	@Column(label = "更新外部语句", comment = "更新外部数据库的语句", nullable = true,length=3000)
	private String outUpdateSql;
	
	@Column(label = "特殊参数", comment = "特殊参数  Srting:String 键值对", nullable = true)
	private String specialParam;
	
	@Column(label = "处理类", comment = "", nullable = true,length=100)
	private String excuteInterfaceBean;
	
	@Column(label = "启用状态", comment = "启用 ON、不起用  OFF", nullable = true)
	private String state;
	
	@Column(label = "是否是导入", comment = "导入 YES、导出NO", nullable = true)
	private String isImport = "YES";

	public String getConfigName()
	{
		return configName;
	}

	public void setConfigName(String configName)
	{
		this.configName = configName;
	}

	public String getConnectInfo()
	{
		return connectInfo;
	}

	public void setConnectInfo(String connectInfo)
	{
		this.connectInfo = connectInfo;
	}

	public String getOutQuerySql()
	{
		return outQuerySql;
	}

	public void setOutQuerySql(String outQuerySql)
	{
		this.outQuerySql = outQuerySql;
	}

	public String getLocalQuerySql()
	{
		return localQuerySql;
	}

	public void setLocalQuerySql(String localQuerySql)
	{
		this.localQuerySql = localQuerySql;
	}

	public String getLocalUpdateSql()
	{
		return localUpdateSql;
	}

	public void setLocalUpdateSql(String localUpdateSql)
	{
		this.localUpdateSql = localUpdateSql;
	}

	public String getOutUpdateSql()
	{
		return outUpdateSql;
	}

	public void setOutUpdateSql(String outUpdateSql)
	{
		this.outUpdateSql = outUpdateSql;
	}

	public String getSpecialParam()
	{
		return specialParam;
	}

	public void setSpecialParam(String specialParam)
	{
		this.specialParam = specialParam;
	}

	public String getExcuteInterfaceBean()
	{
		return excuteInterfaceBean;
	}

	public void setExcuteInterfaceBean(String excuteInterfaceBean)
	{
		this.excuteInterfaceBean = excuteInterfaceBean;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getIsImport()
	{
		return isImport;
	}

	public void setIsImport(String isImport)
	{
		this.isImport = isImport;
	}
	
	
}
