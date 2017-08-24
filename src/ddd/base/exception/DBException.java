package ddd.base.exception;

public class DBException extends DDDException {

	public DBException(String message) {
		super(message);
	}
	public DBException(String code,String message, Throwable cause) {
		super(code,message, cause);
	}
	public DBException( String code,String message) {
		super(code,message);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
