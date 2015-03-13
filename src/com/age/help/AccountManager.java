package com.age.help;

import java.util.List;

import com.age.data.Board;
import com.age.data.Pin;
import com.age.data.Pinner;
import com.age.data.PinterestAccount;
import com.age.pinterest.api.PinterestApi;

public class AccountManager {
	private final PinterestApi api;

	public AccountManager(PinterestAccount account) {
		api = new PinterestApi(account);
	}

	public List<Pinner> getFollowList(int size) {
		return api.getFollowList(size);
	}

	public List<Pinner> getUnfollowList(int minFollowers) {
		return api.getUnfollowList(minFollowers);
	}

	public void follow(Pinner target) {
		api.follow(target);
	}

	public void unfollow(Pinner target) {
		api.unfollow(target);
	}

	public void pin(Pin pin, Board board) {
		api.pin(pin, board);
	}

	public List<Board> getBords() {
		return api.getBoards();
	}
}
