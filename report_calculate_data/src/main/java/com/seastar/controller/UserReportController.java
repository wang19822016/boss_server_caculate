package com.seastar.controller;

import com.seastar.entity.UserReportReq;
import com.seastar.model.UserReportModel;
import com.seastar.service.UserReportService;
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
public class UserReportController
{
    @Autowired
    private UserReportService userReportService;

//    @RequestMapping(value = "/user_report", method = RequestMethod.POST)
//    public List<UserReportModel> OnGetUserReportList(@RequestBody UserReportReq req, HttpServletRequest request)
//    {
//        return userReportService.doGetUserReportList(req);
//    }
}
