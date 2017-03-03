package com.seastar.controller;

import com.seastar.entity.AdDetailsReq;
import com.seastar.entity.AdDetailsRsp;
import com.seastar.entity.ChannelReportReq;
import com.seastar.entity.UserReportReq;
import com.seastar.model.ChannelReportModel;
import com.seastar.model.UserReportModel;
import com.seastar.service.ChannelReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by e on 2017/2/24.
 */
@RestController
public class ChannelReportController
{
    @Autowired
    private ChannelReportService channelReportService;

    @RequestMapping(value = "/channel_report/day", method = RequestMethod.POST)
    public List<ChannelReportModel> OnGetChannelReportListByDay(@RequestBody ChannelReportReq req, HttpServletRequest request)
    {
        return channelReportService.doGetChannelReportListByDay(req);
    }

    @RequestMapping(value = "/channel_report/range", method = RequestMethod.POST)
    public List<ChannelReportModel> OnGetChannelReportListByRange(@RequestBody ChannelReportReq req, HttpServletRequest request)
    {
        return channelReportService.doGetChannelReportListByRange(req);
    }

    @RequestMapping(value = "/add_ad_details", method = RequestMethod.POST)
    public AdDetailsRsp OnAddAdDetails(@RequestBody AdDetailsReq req, HttpServletRequest request)
    {
        return channelReportService.doAddAdDetails(req);
    }
}
