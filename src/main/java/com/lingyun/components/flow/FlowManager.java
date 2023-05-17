package com.lingyun.components.flow;

import com.lingyun.components.common.BizIdentity;
import com.lingyun.components.plugin.PluginManager;
import com.lingyun.components.plugin.SpiPlugin;
import com.lingyun.components.spi.ContextUtil;
import com.lingyun.components.spi.SpiBase;
import com.lingyun.components.spi.SpiManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务流程管理器，负责业务流程的注册和执行
 *
 * @author caolianghong
 * @date 2022/8/4 4:04 下午
 */
public class FlowManager {
    private static final Map<String, BizFlowGroup<?, ? extends BizIdentity>> BIZ_FLOW_GROUP_MAP = new HashMap<>();

    public static <T, I extends BizIdentity> void register(FlowTemplate<T, I> flowTemplate) {
        BizFlowGroup<T, I> bizFlowGroup = getBizFlowGroup(flowTemplate.getBizName());
        if (bizFlowGroup == null) {
            bizFlowGroup = new BizFlowGroup<>(flowTemplate);
            BIZ_FLOW_GROUP_MAP.put(flowTemplate.getBizName(), bizFlowGroup);
        } else {
            //业务防重
            if (bizFlowGroup.contains(flowTemplate.getBizIdentity())) {
                throw new IllegalStateException(String.format("duplicated flow: (%s, %s)", flowTemplate.getBizName(), flowTemplate.getBizIdentity()));
            }
        }

        bizFlowGroup.add(flowTemplate);
    }

    /**
     * 执行流程
     * @param bizName 业务名称
     * @param bizIdentity 业务身份
     * @param bizContext 上下文对象
     * @param <T> 上下文对象类型
     */
    public static <T, I extends BizIdentity> void execute(String bizName, I bizIdentity, T bizContext) {
        if (bizIdentity == null) {
            throw new IllegalArgumentException("bizIdentity is null!");
        }

        BizFlowGroup<T, I> bizFlowGroup = getBizFlowGroup(bizName);
        if (bizFlowGroup == null) {
            throw new IllegalStateException("none flow exists for " + bizName);
        }

        FlowTemplate<T, I> flowTemplate = bizFlowGroup.get(bizIdentity);
        try {
            doExecuteFlow(flowTemplate, bizIdentity, bizContext);
        } finally {
            ContextUtil.clear();
        }
    }

    /**
     * 校验流程合法性
     */
    public static void validateFlows() {
        BIZ_FLOW_GROUP_MAP.values().forEach(BizFlowGroup::validate);
    }

    private static <T, I extends BizIdentity> void doExecuteFlow(FlowTemplate<T, I> flowTemplate, I bizIdentity, T bizContext) {
        for (Class<? extends SpiBase<T, I>> spiBaseClass : flowTemplate.getSpiBaseList()) {
            SpiBase<T, I> spiInstance = SpiManager.get(spiBaseClass, flowTemplate.getBizName(), bizIdentity);
            if (spiInstance == null) {
                throw new IllegalStateException("spiInstance not found for SPI interface:" + spiBaseClass.getName());
            }

            if (ContextUtil.isFinished()) {
                break;
            }

            //查询为该SPI注册的插件
            List<SpiPlugin<T>> pluginList = PluginManager.get(spiBaseClass);

            //执行插件before方法
            executeBeforeSpi(pluginList, bizContext);

            //执行SPI实现
            spiInstance.execute(bizContext);

            //执行插件after方法
            executeAfterSpi(pluginList, bizContext);
        }
    }

    private static <T> void executeBeforeSpi(List<SpiPlugin<T>> pluginList, T bizContext) {
        if (pluginList.isEmpty()) {
            return;
        }

        pluginList.forEach(plugin -> plugin.before(bizContext));
    }

    private static <T> void executeAfterSpi(List<SpiPlugin<T>> pluginList, T bizContext) {
        if (pluginList.isEmpty()) {
            return;
        }

        pluginList.forEach(plugin -> plugin.after(bizContext));
    }

    @SuppressWarnings("unchecked")
    private static <T, I extends BizIdentity> BizFlowGroup<T, I> getBizFlowGroup(String bizName) {
        return (BizFlowGroup<T, I>)BIZ_FLOW_GROUP_MAP.get(bizName);
    }
}
