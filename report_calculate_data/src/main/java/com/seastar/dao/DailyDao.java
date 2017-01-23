package com.seastar.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seastar.model.DailyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by lj on 2017/1/17.
 * 用户每日数据
 */
@Component
public class DailyDao
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    //最早日期数据(当前游戏报表)
    public DailyModel findMinDateData(String appId)
    {
        DailyModel dailyModel = null;

        try
        {
            String tableName = appId + "_" + "daily_data";

            String sql = "select min(loginTime) from " + tableName;

            Map<String,Object> result = jdbcTemplate.queryForMap(sql);

            dailyModel = objectMapper.readValue(objectMapper.writeValueAsString(result), DailyModel.class);
        }
        catch (EmptyResultDataAccessException e)
        {
            return null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return dailyModel;
    }
}