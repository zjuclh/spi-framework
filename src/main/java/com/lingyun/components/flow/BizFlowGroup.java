package com.lingyun.components.flow;

import com.lingyun.components.common.BizIdentity;
import com.lingyun.components.spi.SpiBase;
import com.lingyun.components.spi.SpiManager;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务流程组，代表某个业务的各种流程集合
 *
 * @author caolianghong
 * @date 2022/8/4 4:08 下午
 */
@Data
public class BizFlowGroup<T, I extends BizIdentity> {
    /**
     * 业务名称（比如结算、下单）
     * <p>bizName 和 identity唯一决定一个业务流程</p>
     */
    private String bizName;

    /**
     * 不同业务身份和流程的映射
     */
    private Map<BizIdentity, FlowTemplate<T, I>> flowTemplateMap = new HashMap<>();

    /**
     * 该业务的默认流程
     */
    private FlowTemplate<T, I> defaultFlow;

    public BizFlowGroup(FlowTemplate<T, I> flowTemplate) {
        this.bizName = flowTemplate.getBizName();
        flowTemplateMap.put(flowTemplate.getBizIdentity(), flowTemplate);
    }

    public boolean contains(BizIdentity bizIdentity) {
        return flowTemplateMap.containsKey(bizIdentity);
    }

    public void add(FlowTemplate<T, I> flowTemplate) {
        if (flowTemplate == null) {
            throw new NullPointerException("flowTemplate is null!");
        }

        flowTemplateMap.put(flowTemplate.getBizIdentity(), flowTemplate);

        if (flowTemplate.getBizIdentity().isDefault()) {
            if (defaultFlow != null) {
                throw new IllegalStateException("there are more than one default flow! please check: " + flowTemplate.getBizIdentity().getClass().getName());
            }

            this.defaultFlow = flowTemplate;
        }
    }

    public FlowTemplate<T, I> get(I bizIdentity) {
        FlowTemplate<T, I> flowTemplate = flowTemplateMap.get(bizIdentity);
        return flowTemplate != null ? flowTemplate : defaultFlow;
    }

    public void validate() {
        flowTemplateMap.forEach((bizIdentity, flowTemplate) -> {
            List<Class<? extends SpiBase<T, I>>> spiInstanceList = flowTemplate.getSpiBaseList();
            if (spiInstanceList == null || spiInstanceList.isEmpty()) {
                return;
            }

            for (Class<? extends SpiBase<T, I>> clazz : spiInstanceList) {
                if (SpiManager.get(clazz, flowTemplate.getBizName(), bizIdentity) == null) {
                    throw new IllegalStateException("spiInstance not found for SPI interface:" + clazz.getName());
                }
            }
        });
    }
}
