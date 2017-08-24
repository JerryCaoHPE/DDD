package ddd.base.exception;
/**
 *@author 作者:吴桥伟
 *@version 创建时间：2015年1月28日 下午9:18:37
 * 类说明
 */
public class WhereConditionException extends RuntimeException {
	public WhereConditionException() {
		super();
	}
	
	public WhereConditionException(String desc) {
		super(desc);
	}
}
