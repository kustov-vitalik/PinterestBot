package com.age.param;

public class GeneratePinsParam {
	private String tag;
	private String source;

	public GeneratePinsParam(String tag, String source) {
		this.tag = tag;
		this.source = source;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return this.tag;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return this.source;
	}
}
