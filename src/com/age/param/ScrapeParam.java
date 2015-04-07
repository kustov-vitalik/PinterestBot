package com.age.param;

public class ScrapeParam {
	private String keyword;
	private String tag;
	private int count;

	public ScrapeParam(String keyword, String tag, int count) {
		this.keyword = keyword;
		this.tag = tag;
		this.count = count;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return this.tag;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return this.count;
	}

}