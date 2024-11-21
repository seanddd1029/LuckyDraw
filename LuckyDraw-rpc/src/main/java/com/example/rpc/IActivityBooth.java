package com.example.rpc;


import com.example.rpc.req.ActivityReq;
import com.example.rpc.res.ActivityRes;

//定义活动展台的接口类，用于包装活动的创建、查询、修改、审核的接口
/**
 活动展台 创建活动，更新活动，查询活动
 */
public interface IActivityBooth {
    ActivityRes queryActivityById (ActivityReq req);

}