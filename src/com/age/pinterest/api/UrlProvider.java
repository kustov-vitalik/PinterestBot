package com.age.pinterest.api;

public class UrlProvider {
	public static String getBoards(String username) {
		return "https://www.pinterest.com/resource/UserResource/get/?source_url=%2F"
				+ username
				+ "%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22"
				+ username
				+ "%22%2C%22invite_code%22%3Anull%7D%2C%22context%22%3A%7B%7D%2C%22module%22%3A%7B%22name%22%3A%22UserProfileContent%22%2C%22options%22%3A%7B%22tab%22%3A%22boards%22%7D%7D%2C%22render_type%22%3A1%2C%22error_strategy%22%3A0%7D&_=1426178220430";
	}

	public static String getFollow(String username, String id) {
		return "source_url=/"
				+ username
				+ "/&data={\"options\":{\"user_id\":\""
				+ id
				+ "\"},\"context\":{}}&module_path=App()>UserProfilePage(resource=UserResource(username="
				+ username
				+ "))>UserProfileContent(resource=UserResource(username="
				+ username
				+ "))>Grid(resource=UserFollowingResource(username="
				+ username
				+ "))>GridItems(resource=UserFollowingResource(username="
				+ username
				+ "))>User(resource=UserResource(username="
				+ username
				+ "))>UserFollowButton(followed=false, class_name=gridItem, unfollow_text=Unfollow, follow_ga_category=user_follow, unfollow_ga_category=user_unfollow, disabled=false, is_me=false, follow_class=default, log_element_type=62, text=Follow, user_id="
				+ id + ", follow_text=Follow, color=default)";
	}

	public static String getFollowedPre(String username) {
		return "https://www.pinterest.com/resource/UserFollowingResource/get/?source_url=%2F"
				+ username
				+ "%2Ffollowing%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22"
				+ username
				+ "%22%7D%2C%22context%22%3A%7B%7D%7D&module_path=App()%3EUserProfilePage(resource%3DUserResource(username%3D"
				+ username
				+ "))%3EUserProfileContent(resource%3DUserResource(username%3D"
				+ username
				+ "%2C+invite_code%3Dnull))%3EFollowingSwitcher()%3EButton(class_name%3DnavScopeBtn%2C+text%3DPinners%2C+element_type%3Da%2C+rounded%3Dfalse)&_="
				+ Long.toString(System.currentTimeMillis());

	}

	public static String getFollowedPost(String username, String bookmark) {
		return "https://www.pinterest.com/resource/UserFollowingResource/get/?source_url=%2F" + username
				+ "%2Ffollowing%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22" + username + "%22%2C%22bookmarks%22%3A%5B%22"
				+ bookmark + "%3D%3D%22%5D%7D%2C%22context%22%3A%7B%7D%7D&module_path=App(module%3D%5Bobject+Object%5D)&_="
				+ Long.toString(System.currentTimeMillis());
	}

	public static String getFollowersPre(String username) {
		return "https://www.pinterest.com/resource/UserResource/get/?source_url=%2F"
				+ username
				+ "%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22"
				+ username
				+ "%22%2C%22invite_code%22%3Anull%7D%2C%22context%22%3A%7B%7D%2C%22module%22%3A%7B%22name%22%3A%22UserProfileContent%22%2C%22options%22%3A%7B%22tab%22%3A%22followers%22%7D%7D%2C%22render_type%22%3A1%2C%22error_strategy%22%3A0%7D&module_path=App()%3EUserProfilePage(resource%3DUserResource(username%3D"
				+ username
				+ "))%3EUserInfoBar(tab%3Dfollowers%2C+spinner%3D%5Bobject+Object%5D%2C+resource%3DUserResource(username%3D"
				+ username + "%2C+invite_code%3Dnull))&_=" + Long.toString(System.currentTimeMillis());
	}

	public static String getFollowersPost(String username, String bookmark) {
		return "https://www.pinterest.com/resource/UserFollowersResource/get/?source_url=%2F" + username
				+ "%2Ffollowers%2F&data=%7B%22options%22%3A%7B%22username%22%3A%22" + username + "%22%2C%22bookmarks%22%3A%5B%22"
				+ bookmark
				+ "%22%5D%7D%2C%22context%22%3A%7B%7D%7D&module_path=App()%3EUserProfilePage(resource%3DUserResource(username%3D"
				+ username
				+ "))%3EUserInfoBar(tab%3Dfollowers%2C+spinner%3D%5Bobject+Object%5D%2C+resource%3DUserResource(username%3D"
				+ username + "%2C+invite_code%3Dnull))&_=" + Long.toString(System.currentTimeMillis());
	}

	public static String getLogin(String email, String password) {
		return "source_url=%2flogin%2f&data=%7b%22options%22%3a%7b%22username_or_email%22%3a%22"
				+ email
				+ "%22%2c%22password%22%3a%22"
				+ password
				+ "%22%7d%2c%22context%22%3a%7b%7d%7d&module_path=App()%3eLoginPage()%3eLogin()%3eButton(class_name%3dprimary%2c+text%3dLog+in%2c+type%3dsubmit%2c+size%3dlarge)";
	}

	public static String getPin(String username, String boardName, String description, String boardId, String imageUrl) {
		return "https://www.pinterest.com/source_url=%2F"
				+ username
				+ "%2F"
				+ boardName
				+ "%2F&data=%7B%22options%22%3A%7B%22board_id%22%3A%22"
				+ boardId
				+ "%22%2C%22description%22%3A%22"
				+ description
				+ "%22%2C%22link%22%3A%22%22%2C%22image_url%22%3A%22"
				+ imageUrl
				+ "%22%2C%22method%22%3A%22uploaded%22%7D%2C%22context%22%3A%7B%7D%7D&module_path=PinUploader(default_board_id%3D"
				+ boardId + ")%23Modal(module%3DPinCreate())";
	}

	public static String getUnfollow(String thisUser, String username, String id) {
		return "source_url=/"
				+ thisUser
				+ "/following/&data={\"options\":{\"user_id\":\""
				+ id
				+ "\"},\"context\":{}}&module_path=App()>UserProfilePage(resource=UserResource(username="
				+ thisUser
				+ "))>UserProfileContent(resource=UserResource(username="
				+ thisUser
				+ "))>Grid(resource=UserFollowingResource(username="
				+ thisUser
				+ "))>GridItems(resource=UserFollowingResource(username="
				+ thisUser
				+ "))>User(resource=UserResource(username="
				+ username
				+ "))>UserFollowButton(followed=true, class_name=gridItem, unfollow_text=Unfollow, follow_ga_category=user_follow, unfollow_ga_category=user_unfollow, disabled=false, is_me=false, follow_class=default, log_element_type=62, text=Unfollow, user_id="
				+ id + ", follow_text=Follow, color=dim)";
	}
}
