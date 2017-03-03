package com.seastar.task;

import com.seastar.common.ChannelType;
import com.seastar.dao.DailyDao;
import com.seastar.dao.ReportDao;
import com.seastar.model.ChannelReportModel;
import com.seastar.model.DailyModel;
import com.seastar.model.UserReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by e on 2017/1/19.
 */
@Component
public class CalculateDataTask
{
    @Autowired
    private DailyDao dailyDao;

    @Autowired
    private ReportDao reportDao;

    //report apps
    private String[] appList = new String[]{"10"};

    //每隔XX时间更新新据
    //@Scheduled(fixedRate = 600000000)       //30秒测试
    //@Scheduled(cron = "0 */2 * * *")    //每两个小时执行一次
    public void UpdateHourReport()
    {
        long startTime = System.currentTimeMillis();
        System.out.println("start: " + startTime);

        for (int day = 0; day < 1; day++)
        {
            Date dt = new Date(System.currentTimeMillis() + dayTime * day);   //0测试用 更新当天的数据

            for (int i = 0; i < appList.length; i++)
            {
                String appId = appList[i];

                //UpdateUserData(dt, appId);       //用户综合数据

                UpdateChannelData(dt, appId);   //渠道综合数据
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("totalTime: " + (endTime - startTime) / 1000);

    }

    //昨日完整数据(每天凌晨6点清算)
    //@Scheduled(cron = "0 6 * * *")
    //@Scheduled(fixedRate = 120000000)       //60秒测试
    public void UpdateYestodayReport()
    {
        long startTime = System.currentTimeMillis();
        System.out.println("start: " + startTime);

        for (int day = 0; day < 31; day++)
        {
            Date dt = new Date(System.currentTimeMillis() + dayTime * day);   //*3测试用 模拟3天后的计算

            for (int i = 0; i < appList.length; i++)
            {
                String appId = appList[i];
                //UpdateUserData(dt, appId);        //用户综合数据
                UpdateRemain(dt, appId);            //用户留存
                System.out.println("oneDay: " + (System.currentTimeMillis() - startTime)/1000);
                //UpdateChannelData(dt, appId);     //渠道综合数据
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("totalTime: " + (endTime - startTime)/1000);
        System.out.println("YestodayReport_OK");
    }

    /**
     * 留存率
     * 梯子形计算 如下（数字2代表次日留存，3代表3日留存..)
     * 如果是1月5日进行清算 则需要计算的为1-1的6日留存,1-2日的5日留存 以此类推..
     * 1-1  23456
     * 1-2  2345
     * 1-3  234
     * 1-4  23
     * 1-5  2
     * 类似留存,LTV等需要持续计算的单独封装。
     * */
    private long dayTime = (1000 * 3600 * 24);
    private void UpdateRemain(Date dt, String appId)
    {
        DailyModel dailyModel = dailyDao.findMinDateData(appId);

        if (dailyModel == null)
            return;

        long startTime = dailyModel.getLoginTime().getTime() / dayTime * dayTime;       //统一日期取整（如1-1 08:00:00)
        long currentTime = dt.getTime() / dayTime * dayTime;

        for (long time = startTime; time < currentTime; time+=dayTime)
        {
            int days = (int)((currentTime - time) / dayTime);

            if (days <= 1)
                continue;

            //以后留存率会有单独报表则按照days存储就可以了（如4日，5日留存等）
            if (days == 2 || days == 3 || days == 7 || days == 30)
            {
                //当天新增用户的N天留存率
                Date currentDay = new Date(time);
                int raminRate = reportDao.getRemainRate(currentDay, days, appId, ChannelType.ALL);

                //0代表当天数据为null
                if (raminRate > 0)
                    reportDao.updateRemainRate(currentDay, days, raminRate, appId);
            }
        }
    }

    //用户综合数据
    private void UpdateUserData(Date dt, String appId)
    {
        UserReportModel urm = reportDao.createUserReportData(dt, appId);
        boolean isHave = reportDao.isHaveUserReportData(dt, appId);

        if (isHave)
            reportDao.updateUserReportData(urm, appId);
        else
            reportDao.saveUserReportData(urm, appId);
    }

    //渠道综合数据
    private void UpdateChannelData(Date date, String appId)
    {
        List<String> list = reportDao.getChannelListByDate(date, appId);

        long startTime = System.currentTimeMillis();
        System.out.println("dayStart: " + startTime);

        for (int i = 0; i < list.size(); i++)
        {
            String channelType = list.get(i);
            if (channelType.isEmpty())
            {
                System.out.println("ChannelType Error!!!");
                continue;
            }

            ChannelReportModel crm = reportDao.createChannelReportModelByChannel(date, appId, channelType);

            boolean isHave = reportDao.isHaveChannelReportData(appId, date, channelType);

            if (isHave)
                reportDao.updateChannelReportData(crm, appId);
            else
                reportDao.saveChannelReportData(crm, appId);

            System.out.println("oneChannel: " + (System.currentTimeMillis() - startTime)/1000);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("dayTotalTime: " + (endTime - startTime)/1000);
    }
}
