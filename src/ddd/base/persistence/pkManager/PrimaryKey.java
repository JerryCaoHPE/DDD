package ddd.base.persistence.pkManager;

import java.io.Serializable;

import ddd.base.annotation.Column; import java.util.Date;
import ddd.base.persistence.baseEntity.Entity;

@ddd.base.annotation.Entity(name="PrimaryKey",label="序列配置")
public class PrimaryKey extends Entity implements Serializable {
	
    
	//web服務器的實例ID,即唯一標識一箇Web實例
	@Column(name="serverId",nullable=false,label="服务标识")
    private String serverId;
    
    //序列名称,如果key是與實例相關，則此是數據庫錶的名稱，如果不相關，則 PrimaryKey 的唯一標識
	@Column(name="name",nullable=false,unique=false,label="序列名称",comment="序列名称,如果序列与实体相关，则此是实体对应的数据库表名，如果不相关，则是序列的唯一表识")
    private String name;
 
    //用這箇Key相關的字段名稱
	@Column(name="tableName",nullable=false,label="表名",comment="使用序列所在的表名")
    private String tableName ="";
	
    //用這箇Key相關的字段名稱
	@Column(name="columnName",nullable=false,label="字段名",comment="")
    private String columnName ="";
    
    //噹前的key
	@Column(name="currentKey",nullable=false,label="当前序列号")
    private Long currentKey;
    
    //噹前已分配範圍的起始key
	@Column(name="startKey",nullable=false,label="缓存开始号",comment="当前缓存开始号")
    private Long startKey;
    
    //噹前已分配範圍的結束key，如果 currentKey 等 于 endKey，則需要重新分配大小爲 incrementSize的最新範圍，
    //如果 endKey大于maxKey,則需要爲此server(即App)重新分配 maxKey 和 minkey
	@Column(name="endKey",nullable=false,label="缓存结束号",comment="当前缓存结束号")
    private Long endKey;
    
	@Column(name="maxKey",nullable=false,label="分配的最大号",comment="给当前服务器分配的最大号")
    private Long maxKey;
    
	@Column(name="minKey",nullable=false,label="分配的最小号",comment="给当前服务器分配的最小号")
    private Long minKey;
    
	@Column(name="banKey",nullable=true,label="禁止的序列号",comment="禁止分配的序列号，即在分配时要路过这些号")
    private String banKey=" ";
    
	@Column(name="keyType",nullable=false,label="序列类型",comment="序列分类：table 用于表的EId，code 用于编码，other 用于其他用途")
    private String keyType;
    
    //如果分段不足，增加的大小
	@Column(name="cachedSize",nullable=false,label="缓存大小",comment="即每次缓存的序列号的大小，通常有：缓存大小=缓存结束号 - 缓存开始号")
    private Long cachedSize;
    
	@Column(name="billCodeTypeName",nullable=true,label="使用序列的编码类型名称")
	private String billCodeTypeName = " ";
	
    private boolean needUpdate = false;
 
	public Long getCurrentKey() {
		return currentKey;
	}

	public void setCurrentKey(Long currentKey) {
		this.currentKey = currentKey;
	}

	public Long getStartKey() {
		return startKey;
	}

	public void setStartKey(Long startKey) {
		this.startKey = startKey;
	}

	public Long getEndKey() {
		return endKey;
	}

	public void setEndKey(Long endKey) {
		this.endKey = endKey;
	}

	public String getBanKey() {
		return banKey;
	}

	public void setBanKey(String banKey) {
		this.banKey = banKey;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public boolean isNeedUpdate() {
		return needUpdate;
	}

	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

 
 
 
	/** 
	* @return maxKey 
	*/ 
	
	public Long getMaxKey()
	{
		return maxKey;
	}

	/** 
	* @param maxKey 要设置的 maxKey 
	*/
	
	public void setMaxKey(Long maxKey)
	{
		this.maxKey = maxKey;
	}

	/** 
	* @return minKey 
	*/ 
	
	public Long getMinKey()
	{
		return minKey;
	}

	/** 
	* @param minKey 要设置的 minKey 
	*/
	
	public void setMinKey(Long minKey)
	{
		this.minKey = minKey;
	}

	/** 
	* @return cachedSize 
	*/ 
	
	public Long getCachedSize()
	{
		return cachedSize;
	}

	/** 
	* @param cachedSize 要设置的 cachedSize 
	*/
	
	public void setCachedSize(Long cachedSize)
	{
		this.cachedSize = cachedSize;
	}

	/** 
	* @return name 
	*/ 
	
	public String getName()
	{
		return name;
	}

	/** 
	* @param name 要设置的 name 
	*/
	
	public void setName(String name)
	{
		this.name = name;
	}

	/** 
	* @return serverId 
	*/ 
	
	public String getServerId()
	{
		return serverId;
	}

	/** 
	* @param serverId 要设置的 serverId 
	*/
	
	public void setServerId(String serverId)
	{
		this.serverId = serverId;
	}

	/** 
	* @return tableName 
	*/ 
	
	public String getTableName()
	{
		return tableName;
	}

	/** 
	* @param tableName 要设置的 tableName 
	*/
	
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	/** 
	* @return billCodeTypeName 
	*/ 
	
	public String getBillCodeTypeName()
	{
		return billCodeTypeName;
	}

	/** 
	* @param billCodeTypeName 要设置的 billCodeTypeName 
	*/
	
	public void setBillCodeTypeName(String billCodeTypeName)
	{
		this.billCodeTypeName = billCodeTypeName;
	}

	/** 
	* @return columnName 
	*/ 
	
	public String getColumnName()
	{
		return columnName;
	}

	/** 
	* @param columnName 要设置的 columnName 
	*/
	
	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}
 
}
