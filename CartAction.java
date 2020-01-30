package com.internousdev.espresso.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.espresso.dao.CartInfoDAO;
import com.internousdev.espresso.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class CartAction extends ActionSupport implements SessionAware{

		//セッション
	private Map<String,Object> session;
		//カートの情報
	private List<CartInfoDTO> cartList;
		//合計金額
	int totalPrice = 0;
	private String errorMessage;

	/**
	 * カートの情報を受け取る
	 */
	public String execute() {

			//セッションタイムアウトかどうかを調べる
		if(!session.containsKey("tempId") && !session.containsKey("userId")) {
			return "sessionTimeout";
		}

			//初期化を行う
		String userId;
		cartList = new ArrayList<>();
			//ログインフラグを取り出す
		String loginedFlg = String.valueOf(session.get("loginFlg"));
			//文字列null(ろぐいんふらぐがいれられてない)場合、0それ以外はloginedFlgを数値化
		int logined = "null".equals(loginedFlg)? 0 : Integer.parseInt(loginedFlg);
			//ログインしていたらユーザーIDを、していなかったら仮ユーザーIDを取り出す
		userId = (logined == 1) ?
				session.get("userId").toString():
				session.get("tempId").toString();

		CartInfoDAO dao = new CartInfoDAO();

		 	//カート情報を受け取る
		cartList = dao.getCartInfoDTOList(userId);

			//合計金額を受け取る
		totalPrice = dao.getTotalPrice(userId);

		if(cartList.size() <= 0) {
			errorMessage = "カート情報はありません。";
		}

		return SUCCESS;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Map<String,Object> getSession(){
		return session;
	}

	public void setSession(Map<String,Object> session) {
		this.session = session;
	}

	public List<CartInfoDTO> getCartList() {
		return cartList;
	}

	public void setCartList(List<CartInfoDTO> cartList) {
		this.cartList = cartList;
	}



	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

}
