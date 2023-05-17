package com.lingyun.components.spring.biz.core.impl;

import com.lingyun.components.spring.biz.SettleBizIdentity;
import com.lingyun.components.spring.biz.SettleContext;
import com.lingyun.components.spring.biz.core.spi.CalcPrice;
import org.springframework.stereotype.Component;

/**
 * @author caolianghong
 * @date 2022/8/11 3:31 下午
 */
@Component
public class DefaultCalcPrice implements CalcPrice {
    @Override
    public SettleBizIdentity identity() {
        return SettleBizIdentity.NORMAL;
    }

    /**
     * 这里实现默认的计价逻辑
     * @param bizContext 上下文数据
     */
    @Override
    public void execute(SettleContext bizContext) {
        System.out.println("|--DefaultCalcPrice success");
    }
}
