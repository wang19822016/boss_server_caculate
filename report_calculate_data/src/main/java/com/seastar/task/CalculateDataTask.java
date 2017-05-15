package com.seastar.task;

import com.seastar.common.ChannelType;
import com.seastar.common.PlatformType;
import com.seastar.dao.DailyDao;
import com.seastar.dao.ReportDao2;
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
    private ReportDao2 reportDao;

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

            UpdateUserData(dt, appId);                                //日报
            UpdateChannelData(dt, appId);                             //渠道
            UpdateChannelLastData(appId);                             //渠道持续累计数据
            UpdateRemain(dt, appId, PlatformType.ALL);                //留存
            UpdateLTV(dt, appId,PlatformType.ALL);                    //更新LTV
            UpdateROI(dt, appId, PlatformType.ALL);                   //更新ROI
            UpdatePayConversion(dt, appId, PlatformType.ALL);         //更新付费转化

            UpdateUserData(dt, appId, PlatformType.Android);          //Android日报
            UpdateUserData(dt, appId, PlatformType.IOS);              //Ios日报
            UpdateChannelData(dt, appId, PlatformType.Android);       //Android渠道
            UpdateChannelData(dt, appId, PlatformType.IOS);           //IOS渠道
            UpdateChannelLastData(appId, PlatformType.Android);       //Android渠道持续累计数据
            UpdateChannelLastData(appId, PlatformType.IOS);           //IOS渠道持续累计数据
            UpdateRemain(dt, appId, PlatformType.Android);            //Android留存
            UpdateRemain(dt, appId, PlatformType.IOS);                //IOS留存
            UpdateLTV(dt, appId, PlatformType.Android);               //Android更新LTV
            UpdateLTV(dt, appId, PlatformType.IOS);                   //IOS更新LTV
            UpdateROI(dt, appId, PlatformType.Android);               //Android ROI
            UpdateROI(dt, appId, PlatformType.IOS);                   //IOS ROI
            UpdatePayConversion(dt, appId, PlatformType.Android);     //Android付费转化
            UpdatePayConversion(dt, appId, PlatformType.IOS);         //IOS付费转化
        }

        long endTime = System.currentTimeMillis();

        System.out.println("DayDataEnd: " + new Date(endTime));
        System.out.println("DayDataTotalTime: " + (float)(endTime - startTime)/1000);

        System.out.println("DayReport_OK");
    }

    //每N时更新数据
    //@Scheduled(fixedDelay = 1000 * 60 * 60, initialDelay = 1)
    public void UpdateHourData()
    {
        UpdateDayData();
    }

    //每N分钟更新数据
    @Scheduled(fixedDelay = 1000 * 60 * 5, initialDelay = 1)
    public void UpdateMinuteData()
    {
        List<String> appList = reportDao.getApps();

        long startTime = System.currentTimeMillis();
        System.out.println("MinuteDataStart: " + new Date(startTime));

        Date dt = new Date(startTime);

        for (int i = 0; i < appList.size(); i++)
        {
            String appId = appList.get(i);

            UpdateUserData(dt, appId, PlatformType.Android);          //Android日报
            UpdateUserData(dt, appId, PlatformType.IOS);              //Ios日报

            UpdateChannelData(dt, appId, PlatformType.Android);       //Android渠道
            UpdateChannelData(dt, appId, PlatformType.IOS);           //IOS渠道

            UpdateChannelLastData(appId, PlatformType.Android);       //Android渠道持续累计数据
            UpdateChannelLastData(appId, PlatformType.IOS);           //IOS渠道持续累计数据

            UpdateRemain(dt, appId, PlatformType.Android);            //Android留存
            UpdateRemain(dt, appId, PlatformType.IOS);                //IOS留存

            UpdateLTV(dt, appId, PlatformType.Android);               //Android更新LTV
            UpdateLTV(dt, appId, PlatformType.IOS);                   //IOS更新LTV

            UpdateROI(dt, appId, PlatformType.Android);               //Android ROI
            UpdateROI(dt, appId, PlatformType.IOS);                   //IOS ROI

            UpdatePayConversion(dt, appId, PlatformType.Android);     //Android付费转化
            UpdatePayConversion(dt, appId, PlatformType.IOS);         //IOS付费转化
        }

        long endTime = System.currentTimeMillis();

        System.out.println("MinuteDataEnd: " + new Date(endTime));
        System.out.println("MinuteDataTotalTime: " + (float)(endTime - startTime)/1000);

        System.out.println("MinuteReport_OK");
    }

    //更新测试
    //@Scheduled(fixedRate = 30000)         //30秒测试
    //@Scheduled(cron = "0 19 20 ? * *")    //每天20点19执行一次
    //@Scheduled(fixedDelay = 1000 * 60 * 5, initialDelay = 1)
    public void UpdateTest()
    {
        List<String> appList = reportDao.getApps();

        long startTime = System.currentTimeMillis();
        System.out.println("start: " + startTime);

        //更新当天的数据 加for循环主要是用于测试
//        for (int day = -33; day <= 0; day++)
//        {
//
//            Date dt = new Date(System.currentTimeMillis() + dayTime * day);
//            for (int i = 0; i < appList.size(); i++)
//            {
//                String appId = appList.get(i);
////                System.out.println("appId: " + appId);
//                UpdateUserData(dt, appId, PlatformType.Android);          //Android日报
//                UpdateUserData(dt, appId, PlatformType.IOS);              //Ios日报
//
//                UpdateChannelData(dt, appId, PlatformType.Android);       //Android渠道
//                UpdateChannelData(dt, appId, PlatformType.IOS);           //IOS渠道
//
//                UpdateChannelLastData(appId, PlatformType.Android);       //Android渠道持续累计数据
//                UpdateChannelLastData(appId, PlatformType.IOS);           //IOS渠道持续累计数据
//
//                UpdateRemain(dt, appId, PlatformType.Android);            //Android留存
//                UpdateRemain(dt, appId, PlatformType.IOS);                //IOS留存
//
//                UpdateLTV(dt, appId, PlatformType.Android);               //Android更新LTV
//                UpdateLTV(dt, appId, PlatformType.IOS);                   //IOS更新LTV
//
//                UpdateROI(dt, appId, PlatformType.Android);               //Android ROI
//                UpdateROI(dt, appId, PlatformType.IOS);                   //IOS ROI
//
//                UpdatePayConversion(dt, appId, PlatformType.Android);     //Android付费转化
//                UpdatePayConversion(dt, appId, PlatformType.IOS);         //IOS付费转化
//            }
//        }

        long endTime = System.currentTimeMillis();
        System.out.println("totalTime: " + (float)(endTime - startTime) / 1000);
    }


    /****************************更新数据如下****************************************/

    //日报 - all
    private void UpdateUserData(Date dt, String appId)
    {
        UpdateUserData(dt, appId, PlatformType.ALL);
    }

    //日报 ios/android
    private void UpdateUserData(Date dt, String appId, String platform)
    {
        UserReportModel urm = reportDao.createUserReportData(dt, appId, ChannelType.ALL, platform);
        boolean isHave = reportDao.isHaveUserReportData(dt, appId, platform);

        if (isHave)
            reportDao.updateUserReportData(urm, appId, platform);
        else
            reportDao.saveUserReportData(urm, appId, platform);
    }

    //渠道 - all
    private void UpdateChannelData(Date dt, String appId)
    {
        UpdateUserData(dt, appId, PlatformType.ALL);
    }

    //渠道 - ios/android
    private void UpdateChannelData(Date dt, String appId, String platform)
    {
        List<String> list = reportDao.getChannelListByDate(dt, appId, platform);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < list.size(); i++)
        {
            String channelType = list.get(i);
            if (channelType == null || channelType.isEmpty())
            {
                System.out.println("Channel-ChannelType Unknown!!!");
                continue;
            }

            ChannelReportModel crm = reportDao.createChannelBaseData(dt, appId, channelType, platform);

            boolean isHave = reportDao.isHaveChannelData(appId, dt, channelType, platform);

            if (isHave)
                reportDao.updateChannelBaseData(crm, appId, platform);
            else
                reportDao.saveChannelBaseData(crm, appId, platform);
        }

        long endTime = System.currentTimeMillis();

        System.out.println("ChannelTotalTime: " + (float)(endTime - startTime)/1000);
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
    private void UpdateRemain(Date dt, String appId, String platform)
    {
        DailyModel dailyModel = dailyDao.findMinDateData(appId, platform);

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
            float remainRate = reportDao.getRemainRate(currentDay, days, appId, ChannelType.ALL, platform);

//            if (remainRate > 0)
//                System.out.println("raminRate: " + currentDay + " days" + days + "remainRate:" + remainRate);

            //每日留存
            int dnu = reportDao.getDnu(currentDay, appId, ChannelType.ALL, platform);
            reportDao.updateRemainRate(currentDay, days, remainRate, dnu, appId, platform);

            if (days == 2 || days == 3 || days == 7 || days == 30)
            {
                //日报留存
                if (remainRate > 0)
                    reportDao.updateUserReportRemainRate(currentDay, days, remainRate, appId, platform);

                //渠道留存(当日所有)
                if (remainRate > 0)
                    reportDao.UpdateAllChannelRemain(appId, currentDay, days, platform);
            }
        }
    }

    private void UpdateChannelLastData(String appId)
    {
        UpdateChannelLastData(appId, PlatformType.ALL);
    }

    //更新渠道持续数据（涉及持续收入带来的变化数据 如cpi, roi, 付费, 付费率等)
    private void UpdateChannelLastData(String appId, String platform)
    {
        reportDao.UpdateChannelLastData(appId, platform);
    }

    //更新N日LTV
    private void UpdateLTV(Date dt, String appId, String platform)
    {
        DailyModel dailyModel = dailyDao.findMinDateData(appId, platform);

        if (dailyModel == null)
            return;

        long startTime = dailyModel.getLoginTime().getTime() / dayTime * dayTime;

        long currentTime = dt.getTime() / dayTime * dayTime;

        for (long time = startTime; time <= currentTime; time+=dayTime)
        {
            int days = (int) ((currentTime - time) / dayTime);

            Date currentDay = new Date(time);

            float ltv = reportDao.getLTV(currentDay, days, appId, ChannelType.ALL, platform);

            float cpi = reportDao.getCpi(currentDay, appId, ChannelType.ALL, platform);

            reportDao.updateLTV(currentDay, days+1, ltv, cpi, appId, platform);
        }
    }

    //更新N日ROI
    private void UpdateROI(Date dt, String appId, String platform)
    {
        DailyModel dailyModel = dailyDao.findMinDateData(appId, platform);

        if (dailyModel == null)
            return;

        long startTime = dailyModel.getLoginTime().getTime() / dayTime * dayTime;

        long currentTime = dt.getTime() / dayTime * dayTime;

        for (long time = startTime; time <= currentTime; time+=dayTime)
        {
            int days = (int) ((currentTime - time) / dayTime);

            Date currentDay = new Date(time);

            float grossIncome = reportDao.getGrossIncome(currentDay, days, appId, ChannelType.ALL, platform);

            float cost = reportDao.getCostMoney(currentDay, appId, ChannelType.ALL, platform);

            reportDao.updateROI(currentDay, days+1, grossIncome, cost, appId, platform);
        }
    }

    //更新付费转化
    private void UpdatePayConversion(Date dt, String appId, String platform)
    {
        DailyModel dailyModel = dailyDao.findMinDateData(appId, platform);

        if (dailyModel == null)
            return;

        long startTime = dailyModel.getLoginTime().getTime() / dayTime * dayTime;       //统一日期取整（如1-1 08:00:00)

        long currentTime = dt.getTime() / dayTime * dayTime;

        for (long time = startTime; time <= currentTime; time+=dayTime)
        {
            int days = (int) ((currentTime - time) / dayTime);

            Date currentDay = new Date(time);

            int dnu = reportDao.getDnu(currentDay, appId, ChannelType.ALL, platform);
            int dnuPayNum = reportDao.getDnuPayNumByDays(currentDay, days, appId, ChannelType.ALL, platform);
            int dnuPayTimes = reportDao.getDnuPayTimesByDays(currentDay, days, appId, ChannelType.ALL, platform);

            reportDao.updatePayConversion(currentDay, days+1, dnu, dnuPayNum, dnuPayTimes, appId, platform);
        }
    }
}
