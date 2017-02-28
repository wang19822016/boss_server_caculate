package com.seastar.model;

import java.util.Date;
import java.util.Map;

/**
 * Created by e on 2017/1/20.
 */
public class UserReportModel
{
    private long id;
    private Date date;
    private int installNum;
    private int regNum;
    private int validNum;
    private int dau;
    private int dou;
    private int payMoney;
    private int payNum;
    private int payRate;
    private int newUserPayMoney;
    private int newUserPayNum;
    private int newUserPayRate;
    private int arpu;
    private int arppu;
    private int remain2;
    private int remain3;
    private int remain7;
    private int remain30;
    private int avgOnlineNum;
    private int avgOnlineTime;

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

    public int getInstallNum() {
        return installNum;
    }

    public void setInstallNum(int installNum) {
        this.installNum = installNum;
    }

    public int getRegNum() {
        return regNum;
    }

    public void setRegNum(int regNum) {
        this.regNum = regNum;
    }

    public int getValidNum() {
        return validNum;
    }

    public void setValidNum(int validNum) {
        this.validNum = validNum;
    }

    public int getDau() {
        return dau;
    }

    public void setDau(int dau) {
        this.dau = dau;
    }

    public int getDou() {
        return dou;
    }

    public void setDou(int dou) {
        this.dou = dou;
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

    public int getNewUserPayMoney() {
        return newUserPayMoney;
    }

    public void setNewUserPayMoney(int newUserPayMoney) {
        this.newUserPayMoney = newUserPayMoney;
    }

    public int getNewUserPayNum() {
        return newUserPayNum;
    }

    public void setNewUserPayNum(int newUserPayNum) {
        this.newUserPayNum = newUserPayNum;
    }

    public int getNewUserPayRate() {
        return newUserPayRate;
    }

    public void setNewUserPayRate(int newUserPayRate) {
        this.newUserPayRate = newUserPayRate;
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

    public int getAvgOnlineNum() {
        return avgOnlineNum;
    }

    public void setAvgOnlineNum(int avgOnlineNum) {
        this.avgOnlineNum = avgOnlineNum;
    }

    public int getAvgOnlineTime() {
        return avgOnlineTime;
    }

    public void setAvgOnlineTime(int avgOnlineTime) {
        this.avgOnlineTime = avgOnlineTime;
    }

    public void toObject(Map<String, Object> map)
    {
        id = (long)map.get("id");
        date = (Date) map.get("date");
        installNum = (int)map.getOrDefault("installNum","0");
        regNum = (int)map.getOrDefault("regNum","0");
        validNum = (int)map.getOrDefault("validNum","0");
        dau = (int)map.getOrDefault("dau","0");
        dou = (int)map.getOrDefault("dou","0");
        payMoney = (int)map.getOrDefault("payMoney","0");
        payNum = (int)map.getOrDefault("payNum","0");
        payRate = (int)map.getOrDefault("payRate","0");
        newUserPayMoney = (int)map.getOrDefault("newUserPayMoney","0");
        newUserPayNum = (int)map.getOrDefault("newUserPayNum","0");
        newUserPayRate = (int)map.getOrDefault("newUserPayRate","0");
        arpu = (int)map.getOrDefault("arpu","0");
        arppu = (int)map.getOrDefault("arppu","0");
        remain2 = (int)map.getOrDefault("remain2","0");
        remain3 = (int)map.getOrDefault("remain3","0");
        remain7 = (int)map.getOrDefault("remain7","0");
        remain30 = (int)map.getOrDefault("remain30","0");
        avgOnlineNum = (int)map.getOrDefault("avgOnlineNum","0");
        avgOnlineTime = (int)map.getOrDefault("avgOnlineTime","0");
    }
}
