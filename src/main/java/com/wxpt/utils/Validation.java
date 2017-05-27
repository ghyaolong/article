package com.wxpt.utils;

/**
 * @author lihui 正则表达式对数据进行效验 工具类
 */
public class Validation {

	/**
	 * 大于0，小于等于“小额大额界线”金额格式，经确到小数点后两位（只能输入“ . ”和数字，输入到小数点后两位，无法输入） 提示：输入金额不正确
	 */
	String Money = "^(([1-9]+)|([0-9]+\\.[0-9]{1,2}))${0,50000}";

	/**
	 * 大额充值 付款账户名 32个字符以内
	 */
	String AccountName = "^.{1,32}$";

	/**
	 * 转账银行 32个字符以内
	 */
	String bank = "^.{1,32}$";

	/**
	 * 付款账号 纯数字 32个字符以内
	 */
	String panymentAccount = "^[0-9]*${1,32}";

	/**
	 * 转账流水号       纯数字 或 数字和字母组合 或 纯字母。
	 */
	String SerialNumber = "^[0-9]*$|^[A-Za-z0-9]+$";

	/**
	 * 充值金额
	 */
	String Topupamount = "^(([1-9]\\d{0,9})|0)(\\.\\d{1,2})?$";

	/**
	 * 备注 120个字符以内
	 */
	String Note = "^.{1,120}$";

	/**
	 * 登录密码
	 */
	// String LogigPassword =
	// "(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*])(?=.*[A-Z]).{6,16}";
	//
	// String PayThePassword = "^[0-9]{6}$";
	//
	// String Name = "^.{1,16}$";
	//
	// String Phone = "^(13[0-9]|15[0-9]|18[0-9])\\d{8}$";
	/**
	 * 转账金额
	 */
	String ZhuanBlance = "^(([0-9]\\d{0,9})|0)(\\.\\d{1,2})?$";

	public String getMoney() {
		return Money;
	}

	public String getAccountName() {
		return AccountName;
	}

	public String getBank() {
		return bank;
	}

	public String getPanymentAccount() {
		return panymentAccount;
	}

	public String getSerialNumber() {
		return SerialNumber;
	}

	public String getTopupamount() {
		return Topupamount;
	}

	public String getNote() {
		return Note;
	}

	public String getZhuanBlance() {
		return ZhuanBlance;
	}

}
