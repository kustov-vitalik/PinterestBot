package com.age.pinterest.api;

import java.io.DataOutputStream;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

import com.age.data.Board;
import com.age.data.CookieList;
import com.age.ui.Log;

public class ApiEditPin {
	public static void edit(String username, Board board, String pinId, String description, String link, CookieList cookies) {
		String boardId = board.getId();
		String boardName = board.getName();
		String urlParams = "source_url=%2F"
				+ username
				+ "%2F"
				+ boardName
				+ "%2F&data=%7B%22options%22%3A%7B%22board_id%22%3A%22"
				+ boardId
				+ "%22%2C%22description%22%3A%22"
				+ description
				+ "%22%2C%22link%22%3A%22"
				+ link
				+ "%22%2C%22place%22%3A0%2C%22id%22%3A%22"
				+ pinId
				+ "%22%7D%2C%22context%22%3A%7B%7D%7D&module_path=App()%3EBoardPage(resource%3DBoardResource(username%3D"
				+ username
				+ "%2C+slug%3D"
				+ boardName
				+ "))%3EGrid(resource%3DBoardFeedResource(board_id%3D"
				+ boardId
				+ "%2C+board_url%3D%2F"
				+ pinId
				+ "%2F"
				+ boardName
				+ "%2F%2C+board_layout%3Ddefault%2C+prepend%3Dtrue%2C+page_size%3Dnull%2C+access%3Dwrite%2Cdelete))%3EGridItems(resource%3DBoardFeedResource(board_id%3D"
				+ boardId
				+ "%2C+board_url%3D%2F"
				+ pinId
				+ "%2F"
				+ boardName
				+ "%2F%2C+board_layout%3Ddefault%2C+prepend%3Dtrue%2C+page_size%3Dnull%2C+access%3Dwrite%2Cdelete))%3EPin(resource%3DPinResource(id%3D"
				+ pinId + "))%3EShowModalButton(module%3DPinEdit)%23Modal(module%3DPinEdit(resource%3DPinResource(id%3D" + pinId + ")))";

		try {
			byte[] postData = urlParams.getBytes(Charset.forName("UTF-8"));
			int len = postData.length;
			String req = "https://www.pinterest.com/resource/PinResource/update/";
			HttpsURLConnection con = (HttpsURLConnection) new URL(req).openConnection();
			CommonHeaders.addCommonHeaders(con);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			con.setRequestProperty("Accept-Encoding", "json,deflate");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Cookie", cookies.toString());
			con.setRequestProperty("Content-Length", Integer.toString(len));
			con.setRequestProperty("X-CSRFToken", cookies.getSslCookie().getValue());
			con.setRequestProperty("Referer", "https://www.pinterest.com/");
			con.setRequestProperty("Origin", "https://www.pinterest.com/");

			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.write(postData);
			}
			Log.log("Response code from edit " + con.getResponseCode());

			con.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
