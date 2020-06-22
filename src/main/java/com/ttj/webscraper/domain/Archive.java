package com.ttj.webscraper.domain;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="Archive", description ="Archives")
public class Archive implements Serializable{
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("Line And Size Archive")
	private String lineAndSizeTag;

	@ApiModelProperty("Name Archive")
	private String file;
	
	public Archive() {
		
	}
	public Archive(String lineAndSizeTag,String file) {
		super();
		this.lineAndSizeTag = lineAndSizeTag;
		this.file = file;

	}
	public String list() {
		return file + lineAndSizeTag;
	}

	public void setlineAndSizeTag(String lineAndSizeTag) {
		this.lineAndSizeTag = lineAndSizeTag;
	}
		
	public void file(String file) {
		this.file = file;
	}

	public String file() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}

	public String lineAndSizeTag() {
		return lineAndSizeTag;
	}
	public void setLineAndSizeTag(String lineAndSizeTag) {
		this.lineAndSizeTag = lineAndSizeTag;
	}



}
