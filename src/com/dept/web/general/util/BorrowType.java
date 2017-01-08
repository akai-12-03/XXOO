/**
 * 邦尼财富专用type定义
 */
package com.dept.web.general.util;


public class BorrowType {
	//标状态定义
	public String getBorrowName(int type){
			
			 if(type==1){
				return "邦诺盈";
			}else if(type==2){
				return "邦即盈";
			}else if(type==3){
				return "邦稳盈 ";
			}else if(type==4){
				return "邦安盈";
			}else if(type==5){
				return "信托";
			}else if(type==6){
				return "资管";
			}else if(type==7){
				return "基金";
			}else{
				return "未知";
			}
			
		}
	//还款类型
	public String getRepaymentType(int type){
		 if(type==1){
			return "等额本息";
		}else if(type==2){
			return "到期还本还息";
		}else if(type==3){
			return "按月付息到期还本";
		}
		return "未知";
	}
	//银行简称缩写
	public String getBankName(String status){
		if(status==null||"".equals(status)){
			return "无";
		}else if(status.equals("BOCO")){
			return "交通银行";
		}else if(status.equals("CEB")){
			return "光大银行";
		}else if(status.equals("SPDB")){
			return "上海浦东发展银行";
		}else if(status.equals("ABC")){
			return "农业银行";
		}else if(status.equals("ECITIC")){
			return "中信银行";
		}else if(status.equals("CCB")){
			return "建设银行";
		}else if(status.equals("CMBC")){
			return "民生银行";
		}else if(status.equals("SDB")){
			return "平安银行";
		}else if(status.equals("POST")){
			return "中国邮政储蓄";
		}else if(status.equals("CMBCHINA")){
			return "招商银行";
		}else if(status.equals("CIB")){
			return "兴业银行";
		}else if(status.equals("ICBC")){
			return "中国工商银行";
		}else if(status.equals("BOC")){
			return "中国银行";
		}else if(status.equals("BCCB")){
			return "北京银行";
		}else if(status.equals("GDB")){
			return "广发银行";
		}else if(status.equals("HXB")){
			return "华夏银行";
		}else if(status.equals("XAYHGFYHGS")){
			return "西安市商业银行";
		}else if(status.equals("SHYH")){
			return "上海银行";
		}else if(status.equals("TJYH")){
			return "天津市商业银行";
		}else if(status.equals("SZNCSYYHGFYHGS")){
			return "深圳农村商业银行";
		}else if(status.equals("BJNCSYYHGFYHGS")){
			return "北京农商银行";
		}else if(status.equals("HZYHGFYHGS")){
			return "杭州市商业银行";
		}else if(status.equals("KLYHGFYHGS")){
			return "昆仑银行";
		}
		
		else if(status.equals("ZZYH")){
			return "郑州银行";
		}else if(status.equals("WZYH")){
			return "温州银行";
		}else if(status.equals("HKYH")){
			return "汉口银行";
		}else if(status.equals("NJYHGFYHGS")){
			return "南京银行";
		}else if(status.equals("XMYHGFYHGS")){
			return "厦门银行";
		}else if(status.equals("NCYH")){
			return "南昌银行";
		}else if(status.equals("JISYHGFYHGS")){
			return "江苏银行";
		}else if(status.equals("HKBEA")){
			return "东亚银行(中国)";
		}else if(status.equals("CDYH")){
			return "成都银行";
		}else if(status.equals("NBYHGFYHGS")){
			return "宁波银行";
		}else if(status.equals("CSYHGFYHGS")){
			return "长沙银行";
		}else if(status.equals("HBYHGFYHGS")){
			return "河北银行";
		}else if(status.equals("GUAZYHGFYHGS")){
			return "广州银行";
		}
		return "无";
	}
}

