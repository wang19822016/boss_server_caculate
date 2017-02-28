package com.seastar.entity;

import java.util.Date;

/**
 * Created by e on 2017/2/24.
 */
public class ChannelReportReq extends BaseRequest
{
    public int selectType;      // 0. 单日查询  1. 累计查询
    public Date beginTime;
    public Date endTime;
}
