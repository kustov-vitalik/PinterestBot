package com.age.pinterest.api;

import java.util.ArrayList;
import java.util.List;

import com.age.data.Board;
import com.age.data.Pin;
import com.age.data.Pinner;
import com.age.data.PinterestAccount;
import com.age.data.User;
import com.age.ui.Log;

public class PinterestApi {
	private static final int WAVE_FOLLOW_USERS_NUM = 100;
	private final User user;

	public PinterestApi(PinterestAccount account) {
		this.user = setUpUser(account);
	}

	public PinterestApi(User user) {
		this.user = user;
	}

	public void follow(Pinner target) {
		ApiFollow.follow(target, user.getCookies());
	}

	public List<Board> getBoards() {
		return ApiGetBoards.getBoards(user.getAccount().getUser());
	}

	public List<Pinner> getFollowed(String target, int maxListSize, int minFollowers) {
		return ApiGetFollowed.getFollowed(target, maxListSize, minFollowers, user.getCookies());
	}

	public List<Pinner> getFollowers(String target, int maxListSize) {
		return ApiGetFollowers.getFollowers(target, maxListSize, user.getCookies());
	}

	public List<Pinner> getPinnersByKeyword(String keyword, int maxListSize) {
		return ApiGetPinnersByWord.getPinnersByKeyword(user.getAccount().getUser(), maxListSize, keyword, user.getCookies());
	}

	public void pin(Pin pin, Board board) {
		String pinId = ApiPin.pin(pin, user.getAccount().getUser(), board, user.getCookies());
		editPin(board, pinId, pin.getDescription(), pin.getSource());
	}

	public void unfollow(Pinner target) {
		ApiUnfollow.unfollow(user.getAccount().getUser(), target, user.getCookies());
	}

	public void editPin(Board board, String pinId, String description, String link) {
		ApiEditPin.edit(user.getAccount().getUser(), board, pinId, description, link, user.getCookies());
	}

	public User getManagedUser() {
		return this.user;
	}

	public List<Pinner> getFollowList(int minListSize) {
		Log.log("Will get " + minListSize);
		String thisUser = user.getAccount().getUser();
		List<Pinner> userFollowers = this.getFollowers(thisUser, WAVE_FOLLOW_USERS_NUM);
		while (userFollowers.size() <= WAVE_FOLLOW_USERS_NUM) {
			List<Pinner> extraPinners = ApiGetPinnersByWord.getPinnersByKeyword(user.getAccount().getUser(), WAVE_FOLLOW_USERS_NUM,
					"fashion", user.getCookies());
			userFollowers.addAll(extraPinners);
		}
		ArrayList<Pinner> targets = new ArrayList<Pinner>();
		for (Pinner p : userFollowers) {
			int remaining = minListSize - targets.size();
			if (remaining <= 0) {
				break;
			}
			List<Pinner> part = this.getFollowers(p.getUsername(), remaining);
			for (Pinner pnr : part) {
				targets.add(pnr);
			}
			if (targets.size() > minListSize) {
				break;
			}
		}
		return targets;
	}

	public List<Pinner> getUnfollowList(int minFollowers) {
		String thisUser = user.getAccount().getUser();
		return ApiGetFollowed.getFollowed(thisUser, -1, minFollowers, user.getCookies());
	}

	private User setUpUser(PinterestAccount acc) {
		User user = new User();
		user.setAccount(acc);
		user.setCookies(ApiLogin.login(user.getAccount()));
		user.setBoards(ApiGetBoards.getBoards(acc.getUser()));
		return user;
	}
}
