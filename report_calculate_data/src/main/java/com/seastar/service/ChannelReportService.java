package com.seastar.service;

import com.seastar.common.ReturnCode;
import com.seastar.entity.AdDetailsReq;
import com.seastar.entity.AdDetailsRsp;
import com.seastar.entity.ChannelReportReq;
import com.seastar.model.ChannelReportModel;

import java.util.List;

/**
 * Created by e on 2017/2/24.
 */
public interface ChannelReportService
{
    List<ChannelReportModel> doGetChannelReportList(ChannelReportReq req);
    AdDetailsRsp doAddAdDetails(AdDetailsReq req);
}
