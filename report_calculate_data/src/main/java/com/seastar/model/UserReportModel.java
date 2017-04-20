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
    private float payRate;
    private float newUserPayMoney;
    private int newUserPayNum;
    private float newUserPayRate;
    private float arpu;
    private float arppu;
    private float remain2;
    private float remain3;
    private float remain7;
    private float remain30;
    private int avgOnlineNum;
    private float avgOnlineTime;

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

    public float getPayRate() {
        return payRate;
    }

    public void setPayRate(float payRate) {
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

    public float getNewUserPayRate() {
        return newUserPayRate;
    }

    public void setNewUserPayRate(float newUserPayRate) {
        this.newUserPayRate = newUserPayRate;
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

    public int getAvgOnlineNum() {
        return avgOnlineNum;
    }

    public void setAvgOnlineNum(int avgOnlineNum) {
        this.avgOnlineNum = avgOnlineNum;
    }

    public float getAvgOnlineTime() {
        return avgOnlineTime;
    }

    public void setAvgOnlineTime(float avgOnlineTime) {
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
        payRate = (float)map.get("payRate");
        newUserPayMoney = (float)map.get("newUserPayMoney");
        newUserPayNum = (int)map.get("newUserPayNum");
        newUserPayRate = (float)map.get("newUserPayRate");
        arpu = (float)map.get("arpu");
        arppu = (float)map.get("arppu");
        remain2 = (float)map.get("remain2");
        remain3 = (float)map.get("remain3");
        remain7 = (float)map.get("remain7");
        remain30 = (float)map.get("remain30");
        avgOnlineNum = (int)map.get("avgOnlineNum");
        avgOnlineTime = (float)map.get("avgOnlineTime");
    }
}
