package com.example.domain.strategy.service.algorithm;

import com.example.domain.strategy.model.vo.AwardRateInfo;

import java.util.List;




public interface IDrawAlgorithm {
    /**
     * IDrawAlgorithm总体抽奖概率/奖品被抽中之后重新分配计算
     * @param strategyId 策略ID
     * @param excludeAwardIds 排除掉已经不能作为抽奖的奖品ID，留给风控和空库存使用
     * @return 中奖结果
     */
    String AllrandomDraw (Long strategyId, List<String> excludeAwardIds);

    /**
     * 程序启动时初始化概率元组，在初始化完成后使用过程中不允许修改
     * <p>
     * 元祖数据作用在于 将百分比内(0.2、0.3、0.5)的数据，转换为一整条数组上分区数据，如下；
     * 0.2 = 0 ~ 0.2
     * 0.3 = 0 + 0.2 ~ 0.2 + 0.3 = 0.2 ~ 0.5
     * 0.5 = 0.5 ~ 1 （计算方式同上）
     * <p>
     * 通过数据拆分为整条后，再根据0-100中各个区间的奖品信息，使用斐波那契散列计算出索引位置，把奖品数据存放到元祖中。比如：
     * <>
     *     1.把0.2转换为20
     *     2.20 对应的斐波那契值哈希值：（20 * HASH_INCREMENT + HASH_INCREMENT）= -1549107828 HASH_INCREMENT = 0x61c88647
     *
     * </>
     *
     * */
    void initRateTuple(Long strategyId, List<AwardRateInfo> awardRateInfoList);

    boolean isExistRateTuple(Long stragetyId);

    String SinglerandomDraw(Long stragetyId, List<String> exculeAwardIds);
}




