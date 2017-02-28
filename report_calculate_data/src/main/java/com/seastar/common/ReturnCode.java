package com.seastar.common;

/**
 * Created by e on 2017/1/15.
 */
public class ReturnCode
{
    public final static String CODE_OK = "0";
    public final static String CODE_OK_DESC = "操作成功";

    public final static String CODE_TABLE_INIT_OK = "100";
    public final static String CODE_TABLE_INIT_OK_DESC = "操作成功";

    public final static String CODE_DEVICE_Null = "11";
    public final static String CODE_DEVICE_Null_DESC = "设备ID为空";

    public final static String CODE_DEVICE_FIND = "12";
    public final static String CODE_DEVICE_FIND_DESC = "设备已存在";

    public final static String CODE_USERID_ERROR = "20";
    public final static String CODE_USERID_ERROR_DESC = "用户ID不合法";

    public final static String CODE_USER_Null = "21";
    public final static String CODE_USER_Null_DESC = "用户不存在";

    public final static String CODE_USER_FIND = "22";
    public final static String CODE_USER_FIND_DESC = "用户已存在";

    public final static String CODE_USER_ONLINE_ERROR = "30";
    public final static String CODE_USER_ONLINE_ERROR_DESC = "在线时长边界错误";


}
