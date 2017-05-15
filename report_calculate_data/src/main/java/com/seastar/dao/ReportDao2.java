package com.seastar.dao;

import com.seastar.common.ChannelType;
import com.seastar.common.PlatformType;
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
public class ReportDao2
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
    public int getInstallNum(Date date, String appId, String channelType, String platform)
    {
        String tableName = getDeviceBaseTbName(appId);
        String dt = sdf.format(date);

        String sql = "select count(deviceId) from " + tableName + " where serverDate = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(dt);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());

        return num == null ? 0 : num.intValue();
    }

    //注册人数
    public int getRegNum(Date date, String appId, String channelType, String platform)
    {
        String tableName = getUserBaseTbName(appId);
        String dt = sdf.format(date);

        String sql = "select count(userId) from " + tableName + " where serverDate = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(dt);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());

        return num == null ? 0 : num.intValue();
    }

    //有效人数(当天新增)
    public int getValidNum(Date date, String appId, String channelType, String platform)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql = "select count(userId) from "+ tableName +" where loginTime = ? and loginTime = regTime and onlineTime >= 5";

        List<Object> params = new ArrayList<Object>();
        params.add(dt);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
        return num == null ? 0 : num.intValue();
    }

    //每日活跃用户
    public int getDau(Date date, String appId, String channelType, String platform)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql = "select count(distinct userId) from "+tableName+" where loginTime = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(dt);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
        return num == null ? 0 : num.intValue();
    }

    //N日活跃用户
    public int getNau(Date begin, Date end, String appId, String channelType, String platform)
    {
        String tableName = getDailyDataTbName(appId);
        String bt = sdf.format(begin);
        String et = sdf.format(end);
        String sql = "select count(distinct userId) from "+tableName+" where loginTime between ? and ?";

        List<Object> params = new ArrayList<Object>();
        params.add(bt);
        params.add(et);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
        return num == null ? 0 : num.intValue();
    }

    //每日活跃老用户
    public int getDou(Date date, String appId, String channelType, String platform)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql = "select count(userId) from "+tableName+" where loginTime = ? and loginTime > regTime";

        List<Object> params = new ArrayList<Object>();
        params.add(dt);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
        return num == null ? 0 : num.intValue();
    }

    //每日活跃新用户(day new user)
    public int getDnu(Date date, String appId, String channelType, String platform)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql = "select count(userId) from "+tableName+" where loginTime = ? and loginTime = regTime";

        List<Object> params = new ArrayList<Object>();
        params.add(dt);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
        return num == null ? 0 : num.intValue();
    }

    //每日付费金额
    public float getPayMoney(Date date, String appId, String channelType, String platform)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql = "select sum(payMoney) from "+tableName+" where loginTime = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(dt);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Float num = jdbcTemplate.queryForObject(sql, Float.class, params.toArray());
        return num == null ? 0 : num.floatValue();
    }

    //N日付费金额
    public float getDaysPayMoney(Date begin, Date end, String appId, String channelType, String platform)
    {
        String tableName = getDailyDataTbName(appId);
        String bt = sdf.format(begin);
        String et = sdf.format(end);
        String sql = "select sum(payMoney) from "+tableName+" where loginTime between ? and ?";

        List<Object> params = new ArrayList<Object>();
        params.add(bt);
        params.add(et);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Float num = jdbcTemplate.queryForObject(sql, Float.class, params.toArray());
        return num == null ? 0 : num.floatValue();
    }

    //每日付费人数
    public int getPayNum(Date date, String appId, String channelType, String platform)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql = "select count(userId) from "+tableName+" where loginTime = ? and payMoney > 0";

        List<Object> params = new ArrayList<Object>();
        params.add(dt);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());

        return num == null ? 0 : num.intValue();
    }

    //N日付费人数
    public int getDaysPayNum(Date begin, Date end, String appId, String channelType, String platform)
    {
        String tableName = getDailyDataTbName(appId);
        String bt = sdf.format(begin);
        String et = sdf.format(end);
        String sql = "select  count(userId) from "+tableName+" where loginTime between ? and ? and payMoney > 0";

        List<Object> params = new ArrayList<Object>();
        params.add(bt);
        params.add(et);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
        return num == null ? 0 : num.intValue();
    }

    //日付费率
    public float getPayRate(Date date, String appId,String channelType, String platform)
    {
        float payNum = (float)getPayNum(date, appId, channelType, platform);
        float dau = (float)getDau(date, appId, channelType, platform);
        if (dau <= 0)
            return 0;
        return payNum / dau;
    }

    //N日付费率
    public float getDaysPayRate(Date begin, Date end, String appId, String channelType, String platform)
    {
        float payNum = (float)getDaysPayNum(begin, end, appId, channelType,platform);
        float nau = (float)getNau(begin, end, appId, channelType,platform);
        if (nau <= 0)
            return 0;
        return payNum / nau;
    }

    //新用户每日付费金额
    public float getNewUserPayMoney(Date date, String appId, String channelType, String platform)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql = "select sum(payMoney) from "+tableName+" where loginTime = ? and loginTime = regTime";
        //String sql = "select sum(payMoney) from "+tableName+" where DATEDIFF(loginTime,?) = 0";
        List<Object> params = new ArrayList<Object>();
        params.add(dt);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Float num = jdbcTemplate.queryForObject(sql, Float.class, params.toArray());
        return num == null ? 0 : num.floatValue();
    }

    //新用户每日付费人数
    public int getNewUserPayNum(Date date, String appId, String channelType, String platform)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String sql = "select count(userId) from "+tableName+" where loginTime = ? and loginTime = regTime and payMoney > 0 ";

        List<Object> params = new ArrayList<Object>();
        params.add(dt);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
        return num == null ? 0 : num.intValue();
    }

    //新用户日付费率
    public float getNewUserPayRate(Date date, String appId, String channelType, String platform)
    {
        float payNum = (float)getNewUserPayNum(date, appId, channelType, platform);
        float dnu = (float)getDnu(date, appId, channelType, platform);
        if (dnu <= 0)
            return 0;
        return payNum / dnu;
    }

    //注册用户总付费金额
    public float getRegUserTotalPayMoney(String appId, String regDate, String channelType, String platform)
    {
        String tableName = getUserPayTbName(appId);
        String sql = "select sum(payMoney) from "+tableName+" where regDate = ?";

        List<Object> params = new ArrayList<Object>();
        params.add(regDate);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Float money = jdbcTemplate.queryForObject(sql, Float.class, params.toArray());
        return money == null ? 0 : money.floatValue();
    }

    //注册用户总付费人数
    public int getRegUserTotalPayNum(String appId, Date regDate, String channelType, String platform)
    {
        String payTbName = getUserPayTbName(appId);
        String dt = sdf.format(regDate);
        String sql = "select count(distinct userId) from " + payTbName + " where regDate = ?";

        List<Object> params = new ArrayList<Object>();
        params.add(dt);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Integer payNumInteger = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());

        return payNumInteger == null ? 0 : payNumInteger.intValue();
    }

    //活跃用户平均付费金额(百位制,客户端需除以100）
    public float getArpu(Date date, String appId, String channelType, String platform)
    {
        float payMoney = getPayMoney(date, appId, channelType, platform);
        float dau = (float)getDau(date, appId, channelType, platform);

        if (dau <= 0)
            return 0;

        return payMoney / dau;
    }

    //N日活跃用户平均付费金额
    public float getDaysArpu(Date begin, Date end, String appId, String channelType, String platform)
    {
        float payMoney = getDaysPayMoney(begin, end, appId, channelType, platform);
        float nau = (float)getNau(begin, end, appId, channelType, platform);
        if (nau <= 0)
            return 0;
        return payMoney / nau;
    }

    //付费用户平均付费金额(百位制,客户端需除以100）
    public float getArppu(Date date, String appId, String channelType, String platform)
    {
        float payMoney = getPayMoney(date, appId, channelType, platform);
        float payNum = (float)getPayNum(date, appId, channelType, platform);
        if (payNum <= 0)
            return 0;
        return payMoney / payNum;
    }

    //N日付费用户平均付费金额(百位制,客户端需除以100）
    public float getDaysArppu(Date begin, Date end, String appId, String channelType, String platform)
    {
        float payMoney = getDaysPayMoney(begin, end, appId, channelType, platform);
        float payNum = (float)getDaysPayNum(begin, end, appId, channelType, platform);

        if (payNum <= 0)
            return 0;

        return payMoney / payNum;
    }

    //留存率(-1代表暂无数据)
    public float getRemainRate(Date date, int days, String appId, String channelType, String platform)
    {
        String tableName = getUserLoginTbName(appId);
        String dt = sdf.format(date);
        String userSql = "select count(distinct userId) from " + tableName + " where regDate = ?";
        String remainSql = "select count(distinct userId) from " + tableName + " where regDate = ? and serverDate = (regDate + INTERVAL ? DAY)";

        List<Object> params1 = new ArrayList<Object>();
        List<Object> params2 = new ArrayList<Object>();
        params1.add(dt);
        params2.add(dt);
        params2.add(days - 1);

        if (channelType != ChannelType.ALL)
        {
            userSql += " and channelType = ?";
            remainSql += " and channelType = ? ";
            params1.add(channelType);
            params2.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            userSql += " and platform = ?";
            remainSql += " and platform = ? ";
            params1.add(platform);
            params2.add(platform);
        }

        Integer userInteger = jdbcTemplate.queryForObject(userSql, Integer.class, params1.toArray());
        Integer remainInteger = jdbcTemplate.queryForObject(remainSql, Integer.class, params2.toArray());

        if (userInteger == null || userInteger.intValue() <= 0)
            return 0;

        float userNum = userInteger == null ? 0 : userInteger.floatValue();
        float remainNum = remainInteger == null ? 0 : remainInteger.floatValue();

        return remainNum / userNum;
    }


    private long dayTime = (1000 * 3600 * 24);

    //LTV
    public float getLTV(Date date, int days, String appId, String channelType, String platform)
    {
        String payTbName = getUserPayTbName(appId);
        String deviceTbName = getDeviceBaseTbName(appId);
        String dt = sdf.format(date);
        String paySql = "select sum(payMoney) from " + payTbName + " where regDate = ? and serverDate <= ?";
        String deviceSql = "select count(deviceId) from " + deviceTbName + " where serverDate = ?";

        Date endDate = new Date(date.getTime() + days * dayTime);
        String endDt = sdf.format(endDate);

        List<Object> params1 = new ArrayList<Object>();
        List<Object> params2 = new ArrayList<Object>();
        params1.add(dt);
        params1.add(endDate);
        params2.add(dt);

        if (channelType != ChannelType.ALL)
        {
            paySql += " and channelType = ? ";
            deviceSql += " and channelType = ?";
            params1.add(channelType);
            params2.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            paySql += " and platform = ? ";
            deviceSql += " and platform = ?";
            params1.add(platform);
            params2.add(platform);
        }

        Integer payInteger = jdbcTemplate.queryForObject(paySql, Integer.class, params1.toArray());
        Integer deviceInteger = jdbcTemplate.queryForObject(deviceSql, Integer.class, params2.toArray());


        if (deviceInteger == null || deviceInteger.intValue() <= 0)
            return 0;

        float payMoney = payInteger == null ? 0 : payInteger.floatValue();
        float deviceNum = deviceInteger == null ? 0 : deviceInteger.floatValue();

        //System.out.println("payMoney: " + payMoney + "deviceNum: " + deviceNum);

        return  payMoney / deviceNum;
    }

    //平均在线人数
    public int getAvgOnlineNum(Date date, String appId, String channelType, String platform)
    {
        String tableName = "user_login_" + appId;
        String dt = sdf.format(date);
        String sql = "select count(userId) from "+tableName+" where serverDate = ?";

        List<Object> params = new ArrayList<Object>();
        params.add(dt);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
        return num == null ? 0 : num.intValue() / 24;
    }

    //平均在线时长
    public float getAvgOnlineTime(Date date, String appId, String channelType, String platform)
    {
        String tableName = getDailyDataTbName(appId);
        String dt = sdf.format(date);
        String timeSql = "select sum(onlineTime) from "+tableName+" where loginTime = ?";
        String numSql = "select count(userId) from "+tableName+" where loginTime = ?";

        List<Object> params1 = new ArrayList<Object>();
        List<Object> params2 = new ArrayList<Object>();
        params1.add(dt);
        params2.add(dt);

        if (channelType != ChannelType.ALL)
        {
            timeSql += " and  channelType = ?";
            numSql += " and  channelType = ?";
            params1.add(channelType);
            params2.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            timeSql += " and platform = ?";
            numSql += " and platform = ?";
            params1.add(platform);
            params2.add(platform);
        }

        Integer time = jdbcTemplate.queryForObject(timeSql, Integer.class, params1.toArray());
        Integer num = jdbcTemplate.queryForObject(numSql, Integer.class, params2.toArray());

        float timeValue = time == null ? 0 : time.floatValue();
        float numValue =  num == null ? 0 : num.floatValue();
        if (numValue <= 0)
            return 0;
        return  timeValue / numValue;   //numvalue 不应该为0
    }

    //创建一条日报数据
    public UserReportModel createUserReportData(Date date, String appId, String channelType, String platform)
    {
        UserReportModel userReportModel = new UserReportModel();
        userReportModel.setDate(date);
        userReportModel.setInstallNum(getInstallNum(date, appId, channelType, platform));
        userReportModel.setRegNum(getRegNum(date, appId, channelType, platform));
        userReportModel.setValidNum(getValidNum(date, appId, channelType, platform));
        userReportModel.setDau(getDau(date, appId, channelType, platform));
        userReportModel.setDou(getDou(date, appId,channelType, platform));
        userReportModel.setPayMoney(getPayMoney(date, appId, channelType, platform));
        userReportModel.setPayNum(getPayNum(date, appId, channelType, platform));
        userReportModel.setPayRate(getPayRate(date, appId, channelType, platform));
        userReportModel.setNewUserPayMoney(getNewUserPayMoney(date, appId, channelType, platform));
        userReportModel.setNewUserPayNum(getNewUserPayNum(date, appId, channelType, platform));
        userReportModel.setNewUserPayRate(getNewUserPayRate(date, appId, channelType, platform));
        userReportModel.setArpu(getArpu(date, appId, channelType, platform));
        userReportModel.setArppu(getArppu(date, appId, channelType, platform));
        userReportModel.setAvgOnlineNum(getAvgOnlineNum(date, appId, channelType, platform));
        userReportModel.setAvgOnlineTime(getAvgOnlineTime(date, appId, channelType, platform));
        return userReportModel;
    }

    //是否存在某天的日报数据
    public boolean isHaveUserReportData(Date date, String appId, String platform)
    {
        String tableName = getUserReportTbName(appId, platform);

        String dt = sdf.format(date);
        String sql = "select count(date) from "+tableName+"  where date = ?";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        int numValue = num == null ? 0 : num.intValue();
        if (numValue > 0)
            return true;
        else
            return false;
    }

    //存储日报数据
    public void saveUserReportData(UserReportModel urm, String appId, String platform)
    {
        String tableName = getUserReportTbName(appId, platform);

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

    //更新日报数据
    public void updateUserReportData(UserReportModel urm, String appId, String platform)
    {
        String tableName = getUserReportTbName(appId, platform);
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

    //更新用户报表N天留存率
    public void updateUserReportRemainRate(Date date, int days, float remainRate, String appId, String platform)
    {
        String tableName = getUserReportTbName(appId, platform);
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
    public void updateRemainRate(Date date, int days, float remainRate, int dnu, String appId, String platform)
    {
        try
        {
            String tableName = getRemainTbName(appId, platform);
            String dt = sdf.format(date);

            String sql = "select count(id) from "+tableName+" where date = ? and remainDays = ?";
            Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt, days);

            int numValue = num == null ? 0 : num.intValue();

            if (numValue > 0)
                jdbcTemplate.update("UPDATE "+tableName+" set remainValue = ?, dnu = ? WHERE date = ? and remainDays = ?", remainRate, dnu, dt, days);
            else
                jdbcTemplate.update("INSERT INTO "+tableName+"(date, remainDays, remainValue, dnu) VALUES (?,?,?,?)", dt, days, remainRate, dnu);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //更新N天LTV
    public void updateLTV(Date date, int days, float ltv, float cpi, String appId, String platform)
    {
        try
        {
            String tableName = getLtvTbName(appId, platform);
            String dt = sdf.format(date);

            String sql = "select count(id) from "+tableName+" where date = ? and ltvDays = ?";
            Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt, days);

            int numValue = num == null ? 0 : num.intValue();

            if (numValue > 0)
                jdbcTemplate.update("UPDATE "+tableName+" set ltvValue = ? , cpi = ? WHERE date = ? and ltvDays = ?", ltv, cpi, dt, days);
            else
                jdbcTemplate.update("INSERT INTO "+tableName+"(date, ltvDays, ltvValue, cpi) VALUES (?,?,?,?)", dt, days, ltv, cpi);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    //更新N天ROI
    public void updateROI(Date date, int days, float grossIncome, float cost, String appId, String platform)
    {
        try
        {
            float roi = cost <= 0 ? 0 : grossIncome / cost;

            String tableName = getRoiTbName(appId, platform);
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
    public void updatePayConversion(Date date, int days, int dnu, int payNum, int payTimes, String appId, String platform)
    {
        try
        {
            float payRate = dnu <= 0 ? 0 : (float)payNum / (float)dnu;

            String tableName = getPayConversionTbName(appId, platform);
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




    /*************************渠道日报*****************************************/

    //获得当日的渠道列表
    public List<String> getChannelListByDate(Date date, String appId, String platform)
    {
        String tableName = getDeviceBaseTbName(appId);
        String dt = sdf.format(date);
        String sql;
        List<String> list;

        if (platform == PlatformType.ALL)
        {
            sql = "select distinct channelType from " + tableName + " where serverDate = ?";
            list = jdbcTemplate.queryForList(sql, String.class, dt);
        }
        else
        {
            sql = "select distinct channelType from " + tableName + " where serverDate = ? and platform = ?";
            list = jdbcTemplate.queryForList(sql, String.class, dt, platform);
        }

        return list;
    }

    //是否存在某天的渠道数据
    public boolean isHaveChannelData(String appId, Date date, String channelType, String platform)
    {
        String tableName = getChannelReportTbName(appId, platform);
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
    public ChannelReportModel createChannelBaseData(Date date, String appId, String channelType, String platform)
    {
        ChannelReportModel crm = new ChannelReportModel();
        crm.setDate(date);
        crm.setChannelType(channelType);
        //crm.setShowNum();
        //crm.setClickNum();
        //crm.setCpc(getCpc(date, appId, channelType));
        //crm.setCpm(getCpm(date, appId, channelType));
        crm.setInstallNum(getInstallNum(date, appId, channelType, platform));
        //crm.setCpi(getCpi(date, appId, channelType));
        crm.setValidNum(getValidNum(date, appId, channelType, platform));
        crm.setClickRate(getClickRate(date, appId, channelType,platform));
        crm.setInstallRate(getInstallRate(date, appId, channelType, platform));
        crm.setRegRate(getRegRate(date, appId, channelType, platform));
        crm.setValidRate(getValidRate(date, appId, channelType, platform));
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
    public void saveChannelBaseData(ChannelReportModel crm, String appId, String platform)
    {
        String tableName = getChannelReportTbName(appId, platform);

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
    public void updateChannelBaseData(ChannelReportModel crm, String appId, String platform)
    {
        String tableName = getChannelReportTbName(appId, platform);
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
    public void UpdateAllChannelRemain(String appId, Date date, int days, String platform)
    {
        List<String> list = getChannelListByDate(date, appId, platform);

        for (int i = 0; i < list.size(); i++)
        {
            String channelType = list.get(i);

            if (channelType == null || channelType.isEmpty())
            {
                System.out.println("Remain ChannelType Unknown!!!");
                continue;
            }

            boolean isHave = isHaveChannelData(appId, date, channelType, platform);

            if (isHave)
            {
                String tableName = getChannelReportTbName(appId, platform);
                String dt = sdf.format(date);
                try
                {
                    float remainRate = getRemainRate(date, days, appId, channelType, platform);
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
    public void UpdateChannelLastData(String appId, String platform)
    {
        String channelTbName = getChannelReportTbName(appId, platform);

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

            int installNum = getInstallNum(date, appId, channelType, platform);
            float payMoney = getRegUserTotalPayMoney(appId, dt, channelType, platform);
            int payNum = getRegUserTotalPayNum(appId, date, channelType, platform);
            float payRate = installNum <= 0 ? 0 : (float)payNum / (float)installNum;
            float arpu = installNum <= 0 ? 0 : payMoney / (float)installNum;
            float arppu = payNum <= 0 ? 0 : payMoney / (float)payNum;

            float cpi = installNum <= 0 ? 0 : costMoney / (float)installNum;
            float cpc = clickNum <= 0 ? 0 : costMoney / (float)clickNum;
            float cpm = showNum <= 0 ? 0 : costMoney / (float)showNum * 1000;
            float roi = costMoney <= 0 ? 0 : payMoney / costMoney;

            //System.out.println("Channel grossIncome: " +date+" "+channelType+" "+payMoney);

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
    public float getRegRate(Date date, String appId, String channelType, String platform)
    {
        int regNum = getRegNum(date, appId, channelType, platform);
        int installNum = getInstallNum(date, appId, channelType, platform);
        if (installNum <= 0)
            return 0;
        return (float) regNum / (float)installNum;
    }

    //有效转化率
    public float getValidRate(Date date, String appId, String channelType, String platform)
    {
        int validNum = getValidNum(date, appId, channelType, platform);
        int installNum = getInstallNum(date, appId, channelType, platform);
        if (installNum <= 0)
            return 0;
        return (float) validNum / (float)installNum;
    }

    //单用户安装成本
    public float getCpi(Date date, String appId, String channelType, String platform)
    {
        String tableName = getChannelReportTbName(appId, platform);
        String dt = sdf.format(date);

        float money = getCostMoney(date, appId, channelType, platform);
        int installNum = getInstallNum(date, appId, channelType, platform);

        if (installNum <= 0)
            return 0;

        return money / (float)installNum;
    }

    //点击率
    public float getClickRate(Date date, String appId, String channelType, String platform)
    {
        int showNum = getShowNum(date, appId, channelType, platform);
        int clickNum = getClickNum(date, appId, channelType, platform);

        if (showNum <= 0)
            return 0;

        return (float) clickNum / (float)showNum;
    }

    //安装率
    public float getInstallRate(Date date, String appId, String channelType, String platform)
    {
        int clickNum = getClickNum(date, appId, channelType, platform);
        int installNum = getInstallNum(date, appId, channelType, platform);

        if (clickNum <= 0)
            return 0;

        return (float) installNum / (float)clickNum;
    }

    //花费
    public float getCostMoney(Date date, String appId, String channelType, String platform)
    {
        String tableName = getChannelReportTbName(appId, platform);
        String dt = sdf.format(date);
        String sql = "select sum(costMoney) from " + tableName +" where date = ?";

        List<Object> params = new ArrayList<Object>();
        params.add(dt);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

//        if (platform != PlatformType.ALL)
//        {
//            sql += " and platform = ?";
//            params.add(platform);
//        }

        Float money = jdbcTemplate.queryForObject(sql, Float.class, params.toArray());
        return money == null ? 0 : money.floatValue();
    }

    //N天内的收益 - ROI
    public float getGrossIncome(Date date, int days, String appId, String channelType, String platform)
    {
        String payTbName = getUserPayTbName(appId);

        String dt = sdf.format(date);
        String sql = "select sum(payMoney) from " + payTbName + " where regDate = ? and serverDate <= ?";

        Date endDate = new Date(date.getTime() + days * dayTime);
        String endDt = sdf.format(endDate);

        List<Object> params = new ArrayList<Object>();
        params.add(dt);
        params.add(endDate);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Float payInteger = jdbcTemplate.queryForObject(sql, Float.class, params.toArray());

        return payInteger == null ? 0 : payInteger.floatValue();
    }

    //付费转化-当天新增活跃用户的N天付费人数
    public int getDnuPayNumByDays(Date date, int days, String appId, String channelType, String platform)
    {
        String payTbName = getUserPayTbName(appId);

        String dt = sdf.format(date);
        String sql = "select count(distinct userId) from " + payTbName + " where regDate = ? and serverDate <= ?";

        Date endDate = new Date(date.getTime() + days * dayTime);
        String endDt = sdf.format(endDate);

        List<Object> params = new ArrayList<Object>();
        params.add(dt);
        params.add(endDate);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Integer payNumInteger = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());

        return payNumInteger == null ? 0 : payNumInteger.intValue();
    }

    //付费转化-当天新增活跃用户的N天付费人次
    public int getDnuPayTimesByDays(Date date, int days, String appId, String channelType, String platform)
    {
        String payTbName = getUserPayTbName(appId);

        String dt = sdf.format(date);
        String sql = "select count(userId) from " + payTbName + " where regDate = ? and serverDate <= ?";

        Date endDate = new Date(date.getTime() + days * dayTime);
        String endDt = sdf.format(endDate);

        List<Object> params = new ArrayList<Object>();
        params.add(dt);
        params.add(endDate);

        if (channelType != ChannelType.ALL)
        {
            sql += " and channelType = ?";
            params.add(channelType);
        }

        if (platform != PlatformType.ALL)
        {
            sql += " and platform = ?";
            params.add(platform);
        }

        Integer payTimesInteger = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());

        return payTimesInteger == null ? 0 : payTimesInteger.intValue();
    }

    //展示数
    public int getShowNum(Date date, String appId, String channelType, String platform)
    {
        String tableName = getChannelReportTbName(appId, platform);
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
    public int getClickNum(Date date, String appId, String channelType, String platform)
    {
        String tableName = getChannelReportTbName(appId, platform);
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
    public float getCpc(Date date, String appId, String channelType, String platform)
    {
        float costMoney = getCostMoney(date, appId, channelType, platform);
        int clickNum = getClickNum(date, appId, channelType, platform);
        return costMoney / (float)clickNum;
    }

    //按展示1000次购买
    public float getCpm(Date date, String appId, String channelType, String platform)
    {
        float costMoney = getCostMoney(date, appId, channelType, platform);
        int showNum = getShowNum(date, appId, channelType, platform);
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

    private String getUserReportTbName(String appId, String platform)
    {
        if (platform == PlatformType.ALL)
            return "user_report_" + appId;

        return "user_report_" + platform + "_" + appId;
    }


    private String getChannelReportTbName(String appId, String platform)
    {
        if (platform == PlatformType.ALL)
            return "channel_report_" + appId;

        return "channel_report_" + platform + "_" + appId;
    }

    private String getUserPayTbName(String appId)
    {
        return "user_pay_" + appId;
    }

    private String getLtvTbName(String appId)
    {
        return "ltv_" + appId;
    }

    private String getLtvTbName(String appId, String platform)
    {
        if (platform == PlatformType.ALL)
            return "ltv_" + appId;

        return "ltv_" + platform + "_" + appId;
    }

    private String getRoiTbName(String appId)
    {
        return "roi_" + appId;
    }

    private String getRoiTbName(String appId, String platform)
    {
        if (platform == PlatformType.ALL)
            return "roi_" + appId;

        return "roi_" + platform + "_" + appId;
    }

    private String getPayConversionTbName(String appId)
    {
        return "pay_conversion_" + appId;
    }

    private String getPayConversionTbName(String appId, String platform)
    {
        if (platform == PlatformType.ALL)
            return "pay_conversion_" + appId;

        return "pay_conversion_" + platform + "_" + appId;
    }

    private String getRemainTbName(String appId)
    {
        return "remain_" + appId;
    }

    private String getRemainTbName(String appId, String platform)
    {
        if (platform == PlatformType.ALL)
            return "remain_" + appId;

        return "remain_" + platform + "_" + appId;
    }

}
