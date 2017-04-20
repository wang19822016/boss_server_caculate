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

    private long dayTime = (1000 * 3600 * 24);

    //昨日数据更新, 每天北京时间8点执行一次
    @Scheduled(cron = "0 0 8 ? * *")
    public void UpdateDayData()
    {
        List<String> appList = reportDao.getApps();

        long startTime = System.currentTimeMillis();
        System.out.println("DayDataStart: " + new Date(startTime));

        Date dt = new Date(startTime - dayTime);

        for (int i = 0; i < appList.size(); i++)
        {
            String appId = appList.get(i);

            UpdateUserData(dt, appId);          //日报
            UpdateChannelData(dt, appId);       //渠道
            UpdateChannelLastData(appId);       //渠道持续累计数据
            UpdateRemain(dt, appId);            //留存
            UpdateLTV(dt, appId);               //更新LTV
            UpdateROI(dt, appId);               //更新ROI
            UpdatePayConversion(dt, appId);     //更新付费转化
        }

        long endTime = System.currentTimeMillis();

        System.out.println("DayDataEnd: " + new Date(endTime));
        System.out.println("DayDataTotalTime: " + (endTime - startTime)/1000);

        System.out.println("DayReport_OK");
    }

    //每N时更新数据
    //@Scheduled(fixedDelay = 1000 * 60 * 60, initialDelay = 1)
    public void UpdateHourData()
    {
        UpdateDayData();
    }

    //每N分钟更新数据
    //@Scheduled(fixedDelay = 1000 * 60 * 5, initialDelay = 1)
    public void UpdateMinuteData()
    {
        UpdateDayData();
    }

    //更新测试
    //@Scheduled(fixedRate = 30000)         //30秒测试
    //@Scheduled(cron = "0 19 20 ? * *")    //每天20点19执行一次
    @Scheduled(fixedDelay = 1000 * 60 * 5, initialDelay = 1)
    public void UpdateTest()
    {
        List<String> appList = reportDao.getApps();

        long startTime = System.currentTimeMillis();
        System.out.println("start: " + startTime);

        //更新当天的数据 加for循环主要是用于测试
        for (int day = 0; day < 7; day++)
        {
            Date dt = new Date(System.currentTimeMillis() - dayTime * day);

            for (int i = 0; i < appList.size(); i++)
            {
                String appId = appList.get(i);

                UpdateUserData(dt, appId);          //日报
                UpdateChannelData(dt, appId);       //渠道
                UpdateChannelLastData(appId);       //渠道持续累计数据
                UpdateRemain(dt, appId);            //留存
                UpdateLTV(dt, appId);               //更新LTV
                UpdateROI(dt, appId);               //更新ROI
                UpdatePayConversion(dt, appId);     //更新付费转化
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("totalTime: " + (endTime - startTime) / 1000);
    }


    /****************************更新数据如下****************************************/

    //日报
    private void UpdateUserData(Date dt, String appId)
    {
        UserReportModel urm = reportDao.createUserReportData(dt, appId);
        boolean isHave = reportDao.isHaveUserReportData(dt, appId);

        if (isHave)
            reportDao.updateUserReportData(urm, appId);
        else
            reportDao.saveUserReportData(urm, appId);
    }

    //渠道
    private void UpdateChannelData(Date date, String appId)
    {
        List<String> list = reportDao.getChannelListByDate(date, appId);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < list.size(); i++)
        {
            String channelType = list.get(i);
            if (channelType == null || channelType.isEmpty())
            {
                System.out.println("Channel-ChannelType Unknown!!!");
                continue;
            }

            ChannelReportModel crm = reportDao.createChannelBaseData(date, appId, channelType);

            boolean isHave = reportDao.isHaveChannelData(appId, date, channelType);

            if (isHave)
                reportDao.updateChannelBaseData(crm, appId);
            else
                reportDao.saveChannelBaseData(crm, appId);
        }

        long endTime = System.currentTimeMillis();

        System.out.println("ChannelTotalTime: " + (endTime - startTime)/1000);
    }

    /**
     * 更新留存
     * 梯子形计算 如下（数字2代表次日留存，3代表3日留存..)
     * 如果是1月5日进行清算 则需要计算的为1-1的6日留存,1-2日的5日留存 以此类推..
     * 1-1  23456
     * 1-2  2345
     * 1-3  234
     * 1-4  23
     * 1-5  2
     * */
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

            //当天新增用户的N天留存率
            Date currentDay = new Date(time);
            float raminRate = reportDao.getRemainRate(currentDay, days, appId, ChannelType.ALL);

            //每日留存
            if (raminRate > 0)
                reportDao.updateRemainRate(currentDay, days, (float)raminRate / 100, appId);

            if (days == 2 || days == 3 || days == 7 || days == 30)
            {
                //日报留存
                if (raminRate > 0)
                    reportDao.updateUserReportRemainRate(currentDay, days, raminRate, appId);

                //渠道留存(当日所有)
                if (raminRate > 0)
                    reportDao.UpdateAllChannelRemain(appId, currentDay, days);
            }
        }
    }

    //更新渠道持续数据（涉及持续收入带来的变化数据 如cpi, roi, 付费, 付费率等)
    private void UpdateChannelLastData(String appId)
    {
        reportDao.UpdateChannelLastData(appId);
    }

    //更新N日LTV
    private void UpdateLTV(Date dt, String appId)
    {
        DailyModel dailyModel = dailyDao.findMinDateData(appId);

        if (dailyModel == null)
            return;

        long startTime = dailyModel.getLoginTime().getTime() / dayTime * dayTime;

        long currentTime = dt.getTime() / dayTime * dayTime;

        for (long time = startTime; time <= currentTime; time+=dayTime)
        {
            int days = (int) ((currentTime - time) / dayTime);

            Date currentDay = new Date(time);

            float ltv = reportDao.getLTV(currentDay, days, appId, ChannelType.ALL);

            reportDao.updateLTV(currentDay, days+1, ltv, appId);
        }
    }

    //更新N日ROI
    private void UpdateROI(Date dt, String appId)
    {
        DailyModel dailyModel = dailyDao.findMinDateData(appId);

        if (dailyModel == null)
            return;

        long startTime = dailyModel.getLoginTime().getTime() / dayTime * dayTime;

        long currentTime = dt.getTime() / dayTime * dayTime;

        for (long time = startTime; time <= currentTime; time+=dayTime)
        {
            int days = (int) ((currentTime - time) / dayTime);

            Date currentDay = new Date(time);

            float grossIncome = reportDao.getGrossIncome(currentDay, days, appId, ChannelType.ALL);

            float cost = reportDao.getCostMoney(currentDay, appId, ChannelType.ALL);

            reportDao.updateROI(currentDay, days+1, grossIncome, cost, appId);
        }
    }

    //更新付费转化
    private void UpdatePayConversion(Date dt, String appId)
    {
        DailyModel dailyModel = dailyDao.findMinDateData(appId);

        if (dailyModel == null)
            return;

        long startTime = dailyModel.getLoginTime().getTime() / dayTime * dayTime;       //统一日期取整（如1-1 08:00:00)

        long currentTime = dt.getTime() / dayTime * dayTime;

        for (long time = startTime; time <= currentTime; time+=dayTime)
        {
            int days = (int) ((currentTime - time) / dayTime);

            Date currentDay = new Date(time);

            int dnu = reportDao.getDnu(currentDay, appId);
            int dnuPayNum = reportDao.getDnuPayNumByDays(currentDay, days, appId, ChannelType.ALL);
            int dnuPayTimes = reportDao.getDnuPayTimesByDays(currentDay, days, appId, ChannelType.ALL);

            reportDao.updatePayConversion(currentDay, days+1, dnu, dnuPayNum, dnuPayTimes, appId);
        }
    }
}
