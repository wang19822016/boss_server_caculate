package com.seastar.dao;

import com.seastar.model.UserReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    //安装数量
    public int getInstallNum(Date date, String appId)
    {
        String tableName = appId + "_" + "device_base";

        String sql = "select count(deviceId) from " + tableName +" where DATEDIFF(serverTime,?) = 0";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, date);
        return num == null ? 0 : num.intValue();
    }

    //注册人数
    public int getRegNum(Date date, String appId)
    {
        String tableName = appId + "_" + "user_base";

        String sql = "select count(userId) from " + tableName + " where DATEDIFF(serverTime,?) = 0";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, date);
        return num == null ? 0 : num.intValue();
    }

    //有效人数(当天新增)
    public int getValidNum(Date date, String appId)
    {
        String tableName = appId + "_" + "daily_data";

        String sql = "select count(userId) from "+ tableName +" where DATEDIFF(loginTime, ?) = 0 and DATEDIFF(loginTime,regTime) = 0 and onlineTime >= 10";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, date);
        return num == null ? 0 : num.intValue();
    }

    //每日活跃用户
    public int getDau(Date date, String appId)
    {
        String tableName = appId + "_" + "daily_data";
        String sql = "select count(userId) from "+tableName+" where DATEDIFF(loginTime,?) = 0";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, date);
        return num == null ? 0 : num.intValue();
    }

    //每日活跃老用户
    public int getDou(Date date, String appId)
    {
        String tableName = appId + "_" + "daily_data";
        String sql = "select count(userId) from "+tableName+" where DATEDIFF(loginTime,?) = 0 and DATEDIFF(loginTime,regTime) > 0";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, date);
        return num == null ? 0 : num.intValue();
    }

    //每日活跃新用户(day new user)
    public int getDnu(Date date, String appId)
    {
        String tableName = appId + "_" + "daily_data";
        String sql = "select count(userId) from "+tableName+" where DATEDIFF(loginTime,?) = 0 and DATEDIFF(loginTime,regTime) = 0";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, date);
        return num == null ? 0 : num.intValue();
    }

    //每日付费金额
    public int getPayMoney(Date date, String appId)
    {
        String tableName = appId + "_" + "daily_data";
        String sql = "select sum(payMoney) from "+tableName+" where DATEDIFF(loginTime,?) = 0";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, date);
        return num == null ? 0 : num.intValue();
    }

    //每日付费人数
    public int getPayNum(Date date, String appId)
    {
        String tableName = appId + "_" + "daily_data";
        String sql = "select count(userId) from "+tableName+" where DATEDIFF(loginTime,?) = 0 and payMoney > 0";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, date);
        return num == null ? 0 : num.intValue();
    }

    //日付费率
    public int getPayRate(Date date, String appId)
    {
        float payNum = (float)getPayNum(date, appId);
        float dau = (float)getDau(date, appId);

        return (int)((payNum / dau) * 100);
    }

    //新用户每日付费金额
    public int getNewUserPayMoney(Date date, String appId)
    {
        String tableName = appId + "_" + "daily_data";
        String sql = "select sum(payMoney) from "+tableName+" where DATEDIFF(loginTime,?) = 0 and DATEDIFF(loginTime,regTime) = 0";
        //String sql = "select sum(payMoney) from "+tableName+" where DATEDIFF(loginTime,?) = 0";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, date);
        return num == null ? 0 : num.intValue();
    }

    //新用户每日付费人数
    public int getNewUserPayNum(Date date, String appId)
    {
        String tableName = appId + "_" + "daily_data";
        String sql = "select count(userId) from "+tableName+" where DATEDIFF(loginTime,?) = 0 and payMoney > 0 and DATEDIFF(loginTime,regTime) = 0";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, date);
        return num == null ? 0 : num.intValue();
    }

    //新用户日付费率
    public int getNewUserPayRate(Date date, String appId)
    {
        float payNum = (float)getNewUserPayNum(date, appId);
        float dnu = (float)getDnu(date, appId);

        return (int)(payNum / dnu * 100);
    }

    //活跃用户平均付费金额(百位制,客户端需除以100）
    public int getArpu(Date date, String appId)
    {
        float payMoney = (float)getPayMoney(date, appId);
        float dau = (float)getDau(date, appId);
        int arpu = (int)(payMoney / dau * 100) ;
        return arpu;
    }

    //付费用户平均付费金额(百位制,客户端需除以100）
    public int getArppu(Date date, String appId)
    {
        float payMoney = (float)getPayMoney(date, appId);
        float payNum = (float)getPayNum(date, appId);
        int arppu = (int)(payMoney / payNum * 100) ;
        return arppu;
    }

    //留存率(-1代表暂无数据)
    public int getRemainRate(Date date, int days, String appId)
    {
        String tableName = appId + "_" + "daily_data";
        String sql = "select userId from "+tableName+" where DATEDIFF(loginTime,?) = 0 and DATEDIFF(loginTime,regTime) = 0";
        List newUsers = jdbcTemplate.queryForList(sql, date);

        if (newUsers.size() <= 0)
            return  - 1;

        Iterator it = newUsers.iterator();
        int remainNum = 0;

        while(it.hasNext())
        {
            Map userMap = (Map) it.next();
            sql = "select count(userId) from "+tableName+" where userId = ? and DATEDIFF(loginTime,regTime) = ?";
            Integer num = jdbcTemplate.queryForObject(sql, Integer.class, userMap.get("userId"), days - 1);
//            if (num > 0)
//                System.out.println("num.... " + num);
            int numValue = num == null ? 0 : num.intValue();

            if (numValue > 0)
                remainNum++;
        }

        return (int)((float) remainNum / (float)newUsers.size() * 100);
    }

    //平均在线人数
    public int getAvgOnlineNum(Date date, String appId)
    {
        String tableName = appId + "_" + "user_login";
        String sql = "select count(userId) from "+tableName+" where DATEDIFF(serverTime,?) = 0";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, date);
        return num == null ? 0 : num.intValue() / 24;
    }

    //平均在线时长
    public int getAvgOnlineTime(Date date, String appId)
    {
        String tableName = appId + "_" + "daily_data";

        String sql = "select sum(onlineTime) from "+tableName+" where DATEDIFF(loginTime,?) = 0";
        Integer time = jdbcTemplate.queryForObject(sql, Integer.class, date);

        sql = "select count(userId) from "+tableName+" where DATEDIFF(loginTime,?) = 0";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, date);

        int timeValue = time == null ? 0 : time.intValue();
        int numValue =  num == null ? 0 : num.intValue();

        return  timeValue / numValue;   //numvalue 不应该为0
    }

    //创建一条日报数据
    public UserReportModel createUserReportData(Date date, String appId)
    {
        UserReportModel userReportModel = new UserReportModel();
        userReportModel.setDate(date);
        userReportModel.setInstallNum(getInstallNum(date, appId));
        userReportModel.setRegNum(getRegNum(date, appId));
        userReportModel.setValidNum(getValidNum(date, appId));
        userReportModel.setDau(getDau(date, appId));
        userReportModel.setDou(getDou(date, appId));
        userReportModel.setPayMoney(getPayMoney(date, appId));
        userReportModel.setPayNum(getPayNum(date, appId));
        userReportModel.setPayRate(getPayRate(date, appId));
        userReportModel.setNewUserPayMoney(getNewUserPayMoney(date, appId));
        userReportModel.setNewUserPayNum(getNewUserPayNum(date, appId));
        userReportModel.setNewUserPayRate(getNewUserPayRate(date, appId));
        userReportModel.setArpu(getArpu(date, appId));
        userReportModel.setArppu(getArppu(date, appId));
//        userReportModel.setRemain2(getRemainRate(date, 2, appId));
//        userReportModel.setRemain3(getRemainRate(date, 3, appId));
//        userReportModel.setRemain7(getRemainRate(date, 7, appId));
//        userReportModel.setRemain30(getRemainRate(date, 30, appId));
        userReportModel.setAvgOnlineNum(getAvgOnlineNum(date, appId));
        userReportModel.setAvgOnlineTime(getAvgOnlineTime(date, appId));
        return userReportModel;
    }

    //是否存在某天的日报数据
    public boolean isHaveUserReportData(Date dt, String appId)
    {
        String tableName = appId + "_" + "user_report";
        String sql = "select count(date) from "+tableName+" where DATEDIFF(date,?) = 0";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        int numValue = num == null ? 0 : num.intValue();
        if (numValue > 0)
            return true;
        else
            return false;
    }

    //存储日报数据
    public void saveUserReportData(UserReportModel urm, String appId)
    {
        String tableName = appId + "_" + "user_report";

        jdbcTemplate.update("INSERT INTO "+tableName+"(date, installNum, regNum, validNum, dau, dou, payMoney, payNum, payRate, " +
                        "newUserPayMoney,newUserPayNum,newUserPayRate,arpu,arppu,remain2,remain3,remain7,remain30,avgOnlineNum,avgOnlineTime) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
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
                urm.getRemain2(),
                urm.getRemain3(),
                urm.getRemain7(),
                urm.getRemain30(),
                urm.getAvgOnlineNum(),
                urm.getAvgOnlineTime()
                );
    }

    //更新N天留存率
    public void updateRemainRate(Date date, int days, int remainRate, String appId)
    {
        String tableName = appId + "_" + "user_report";

        try
        {
            jdbcTemplate.update("UPDATE "+tableName+" set remain" + days + " = ? WHERE DATEDIFF(date,?) = 0" ,
                    remainRate,
                    date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    //更新日报数据
    public void updateUserReportData(UserReportModel urm, String appId)
    {
        String tableName = appId + "_" + "user_report";

        jdbcTemplate.update("UPDATE "+tableName+" SET installNum = ?, regNum = ?, validNum = ?, dau = ?, dou = ?, " +
                        "payMoney = ?, payNum = ?, payRate = ?, newUserPayMoney = ?,newUserPayNum = ?,newUserPayRate = ?," +
                        "arpu = ?,arppu = ?,remain2 = ?,remain3 = ?,remain7 = ?,remain30 = ?,avgOnlineNum = ?,avgOnlineTime = ? " +
                        "WHERE DATEDIFF(date,?) = 0" ,
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
                urm.getRemain2(),
                urm.getRemain3(),
                urm.getRemain7(),
                urm.getRemain30(),
                urm.getAvgOnlineNum(),
                urm.getAvgOnlineTime(),
                urm.getDate()

//        jdbcTemplate.update("UPDATE "+tableName+" SET remain2 = ?) " +
//                        "WHERE DATEDIFF(date,?) = 0" ,
//                10,
//                urm.getDate()
        );
    }
}
