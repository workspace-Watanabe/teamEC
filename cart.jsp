<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ja">
<head>
<script
src="https://code.jquery.com/jquery-3.3.1.min.js"
integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
crossorigin="anonymous"></script>
<script src="./js/cart.js"></script>
<link rel="stylesheet" type="text/css" href="./css/style.css">
<link rel="stylesheet" type="text/css" href="./css/table.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>カート画面</title>
</head>
<body>
<jsp:include page="header.jsp" />

	<!-- ================================================================ -->
	<!-- ===================== ここからメイン画面 ================= -->

	<div id="main">
		<div id="mainLogo">
			<h1>カート画面</h1>
		</div>

		<s:if test="cartList != null && cartList.size()>0">
			<s:form action="DeleteCartAction" method="POST">
				<table class="not_form_table_list">
					<tr>
						<th>#</th>
						<th>商品名</th>
						<th>商品名(ふりがな)</th>
						<th>商品画像</th>
						<th>値段</th>
						<th>発売会社名</th>
						<th>発売年月日</th>
						<th>購入個数</th>
						<th>合計金額</th>
					</tr>

					<s:iterator value="cartList">

						<tr>
							<td><input type="checkbox" name="checkList"
								class="checkList" value='<s:property value="productId" />'
								onchange="checkValue()"></td>
							<td><s:property value="productName" /></td>
							<td><s:property value="productNameKana" /></td>
							<td><img src='<s:property value="imageFilePath"/>'
								width="50px" height="50px" /></td>
							<td><s:property value="price" />円</td>
							<td><s:property value="releaseCompany" /></td>
							<td><s:property value="releaseDate" /></td>
							<td><s:property value="productCount" /></td>
							<td><s:property value="subtotal" />円</td>
						</tr>
					</s:iterator>
				</table>
				<!-- 合計金額 -->
				<h3>
					<label>カート合計金額</label>
					<s:property value="totalPrice" />円
				</h3>
				<!-- 削除ボタン -->

				<div class="submit_btn_box">
					<s:submit value="削除" id="deleteButton" class="submit_btn"
						disabled="true" />
				</div>

			</s:form>

				<div class="submit_btn_box">
					<s:if test="#session.loginFlg==1">
						<s:form id="form" action="SettlementConfirmAction" method="POST">
							<s:submit value="決済" class="submit_btn" id ="confirm" />
						</s:form>
					</s:if>
					<s:else>
						<s:submit value="決済" class="submit_btn" id="goLogin"/>
							<s:form action="GoLoginAction" method="POST" id="login">
								<s:hidden name="cartFlg" value="1" />
							</s:form>
					</s:else>
				</div>

		</s:if>
		<s:else>
			<div class="info">カート情報はありません。</div>
		</s:else>
	</div>

</body>
</html>