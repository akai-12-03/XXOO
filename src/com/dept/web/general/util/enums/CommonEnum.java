package com.dept.web.general.util.enums;

public enum CommonEnum implements IEnum{
	/**
	 * 科室首页概要描述
	 */
	DEPT_TYPE_INDEX(0),
	/**
	 * 科室详细信息
	 */
	DEPT_TYPE_DETAILS(1)
	
	;
	private int type;
	
	private CommonEnum(int type){
		this.type = type;
	}

	@Override
	public int getType() {
		return type;
	}

}
