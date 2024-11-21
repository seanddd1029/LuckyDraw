package com.example.rpc.req;

import java.io.Serializable;

/**
 *请求类，专门处理请求相关的数据。
 * 是一个请求对象，包含了客户端向服务端发送请求时所需的数据
 * */
public class ActivityReq implements Serializable {
    private Long activityId;
    public Long getActivityId(){
        return activityId;
    }
    public void setActivityId(Long activityId){
        this.activityId=activityId;
    }
}
