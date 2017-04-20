package com.seastar.dao;

import com.seastar.common.ChannelType;
import com.seastar.model.ChannelReportModel;
import com.seastar.model.UserReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by e on 2017/1/16.
 */
@Component
public class ReportDao
{
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public List<String> getApps()
    {
        String sql = "select appId from apps";
        List<String> list = jdbcTemplate.queryForList(sql, String.class);
        return list;
    }

    //安装数量
    public int getInstallNum(Date date, String appId, String channelType)
    {
        String tableName = getDeviceBaseTbName(appId);
        String dt = sdf.format(date);
        String sql;
        Integer num;

        if (channelType == ChannelType.ALL)
        {
            sql = "select count(deviceId) from " + tableName + " where serverDate = ?";
            num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        }
        else
        {
            sql = "select count(deviceId) from " + tableName +" where serverDate = ? and channelType = ?";
            num = jdbcTemplate.queryForObject(sql, Integer.class, dt, channelType);
        }

        return num == null ? 0 : num.intValue();
    }

    //注册人数
    public int getRegNum(Date date, String appId, String channelType)
    {
        String tableName = getUserBaseTbName(appId);
        String dt = sdf.format(date);
        String sql;
        Integer num;

        if (channelType == ChannelType.ALL)
        {
            sql = "select count(userId) from " + tableName + " where serverDate = ?";
            num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        }
        else
        {
            sql = "select count(userId) from " + tableName + " where serverDate = ? and channelType = ?";
            num = jdbcTemplate.queryForObject(sql, Integer.class, dt, channelType);
        }

        return num == null ? 0 : num.intValue();
    }

    //有效人数(当天新增)
    public int getValidNum(Date date, String appId, String channelType)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql;
        Integer num;
        if (channelType == ChannelType.ALL)
        {
            sql = "select count(userId) from "+ tableName +" where loginTime = ? and loginTime = regTime and onlineTime >= 5";
            num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        }
        else
        {
            sql = "select count(userId) from "+ tableName +" where loginTime = ? and loginTime = regTime and channelType = ? and onlineTime >= 5";
            num = jdbcTemplate.queryForObject(sql, Integer.class, dt, channelType);
        }

        return num == null ? 0 : num.intValue();
    }

    //每日活跃用户
    public int getDau(Date date, String appId, String channelType)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql;
        Integer num;
        if (channelType == ChannelType.ALL)
        {
            sql = "select count(distinct userId) from "+tableName+" where loginTime = ?";
            num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        }
        else
        {
            sql = "select count(distinct userId) from "+tableName+" where loginTime = ? and channelType = ?";
            num = jdbcTemplate.queryForObject(sql, Integer.class, dt, channelType);
        }

        return num == null ? 0 : num.intValue();
    }

    //N日活跃用户
    public int getNau(Date begin, Date end, String appId, String channelType)
    {
        String tableName = getDailyDataTbName(appId);
        String bt = sdf.format(begin);
        String et = sdf.format(end);
        String sql;
        Integer num;

        if (channelType == ChannelType.ALL)
        {
            sql = "select count(distinct userId) from "+tableName+" where loginTime between ? and ?";
            num = jdbcTemplate.queryForObject(sql, Integer.class, bt, et);
        }
        else
        {
            sql = "select count(distinct userId) from "+tableName+" where loginTime between ? and ? and channelType = ?";
            num = jdbcTemplate.queryForObject(sql, Integer.class, bt, et, channelType);
        }

        return num == null ? 0 : num.intValue();
    }

    //每日活跃老用户
    public int getDou(Date date, String appId)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql = "select count(userId) from "+tableName+" where loginTime = ? and loginTime > regTime";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        return num == null ? 0 : num.intValue();
    }

    //每日活跃新用户(day new user)
    public int getDnu(Date date, String appId)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql = "select count(userId) from "+tableName+" where loginTime = ? and loginTime = regTime";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        return num == null ? 0 : num.intValue();
    }

    //每日付费金额
    public float getPayMoney(Date date, String appId, String channelType)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql;
        Integer num;
        if (channelType == ChannelType.ALL)
        {
            sql = "select sum(payMoney) from "+tableName+" where loginTime = ?";
            num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        }
        else
        {
            sql = "select sum(payMoney) from "+tableName+" where loginTime = ? and channelType = ?";
            num = jdbcTemplate.queryForObject(sql, Integer.class, dt, channelType);
        }

        return num == null ? 0 : num.floatValue();
    }

    //N日付费金额
    public float getDaysPayMoney(Date begin, Date end, String appId, String channelType)
    {
        String tableName = getDailyDataTbName(appId);
        String bt = sdf.format(begin);
        String et = sdf.format(end);
        String sql;
        Integer num;

        if (channelType == ChannelType.ALL)
        {
            sql = "select sum(payMoney) from "+tableName+" where loginTime between ? and ?";
            num = jdbcTemplate.queryForObject(sql, Integer.class, bt, et);
        }
        else
        {
            sql = "select sum(payMoney) from "+tableName+" where loginTime between ? and ? and channelType = ?";
            num = jdbcTemplate.queryForObject(sql, Integer.class, bt, et, channelType);
        }

        return num == null ? 0 : num.floatValue();
    }

    //每日付费人数
    public int getPayNum(Date date, String appId, String channelType)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql;
        Integer num;
        if (channelType == ChannelType.ALL)
        {
            sql = "select count(userId) from "+tableName+" where loginTime = ? and payMoney > 0";
            num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        }
        else
        {
            sql = "select count(userId) from "+tableName+" where loginTime = ? and channelType = ? and payMoney > 0";
            num = jdbcTemplate.queryForObject(sql, Integer.class, dt, channelType);
        }

        return num == null ? 0 : num.intValue();
    }

    //N日付费人数
    public int getDaysPayNum(Date begin, Date end, String appId, String channelType)
    {
        String tableName = getDailyDataTbName(appId);
        String bt = sdf.format(begin);
        String et = sdf.format(end);
        String sql;
        Integer num;

        if (channelType == ChannelType.ALL)
        {
            sql = "select  count(userId) from "+tableName+" where loginTime between ? and ? and payMoney > 0";
            num = jdbcTemplate.queryForObject(sql, Integer.class, bt, et);
        }
        else
        {
            sql = "select distinct count(userId) from "+tableName+" where loginTime between ? and ? and channelType = ? and payMoney > 0";
            num = jdbcTemplate.queryForObject(sql, Integer.class, bt, et, channelType);
        }

        return num == null ? 0 : num.intValue();
    }

    //日付费率
    public float getPayRate(Date date, String appId,String channelType)
    {
        float payNum = (float)getPayNum(date, appId, channelType);
        float dau = (float)getDau(date, appId, channelType);
        if (dau <= 0)
            return 0;
        return payNum / dau;
    }

    //N日付费率
    public float getDaysPayRate(Date begin, Date end, String appId, String channelType)
    {
        float payNum = (float)getDaysPayNum(begin, end, appId, channelType);
        float nau = (float)getNau(begin, end, appId, channelType);
        if (nau <= 0)
            return 0;
        return payNum / nau;
    }

    //新用户每日付费金额
    public float getNewUserPayMoney(Date date, String appId)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql = "select sum(payMoney) from "+tableName+" where loginTime = ? and loginTime = regTime";
        //String sql = "select sum(payMoney) from "+tableName+" where DATEDIFF(loginTime,?) = 0";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        return num == null ? 0 : num.floatValue();
    }

    //新用户每日付费人数
    public int getNewUserPayNum(Date date, String appId)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql = "select count(userId) from "+tableName+" where loginTime = ? and loginTime = regTime and payMoney > 0 ";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        return num == null ? 0 : num.intValue();
    }

    //新用户日付费率
    public float getNewUserPayRate(Date date, String appId)
    {
        float payNum = (float)getNewUserPayNum(date, appId);
        float dnu = (float)getDnu(date, appId);
        if (dnu <= 0)
            return 0;
        return payNum / dnu;
    }

    //注册用户总付费金额
    public float getRegUserTotalPayMoney(String appId, String regDate, String channelType)
    {
        String tableName = getUserPayTbName(appId);
        String sql;
        Integer money;

        if (channelType == ChannelType.ALL)
        {
            sql = "select sum(payMoney) from "+tableName+" where regDate = ?";
            money = jdbcTemplate.queryForObject(sql, Integer.class, regDate, channelType);
        }
        else
        {
            sql = "select sum(payMoney) from "+tableName+" where regDate = ? and channelType = ?";
            money = jdbcTemplate.queryForObject(sql, Integer.class, regDate, channelType);
        }


        return money == null ? 0 : money.floatValue();
    }

    //注册用户总付费人数
    public int getRegUserTotalPayNum(String appId, Date regDate, String channelType)
    {
        String payTbName = getUserPayTbName(appId);
        String dt = sdf.format(regDate);
        String sql;
        Integer payNumInteger;

        if (channelType == ChannelType.ALL)
        {
            sql = "select count(distinct userId) from " + payTbName + " where regDate = ?";
            payNumInteger = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        }
        else
        {
            sql = "select count(distinct userId) from " + payTbName + " where regDate = ? and channelType = ?";
            payNumInteger = jdbcTemplate.queryForObject(sql, Integer.class, dt, channelType);
        }

        int payNum = payNumInteger == null ? 0 : payNumInteger.intValue();

        return payNum;
    }

    //活跃用户平均付费金额(百位制,客户端需除以100）
    public float getArpu(Date date, String appId, String channelType)
    {
        float payMoney = getPayMoney(date, appId, channelType);
        float dau = (float)getDau(date, appId, channelType);

        if (dau <= 0)
            return 0;

        return payMoney / dau;
    }

    //N日活跃用户平均付费金额
    public float getDaysArpu(Date begin, Date end, String appId, String channelType)
    {
        float payMoney = getDaysPayMoney(begin, end, appId, channelType);
        float nau = (float)getNau(begin, end, appId, channelType);
        if (nau <= 0)
            return 0;
        return payMoney / nau;
    }

    //付费用户平均付费金额(百位制,客户端需除以100）
    public float getArppu(Date date, String appId, String channelType)
    {
        float payMoney = getPayMoney(date, appId, channelType);
        float payNum = (float)getPayNum(date, appId, channelType);
        if (payNum <= 0)
            return 0;
        return payMoney / payNum;
    }

    //N日付费用户平均付费金额(百位制,客户端需除以100）
    public float getDaysArppu(Date begin, Date end, String appId, String channelType)
    {
        float payMoney = getDaysPayMoney(begin, end, appId, channelType);
        float payNum = (float)getDaysPayNum(begin, end, appId, channelType);

        if (payNum <= 0)
            return 0;

        return payMoney / payNum;
    }

    //留存率(-1代表暂无数据)
    public float getRemainRate(Date date, int days, String appId, String channelType)
    {
        String tableName = getUserLoginTbName(appId);
        String dt = sdf.format(date);
        String sql;
        Integer userInteger = null;
        Integer remainInteger = null;

        if (channelType == ChannelType.ALL)
        {
            sql= "select count(distinct userId) from " + tableName + " where regDate = ?";
            userInteger = jdbcTemplate.queryForObject(sql, Integer.class, dt);

            sql = "select count(distinct userId) from " + tableName + " where regDate = ? and serverDate = (regDate + ?)";
            remainInteger = jdbcTemplate.queryForObject(sql, Integer.class, dt, days - 1);
        }
        else
        {
            sql= "select count(distinct userId) from " + tableName + " where regDate = ? and channelType = ?";
            userInteger = jdbcTemplate.queryForObject(sql, Integer.class, dt, channelType);

            sql = "select count(distinct userId) from " + tableName + " where regDate = ? and serverDate = (regDate + ?) and channelType = ?";
            remainInteger = jdbcTemplate.queryForObject(sql, Integer.class, dt, days - 1, channelType);
        }

        if (userInteger == null || userInteger.intValue() <= 0)
            return 0;

        float userNum = userInteger == null ? 0 : userInteger.floatValue();
        float remainNum = remainInteger == null ? 0 : remainInteger.floatValue();

        return remainNum / userNum;
    }


    private long dayTime = (1000 * 3600 * 24);

    //LTV
    public float getLTV(Date date, int days, String appId, String channelType)
    {
        String payTbName = getUserPayTbName(appId);
        String deviceTbName = getDeviceBaseTbName(appId);
        String dt = sdf.format(date);
        String sql;
        Integer payInteger = null;
        Integer deviceInteger = null;

        Date endDate = new Date(date.getTime() + days * dayTime);
        String endDt = sdf.format(endDate);

        if (channelType == ChannelType.ALL)
        {
            sql = "select sum(payMoney) from " + payTbName + " where regDate = ? and serverDate <= ?";
            payInteger = jdbcTemplate.queryForObject(sql, Integer.class, dt, endDate);

            sql = "select count(deviceId) from " + deviceTbName + " where serverDate = ?";
            deviceInteger = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        }
        else
        {
            sql = "select sum(payMoney) from " + payTbName + " where regDate = ? and serverDate <= ? and channelType = ?";
            payInteger = jdbcTemplate.queryForObject(sql, Integer.class, dt, endDate, channelType);

            sql = "select count(deviceId) from " + deviceTbName + " where serverDate = ? and channelType = ?)";
            deviceInteger = jdbcTemplate.queryForObject(sql, Integer.class, dt, channelType);
        }

        if (deviceInteger == null || deviceInteger.intValue() <= 0)
            return -1;

        float payMoney = payInteger == null ? 0 : payInteger.floatValue();
        float deviceNum = deviceInteger == null ? 0 : deviceInteger.floatValue();

        System.out.println("payMoney: " + payMoney + "deviceNum: " + deviceNum);

        return  payMoney / deviceNum;
    }

    //平均在线人数
    public int getAvgOnlineNum(Date date, String appId)
    {
        String tableName = "user_login_" + appId;
        String dt = sdf.format(date);
        String sql = "select count(userId) from "+tableName+" where serverDate = ?";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        return num == null ? 0 : num.intValue() / 24;
    }

    //平均在线时长
    public float getAvgOnlineTime(Date date, String appId)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql = "select sum(onlineTime) from "+tableName+" where loginTime = ?";
        Integer time = jdbcTemplate.queryForObject(sql, Integer.class, dt);

        sql = "select count(userId) from "+tableName+" where loginTime = ?";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt);

        float timeValue = time == null ? 0 : time.floatValue();
        float numValue =  num == null ? 0 : num.floatValue();
        if (numValue <= 0)
            return 0;
        return  timeValue / numValue;   //numvalue 不应该为0
    }

    //创建一条日报数据
    public UserReportModel createUserReportData(Date date, String appId)
    {
        UserReportModel userReportModel = new UserReportModel();
        userReportModel.setDate(date);
        userReportModel.setInstallNum(getInstallNum(date, appId, ChannelType.ALL));
        userReportModel.setRegNum(getRegNum(date, appId, ChannelType.ALL));
        userReportModel.setValidNum(getValidNum(date, appId, ChannelType.ALL));
        userReportModel.setDau(getDau(date, appId, ChannelType.ALL));
        userReportModel.setDou(getDou(date, appId));
        userReportModel.setPayMoney(getPayMoney(date, appId, ChannelType.ALL));
        userReportModel.setPayNum(getPayNum(date, appId, ChannelType.ALL));
        userReportModel.setPayRate(getPayRate(date, appId, ChannelType.ALL));
        userReportModel.setNewUserPayMoney(getNewUserPayMoney(date, appId));
        userReportModel.setNewUserPayNum(getNewUserPayNum(date, appId));
        userReportModel.setNewUserPayRate(getNewUserPayRate(date, appId));
        userReportModel.setArpu(getArpu(date, appId, ChannelType.ALL));
        userReportModel.setArppu(getArppu(date, appId, ChannelType.ALL));
        userReportModel.setAvgOnlineNum(getAvgOnlineNum(date, appId));
        userReportModel.setAvgOnlineTime(getAvgOnlineTime(date, appId));
        return userReportModel;
    }

    //是否存在某天的日报数据
    public boolean isHaveUserReportData(Date dt, String appId)
    {
        String tableName = getUserReportTbName(appId);
        String d = sdf.format(dt);
        String sql = "select count(date) from "+tableName+" where date = ?";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, d);
        int numValue = num == null ? 0 : num.intValue();
        if (numValue > 0)
            return true;
        else
            return false;
    }

    //存储日报数据
    public void saveUserReportData(UserReportModel urm, String appId)
    {
        String tableName = getUserReportTbName(appId);

        jdbcTemplate.update("INSERT INTO "+tableName+"(date, installNum, regNum, validNum, dau, dou, payMoney, payNum, payRate, " +
                        "newUserPayMoney,newUserPayNum,newUserPayRate,arpu,arppu,avgOnlineNum,avgOnlineTime) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                urm.getDate(),
                urm.getInstallNum(),
                urm.getRegNum(),
                urm.getValidNum(),
                urm.getDau(),
                urm.getDou(),
                urm.getPayMoney(),
                urm.getPayNum(),
                urm.getPayRate(),
                urm.getNewUserPayMoney(),
                urm.getNewUserPayNum(),
                urm.getNewUserPayRate(),
                urm.getArpu(),
                urm.getArppu(),
                urm.getAvgOnlineNum(),
                urm.getAvgOnlineTime()
                );
    }

    //更新用户报表N天留存率
    public void updateUserReportRemainRate(Date date, int days, float remainRate, String appId)
    {
        String tableName = getUserReportTbName(appId);
        String dt = sdf.format(date);
        try
        {
           int line = jdbcTemplate.update("UPDATE "+tableName+" set remain" + days + " = ? WHERE date = ?" ,remainRate, dt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //更新N天留存率
    public void updateRemainRate(Date date, int days, float remainRate, String appId)
    {
        try
        {
            String tableName = getRemainTbName(appId);
            String dt = sdf.format(date);

            String sql = "select count(id) from "+tableName+" where date = ? and remainDays = ?";
            Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt, days);

            int numValue = num == null ? 0 : num.intValue();

            if (numValue > 0)
                jdbcTemplate.update("UPDATE "+tableName+" set remainValue = ? WHERE date = ? and remainDays = ?", remainRate, dt, days);
            else
                jdbcTemplate.update("INSERT INTO "+tableName+"(date, remainDays, remainValue) VALUES (?,?,?)", dt, days, remainRate);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //更新N天LTV
    public void updateLTV(Date date, int days, float ltv, String appId)
    {
        try
        {
            String tableName = getLtvTbName(appId);
            String dt = sdf.format(date);

            String sql = "select count(id) from "+tableName+" where date = ? and ltvDays = ?";
            Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt, days);

            int numValue = num == null ? 0 : num.intValue();

            if (numValue > 0)
                jdbcTemplate.update("UPDATE "+tableName+" set ltvValue = ? WHERE date = ? and ltvDays = ?", ltv, dt, days);
            else
                jdbcTemplate.update("INSERT INTO "+tableName+"(date, ltvDays, ltvValue) VALUES (?,?,?)", dt, days, ltv);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    //更新N天ROI
    public void updateROI(Date date, int days, float grossIncome, float cost, String appId)
    {
        try
        {
            float roi = cost <= 0 ? 0 : grossIncome / cost;

            String tableName = getRoiTbName(appId);
            String dt = sdf.format(date);

            String sql = "select count(id) from "+tableName+" where date = ? and roiDays = ?";
            Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt, days);

            int numValue = num == null ? 0 : num.intValue();

            if (numValue > 0)
                jdbcTemplate.update("UPDATE "+tableName+" set grossIncome = ?, cost = ?, roiValue = ? WHERE date = ? and roiDays = ?", grossIncome, cost, roi, dt, days);
            else
                jdbcTemplate.update("INSERT INTO "+tableName+"(date, roiDays, grossIncome, cost, roiValue) VALUES (?,?,?,?,?)", dt, days, grossIncome, cost, roi);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //更新N天ROI
    public void updatePayConversion(Date date, int days, int dnu, int payNum, int payTimes, String appId)
    {
        try
        {
            float payRate = dnu <= 0 ? 0 : (float)payNum / (float)dnu;

            String tableName = getPayConversionTbName(appId);
            String dt = sdf.format(date);

            String sql = "select count(id) from "+tableName+" where date = ? and payDays = ?";
            Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt, days);

            int numValue = num == null ? 0 : num.intValue();

            if (numValue > 0)
                jdbcTemplate.update("UPDATE "+tableName+" set dnu = ?, payNum = ?, payTimes = ?, payRate = ? WHERE date = ? and payDays = ?", dnu, payNum, payTimes, payRate, dt, days);
            else
                jdbcTemplate.update("INSERT INTO "+tableName+"(date, payDays, dnu, payNum, payTimes, payRate) VALUES (?,?,?,?,?,?)", dt, days, dnu, payNum, payTimes, payRate);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //更新日报数据
    public void updateUserReportData(UserReportModel urm, String appId)
    {
        String tableName = getUserReportTbName(appId);
        String dt = sdf.format(urm.getDate());
        jdbcTemplate.update("UPDATE "+tableName+" SET installNum = ?, regNum = ?, validNum = ?, dau = ?, dou = ?, " +
                        "payMoney = ?, payNum = ?, payRate = ?, newUserPayMoney = ?,newUserPayNum = ?,newUserPayRate = ?," +
                        "arpu = ?,arppu = ?,avgOnlineNum = ?,avgOnlineTime = ? " +
                        "WHERE date = ?" ,
                urm.getInstallNum(),
                urm.getRegNum(),
                urm.getValidNum(),
                urm.getDau(),
                urm.getDou(),
                urm.getPayMoney(),
                urm.getPayNum(),
                urm.getPayRate(),
                urm.getNewUserPayMoney(),
                urm.getNewUserPayNum(),
                urm.getNewUserPayRate(),
                urm.getArpu(),
                urm.getArppu(),
                urm.getAvgOnlineNum(),
                urm.getAvgOnlineTime(),
                dt
        );
    }



    /*************************渠道日报*****************************************/

    //获得当日的渠道列表
    public List<String> getChannelListByDate(Date date, String appId)
    {
        String tableName = getDeviceBaseTbName(appId);
        String dt = sdf.format(date);
        String sql = "select distinct channelType from " + tableName + " where serverDate = ?";
        List<String> list = jdbcTemplate.queryForList(sql, String.class, dt);
        return list;
    }

    //是否存在某天的渠道数据
    public boolean isHaveChannelData(String appId, Date date, String channelType)
    {
        String tableName = getChannelReportTbName(appId);
        String dt = sdf.format(date);

        String sql = "select count(id) from "+tableName+" where date = ? and channelType = ?";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt, channelType);

        int numValue = num == null ? 0 : num.intValue();

        if (numValue > 0)
            return true;
        else
            return false;
    }

    //根据日期渠道 创建一条渠道数据
    public ChannelReportModel createChannelBaseData(Date date, String appId, String channelType)
    {
        ChannelReportModel crm = new ChannelReportModel();
        crm.setDate(date);
        crm.setChannelType(channelType);
        //crm.setShowNum();
        //crm.setClickNum();
        //crm.setCpc(getCpc(date, appId, channelType));
        //crm.setCpm(getCpm(date, appId, channelType));
        crm.setInstallNum(getInstallNum(date, appId, channelType));
        //crm.setCpi(getCpi(date, appId, channelType));
        crm.setValidNum(getValidNum(date, appId, channelType));
        crm.setClickRate(getClickRate(date, appId, channelType));
        crm.setInstallRate(getInstallRate(date, appId, channelType));
        crm.setRegRate(getRegRate(date, appId, channelType));
        crm.setValidRate(getValidRate(date, appId, channelType));
        //crm.setRoi(getRoi(date, appId, channelType));
        //crm.setCostMoney(getCostMoney(date, appId, channelType));
        //crm.setPayMoney(getPayMoney(date, appId, channelType));
        //crm.setPayNum(getPayNum(date, appId, channelType));
        //crm.setPayRate(getPayRate(date, appId, channelType));
        //crm.setArpu(getArpu(date, appId, channelType));
        //crm.setArppu(getArppu(date, appId, channelType));

        return crm;
    }

    //存储渠道数据
    public void saveChannelBaseData(ChannelReportModel crm, String appId)
    {
        String tableName = getChannelReportTbName(appId);

        jdbcTemplate.update("INSERT INTO "+tableName+"(date, channelType, installNum, validNum, clickRate, installRate," +
                        "regRate, validRate) VALUES (?,?,?,?,?,?,?,?)",
                crm.getDate(),
                crm.getChannelType(),
                crm.getInstallNum(),
                crm.getValidNum(),
                crm.getClickRate(),
                crm.getInstallRate(),
                crm.getRegRate(),
                crm.getValidRate()
        );
    }

    //更新渠道数据
    public void updateChannelBaseData(ChannelReportModel crm, String appId)
    {
        String tableName = getChannelReportTbName(appId);
        String dt = sdf.format(crm.getDate());
        jdbcTemplate.update("UPDATE "+tableName+" SET installNum = ?, validNum = ?, clickRate = ?, installRate = ?, regRate = ?, " +
                        "validRate = ? WHERE date = ? and channelType = ?" ,

                crm.getInstallNum(),
                crm.getValidNum(),
                crm.getClickRate(),
                crm.getInstallRate(),
                crm.getRegRate(),
                crm.getValidRate(),
                dt,
                crm.getChannelType());
    }

    //更新某天的所有渠道留存
    public void UpdateAllChannelRemain(String appId, Date date, int days)
    {
        List<String> list = getChannelListByDate(date, appId);

        for (int i = 0; i < list.size(); i++)
        {
            String channelType = list.get(i);

            if (channelType == null || channelType.isEmpty())
            {
                System.out.println("Remain ChannelType Unknown!!!");
                continue;
            }

            boolean isHave = isHaveChannelData(appId, date, channelType);

            if (isHave)
            {
                String tableName = getChannelReportTbName(appId);
                String dt = sdf.format(date);
                try
                {
                    float remainRate = getRemainRate(date, days, appId, channelType);
                    jdbcTemplate.update("UPDATE "+tableName+" set remain" + days + " = ? WHERE date = ? and channelType = ?" ,remainRate, dt, channelType);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    //更新渠道持续计算的数据
    public void UpdateChannelLastData(String appId)
    {
        String channelTbName = getChannelReportTbName(appId);

        String sql = "select date, channelType, showNum, clickNum, costMoney from " + channelTbName + " order by date,channelType";

        List channelData = jdbcTemplate.queryForList(sql);

        Iterator it = channelData.iterator();

        while(it.hasNext())
        {
            Map map = (Map) it.next();

            Date date = (Date) map.get("date");
            String dt = sdf.format(date);

            String channelType = (String) map.get("channelType");
            int showNum = (int)map.get("showNum");
            int clickNum = (int)map.get("clickNum");
            float costMoney = (float)map.get("costMoney");

            if (channelType == null || channelType.isEmpty())
            {
                System.out.println("Cumulative ChannelType Unknown!!!");
                continue;
            }

            int installNum = getInstallNum(date, appId, channelType);
            float payMoney = getRegUserTotalPayMoney(appId, dt, channelType);
            int payNum = getRegUserTotalPayNum(appId, date, channelType);
            float payRate = installNum <= 0 ? 0 : (float)payNum / (float)installNum;
            float arpu = installNum <= 0 ? 0 : payMoney / (float)installNum;
            float arppu = payNum <= 0 ? 0 : payMoney / (float)payNum;

            float cpi = installNum <= 0 ? 0 : costMoney / (float)installNum;
            float cpc = clickNum <= 0 ? 0 : costMoney / (float)clickNum;
            float cpm = showNum <= 0 ? 0 : costMoney / (float)showNum * 1000;
            float roi = costMoney <= 0 ? 0 : payMoney / costMoney;

            System.out.println("Channel grossIncome: " +date+" "+channelType+" "+payMoney);

            try
            {
                jdbcTemplate.update("UPDATE "+channelTbName+" set cpi = ?, cpc = ?, cpm = ?, roi = ?, payMoney = ?, payNum = ?," +
                        "payRate = ?, arpu = ?, arppu = ? WHERE date = ? and channelType = ?", cpi, cpc, cpm, roi, payMoney, payNum,
                        payRate, arpu, arppu, dt, channelType);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    //注册率
    public float getRegRate(Date date, String appId, String channelType)
    {
        int regNum = getRegNum(date, appId, channelType);
        int installNum = getInstallNum(date, appId, channelType);
        if (installNum <= 0)
            return 0;
        return (float) regNum / (float)installNum;
    }

    //有效转化率
    public float getValidRate(Date date, String appId, String channelType)
    {
        int validNum = getValidNum(date, appId, channelType);
        int installNum = getInstallNum(date, appId, channelType);
        if (installNum <= 0)
            return 0;
        return (float) validNum / (float)installNum;
    }

    //单用户安装成本
    public float getCpi(Date date, String appId, String channelType)
    {
        String tableName = getChannelReportTbName(appId);
        String dt = sdf.format(date);

        float money = getCostMoney(date, appId, channelType);
        int installNum = getInstallNum(date, appId, channelType);

        if (installNum <= 0)
            return -1;

        return money / (float)installNum;
    }

    //点击率
    public float getClickRate(Date date, String appId, String channelType)
    {
        String tableName = getChannelReportTbName(appId);
        int showNum = getShowNum(date, appId, channelType);
        int clickNum = getClickNum(date, appId, channelType);

        if (showNum <= 0)
            return 0;

        return (float) clickNum / (float)showNum;
    }

    //安装率
    public float getInstallRate(Date date, String appId, String channelType)
    {
        String tableName = getChannelReportTbName(appId);
        int clickNum = getClickNum(date, appId, channelType);
        int installNum = getInstallNum(date, appId, channelType);

        if (clickNum <= 0)
            return 0;

        return (float) installNum / (float)clickNum;
    }

    //花费
    public float getCostMoney(Date date, String appId, String channelType)
    {
        String tableName = getChannelReportTbName(appId);
        String dt = sdf.format(date);
        String sql;
        Integer money;

        if (channelType == ChannelType.ALL)
        {
            sql = "select sum(costMoney) from " + tableName +" where date = ?";
            money = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        }
        else
        {
            sql = "select sum(costMoney) from " + tableName +" where date = ? and channelType = ?";
            money = jdbcTemplate.queryForObject(sql, Integer.class, dt, channelType);
        }

        return money == null ? 0 : money.floatValue();
    }

    //回报率
    public float getRoi(Date date, String appId, String channelType)
    {
        float payMoney = getPayMoney(date, appId, channelType);
        float costMoney = getCostMoney(date, appId, channelType);

        if (costMoney <= 0)
            return -1;

        return payMoney / costMoney;
    }

    //N天内的收益 - ROI
    public float getGrossIncome(Date date, int days, String appId, String channelType)
    {
        String payTbName = getUserPayTbName(appId);

        String dt = sdf.format(date);
        String sql;
        Integer payInteger = null;

        Date endDate = new Date(date.getTime() + days * dayTime);
        String endDt = sdf.format(endDate);

        if (channelType == ChannelType.ALL)
        {
            sql = "select sum(payMoney) from " + payTbName + " where regDate = ? and serverDate <= ?";
            payInteger = jdbcTemplate.queryForObject(sql, Integer.class, dt, endDate);
        }
        else
        {
            sql = "select sum(payMoney) from " + payTbName + " where regDate = ? and serverDate <= ? and channelType = ?";
            payInteger = jdbcTemplate.queryForObject(sql, Integer.class, dt, endDate, channelType);
        }

        float payMoney = payInteger == null ? 0 : payInteger.floatValue();

        return  payMoney;
    }

    //付费转化-当天新增活跃用户的N天付费人数
    public int getDnuPayNumByDays(Date date, int days, String appId, String channelType)
    {
        String payTbName = getUserPayTbName(appId);

        String dt = sdf.format(date);
        String sql;
        Integer payNumInteger = null;

        Date endDate = new Date(date.getTime() + days * dayTime);
        String endDt = sdf.format(endDate);

        if (channelType == ChannelType.ALL)
        {
            sql = "select count(distinct userId) from " + payTbName + " where regDate = ? and serverDate <= ?";
            payNumInteger = jdbcTemplate.queryForObject(sql, Integer.class, dt, endDate);
        }
        else
        {
            sql = "select count(distinct userId) from " + payTbName + " where regDate = ? and serverDate <= ? and channelType = ?";
            payNumInteger = jdbcTemplate.queryForObject(sql, Integer.class, dt, endDate, channelType);
        }

        int payNum = payNumInteger == null ? 0 : payNumInteger.intValue();

        return payNum;
    }

    //付费转化-当天新增活跃用户的N天付费人次
    public int getDnuPayTimesByDays(Date date, int days, String appId, String channelType)
    {
        String payTbName = getUserPayTbName(appId);

        String dt = sdf.format(date);
        String sql;
        Integer payTimesInteger = null;

        Date endDate = new Date(date.getTime() + days * dayTime);
        String endDt = sdf.format(endDate);

        if (channelType == ChannelType.ALL)
        {
            sql = "select count(userId) from " + payTbName + " where regDate = ? and serverDate <= ?";
            payTimesInteger = jdbcTemplate.queryForObject(sql, Integer.class, dt, endDate);
        }
        else
        {
            sql = "select count(userId) from " + payTbName + " where regDate = ? and serverDate <= ? and channelType = ?";
            payTimesInteger = jdbcTemplate.queryForObject(sql, Integer.class, dt, endDate, channelType);
        }

        int payTimes = payTimesInteger == null ? 0 : payTimesInteger.intValue();

        return payTimes;
    }

    //展示数
    public int getShowNum(Date date, String appId, String channelType)
    {
        String tableName = getChannelReportTbName(appId);
        String dt = sdf.format(date);
        String sql;
        Integer showNum;

        if (channelType == ChannelType.ALL)
        {
            sql = "select sum(showNum) from " + tableName +" where date = ?";
            showNum = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        }
        else
        {
            sql = "select sum(showNum) from " + tableName +" where date = ? and channelType = ?";
            showNum = jdbcTemplate.queryForObject(sql, Integer.class, dt, channelType);
        }

        return showNum == null ? 0 : showNum.intValue();
    }

    //点击数
    public int getClickNum(Date date, String appId, String channelType)
    {
        String tableName = getChannelReportTbName(appId);
        String dt = sdf.format(date);
        String sql;
        Integer clickNum;

        if (channelType == ChannelType.ALL)
        {
            sql = "select sum(clickNum) from " + tableName +" where date = ?";
            clickNum = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        }
        else
        {
            sql = "select sum(clickNum) from " + tableName +" where date = ? and channelType = ?";
            clickNum = jdbcTemplate.queryForObject(sql, Integer.class, dt, channelType);
        }

        return clickNum == null ? 0 : clickNum.intValue();
    }

    //按点击价格购买
    public float getCpc(Date date, String appId, String channelType)
    {
        float costMoney = getCostMoney(date, appId, channelType);
        int clickNum = getClickNum(date, appId, channelType);
        return costMoney / (float)clickNum;
    }

    //按展示1000次购买
    public float getCpm(Date date, String appId, String channelType)
    {
        float costMoney = getCostMoney(date, appId, channelType);
        int showNum = getShowNum(date, appId, channelType);
        return costMoney / (float)showNum * 1000;
    }


    //获得日报列表
    public List<UserReportModel> getUserReportList(String appId, Date beginTime, Date endTime)
    {
        String tableName = getUserReportTbName(appId);
        String bt = sdf.format(beginTime);
        String et = sdf.format(endTime);

        String sql = "select * from " + tableName + " where date between ? and ?";

        List list = jdbcTemplate.queryForList(sql, bt, et);
        Iterator it = list.iterator();

        List<UserReportModel> urmList = new ArrayList<UserReportModel>();

        while(it.hasNext())
        {
            Map<String, Object> map = (Map) it.next();
            UserReportModel urm = new UserReportModel();
            urm.toObject(map);
            urmList.add(urm);
        }

        return urmList;
    }

    //按单日查询
    public List<ChannelReportModel> getChannelReportListByDay(String appId, Date beginTime, Date endTime)
    {
        String tableName = getChannelReportTbName(appId);
        String bt = sdf.format(beginTime);
        String et = sdf.format(endTime);

        String sql = "select * from " + tableName + " where date between ? and ? order by date,channelType";

        List list = jdbcTemplate.queryForList(sql, bt, et);
        Iterator it = list.iterator();

        List<ChannelReportModel> crmList = new ArrayList<ChannelReportModel>();

        while(it.hasNext())
        {
            Map<String, Object> map = (Map) it.next();
            ChannelReportModel crm = new ChannelReportModel();
            crm.toObject(map);
            crmList.add(crm);
        }

        return crmList;
    }

    private String getDeviceBaseTbName(String appId)
    {
        return "device_base_" + appId;
    }

    private String getUserBaseTbName(String appId)
    {
        return "user_base_" + appId;
    }

    private String getUserLoginTbName(String appId)
    {
        return "user_login_" + appId;
    }

    private String getDailyDataTbName(String appId)
    {
        return "daily_data_" + appId;
    }

    private String getUserReportTbName(String appId)
    {
        return "user_report_" + appId;
    }

    private String getChannelReportTbName(String appId)
    {
        return "channel_report_" + appId;
    }

    private String getUserPayTbName(String appId)
    {
        return "user_pay_" + appId;
    }

    private String getLtvTbName(String appId)
    {
        return "ltv_" + appId;
    }

    private String getRoiTbName(String appId)
    {
        return "roi_" + appId;
    }

    private String getPayConversionTbName(String appId)
    {
        return "pay_conversion_" + appId;
    }

    private String getRemainTbName(String appId)
    {
        return "remain_" + appId;
    }

}
