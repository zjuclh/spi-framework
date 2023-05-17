package com.lingyun.components.spring.biz.core.impl;

import com.lingyun.components.spring.biz.SettleBizIdentity;
import com.lingyun.components.spring.biz.SettleContext;
import com.lingyun.components.spring.biz.core.spi.QueryResource;
import org.springframework.stereotype.Component;

/**
 * @author caolianghong
 * @date 2022/8/11 3:39 下午
 */
@Component
public class DefaultQueryResourceFirstLoad implements QueryResource {
    @Override
    public SettleBizIdentity identity() {
        return SettleBizIdentity.NORMAL;
    }

    @Override
    public String flowName() {
        return "settle_firstLoad";
    }

    @Override
    public void execute(SettleContext bizContext) {
        System.out.println("|--DefaultQueryResource for first load success");
    }
}
