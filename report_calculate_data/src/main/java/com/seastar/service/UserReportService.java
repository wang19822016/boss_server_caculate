package com.seastar.service;

import com.seastar.entity.UserReportReq;
import com.seastar.model.UserReportModel;

import java.util.List;

/**
 * Created by e on 2017/2/24.
 */
public interface UserReportService
{
    List<UserReportModel> doGetUserReportList(UserReportReq req);
}
