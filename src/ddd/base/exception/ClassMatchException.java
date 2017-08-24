package ddd.base.exception;
/**
 *@author 作者:吴桥伟
 *@version 创建时间：2015年1月28日 下午9:18:37
 * 类说明
 */
public class ClassMatchException extends DDDException {
	public ClassMatchException(String code,String message) {
		super(code,message);
	}
	
	public ClassMatchException(String message) {
		super("ClassMatchException",message);
	}
}
