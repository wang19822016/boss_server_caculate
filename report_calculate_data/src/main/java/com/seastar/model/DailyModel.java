package com.seastar.model;

import java.util.Date;

/**
 * Created by e on 2017/1/17.
 */
public class DailyModel
{
    private long id;
    private long userId;

    private String deviceId;
    private String deviceType;
    private String country;
    private int payMoney;
    private int onlineTime;
    private Date onlineLastTime;
    private Date installTime;
    private Date regTime;
    private Date loginTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(int payMoney) {
        this.payMoney = payMoney;
    }

    public int getOnlineTime() {
        return onlineTime;
    }

    public Date getOnlineLastTime() {
        return onlineLastTime;
    }

    public void setOnlineLastTime(Date onlineLastTime) {
        this.onlineLastTime = onlineLastTime;
    }


    public void setOnlineTime(int onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Date getInstallTime() {
        return installTime;
    }

    public void setInstallTime(Date installTime) {
        this.installTime = installTime;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

}
