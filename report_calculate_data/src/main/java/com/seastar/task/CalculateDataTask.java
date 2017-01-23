package com.seastar.task;

import com.seastar.dao.DailyDao;
import com.seastar.dao.ReportDao;
import com.seastar.model.DailyModel;
import com.seastar.model.UserReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by e on 2017/1/19.
 */
@Component
public class CalculateDataTask
{
    @Autowired
    private ReportDao reportDao;

    //report apps
    private String[] appList = new String[]{"1"};

    //每隔XX时间更新新据
    //@Scheduled(fixedRate = 60000)       //60秒测试
    //@Scheduled(cron = "0 */2 * * *")    //每两个小时执行一次
    public void UpdateHourReport()
    {
        Date dt = new Date();

        for (int i = 0; i < appList.length; i++)
        {
            String appId = appList[i];

            UpdateUserData(dt, appId);  //用户综合数据

            //UpdateChannelData(dt, appId);
        }
    }

    //昨日完整数据(每天凌晨6点清算)
    //@Scheduled(cron = "0 6 * * *")
    public void UpdateYestodayReport()
    {
        Date dt = new Date(System.currentTimeMillis() - 24*60*60*1000);

        for (int i = 0; i < appList.length; i++)
        {
            String appId = appList[i];

            UpdateUserData(dt, appId);      //用户综合数据
            UpdateRemain(dt, appId);        //用户留存

            //UpdateChannelData(dt, appId); //渠道综合数据
        }
    }

    @Autowired
    private DailyDao dailyDao;

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
    private void UpdateRemain(Date dt, String appId)
    {
        DailyModel dailyModel = dailyDao.findMinDateData(appId);

        if (dailyModel == null)
            return;

        long startTime = dailyModel.getLoginTime().getTime();
        long currentTime = dt.getTime();
        long dayTime = 24 * 60 * 60 * 1000;

        for (long time = startTime; time < currentTime; time+=dayTime)
        {
            int days = (int)((currentTime - startTime) / dayTime) + 1;

            //大于24小时才会有留存数据(最小如: 1-1 23:59 到1-3 0:01）
            if (days <= 1)
                continue;;

            //以后留存率会有单独报表则按照days存储就可以了（如4日，5日留存等）
            if (days == 2 || days == 3 || days == 7 || days == 30)
            {
                //当天新增用户的N天留存率
                Date currentDay = new Date(time);
                int raminRate = reportDao.getRemainRate(currentDay, days, appId);

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
    private void  UpdateChannelData(Date dt, String appId)
    {

    }
}
