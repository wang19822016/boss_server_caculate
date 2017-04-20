package com.seastar.model;

import java.util.Date;
import java.util.Map;

/**
 * Created by e on 2017/2/14.
 */
public class ChannelReportModel
{
    private long id;
    private Date date;
    private String channelType;
    private int showNum;
    private int clickNum;
    private float cpc;
    private float cpm;
    private int installNum;
    private float cpi;
    private int validNum;
    private float clickRate;
    private float installRate;
    private float regRate;
    private float validRate;
    private float roi;
    private float grossIncome;
    private float costMoney;
    private float remain2;
    private float remain3;
    private float remain7;
    private float remain30;
    private float payMoney;
    private int payNum;
    private float payRate;
    private float arpu;
    private float arppu;

    public void toObject(Map<String, Object> map)
    {
        //id = (long)map.get("id");
        date = (Date) map.get("date");
        channelType = (String) map.get("channelType");
        showNum = (int)map.get("showNum");
        clickNum = (int)map.get("clickNum");
        cpc = (float)map.get("cpc");
        cpm = (float)map.get("cpm");
        installNum = (int)map.get("installNum");
        cpi = (float)map.get("cpi");
        validNum = (int)map.get("validNum");
        clickRate = (float)map.get("clickRate");
        installRate = (float)map.get("installRate");
        regRate = (float)map.get("regRate");
        validRate = (float)map.get("validRate");
        roi = (float)map.get("roi");
        grossIncome = (float)map.get("grossIncome");
        costMoney = (float)map.get("costMoney");
        remain2 = (float)map.get("remain2");
        remain3 = (float)map.get("remain3");
        remain7 = (float)map.get("remain7");
        remain30 = (float)map.get("remain30");

        payMoney = (float)map.get("payMoney");
        payNum = (int)map.get("payNum");
        payRate = (float)map.get("payRate");
        arpu = (float)map.get("arpu");
        arppu = (float)map.get("arppu");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public int getShowNum() {
        return showNum;
    }

    public void setShowNum(int showNum) {
        this.showNum = showNum;
    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }

    public float getCpc() {
        return cpc;
    }

    public void setCpc(float cpc) {
        this.cpc = cpc;
    }

    public float getCpm() {
        return cpm;
    }

    public void setCpm(float cpm) {
        this.cpm = cpm;
    }

    public int getInstallNum() {
        return installNum;
    }

    public void setInstallNum(int installNum) {
        this.installNum = installNum;
    }

    public float getCpi() {
        return cpi;
    }

    public void setCpi(float cpi) {
        this.cpi = cpi;
    }

    public int getValidNum() {
        return validNum;
    }

    public void setValidNum(int validNum) {
        this.validNum = validNum;
    }

    public float getClickRate() {
        return clickRate;
    }

    public void setClickRate(float clickRate) {
        this.clickRate = clickRate;
    }

    public float getInstallRate() {
        return installRate;
    }

    public void setInstallRate(float installRate) {
        this.installRate = installRate;
    }

    public float getRegRate() {
        return regRate;
    }

    public void setRegRate(float regRate) {
        this.regRate = regRate;
    }

    public float getValidRate() {
        return validRate;
    }

    public void setValidRate(float validRate) {
        this.validRate = validRate;
    }

    public float getRoi() {
        return roi;
    }

    public void setRoi(float roi) {
        this.roi = roi;
    }

    public float getGrossIncome() {
        return grossIncome;
    }

    public void setGrossIncome(float grossIncome) {
        this.grossIncome = grossIncome;
    }

    public float getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(float costMoney) {
        this.costMoney = costMoney;
    }

    public float getRemain2() {
        return remain2;
    }

    public void setRemain2(float remain2) {
        this.remain2 = remain2;
    }

    public float getRemain3() {
        return remain3;
    }

    public void setRemain3(float remain3) {
        this.remain3 = remain3;
    }

    public float getRemain7() {
        return remain7;
    }

    public void setRemain7(float remain7) {
        this.remain7 = remain7;
    }

    public float getRemain30() {
        return remain30;
    }

    public void setRemain30(float remain30) {
        this.remain30 = remain30;
    }

    public float getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(float payMoney) {
        this.payMoney = payMoney;
    }

    public int getPayNum() {
        return payNum;
    }

    public void setPayNum(int payNum) {
        this.payNum = payNum;
    }

    public float getPayRate() {
        return payRate;
    }

    public void setPayRate(float payRate) {
        this.payRate = payRate;
    }

    public float getArpu() {
        return arpu;
    }

    public void setArpu(float arpu) {
        this.arpu = arpu;
    }

    public float getArppu() {
        return arppu;
    }

    public void setArppu(float arppu) {
        this.arppu = arppu;
    }
}
