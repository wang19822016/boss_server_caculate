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
    private float payMoney;
    private int payNum;
    private int payRate;
    private float newUserPayMoney;
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

    public int getPayRate() {
        return payRate;
    }

    public void setPayRate(int payRate) {
        this.payRate = payRate;
    }

    public float getNewUserPayMoney() {
        return newUserPayMoney;
    }

    public void setNewUserPayMoney(float newUserPayMoney) {
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
        installNum = (int)map.get("installNum");
        regNum = (int)map.get("regNum");
        validNum = (int)map.get("validNum");
        dau = (int)map.get("dau");
        dou = (int)map.get("dou");
        payMoney = (float)map.get("payMoney");
        payNum = (int)map.get("payNum");
        payRate = (int)map.get("payRate");
        newUserPayMoney = (float)map.get("newUserPayMoney");
        newUserPayNum = (int)map.get("newUserPayNum");
        newUserPayRate = (int)map.get("newUserPayRate");
        arpu = (int)map.get("arpu");
        arppu = (int)map.get("arppu");
        remain2 = (int)map.get("remain2");
        remain3 = (int)map.get("remain3");
        remain7 = (int)map.get("remain7");
        remain30 = (int)map.get("remain30");
        avgOnlineNum = (int)map.get("avgOnlineNum");
        avgOnlineTime = (int)map.get("avgOnlineTime");
    }
}
