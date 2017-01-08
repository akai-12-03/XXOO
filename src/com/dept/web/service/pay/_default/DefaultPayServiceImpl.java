package com.dept.web.service.pay._default;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.dept.web.context.Constant;
import com.dept.web.dao.model.UserAccount;
import com.dept.web.dao.model.UserRecharge;
import com.dept.web.general.util.NewDateUtils;
import com.dept.web.service.pay.AbstractPayServiceImpl;

@Service("defaultPayService")
public class DefaultPayServiceImpl extends AbstractPayServiceImpl {

	@Override
	public UserRecharge creatreCharge(UserRecharge recharge) {

		recharge.setCardNo("xxxxxx");//
		recharge.setRemark("网站充值" + recharge.getAccount() + "元");
		return recharge;
	}

	@Override
	public String payChargeNofity(Map<String, String> params) {
		if (payNofityValidate(params)) {
			String out_trade_no = params.get("out_trade_no");
			UserRecharge userRecharge = rechargeService
					.getRechargeByOrderNo(out_trade_no);
			if (userRecharge.getStatus() == 0) {// 第一次回调
				// 用户账户增加充值金额
				userAccountService.recharge(userRecharge.getCreatedBy(),
						userRecharge.getMoneyRecharge());
				userRecharge.setStatus(1);
				userRecharge.setPayResult("success");
				userRecharge.setUpdatedAt(NewDateUtils.getNowTimeStr());
				// 更新充值记录
				rechargeService.updateRecharge(userRecharge);

				// 生成资金变动记录
				UserAccount account = userAccountService
						.getUserAccount(userRecharge.getCreatedBy());

				accountLogService.addUserAccountLog(account,
						userRecharge.getCreatedBy(),
						userRecharge.getMoneyRecharge(),
						Constant.ACCOUNT_LOG_TYPE_TZCG,
						userRecharge.getCreatedIp(), userRecharge.getRemark());

			}

		}

		return null;
	}

	@Override
	public boolean payNofityValidate(Map<String, String> params) {
		return true;
	}

}
