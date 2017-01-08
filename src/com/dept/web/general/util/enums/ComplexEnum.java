package com.dept.web.general.util.enums;

/**
 * 综合枚举
 * @author h.yang
 * 贵州省平台
 * 2012-8-30
 */
public enum ComplexEnum implements IEnum{
	/**
	 * 类型1：视频
	 */
	COMPLEX_VIDEO(1),
	/**
	 * 类型2：治疗特色
	 */
	COMPLEX_TREATMENT_CHARACTERISTICS(2),
	/**
	 * 类型3：科普知识
	 */
	COMPLEX_SCIENCE_KNOWLEDGE(3),
	/**
	 * 类型4：新闻动态
	 */
	COMPLEX_NEWS(4),
	/**
	 * 类型5：护理天地
	 */
	COMPLEX_CARE_HEAVEN_AND_EARTH(5),
	/**
	 * 类型6：联系我们
	 */
	COMPLEX_CONTACT_US(6)
	;

	private int type;
	
	
	private ComplexEnum(int type){
		this.type = type;
	}
	

	@Override
	public int getType() {
		return type;
	}

}
