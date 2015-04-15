package com.age.param;

import com.age.data.PinterestAccount;

public class AddAccountParam {
	private PinterestAccount account;

	public AddAccountParam(PinterestAccount account) {
		this.account = account;
	}

	public void setAccount(PinterestAccount account) {
		this.account = account;
	}

	public PinterestAccount getAccount() {
		return this.account;
	}
}
