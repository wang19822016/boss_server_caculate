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
        id = (long)map.get("id");
        date = (Date) map.get("date");
        channelType = (String) map.get("channelType");
        showNum = (int)map.getOrDefault("showNum","0");
        clickNum = (int)map.getOrDefault("clickNum","0");
        cpc = (int)map.getOrDefault("cpc","0");
        cpm = (int)map.getOrDefault("cpm","0");
        installNum = (int)map.getOrDefault("installNum","0");
        cpi = (int)map.getOrDefault("cpi","0");
        validNum = (int)map.getOrDefault("validNum","0");
        clickRate = (int)map.getOrDefault("clickRate","0");
        installRate = (int)map.getOrDefault("installRate","0");
        regRate = (int)map.getOrDefault("regRate","0");
        validRate = (int)map.getOrDefault("validRate","0");
        roi = (int)map.getOrDefault("roi","0");
        costMoney = (int)map.getOrDefault("costMoney","0");
        remain2 = (int)map.getOrDefault("remain2","0");
        remain3 = (int)map.getOrDefault("remain3","0");
        remain7 = (int)map.getOrDefault("remain7","0");
        remain30 = (int)map.getOrDefault("remain30","0");

        payMoney = (int)map.getOrDefault("payMoney","0");
        payNum = (int)map.getOrDefault("payNum","0");
        payRate = (int)map.getOrDefault("payRate","0");
        arpu = (int)map.getOrDefault("arpu","0");
        arppu = (int)map.getOrDefault("arppu","0");
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
