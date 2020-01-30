package com.internousdev.espresso.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.espresso.dao.CartInfoDAO;
import com.internousdev.espresso.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteCartAction extends ActionSupport implements SessionAware{

	private Map<String,Object> session;
	private String[] checkList;
	private List<CartInfoDTO> cartList;
	private int totalPrice;

	public String execute() {

			if(!session.containsKey("tempId") && !session.containsKey("userId")) {
				return "sessionTimeout";
			}

			String result = ERROR;
			CartInfoDAO dao = new CartInfoDAO();
			int count = 0;
			String userId = null;
			String tempId = String.valueOf(session.get("loginFlg").toString());
			int logined = "null".equals(tempId)? 0 : Integer.parseInt(tempId);
			if(logined == 1) {
				userId = session.get("userId").toString();
			}else {
				userId = String.valueOf(session.get("tempId"));
			}

			for(String productId : checkList) {

				count += dao.delete(productId, userId);
			}

			if(count == checkList.length) {

				cartList = dao.getCartInfoDTOList(userId);
				totalPrice = dao.getTotalPrice(userId);
				result = SUCCESS;
			}
			return result;
	}

	public Map<String,Object> getSession(){
		return session;
	}

	@Override
	public void setSession(Map<String,Object> session) {
		this.session = session;
	}

	public String[] getCheckList() {
		return checkList;
	}

	public void setCheckList(String[] checkList) {
		this.checkList = checkList;
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


