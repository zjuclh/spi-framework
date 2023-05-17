package com.lingyun.components.plugin;

import com.lingyun.components.common.BizIdentity;
import com.lingyun.components.spi.SpiBase;

/**
 * SPI 插件接口
 * @author caolianghong
 * @date 2022/8/8 7:33 下午
 */
public interface SpiPlugin<T> {
    /**
     * 插件适用的SPI接口
     * @return
     */
    Class<? extends SpiBase<T, ? extends BizIdentity>> getSpiClass();

    /**
     * 在SPI接口执行前执行的逻辑
     *
     * @param context 流程上下文
     */
    void before(T context);

    /**
     * 在SPI接口正常返回后执行的逻辑
     *
     * @param context 流程上线文
     */
    void after(T context);
}
