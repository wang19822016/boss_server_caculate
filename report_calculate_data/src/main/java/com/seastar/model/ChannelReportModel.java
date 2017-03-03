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
    private int cpc;
    private int cpm;
    private int installNum;
    private int cpi;
    private int validNum;
    private int clickRate;
    private int installRate;
    private int regRate;
    private int validRate;
    private int roi;
    private int costMoney;
    private int remain2;
    private int remain3;
    private int remain7;
    private int remain30;
    private int payMoney;
    private int payNum;
    private int payRate;
    private int arpu;
    private int arppu;

    public void toObject(Map<String, Object> map)
    {
        //id = (long)map.get("id");
        date = (Date) map.get("date");
        channelType = (String) map.get("channelType");
        showNum = (int)map.get("showNum");
        clickNum = (int)map.get("clickNum");
        cpc = (int)map.get("cpc");
        cpm = (int)map.get("cpm");
        installNum = (int)map.get("installNum");
        cpi = (int)map.get("cpi");
        validNum = (int)map.get("validNum");
        clickRate = (int)map.get("clickRate");
        installRate = (int)map.get("installRate");
        regRate = (int)map.get("regRate");
        validRate = (int)map.get("validRate");
        roi = (int)map.get("roi");
        costMoney = (int)map.get("costMoney");
        remain2 = (int)map.get("remain2");
        remain3 = (int)map.get("remain3");
        remain7 = (int)map.get("remain7");
        remain30 = (int)map.get("remain30");

        payMoney = (int)map.get("payMoney");
        payNum = (int)map.get("payNum");
        payRate = (int)map.get("payRate");
        arpu = (int)map.get("arpu");
        arppu = (int)map.get("arppu");
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

    public int getCpc() {
        return cpc;
    }

    public void setCpc(int cpc) {
        this.cpc = cpc;
    }

    public int getCpm() {
        return cpm;
    }

    public void setCpm(int cpm) {
        this.cpm = cpm;
    }

    public int getInstallNum() {
        return installNum;
    }

    public void setInstallNum(int installNum) {
        this.installNum = installNum;
    }

    public int getCpi() {
        return cpi;
    }

    public void setCpi(int cpi) {
        this.cpi = cpi;
    }

    public int getValidNum() {
        return validNum;
    }

    public void setValidNum(int validNum) {
        this.validNum = validNum;
    }

    public int getClickRate() {
        return clickRate;
    }

    public void setClickRate(int clickRate) {
        this.clickRate = clickRate;
    }

    public int getInstallRate() {
        return installRate;
    }

    public void setInstallRate(int installRate) {
        this.installRate = installRate;
    }

    public int getRegRate() {
        return regRate;
    }

    public void setRegRate(int regRate) {
        this.regRate = regRate;
    }

    public int getValidRate() {
        return validRate;
    }

    public void setValidRate(int validRate) {
        this.validRate = validRate;
    }

    public int getRoi() {
        return roi;
    }

    public void setRoi(int roi) {
        this.roi = roi;
    }

    public int getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(int costMoney) {
        this.costMoney = costMoney;
    }

    public int getRemain2() {
        return remain2;
    }

    public void setRemain2(int remain2) {
        this.remain2 = remain2;
    }

    public int getRemain3() {
        return remain3;
    }

    public void setRemain3(int remain3) {
        this.remain3 = remain3;
    }

    public int getRemain7() {
        return remain7;
    }

    public void setRemain7(int remain7) {
        this.remain7 = remain7;
    }

    public int getRemain30() {
        return remain30;
    }

    public void setRemain30(int remain30) {
        this.remain30 = remain30;
    }

    public int getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(int payMoney) {
        this.payMoney = payMoney;
    }

    public int getPayNum() {
        return payNum;
    }

    public void setPayNum(int payNum) {
        this.payNum = payNum;
    }

    public int getPayRate() {
        return payRate;
    }

    public void setPayRate(int payRate) {
        this.payRate = payRate;
    }

    public int getArpu() {
        return arpu;
    }

    public void setArpu(int arpu) {
        this.arpu = arpu;
    }

    public int getArppu() {
        return arppu;
    }

    public void setArppu(int arppu) {
        this.arppu = arppu;
    }


}
