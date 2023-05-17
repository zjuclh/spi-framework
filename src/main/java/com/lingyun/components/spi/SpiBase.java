package com.lingyun.components.spi;

import com.lingyun.components.common.BizIdentity;

/**
 * SPI基础接口，标记某个接口代表一个可扩展的业务能力, 所有业务SPI接口都要实现该SPIBase
 *
 * @author caolianghong
 * @date 2022/8/4 10:40 上午
 */
public interface SpiBase<T, I extends BizIdentity> {
    /**
     * 返回业务身份
     *
     * @return
     */
    I identity();

    /**
     * 执行业务逻辑，并决定是否
     *
     * @param bizContext
     * @return
     */
    void execute(T bizContext);

    /**
     * 指定Spi实现类只针对哪个流程生效
     *
     * @return 流程名，如果不需要分区流程，直接返回null
     */
    default String flowName() {
        return null;
    }
}
