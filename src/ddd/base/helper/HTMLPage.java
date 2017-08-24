package ddd.base.helper;

public class HTMLPage {
	
	private String content;
	
	public HTMLPage() {
	}
	
	public HTMLPage(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return content;
	}

}
