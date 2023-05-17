package com.lingyun.components.flow;

import com.lingyun.components.common.BizIdentity;
import com.lingyun.components.spi.SpiBase;

/**
 * 业务流程构造器
 *
 * @author caolianghong
 * @date 2022/8/12 2:08 下午
 */
public class FlowCreator<I extends BizIdentity> {
    private final String bizName;
    private final I bizIdentity;

    private FlowCreator(String bizName, I bizIdentity) {
        this.bizName = bizName;
        this.bizIdentity = bizIdentity;
    }

    /**
     * 创建FlowTemplate构造器
     * @param bizName 业务名称
     * @param bizIdentity 业务身份
     * @param <I> 业务身份Type
     * @return
     */
    public static <I extends BizIdentity> FlowCreator<I> create(String bizName, I bizIdentity) {
        return new FlowCreator<>(bizName, bizIdentity);
    }

    /**
     * 添加SPI节点
     *
     * @param spiClazz SPI接口类
     * @return
     */
    public <T> FlowTemplate<T, I> add(Class<? extends SpiBase<T, I>> spiClazz) {
        FlowTemplate<T, I> flowTemplate = new FlowTemplate<>(bizName, bizIdentity);
        return flowTemplate.add(spiClazz);
    }
}
