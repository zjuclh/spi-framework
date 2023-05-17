package com.lingyun.components.spring.biz.core;

import com.lingyun.components.flow.FlowCreator;
import com.lingyun.components.spring.FlowTemplateRegister;
import com.lingyun.components.spring.biz.SettleBizIdentity;
import com.duowan.components.spring.biz.core.spi.*;
import com.lingyun.components.spring.biz.core.spi.*;
import org.springframework.stereotype.Component;

/**
 * @author caolianghong
 * @date 2022/8/11 4:09 下午
 */
@Component
public class DefaultFlowTemplateRegister implements FlowTemplateRegister {
    @Override
    public void register() {
        FlowCreator.create("settle_firstLoad", SettleBizIdentity.NORMAL)
                .add(QueryResource.class)
                .add(CheckProduct.class)
                .add(QueryReceiverInfo.class)
                .add(QueryAddressTemplate.class)
                .add(NoProductsProcessor.class)
                .add(CalcPrice.class)
                .add(QueryPayInfo.class)
                .add(CheckUserAction.class)
                .add(UpdateAbandonedOrder.class)
                .add(AssembleResult.class)
                .register();

        FlowCreator.create("settle_detail", SettleBizIdentity.NORMAL)
                .add(QueryResource.class)
                .add(CheckProduct.class)
                .add(QueryReceiverInfo.class)
                .add(QueryAddressTemplate.class)
                .add(NoProductsProcessor.class)
                .add(CalcPrice.class)
                .add(QueryPayInfo.class)
                .add(CheckUserAction.class)
                .add(UpdateAbandonedOrder.class)
                .add(AssembleResult.class)
                .register();
    }
}
