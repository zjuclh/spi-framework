package com.lingyun.components.spi;

import com.lingyun.components.common.BizIdentity;
import com.lingyun.components.util.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * SPI 管理器
 * @author caolianghong
 * @date 2022/8/4 4:31 下午
 */
public class SpiManager {
    private static final Map<Class<?>, SpiGroup> SPI_GROUP_MAP = new HashMap<>();

    /**
     * 注册spi接口实例
     *
     * @param spiInstance spi实现类实例
     */
    public static void register(SpiBase<?, ? extends BizIdentity> spiInstance) {
        Class<?> spiInterface = ReflectionUtils.getSpiInterfaceClass(spiInstance.getClass());

        SpiGroup spiGroup = getSpiGroup(spiInterface);
        if (spiGroup == null) {
            spiGroup = new SpiGroup(spiInstance);
            SPI_GROUP_MAP.put(spiInterface, spiGroup);
        } else {
            spiGroup.add(spiInstance);
        }
    }

    public static <T, I extends BizIdentity> SpiBase<T, I> get(Class<? extends SpiBase<T, I>> spiClass, String flowName, BizIdentity bizIdentity) {
        SpiGroup spiGroup = getSpiGroup(spiClass);
        if (spiGroup == null) {
            return null;
        }

        return spiGroup.get(flowName, bizIdentity);
    }

    private static SpiGroup getSpiGroup(Class<?> spiBaseClass) {
        return SPI_GROUP_MAP.get(spiBaseClass);
    }
}
