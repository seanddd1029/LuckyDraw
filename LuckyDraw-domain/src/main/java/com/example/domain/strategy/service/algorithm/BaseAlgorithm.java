package com.example.domain.strategy.service.algorithm;

import com.example.domain.strategy.model.vo.AwardRateInfo;
import com.example.domain.strategy.service.algorithm.impl.SingleRateRandomAlgorithm;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class BaseAlgorithm implements IDrawAlgorithm{
    // 斐波那契散列增量，逻辑：黄金分割点：(√5 - 1) / 2 = 0.6180339887，Math.pow(2, 32) * 0.6180339887 = 0x61c88647
    private final int HASH_INCREMENT = 0x61c88647;
    private final int RATE_TUPLE_LENGTH = 128;
    //存放散列结果 策略id->奖品id 存储每个策略ID对应的概率范围数组。
    protected Map<Long, String[]> rateTupleMap = new ConcurrentHashMap<>();
    //奖品区间概率值，strategyId -> [awardId->begin、awardId->end] 每个策略 ID 对应的奖品概率信息列表。
    protected Map<Long, List<AwardRateInfo>> SingleawardRateInfoMap = new ConcurrentHashMap<>();

    protected Map<Long, List<AwardRateInfo>> AllawardRateInfoMap= new ConcurrentHashMap<>();
    @Override
    public String AllrandomDraw(Long strategyId, List<String> excludeAwardIds) {
        //总体概率，O(n)
        BigDecimal differenceDenominator = BigDecimal.ZERO;//所有奖品的概率总和
        //排除不在抽奖范围的商品集合
        List<AwardRateInfo> diffenceAwardRateList = new ArrayList<>();
        List<AwardRateInfo> awardRateIntervalValList = AllawardRateInfoMap.get(strategyId);
        for(AwardRateInfo awardRateInfo: awardRateIntervalValList){
            String awardId = awardRateInfo.getAwardId();
            if(excludeAwardIds.contains(awardId)){
                continue;
            }
            diffenceAwardRateList.add(awardRateInfo);
            differenceDenominator=differenceDenominator.add(awardRateInfo.getAwardRate());
        }
        //
        if(diffenceAwardRateList.size()==0) {
            return "";
        }
        if(diffenceAwardRateList.size()==1) {
            return diffenceAwardRateList.get(0).getAwardId();
        }
        //基于概率的随机抽奖算法，通过生成一个随机数，并根据每个奖品的概率来决定最终抽中的奖品
        SecureRandom secureRandom = new SecureRandom();
        int randomVal = secureRandom.nextInt(100)+1;
        //循环抽取奖品
        String awardId="";
        int cousorVal=0; //累计当前奖品的概率
        for(AwardRateInfo awardRateInfo:diffenceAwardRateList){
            int rateVal = awardRateInfo.getAwardRate().divide(differenceDenominator,2,BigDecimal.ROUND_UP).multiply(new BigDecimal(100)).intValue();
            if(randomVal <= (rateVal+cousorVal) ){
                awardId = awardRateInfo.getAwardId();
                break;
            }
            cousorVal +=rateVal;
        }
        return awardId;
    }

    @Override
    public void initRateTuple(Long strategyId, List<AwardRateInfo> awardRateInfoList) {
        //保存奖品概率信息
        SingleawardRateInfoMap.put(strategyId, awardRateInfoList);

        //存储概率范围的数组，数组中的每个元素对应一个奖品id
        String[] rateTuple = rateTupleMap.computeIfAbsent(strategyId, k -> new String[RATE_TUPLE_LENGTH]);
        /**
         * Map.computeIfAbsent方法的作用是：如果指定的键（strategyId）在 rateTupleMap 中不存在，则计算一个新值并将其放入 rateTupleMap 中，然后返回该值。
         * */

        int addVal = 0;
        for (AwardRateInfo awardRateInfo:awardRateInfoList){
            int rateVal = awardRateInfo.getAwardRate().multiply(new BigDecimal(100)).intValue();

            for(int i= addVal+1; i<=(rateVal+addVal);i++){
                rateTuple[hashIdx(i)]=awardRateInfo.getAwardId();
            }
            addVal+=rateVal;
        }
    }

    protected int hashIdx(int val) {
        int hashCode = val * HASH_INCREMENT + HASH_INCREMENT;
        return hashCode & (RATE_TUPLE_LENGTH-1);
    }

    @Override
    public boolean isExistRateTuple(Long stragetyId) {
        return rateTupleMap.containsKey(stragetyId);
    }

    @Override
    public String SinglerandomDraw(Long stragetyId, List<String> exculeAwardIds) {
        String[] rateTuple = rateTupleMap.get(stragetyId);
        assert rateTuple != null;

        int randomVal = new SecureRandom().nextInt(100)+1;
        int idx = hashIdx(randomVal);

        String awardId = rateTuple[idx];
        if(exculeAwardIds.contains(awardId)){
            return "未中奖";
        }
        return awardId;
    }
}
