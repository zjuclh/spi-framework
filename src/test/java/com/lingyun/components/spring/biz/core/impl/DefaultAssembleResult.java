package com.lingyun.components.spring.biz.core.impl;

import com.lingyun.components.spring.biz.SettleBizIdentity;
import com.lingyun.components.spring.biz.SettleContext;
import com.lingyun.components.spring.biz.core.spi.AssembleResult;
import org.springframework.stereotype.Component;

/**
 * @author caolianghong
 * @date 2022/8/11 3:30 下午
 */
@Component
public class DefaultAssembleResult implements AssembleResult {

    @Override
    public SettleBizIdentity identity() {
        return SettleBizIdentity.NORMAL;
    }

    @Override
    public void execute(SettleContext bizContext) {
        System.out.println("|--DefaultAssembleResult success!");
    }
}
