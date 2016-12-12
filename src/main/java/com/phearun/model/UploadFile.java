package com.phearun.model;

import java.util.Arrays;

public class UploadFile {
	private byte[] file;
	private String type;
	private String name;
	private String extension;
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
	public String getExtension() {
		extension = name.substring(name.lastIndexOf(".") + 1);
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "UploadFile [file=" + Arrays.toString(file) + ", type=" + type + ", name=" + name + ", extension="
				+ getExtension() + "]";
	}
	
}
