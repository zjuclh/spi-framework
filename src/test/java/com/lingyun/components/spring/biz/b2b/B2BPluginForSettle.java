package com.lingyun.components.spring.biz.b2b;

import com.lingyun.components.common.BizIdentity;
import com.lingyun.components.plugin.SpiPlugin;
import com.lingyun.components.spi.SpiBase;
import com.lingyun.components.spring.biz.SettleContext;
import com.lingyun.components.spring.biz.core.spi.CheckProduct;
import org.springframework.stereotype.Component;

/**
 * @author caolianghong
 * @date 2022/8/11 4:21 下午
 */
@Component
public class B2BPluginForSettle implements SpiPlugin<SettleContext> {
    @Override
    public Class<? extends SpiBase<SettleContext, ? extends BizIdentity>> getSpiClass() {
        return CheckProduct.class;
    }

    @Override
    public void before(SettleContext context) {
    }

    @Override
    public void after(SettleContext context) {
        System.out.println("   |--B2BPluginForSettle checked");
    }
}
