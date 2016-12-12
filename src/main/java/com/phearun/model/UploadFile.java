package com.phearun.model;

import java.util.Arrays;

public class UploadFile {
	private byte[] file;
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	@Override
	public String toString() {
		return "UploadFile [file=" + Arrays.toString(file) + ", type=" + type + "]";
	}
}
