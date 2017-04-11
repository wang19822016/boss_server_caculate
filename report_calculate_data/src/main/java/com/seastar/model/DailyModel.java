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
    private String channelType;     //渠道 facebook
    private String platform;        //平台  ios/android
    private String deviceType;      // samsung
    private String deviceName;      //i9001
    private String country;
    private float payMoney;
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

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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

    public float getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(float payMoney) {
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
