package com.lingyun.components.spring.biz.pos;

import com.lingyun.components.spring.biz.SettleBizIdentity;
import com.lingyun.components.spring.biz.SettleContext;
import com.lingyun.components.spring.biz.core.spi.CalcPrice;
import org.springframework.stereotype.Component;

/**
 * @author caolianghong
 * @date 2022/8/11 3:40 下午
 */
@Component
public class PosCalcPrice implements CalcPrice {
    @Override
    public SettleBizIdentity identity() {
        return SettleBizIdentity.POS;
    }

    @Override
    public void execute(SettleContext bizContext) {
        System.out.println("|--PosCalcPrice success");
    }
}
