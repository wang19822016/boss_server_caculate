package com.seastar.service;

import com.seastar.dao.ReportDao;
import com.seastar.entity.UserReportReq;
import com.seastar.model.UserReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by e on 2017/2/24.
 */
@Service
public class UserReportServiceImpl implements UserReportService
{
    @Autowired
    private ReportDao reportDao;

    public List<UserReportModel> doGetUserReportList(UserReportReq req)
    {
        return reportDao.getUserReportList(req.appId, req.beginTime, req.endTime);
    }
}
