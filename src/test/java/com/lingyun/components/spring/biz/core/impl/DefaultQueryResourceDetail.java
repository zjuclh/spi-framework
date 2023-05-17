package com.lingyun.components.spring.biz.core.impl;

import com.lingyun.components.spring.biz.SettleBizIdentity;
import com.lingyun.components.spring.biz.SettleContext;
import com.lingyun.components.spring.biz.core.spi.QueryResource;
import org.springframework.stereotype.Component;

/**
 * @author caolianghong
 * @date 2022/8/28 9:18 下午
 */
@Component
public class DefaultQueryResourceDetail implements QueryResource {
    @Override
    public SettleBizIdentity identity() {
        return SettleBizIdentity.NORMAL;
    }

    @Override
    public void execute(SettleContext bizContext) {
        System.out.println("|--DefaultQueryResource for detail success");
    }

    @Override
    public String flowName() {
        return "settle_detail";
    }
}
