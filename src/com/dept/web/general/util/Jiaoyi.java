package com.dept.web.general.util;

public class Jiaoyi {
	public String getjyname(int type){
		if(type==1){
			return "充值成功";
		}else if(type==2){
			return "配资支付保证金成功";
		}else if(type==3){
			return "提现冻结";
		}else if(type==4){
				return "红包返现";	
		}else if(type==5){
			return "提现成功";
		}else if(type==6){
			return "提现失败";
		}else if(type==7){
			return "线下充值成功";
		}else if(type==12){
			return "线下扣款成功";
		}else if(type==14){
			return "分配账号扣除保证金";
		}else if(type==15){
			return "追加保证金扣除";
		}else if(type==16){
			return "投资成功生成待收金额";
		}else if(type==22){
			return "退回保证金 ";
		}else if(type==31){
			return "投资成功";
		}else if(type==32){
			return "投资成功后奖励";
		}else if(type==33){
			return "借款入账";
		}else if(type==34){
			return "借款入账扣除奖励";
		}else if(type==35){
			return "网站垫付扣除罚息";
		}else if(type==36){
			return "还款扣除本金";
		}else if(type==37){
			return "还款扣除利息";
		}else if(type==38){
			return "还款扣除逾期利息";
		}else if(type==39){
			return "还款归还本金";
		}else if(type==40){
			return "还款归还利息";
		}else if(type==41){
			return "还款归还利息管理费";
		}else if(type==42){
			return "复审失败待审核";
		}else if(type==43){
			return "复审不通过";
		}else if(type==44){
			return "冻结还款的本息";
		}else if(type==45){
			return "手续费";
		}
		else if(type==88)
		{
			return "债权转让";
		}else if(type==99){
			return "投资成功返还红包";
		}
		return "未知";
	}
}
