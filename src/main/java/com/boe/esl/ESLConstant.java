package com.boe.esl;

public class ESLConstant {

	/**未下发订单*/
	public static final String ORDER_STATUS_UNSENT = "0";
	/**已下发订单*/
	public static final String ORDER_STATUS_SENTED = "1";
	
	/**已删除*/
	public static final int DEL_YES = 1;
	/**未删除*/
	public static final int DEL_NO = 0;
	
	/**普通用户*/
	public static final String ROLE_CODE_USER = "user";
	/**操作员*/
	public static final String ROLE_CODE_OPERATOR = "operator";
	/**管理员*/
	public static final String ROLE_CODE_ADMIN = "admin";
}
