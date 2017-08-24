package ddd.base.helper;

import java.io.InputStream;

public class Img {
	
	private byte[] bytes;
	
	private InputStream inputStream;
	
	public Img() {
	}

	public Img(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public Img(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
}
