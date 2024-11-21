package com.example.rpc.res;

import com.example.common.Result;
import com.example.rpc.dto.ActivityDto;
import com.example.rpc.req.ActivityReq;


import java.io.Serializable;

public class ActivityRes implements Serializable {
    private Result result;
    private ActivityDto activity;

    public ActivityRes() {
    }
    public ActivityRes(Result result){
        this.result=result;
    }
    public ActivityRes(Result result,ActivityDto activity){
        this.result=result;
        this.activity=activity;
    }
    public Result getResult(){
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public ActivityDto getActivity() {
        return activity;
    }

    public void setActivity(ActivityDto activity) {
        this.activity = activity;
    }
}
