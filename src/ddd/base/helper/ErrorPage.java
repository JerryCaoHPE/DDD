package ddd.base.helper;

public class ErrorPage extends ForwardPage{

	private Exception exception;
	
	public ErrorPage(Exception exception){
		this.exception = exception;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
	
}
