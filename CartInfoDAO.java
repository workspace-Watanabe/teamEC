package com.internousdev.espresso.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.espresso.dto.CartInfoDTO;
import com.internousdev.espresso.util.DBConnector;

public class CartInfoDAO {

	/**
	 * 	カートの情報を取得する
	 * @param userId ユーザーID
	 * @return 商品情報を格納したリスト
	 */
	public List<CartInfoDTO> getCartInfoDTOList(String userId) {
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		List<CartInfoDTO> cartInfoDTOList = new ArrayList<CartInfoDTO>();

			//カート情報テーブルをci,商品情報テーブルをpiとし、
			///商品IDを紐づけてテーブル結合
			////更新日と登録日から昇順
		String sql="select"
				+ " ci.id as id,"
				+ " ci.user_id as user_id,"
				+ "ci.product_id as product_id,"
				+ " ci.product_count as product_count,"
				+ " pi.price as price,"
				+ " pi.product_name as product_name,"
				+ " pi.product_name_kana as product_name_kana,"
				+ " pi.image_file_path as image_file_path, "
				+ " pi.image_file_name as image_file_name, "
				+ " pi.release_date as release_date,"
				+ " pi.release_company as release_company,"
				+ " pi.status as status,"
				+ " (ci.product_count * pi.price) as subtotal,"
				+ " ci.regist_date as regist_date,"
				+ " ci.update_date as update_date"
				+ " FROM cart_info as ci"
				+ " LEFT JOIN product_info as pi"
				+ " ON ci.product_id = pi.product_id"
				+ " WHERE ci.user_id = ?"
				+ " order by update_date desc, regist_date desc";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				CartInfoDTO cartInfoDTO = new CartInfoDTO();
				cartInfoDTO.setId(resultSet.getInt("id"));
				cartInfoDTO.setUserId(resultSet.getString("user_id"));
				cartInfoDTO.setProductId(resultSet.getInt("product_id"));
				cartInfoDTO.setProductCount(resultSet.getInt("product_count"));
				cartInfoDTO.setPrice(resultSet.getInt("price"));
				cartInfoDTO.setProductName(resultSet.getString("product_name"));
				cartInfoDTO.setProductNameKana(resultSet.getString("product_name_kana"));
				cartInfoDTO.setImageFilePath(resultSet.getString("image_file_path"));
				cartInfoDTO.setImageFileName(resultSet.getString("image_file_name"));
				cartInfoDTO.setReleaseDate(resultSet.getDate("release_date"));
				cartInfoDTO.setReleaseCompany(resultSet.getString("release_company"));
				cartInfoDTO.setStatus(resultSet.getString("status"));
				cartInfoDTO.setSubtotal(resultSet.getInt("subtotal"));
				cartInfoDTOList.add(cartInfoDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cartInfoDTOList;
	}

	/**
	 * 合計金額を取り出す
	 * @param userId ユーザーID
	 * @return 合計金額
	 */
	public int getTotalPrice(String userId) {
		int totalPrice = 0;
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();

			//商品のこすうと値段を掛けて合計金額を導き出す
		String sql = "select "
					+ "sum(product_count * price) as total_price "
					+ "from cart_info as ci "
					+ "join product_info as pi "
					+ "on ci.product_id = pi.product_id "
					+ "where user_id=? group by user_id";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userId);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				totalPrice = resultSet.getInt("total_price");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return totalPrice;
	}


	/**
	 * 商品を登録する
	 *
	 * @param userId ユーザーID
	 * @param productId 商品ID
	 * @param productCount 商品の個数
	 * @return 挿入したデータの件数
	 */
	public int regist(String userId, int productId, int productCount) {
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		int count = 0;
			//カート情報テーブルに商品情報を挿入する(登録日と更新日には現在の日時を入れる)
		String sql = "insert into cart_info(user_id, product_id, product_count, regist_date, update_date)"
				+ " values (?, ?, ?, now(), now())";

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setInt(2, productId);
			ps.setInt(3, productCount);

			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	/**
	 * カートにすでに存在する商品の購入個数情報を更新する(数を加える)。
	 * @param userId ユーザーID
	 * @param productId 商品ID
	 * @param productCount 商品個数
	 * @return 更新件数
	 */
	public int updateProductCount(String userId, int productId, int productCount){
		//
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
			//現在の商品個数に新たに商品個数を上乗せする
		    ///更新日に現在日時を入れる
		String sql = "UPDATE cart_info SET product_count=(product_count + ?), update_date = now() WHERE user_id=? AND product_id=?";

		int result = 0;

		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1,productCount);
			ps.setString(2,userId);
			ps.setInt(3,productId);
			result = ps.executeUpdate();

		}catch(SQLException e){
			e.printStackTrace();
		} finally {
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 *
	 * @param productId
	 * @param userId
	 * @return
	 */
	public int delete(String productId, String userId) {
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		int count = 0;
		String sql = "delete from cart_info where product_id=? and user_id=?";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, productId);
			preparedStatement.setString(2, userId);

			count = preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	public int deleteAll(String userId) {
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		int count = 0;
		String sql = "delete from cart_info where user_id=?";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userId);

			count = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	/**
	 * 引数として受け取ったuserIdのユーザーが、productIdの商品のカートに入れた情報が存在するかどうかを判別する
	 * @param userId ユーザーID
	 * @param productId 商品ID
	 * @return true:存在する、false：存在しない
	 */
	public boolean isExistsCartInfo (String userId, int productId) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		String sql = "SELECT COUNT(id) AS COUNT FROM cart_info WHERE user_id = ? and product_id=?";

		boolean result = false;

		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setInt(2, productId);
			ResultSet rs = ps.executeQuery();

			if(rs.next()){
				if(rs.getInt("COUNT") > 0) {
					result = true;
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		} finally {
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 仮ユーザーIDに紐づいているカート情報をユーザーIDに紐づけ直す。
	 * @param tempId 仮ユーザーID
	 * @param userId ユーザーID
	 * @param productId 商品ID
	 * @return 処理件数
	 */
	public int linkToUserId(String tempId, String userId, int productId) {
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		int count = 0;
		String sql = "update cart_info set user_id=?, update_date = now() where user_id=? and product_id=?";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userId);
			preparedStatement.setString(2, tempId);
			preparedStatement.setInt(3, productId);
			count = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

}
