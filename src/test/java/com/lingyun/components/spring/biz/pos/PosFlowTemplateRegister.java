package com.lingyun.components.spring.biz.pos;

import com.lingyun.components.flow.FlowCreator;
import com.lingyun.components.spring.FlowTemplateRegister;
import com.lingyun.components.spring.biz.SettleBizIdentity;
import com.duowan.components.spring.biz.core.spi.*;
import com.lingyun.components.spring.biz.core.spi.*;
import org.springframework.stereotype.Component;

/**
 * @author caolianghong
 * @date 2022/8/11 4:18 下午
 */
@Component
public class PosFlowTemplateRegister implements FlowTemplateRegister {
    @Override
    public void register() {
        FlowCreator.create("settle_firstLoad", SettleBizIdentity.POS)
                .add(QueryResource.class)
                .add(CheckProduct.class)
                .add(QueryReceiverInfo.class)
                .add(QueryAddressTemplate.class)
                .add(NoProductsProcessor.class)
                .add(CalcPrice.class)
                .add(QueryPayInfo.class)
                .add(CheckUserAction.class)
                .add(UpdateAbandonedOrder.class)
                .add(AdjustTotalPrice.class)
                .add(AssembleResult.class)
                .register();
    }
}
