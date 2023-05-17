package com.lingyun.components.spi;

import com.lingyun.components.common.BizIdentity;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务SPI分组，维护某个SPI接口的实现集合
 *
 * @author caolianghong
 * @date 2022/8/4 4:31 下午
 */
class SpiGroup {
    private final Map<String, SpiBase<?, ? extends BizIdentity>> spiInstanceMap = new HashMap<>();

    private BizIdentity defaultBizIdentity = null;

    public SpiGroup(SpiBase<?, ? extends BizIdentity> spiInstance) {
        add(spiInstance);
    }

    public void add(SpiBase<?, ? extends BizIdentity> spiInstance) {
        if (spiInstance.identity() == null) {
            throw new IllegalArgumentException(String.format("SPI class:%s not implements identity() method!", spiInstance.getClass().getName()));
        }

        String key = getMapKey(spiInstance);

        if (spiInstanceMap.containsKey(key)) {
            throw new IllegalStateException(String.format("there are more than one spi instance for %s! please check", key));
        }

        spiInstanceMap.put(key, spiInstance);

        if (spiInstance.identity().isDefault()) {
            defaultBizIdentity = spiInstance.identity();
        }
    }

    /**
     * 为某个业务身份查找对应SPI的实现的步骤：
     *     1）根据当前流程名 + 当前身份查找对应实现；
     *     2）直接根据当前身份查找对应实现；
     *     3）查找当前流程名 + 默认身份对应的实现
     *     4）查找默认身份对应的实现
     *
     * @param flowName 流程名
     * @param bizIdentity 业务身份
     * @param <T>
     * @param <I>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T, I extends BizIdentity> SpiBase<T, I> get(String flowName, BizIdentity bizIdentity) {
        String flowKey = flowName + ":" + bizIdentity.bizName();
        SpiBase<?, ? extends BizIdentity> spiInstance = spiInstanceMap.get(flowKey);
        if (spiInstance == null) {
            spiInstance = spiInstanceMap.get(bizIdentity.bizName());
        }
        if (spiInstance != null) {
            return (SpiBase<T, I>)spiInstance;
        }

        if (defaultBizIdentity != null) {
            flowKey = flowName + ":" + defaultBizIdentity.bizName();
            spiInstance = spiInstanceMap.get(flowKey);

            if (spiInstance == null) {
                spiInstance = spiInstanceMap.get(defaultBizIdentity.bizName());
            }
        }

        return (SpiBase<T, I>)spiInstance;
    }

    private String getMapKey(SpiBase<?, ? extends BizIdentity> spiInstance) {
        String flowName = spiInstance.flowName();
        if (flowName == null || flowName.isEmpty()) {
            return spiInstance.identity().bizName();
        }

        return flowName + ":" + spiInstance.identity().bizName();
    }
}
