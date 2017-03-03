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

    //安装数量（全渠道）
    public int getInstallNum(Date date, String appId, String channelType)
    {
        String tableName = appId + "_" + "device_base";
        String dt = DateFormat.getDateInstance().format(date);
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
        String tableName = appId + "_" + "user_base";
        String dt = DateFormat.getDateInstance().format(date);
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
        String tableName = appId + "_" + "daily_data";
        String dt = DateFormat.getDateInstance().format(date);
        String sql;
        Integer num;
        if (channelType == ChannelType.ALL)
        {
            sql = "select count(userId) from "+ tableName +" where loginTime = ? and loginTime = regTime and onlineTime >= 10";
            num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        }
        else
        {
            sql = "select count(userId) from "+ tableName +" where loginTime = ? and loginTime = regTime and channelType = ? and onlineTime >= 10";
            num = jdbcTemplate.queryForObject(sql, Integer.class, dt, channelType);
        }

        return num == null ? 0 : num.intValue();
    }

    //每日活跃用户
    public int getDau(Date date, String appId, String channelType)
    {
        String tableName = appId + "_" + "daily_data";
        String dt = DateFormat.getDateInstance().format(date);
        String sql;
        Integer num;
        if (channelType == ChannelType.ALL)
        {
            sql = "select count(userId) from "+tableName+" where loginTime = ?";
            num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        }
        else
        {
            sql = "select count(userId) from "+tableName+" where loginTime = ? and channelType = ?";
            num = jdbcTemplate.queryForObject(sql, Integer.class, dt, channelType);
        }

        return num == null ? 0 : num.intValue();
    }

    //每日活跃老用户
    public int getDou(Date date, String appId)
    {
        String tableName = appId + "_" + "daily_data";
        String dt = DateFormat.getDateInstance().format(date);
        String sql = "select count(userId) from "+tableName+" where loginTime = ? and loginTime > regTime";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        return num == null ? 0 : num.intValue();
    }

    //每日活跃新用户(day new user)
    public int getDnu(Date date, String appId)
    {
        String tableName = appId + "_" + "daily_data";
        String dt = DateFormat.getDateInstance().format(date);
        String sql = "select count(userId) from "+tableName+" where loginTime = ? and loginTime = regTime";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        return num == null ? 0 : num.intValue();
    }

    //每日付费金额
    public int getPayMoney(Date date, String appId, String channelType)
    {
        String tableName = appId + "_" + "daily_data";
        String dt = DateFormat.getDateInstance().format(date);
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

        return num == null ? 0 : num.intValue();
    }

    //每日付费人数
    public int getPayNum(Date date, String appId, String channelType)
    {
        String tableName = appId + "_" + "daily_data";
        String dt = DateFormat.getDateInstance().format(date);
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

    //日付费率
    public int getPayRate(Date date, String appId,String channelType)
    {
        float payNum = (float)getPayNum(date, appId, channelType);
        float dau = (float)getDau(date, appId, channelType);
        if (dau <= 0)
            return 0;
        return (int)((payNum / dau) * 100);
    }

    //新用户每日付费金额
    public int getNewUserPayMoney(Date date, String appId)
    {
        String tableName = appId + "_" + "daily_data";
        String dt = DateFormat.getDateInstance().format(date);
        String sql = "select sum(payMoney) from "+tableName+" where loginTime = ? and loginTime = regTime";
        //String sql = "select sum(payMoney) from "+tableName+" where DATEDIFF(loginTime,?) = 0";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        return num == null ? 0 : num.intValue();
    }

    //新用户每日付费人数
    public int getNewUserPayNum(Date date, String appId)
    {
        String tableName = appId + "_" + "daily_data";
        String dt = DateFormat.getDateInstance().format(date);
        String sql = "select count(userId) from "+tableName+" where loginTime = ? and loginTime = regTime and payMoney > 0 ";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        return num == null ? 0 : num.intValue();
    }

    //新用户日付费率
    public int getNewUserPayRate(Date date, String appId)
    {
        float payNum = (float)getNewUserPayNum(date, appId);
        float dnu = (float)getDnu(date, appId);
        if (dnu <= 0)
            return 0;
        return (int)(payNum / dnu * 100);
    }

    //活跃用户平均付费金额(百位制,客户端需除以100）
    public int getArpu(Date date, String appId, String channelType)
    {
        float payMoney = (float)getPayMoney(date, appId, channelType);
        float dau = (float)getDau(date, appId, channelType);
        if (dau <= 0)
            return 0;
        int arpu = (int)(payMoney / dau * 100) ;
        return arpu;
    }

    //付费用户平均付费金额(百位制,客户端需除以100）
    public int getArppu(Date date, String appId, String channelType)
    {
        float payMoney = (float)getPayMoney(date, appId, channelType);
        float payNum = (float)getPayNum(date, appId, channelType);
        if (payNum <= 0)
            return 0;
        int arppu = (int)(payMoney / payNum * 100) ;
        return arppu;
    }

    //留存率(-1代表暂无数据)
    public int getRemainRate(Date date, int days, String appId, String channelType)
    {
        String tableName = appId + "_" + "daily_data";
        String dt = DateFormat.getDateInstance().format(date);
        String sql;
        List newUsers;

        if (channelType == ChannelType.ALL)
        {
            sql = "select userId from " + tableName + " where loginTime = ? and loginTime = regTime";
            newUsers = jdbcTemplate.queryForList(sql, dt);
        }
        else
        {
            sql = "select userId from " + tableName + " where loginTime = ? and loginTime = regTime and channelType = ?";
            newUsers = jdbcTemplate.queryForList(sql, dt, channelType);
        }

        if (newUsers.size() <= 0)
            return  - 1;

        Iterator it = newUsers.iterator();
        int remainNum = 0;

        while(it.hasNext())
        {
            Map userMap = (Map) it.next();
            //sql = "select count(userId) from "+tableName+" where userId = ? and DATEDIFF(loginTime,regTime) = ?";
            sql = "select count(userId) from "+tableName+" where userId = ? and loginTime = regTime + ?";
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
        String dt = DateFormat.getDateInstance().format(date);
        String sql = "select count(userId) from "+tableName+" where serverDate = ?";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt);
        return num == null ? 0 : num.intValue() / 24;
    }

    //平均在线时长
    public int getAvgOnlineTime(Date date, String appId)
    {
        String tableName = appId + "_" + "daily_data";
        String dt = DateFormat.getDateInstance().format(date);
        String sql = "select sum(onlineTime) from "+tableName+" where loginTime = ?";
        Integer time = jdbcTemplate.queryForObject(sql, Integer.class, dt);

        sql = "select count(userId) from "+tableName+" where loginTime = ?";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt);

        int timeValue = time == null ? 0 : time.intValue();
        int numValue =  num == null ? 0 : num.intValue();
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
        String d = DateFormat.getDateInstance().format(dt);
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
        String tableName = appId + "_" + "user_report";

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

    //更新N天留存率
    public void updateRemainRate(Date date, int days, int remainRate, String appId)
    {
        String tableName = appId + "_" + "user_report";
        String dt = DateFormat.getDateInstance().format(date);
        try
        {
           int line = jdbcTemplate.update("UPDATE "+tableName+" set remain" + days + " = ? WHERE date = ?" ,
                    remainRate,
                    dt);
           //System.out.println("remain_update_line: " + line);
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
        String dt = DateFormat.getDateInstance().format(urm.getDate());
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
        String tableName = appId + "_" + "device_base";
        String dt = DateFormat.getDateInstance().format(date);
        String sql = "select distinct channelType from " + tableName + " where serverDate = ?";
        List<String> list = jdbcTemplate.queryForList(sql, String.class, dt);
        return list;
    }

    //根据日期渠道 创建一条渠道数据
    public ChannelReportModel createChannelReportModelByChannel(Date date, String appId, String channelType)
    {
        ChannelReportModel crm = new ChannelReportModel();
        crm.setDate(date);
        crm.setChannelType(channelType);
        //crm.setShowNum();
        //crm.setClickNum();
        crm.setCpc(getCpc(date, appId, channelType));
        crm.setCpm(getCpm(date, appId, channelType));
        crm.setInstallNum(getInstallNum(date, appId, channelType));
        crm.setCpi(getCpi(date, appId, channelType));
        crm.setValidNum(getValidNum(date, appId, channelType));
        crm.setClickRate(getClickRate(date, appId, channelType));
        crm.setInstallRate(getInstallRate(date, appId, channelType));
        crm.setRegRate(getRegRate(date, appId, channelType));
        crm.setValidRate(getValidRate(date, appId, channelType));
        crm.setRoi(getRoi(date, appId, channelType));
        //crm.setCostMoney(getCostMoney(date, appId, channelType));
        crm.setRemain2(getRemainRate(date, 2, appId, channelType));
        crm.setRemain7(getRemainRate(date, 7, appId, channelType));
        crm.setRemain30(getRemainRate(date, 30, appId, channelType));
        crm.setPayMoney(getPayMoney(date, appId, channelType));
        crm.setPayNum(getPayNum(date, appId, channelType));
        crm.setPayRate(getPayRate(date, appId, channelType));
        crm.setArpu(getArpu(date, appId, channelType));
        crm.setArppu(getArppu(date, appId, channelType));

        return crm;
    }

    //是否存在某天的渠道数据
    public boolean isHaveChannelReportData(String appId, Date date, String channelType)
    {
        String tableName = appId + "_" + "channel_report";
        String dt = DateFormat.getDateInstance().format(date);

        String sql = "select count(id) from "+tableName+" where date = ? and channelType = ?";
        Integer num = jdbcTemplate.queryForObject(sql, Integer.class, dt, channelType);

        int numValue = num == null ? 0 : num.intValue();

        if (numValue > 0)
            return true;
        else
            return false;
    }

    //存储渠道数据
    public void saveChannelReportData(ChannelReportModel crm, String appId)
    {
        String tableName = appId + "_" + "channel_report";

        jdbcTemplate.update("INSERT INTO "+tableName+"(date, channelType, cpc, cpm, installNum, cpi, validNum, clickRate, installRate,regRate, validRate, " +
                        "roi, remain2, remain3, remain7, remain30,payMoney,payNum,payRate,arpu,arppu) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                crm.getDate(),
                crm.getChannelType(),
                crm.getCpc(),
                crm.getCpm(),
                crm.getInstallNum(),
                crm.getCpi(),
                crm.getValidNum(),
                crm.getClickRate(),
                crm.getInstallRate(),
                crm.getRegRate(),
                crm.getValidRate(),
                crm.getRoi(),
                crm.getRemain2(),
                crm.getRemain3(),
                crm.getRemain7(),
                crm.getRemain30(),
                crm.getPayMoney(),
                crm.getPayNum(),
                crm.getPayRate(),
                crm.getArpu(),
                crm.getArppu()
        );
    }

    //更新渠道数据
    public void updateChannelReportData(ChannelReportModel crm, String appId)
    {
        String tableName = appId + "_" + "channel_report";
        String dt = DateFormat.getDateInstance().format(crm.getDate());
        jdbcTemplate.update("UPDATE "+tableName+" SET showNum = ?, clickNum = ?, cpc = ?, cpm = ?, installNum = ?, cpi = ?, validNum = ?, " +
                        "clickRate = ?, installRate = ?, regRate = ?, validRate = ?, roi = ?, costMoney = ?, remain2 = ?, " +
                        "remain3 = ?, remain7 = ?, remain30 = ?, payMoney = ?,payNum = ?,payRate = ?, arpu = ?,arppu = ? " +
                        "WHERE date = ? and channelType = ?" ,
                crm.getShowNum(),
                crm.getClickNum(),
                crm.getCpc(),
                crm.getCpm(),
                crm.getInstallNum(),
                crm.getCpi(),
                crm.getValidNum(),
                crm.getClickRate(),
                crm.getInstallRate(),
                crm.getRegRate(),
                crm.getValidRate(),
                crm.getRoi(),
                crm.getCostMoney(),
                crm.getRemain2(),
                crm.getRemain3(),
                crm.getRemain7(),
                crm.getRemain30(),
                crm.getPayMoney(),
                crm.getPayNum(),
                crm.getPayRate(),
                crm.getArpu(),
                crm.getArppu(),
                dt,
                crm.getChannelType());
    }

    //注册率
    public int getRegRate(Date date, String appId, String channelType)
    {
        int regNum = getRegNum(date, appId, channelType);
        int installNum = getInstallNum(date, appId, channelType);

        return (int)((float) regNum / (float)installNum * 100);
    }

    //有效转化率
    public int getValidRate(Date date, String appId, String channelType)
    {
        int validNum = getValidNum(date, appId, channelType);
        int installNum = getInstallNum(date, appId, channelType);
        return (int)((float) validNum / (float)installNum * 100);
    }

    //单用户安装成本
    public int getCpi(Date date, String appId, String channelType)
    {
        String tableName = appId + "_" + "channel_report";
        String dt = DateFormat.getDateInstance().format(date);

        int money = getCostMoney(date, appId, channelType);
        int installNum = getInstallNum(date, appId, channelType);

        if (installNum <= 0)
            return -1;

        return (int)((float) money / (float)installNum * 100);
    }

    //点击率
    public int getClickRate(Date date, String appId, String channelType)
    {
        String tableName = appId + "_" + "channel_report";
        int showNum = getShowNum(date, appId, channelType);
        int clickNum = getClickNum(date, appId, channelType);

        if (showNum <= 0)
            return -1;

        return (int)((float) clickNum / (float)showNum * 100);
    }

    //安装率
    public int getInstallRate(Date date, String appId, String channelType)
    {
        String tableName = appId + "_" + "channel_report";

        int clickNum = getClickNum(date, appId, channelType);
        int installNum = getInstallNum(date, appId, channelType);

        if (clickNum <= 0)
            return -1;

        return (int)((float) installNum / (float)clickNum * 100);
    }

    //花费
    public int getCostMoney(Date date, String appId, String channelType)
    {
        String tableName = appId + "_" + "channel_report";
        String dt = DateFormat.getDateInstance().format(date);
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

        return money == null ? 0 : money.intValue();
    }

    //回报率
    public int getRoi(Date date, String appId, String channelType)
    {
        int payMoney = getPayMoney(date, appId, channelType);
        int costMoney = getCostMoney(date, appId, channelType);

        if (costMoney <= 0)
            return -1;

        return (int)((float) payMoney / (float)costMoney * 100);
    }

    //展示数
    public int getShowNum(Date date, String appId, String channelType)
    {
        String tableName = appId + "_" + "channel_report";
        String dt = DateFormat.getDateInstance().format(date);
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
        String tableName = appId + "_" + "channel_report";
        String dt = DateFormat.getDateInstance().format(date);
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
    public int getCpc(Date date, String appId, String channelType)
    {
        int costMoney = getCostMoney(date, appId, channelType);
        int clickNum = getClickNum(date, appId, channelType);
        return (int)((float) costMoney / (float)clickNum * 100);
    }

    //按展示1000次购买
    public int getCpm(Date date, String appId, String channelType)
    {
        int costMoney = getCostMoney(date, appId, channelType);
        int showNum = getShowNum(date, appId, channelType);
        return (int)((float) costMoney / (float)showNum * 1000 * 100);
    }


    //获得日报列表
    public List<UserReportModel> getUserReportList(String appId, Date beginTime, Date endTime)
    {
        String tableName = appId + "_" + "user_report";
        String bt = DateFormat.getDateInstance().format(beginTime);
        String et = DateFormat.getDateInstance().format(endTime);

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
        String tableName = appId + "_" + "channel_report";
        String bt = DateFormat.getDateInstance().format(beginTime);
        String et = DateFormat.getDateInstance().format(endTime);

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

    //按累计查询
    public List<ChannelReportModel> getChannelReportListByRange(String appId, Date beginTime, Date endTime)
    {
        String tableName = appId + "_" + "channel_report";
        String bt = DateFormat.getDateInstance().format(beginTime);
        String et = DateFormat.getDateInstance().format(endTime);

        String sql = "select channelType, sum(showNum) as showNum, sum(clickNum) as clickNum, sum(cpc) as cpc, sum(cpm) as cpm," +
                "sum(installNum) as installNum, sum(cpi) as cpi, sum(validNum) as validNum, sum(clickRate) as clickRate," +
                "sum(installRate) as installRate, sum(regRate) as regRate, sum(validRate) as validRate, sum(roi) as roi, " +
                "sum(costMoney) as costMoney, sum(remain2) as remain2, sum(remain3) as remain3, sum(remain7) as remain7, " +
                "sum(remain30) as remain30, sum(payMoney) as payMoney, sum(payNum) as payNum, sum(payRate) as payRate, " +
                "sum(arpu) as arpu, sum(arppu) as arppu from " + tableName + " where date between ? and ? group by channelType order by channelType";

        List list = jdbcTemplate.queryForList(sql, bt, et);
        Iterator it = list.iterator();

        List<ChannelReportModel> crmList = new ArrayList<ChannelReportModel>();

        while(it.hasNext())
        {
            Map<String, Object> map = (Map) it.next();
            ChannelReportModel crm = new ChannelReportModel();

            int days = (int)((endTime.getTime() - beginTime.getTime()) / (1000*3600*24)) + 1;

            int showNum = ((BigDecimal)map.get("showNum")).intValue();
            int clickNum = ((BigDecimal)map.get("clickNum")).intValue();
            int cpc = ((BigDecimal)map.get("cpc")).intValue() / days;
            int cpm = ((BigDecimal)map.get("cpm")).intValue() / days;
            int installNum = ((BigDecimal)map.get("installNum")).intValue();
            int cpi = ((BigDecimal)map.get("cpi")).intValue() / days;
            int validNum = ((BigDecimal)map.get("validNum")).intValue();
            int clickRate = ((BigDecimal)map.get("clickRate")).intValue() / days;
            int installRate = ((BigDecimal)map.get("installRate")).intValue() / days;
            int regRate = ((BigDecimal)map.get("regRate")).intValue() / days;
            int validRate = ((BigDecimal)map.get("validRate")).intValue() / days;
            int roi = ((BigDecimal)map.get("roi")).intValue();
            int costMoney = ((BigDecimal)map.get("costMoney")).intValue();
            int remain2 = ((BigDecimal)map.get("remain2")).intValue() / days;
            int remain3 = ((BigDecimal)map.get("remain3")).intValue() / days;
            int remain7 = ((BigDecimal)map.get("remain7")).intValue() / days;
            int remain30 = ((BigDecimal)map.get("remain30")).intValue() / days;
            int payMoney = ((BigDecimal)map.get("payMoney")).intValue();
            int payNum = ((BigDecimal)map.get("payNum")).intValue();
            int payRate = ((BigDecimal)map.get("payRate")).intValue() / days;
            int arpu = ((BigDecimal)map.get("arpu")).intValue() / days;
            int arppu = ((BigDecimal)map.get("arppu")).intValue() / days;

            crm.setChannelType((String)map.get("channelType"));
            crm.setShowNum(showNum);
            crm.setClickNum(clickNum);
            crm.setCpc(cpc);
            crm.setCpm(cpm);
            crm.setInstallNum(installNum);
            crm.setCpi(cpi);
            crm.setValidNum(validNum);
            crm.setClickRate(clickRate);
            crm.setInstallRate(installRate);
            crm.setRegRate(regRate);
            crm.setValidRate(validRate);
            crm.setRoi(roi);
            crm.setCostMoney(costMoney);
            crm.setRemain2(remain2);
            crm.setRemain3(remain3);
            crm.setRemain7(remain7);
            crm.setRemain30(remain30);
            crm.setPayMoney(payMoney);
            crm.setPayNum(payNum);
            crm.setPayRate(payRate);
            crm.setArpu(arpu);
            crm.setArppu(arppu);

            crmList.add(crm);
        }

        return crmList;
    }

    //添加广告数据(手动)
    public void addAdDetails(String appId, Date date, String channelType, int showNum, int clickNum, int costMoney)
    {
        String tableName = appId + "_" + "channel_report";

        jdbcTemplate.update("INSERT  INTO " + tableName + "(date, showNum, clickNum, costMoney) VALUES (?,?,?,?)",
                date, channelType, showNum, clickNum, costMoney);
    }

    //更新广告数据
    public void updateAdDetails(String appId, Date date, String channelType, int showNum, int clickNum, int costMoney)
    {
        String tableName = appId + "_" + "channel_report";
        String dt = DateFormat.getDateInstance().format(date);

        jdbcTemplate.update("UPDATE "+tableName+" set showNum = ?, clickNum = ?, costMoney = ? WHERE date = ? and " +
                        "channelType = ?",showNum, clickNum ,costMoney,dt, channelType);
    }
}
