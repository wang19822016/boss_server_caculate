package com.seastar.service;

import com.seastar.common.ReturnCode;
import com.seastar.dao.ReportDao;
import com.seastar.entity.AdDetailsReq;
import com.seastar.entity.AdDetailsRsp;
import com.seastar.entity.ChannelReportReq;
import com.seastar.model.ChannelReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by e on 2017/2/24.
 */
@Service
public class ChannelReportServiceImpl implements ChannelReportService
{
//    @Autowired
//    private ReportDao reportDao;
//
//    //获取渠道报表信息（按天）
//    public List<ChannelReportModel> doGetChannelReportListByDay(ChannelReportReq req)
//    {
//        return reportDao.getChannelReportListByDay(req.appId, req.beginTime, req.endTime);
//    }
//
//    //获取渠道报表信息（按范围）
//    public List<ChannelReportModel> doGetChannelReportListByRange(ChannelReportReq req)
//    {
//        return reportDao.getChannelReportListByRange(req.appId, req.beginTime, req.endTime);
//    }
//
//    //手动添加广告内容(展示数，点击数，花费)
//    public AdDetailsRsp doAddAdDetails(AdDetailsReq req)
//    {
//        if (reportDao.isHaveChannelReportData(req.appId, req.date, req.channelType))
//        {
//            reportDao.updateAdDetails(req.appId, req.date, req.channelType, req.showNum, req.clickNum, req.costMoney);
//        }
//        else
//        {
//            reportDao.addAdDetails(req.appId, req.date, req.channelType, req.showNum, req.clickNum, req.costMoney);
//        }
//
//        AdDetailsRsp rsp = new AdDetailsRsp();
//        rsp.code = ReturnCode.CODE_OK;
//        rsp.codeDesc = ReturnCode.CODE_OK_DESC;
//        return rsp;
//    }
}
