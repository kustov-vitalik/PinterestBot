package com.age.data;

public class Cookie {
	private static final String COOKIE_FORMAT = "%1$s=%2$s;";
	private String name;
	private String value;

	Cookie() {
	}

	public Cookie(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return String.format(COOKIE_FORMAT, this.name, this.value);
	}

}
