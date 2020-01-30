package com.internousdev.espresso.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.espresso.dao.CartInfoDAO;
import com.internousdev.espresso.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class AddCartAction extends ActionSupport implements SessionAware{
	//商品ID
	private int productId;
	private List<CartInfoDTO> cartList;
	private Map<String,Object> session;
	private int totalPrice;
	private int productCount;

		/**
		 *  カートに追加
		 */
	public String execute() {

		if(!session.containsKey("tempId") && !session.containsKey("userId")) {
			return "sessionTimeout";
		}

		String result = ERROR;
		String userId = "";

		String tempId = String.valueOf(session.get("loginFlg"));
			//ログインフラグがnullだったら'0', それ以外だったら 'tempId'を数値化したもの
		int logined = "null".equals(tempId)? 0 : Integer.parseInt(tempId);

			//ログインしていたらuserIdにユーザーIDをしていなかったら仮ユーザーIDを入れる
		userId = (logined == 1)?
				session.get("userId").toString() :
				session.get("tempId").toString();
			//カートDAOをインスタンス化
		CartInfoDAO dao = new CartInfoDAO();
		int count = 0;
			//userIdのユーザーがproductIdの商品のカートに入れた情報が存在するかどうかチェック
		if(dao.isExistsCartInfo(userId, productId)) {
				//存在する場合、商品の個数を増やす
			count = dao.updateProductCount(userId, productId, productCount);
		}else {
				//存在しない場合、新たに登録する
			count = dao.regist(userId, productId, productCount);
		}

			//カウントが0以上の場合
		if(count > 0) {
				//カート情報を取り出す
			cartList = dao.getCartInfoDTOList(userId);
				//合計金額を取り出す
			totalPrice = dao.getTotalPrice(userId);

			result = SUCCESS;
		}

		return result;
	}
		public int getProductId() {
			return productId;
		}
		public void setProductId(int productId) {
			this.productId = productId;
		}

		public List<CartInfoDTO> getCartList() {
			return cartList;
		}
		public void setCartList(List<CartInfoDTO> cartList) {
			this.cartList = cartList;
		}
		public Map<String, Object> getSession() {
			return session;
		}
		public void setSession(Map<String, Object> session) {
			this.session = session;
		}
		public int getTotalPrice() {
			return totalPrice;
		}
		public void setTotalPrice(int totalPrice) {
			this.totalPrice = totalPrice;
		}
		public int getProductCount() {
			return productCount;
		}
		public void setProductCount(int productCount) {
			this.productCount = productCount;
		}

}
