package com.seastar.task;

import com.seastar.dao.ReportDao;
import com.seastar.model.UserReportModel;
import org.springframework.beans.factory.annotation.Autowired;
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

    //更新日报数据
    //@Scheduled(fixedRate = 60000)       //60秒测试
    public void UpdateDailyData()
    {
        for (int i = 0; i < appList.length; i++)
        {
            String appId = appList[i];

            Date dt = new Date();
            UserReportModel urm = reportDao.createUserReportData(dt, appId);
            boolean isHave = reportDao.isHaveUserReportData(dt, appId);

            if (isHave)
                reportDao.updateUserReportData(urm, appId);
            else
                reportDao.saveUserReportData(urm, appId);
        }
    }

    //更新渠道数据
    public void  UpdateChannelData()
    {

    }
}
