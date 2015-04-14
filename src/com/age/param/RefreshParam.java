package com.age.param;

import com.age.data.PinterestAccount;

public class RefreshParam {
	private PinterestAccount account;

	public RefreshParam(PinterestAccount account) {
		this.account = account;
	}

	public void setAccount(PinterestAccount account) {
		this.account = account;
	}

	public PinterestAccount getAccount() {
		return this.account;
	}

}
