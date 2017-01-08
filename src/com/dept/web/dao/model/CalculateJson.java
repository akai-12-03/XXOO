package com.dept.web.dao.model;

public class CalculateJson {
    
    /**
     * 操盘资金
     */
    private double cpzj; 
    
    /**
     * 亏损警戒线
     */
    private double ksjjx;
    
    /**
     * 亏损平仓线
     */
    private double kspcx;
    
    /**
     * 借款利率
     */
    private double jklv;
    
    
    /**
     * 管理费
     */
    private double glf;
    
    /**
     * 保证金
     */
    private double bzj;
    
    private double power;


    public double getCpzj() {
        return cpzj;
    }


    public void setCpzj(double cpzj) {
        this.cpzj = cpzj;
    }


    public double getKsjjx() {
        return ksjjx;
    }


    public void setKsjjx(double ksjjx) {
        this.ksjjx = ksjjx;
    }


    public double getKspcx() {
        return kspcx;
    }


    public void setKspcx(double kspcx) {
        this.kspcx = kspcx;
    }


    public double getJklv() {
        return jklv;
    }


    public void setJklv(double jklv) {
        this.jklv = jklv;
    }


    public double getGlf() {
        return glf;
    }


    public void setGlf(double glf) {
        this.glf = glf;
    }


    public double getBzj() {
        return bzj;
    }


    public void setBzj(double bzj) {
        this.bzj = bzj;
    }


    public double getPower() {
        return power;
    }


    public void setPower(double power) {
        this.power = power;
    }
    
    
    
}
