package com.dept.web.context;

import java.io.UnsupportedEncodingException;

/**
 * 
 * 
 * @ClassName:     Constant
 * @Description:   
 *
 * @author         cannavaro
 * @version        V1.0 
 * @Date           2014年11月10日 下午7:14:24 
 * <b>Copyright (c)</b> 雄猫软件版权所有 <br/>
 */
public class Constant {

    /**
     * 借款标_等待初审 =0
     */
    public static int BORROW_STATUS_DDCS = 0;
    
    /**
     * 借款标_初审通过，可投标 =1
     */
    public static int BORROW_STATUS_CSTG = 1;
    
    /**
     * 借款标_满标待复审 =2
     */
    public static int BORROW_STATUS_MBDFS = 2;
    
    /**
     * 借款标_复审通过 = 3
     */
    public static int BORROW_STATUS_FSTG = 3;
    
    /**
     * 借款标_还款中 =5
     */
    public static int BORROW_STATUS_HKZ = 5;
    
    /**
     * 借款标_已还款 =6
     */
    public static int BORROW_STATUS_YHK = 6;
    
    /**
     * 借款标_已经过期 =7
     */
    public static int BORROW_STATUS_YGQ = 7;
    
    /**
     * 借款标_成功完成 =11
     */
    public static int BORROW_STATUS_CGWC = 11;
    
    /**
     * 借款标_初审不通过 =41
     */
    public static int BORROW_STATUS_CSSB = 41;
    
    /**
     * 借款标_复审不通过 =42
     */
    public static int BORROW_STATUS_FSSB = 42;
    
    
    /**
     * 投标_新建 =0
     */
    public static int TENDER_STATUS_XJ = 0;
    
    
    /**
     * 投标_成功 =1
     */
    public static int TENDER_STATUS_CG = 1;
    
    /**
     * 投标_失败 = 9
     */
    public static int TENDER_STATUS_SB = 9;
    
    
    /**
     * 等额本息 =1
     */
    public static int BORROW_REPAYMENT_STYLE_DEBX = 1;
    
    /**
     * 到期还本还息 =2
     */
    public static int BORROW_REPAYMENT_STYLE_DQHBHX = 2;
    
    /**
     * 按月付息到期还本 =3
     */
    public static int BORROW_REPAYMENT_STYLE_MYHX = 3;
    
    /**
     * 待收记录新建 =0
     */
    public static int BORROW_COLLECTION_STATUS_XJ = 0;
    
    /**
     * 待收记录还款中 =1
     */
    public static int BORROW_COLLECTION_STATUS_HKZ = 1;

    /**
     * 待收记录已还款 =2
     */
    public static int BORROW_COLLECTION_STATUS_YHK = 2;
        
    /**
     * 交易记录类型充值成功 =1
     */
    public static int ACCOUNT_LOG_TYPE_CZCG = 1;    
    
    /**
     * 交易记录类型配资支付保证金成功 =2
     */
    public static int ACCOUNT_LOG_TYPE_PZBZJ = 2;
    
    /**
     * 交易记录类型配资支付利息管理费成功 =3
     */
    public static int ACCOUNT_LOG_TYPE_PZGLF = 3;
    
    /**
     * 交易记录类型配资追加保证金成功 =11
     */
    public static int ACCOUNT_LOG_TYPE_PZZJBZJ = 11;  
    
    /**
     * 交易记录类型提现成功 =5
     */
    public static int ACCOUNT_LOG_TYPE_TXCG = 5;
    
    /**
     * 交易记录类型提现失败 =6
     */
    public static int ACCOUNT_LOG_TYPE_TXSB = 6;
    
    /**
     * 交易记录类型线下充值成功 =7
     */
    public static int ACCOUNT_LOG_TYPE_XXCZCG = 7;
    
    /**
     * 交易记录类型线下扣款成功 =12
     */
    public static int ACCOUNT_LOG_TYPE_XXKKCG = 12;
    
    /**
     * 交易记录类型分配账号扣除保证金 =14
     */
    public static int ACCOUNT_LOG_TYPE_FPZHKCBZJ = 14;
    
    /**
     * 交易记录类型追加保证金扣除 =15
     */
    public static int ACCOUNT_LOG_TYPE_ZJBZJ = 15;
    
    /**
     * 交易记录类型借款入账扣除冻结资金,生成待收记录 = 16
     */
    public static int ACCOUNT_LOG_TYPE_JKKCDJZJ = 16;
    
    /**
     * 交易记录类型退回保证金 =22
     */
    public static int ACCOUNT_LOG_TYPE_THBZJ = 22;
    
    /**
     * 交易记录类型投资成功 =31
     */
    public static int ACCOUNT_LOG_TYPE_TZCG = 31; 
    
    /**
     * 交易记录类型投资成功后奖励 =32
     */
    public static int ACCOUNT_LOG_TYPE_TZCGJL = 32; 
    
    
    /**
     * 交易记录类型借款入账 =33
     */
    public static int ACCOUNT_LOG_TYPE_JKRZ = 33; 
    
    /**
     * 交易记录类型借款入账扣除奖励 =34
     */
    public static int ACCOUNT_LOG_TYPE_JKRZ_KCJL = 34; 
    
    /**
     * 交易记录类型网站垫付扣除罚息 =35
     */
    public static int ACCOUNT_LOG_TYPE_HK_KCFX = 35;
    
    /**
     * 交易记录类型还款扣除本金 =36
     */
    public static int ACCOUNT_LOG_TYPE_HK_KCBJ = 36;
    
    /**
     * 交易记录类型还款扣除利息 =37
     */
    public static int ACCOUNT_LOG_TYPE_HK_KCLX = 37;
    
    /**
     * 交易记录类型还款扣除逾期利息 =38
     */
    public static int ACCOUNT_LOG_TYPE_HK_KCYQLX = 38;
    
    /**
     * 交易记录类型还款归还本金 =39
     */
    public static int ACCOUNT_LOG_TYPE_HK_GHBJ = 39;
    
    /**
     * 交易记录类型还款归还利息 =40
     */
    public static int ACCOUNT_LOG_TYPE_HK_GHLX = 40;
    
    /**
     * 交易记录类型还款归还利息管理费 =41
     */
    public static int ACCOUNT_LOG_TYPE_HK_GHLX_GLF = 41;    
    
    /**
     * 交易记录类型还款归还逾期利息 =42
     */
    public static int ACCOUNT_LOG_TYPE_HK_GHYQLX = 42;  
    
    /**
     * 交易记录类型流标解冻资金 =43
     */
    public static int ACCOUNT_LOG_TYPE_TB_JDZJ = 43; 
    
    /**
     * 交易记录类型追加保证金拒绝 = 44
     */
    public static int ACCOUNT_LOG_TYPE_BZJ_ZJJJ = 44;
    
    /**
     * 审核借款标操作类型初审通过 =1
     */
    public static int BORROW_VERIFY_OPT_TYPE_CSTG = 1;
    
    
    /**
     * 审核借款标操作类型初审失败 =2
     */
    public static int BORROW_VERIFY_OPT_TYPE_CSSB = 2;
    
    
    /**
     * 审核借款标操作类型复审通过 =3
     */
    public static int BORROW_VERIFY_OPT_TYPE_FSTG = 3;
    
    
    /**
     * 审核借款标操作类型复审失败 =4
     */
    public static int BORROW_VERIFY_OPT_TYPE_FSSB = 4;
    
    /**
     * 投标状态新建 = 0
     */
    public static int BORROW_TENDER_STATUS_XJ = 0;
    
    /**
     * 投标状态已放款 =1
     */
    public static int BORROW_TENDER_STATUS_YFK = 1;
    
    /**
     * 理财产品投资奖励没有 =0
     */
    public static int BORROW_AWARD_TYPE_NO = 0;
    
    /**
     * 理财产品投资奖励按比例 =1
     */
    public static int BORROW_AWARD_TYPE_PART_ACCOUNT = 1;
    
    /**
     * 理财产品投资奖励按金额 =2
     */
    public static int BORROW_AWARD_TYPE_FUNDS = 2;
    
    
    /**
     * 还款计划表还款状态新建(未还款) =0
     */
    public static int BORROW_REPAYMENT_STATUS_XJ = 0;
    
    /**
     * 还款计划表还款状态已还 =1
     */
    public static int BORROW_REPAYMENT_STATUS_YH = 1;
    
    /**
     * 还款计划表还款状态逾期 =2
     */
    public static int BORROW_REPAYMENT_STATUS_YQ = 2;
    
    /**
     * 还款计划表等待还款 =9
     */
    public static int BORROW_REPAYMENT_STATUS_DDHK = 9;
    /**
     * 还款计划表非网站待还 =0
     */
    public static int BORROW_REPAYMENT_WEBSTATUS_FDH = 0;
    
    /**
     * 还款计划表网站待还 =1
     */
    public static int BORROW_REPAYMENT_WEBSTATUS_DH = 1;
    
    /**
     * 个人
     */
    public static int PERSON = 1;
    
    /**
     * 企业
     */
    public static int ENTERPRISE = 2;
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        
        for (int i = 0; i < 5; i++) {
            int j = i+1;
            System.out.print(j++);
        }
    }
    
    /**
     * 转让关系新建
     */
    public static int DEBT_TRUST_STATUS_XJ = 0;
    
    /**
     * 转让关系成功
     */
    public static int DEBT_TRUST_STATUS_CG = 1;
    
    /**
     * 转让关系失败
     */
    public static int DEBT_TRUST_STATUS_ERROR = 9;
}
