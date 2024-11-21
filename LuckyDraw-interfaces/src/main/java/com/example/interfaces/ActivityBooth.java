package com.example.interfaces;

import com.example.common.Constants;
import com.example.common.Result;
import com.example.infrastucture.dao.IActivityDao;
import com.example.infrastucture.po.Activity;
import com.example.rpc.IActivityBooth;
import com.example.rpc.dto.ActivityDto;
import com.example.rpc.req.ActivityReq;
import com.example.rpc.res.ActivityRes;
import io.netty.util.Constant;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
/**
 * 接口层
 *
 * 接口服务位于用户接口层，用于处理用户发送的Resultful请求，解析用户输入的配置文件等，并将信息传递给应用层
 *
 */
@Service
public class ActivityBooth implements IActivityBooth{

    @Resource //依赖注入，将IActivityDao类型的bean注入到
    private IActivityDao activityDao;

    @Override
    public ActivityRes queryActivityById(ActivityReq req) {
        Activity activity=activityDao.queryActivityById(req.getActivityId());

        ActivityDto activityDto =new ActivityDto();
        activityDto.setActivityId(activity.getActivityId());
        activityDto.setActivityName(activity.getActivityName());
        activityDto.setActivityDesc(activity.getActivityDesc());
        activityDto.setBeginDateTime(activity.getBeginDateTime());
        activityDto.setEndDateTime(activity.getEndDateTime());
        activityDto.setStockCount(activity.getStockCount());
        activityDto.setTakeCount(activity.getTakeCount());

        return new ActivityRes(
                new Result(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo()),activityDto
        );
    }
}
