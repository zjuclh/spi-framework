package com.lingyun.components.flow;

import com.lingyun.components.common.BizIdentity;
import com.lingyun.components.spi.SpiBase;
import com.lingyun.components.util.ReflectionUtils;
import lombok.Data;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 业务流程模板
 *
 * @author caolianghong
 * @date 2022/8/4 11:57 上午
 */
@Data
public class FlowTemplate<T, I extends BizIdentity> {
    /**
     * 业务名称（比如结算、下单）
     * <p>bizName 和 identity唯一决定一个业务流程</p>
     */
    private String bizName;

    /**
     * 业务身份
     */
    private I bizIdentity;

    private Class<?> contextClass;

    private Class<?> identityClass;

    /**
     * 组成该流程的SPI节点列表
     */
    private List<Class<? extends SpiBase<T, I>>> spiBaseList;

    FlowTemplate(String bizName, I identity) {
        if (bizName == null || bizName.isEmpty()) {
            throw new IllegalArgumentException("bizName is empty!");
        }

        if (identity == null) {
            throw new IllegalArgumentException("identity is empty!");
        }

        this.bizName = bizName;
        this.bizIdentity = identity;
    }

    /**
     * 添加SPI节点
     * @param spiClazz SPI接口类
     * @return
     */
    public FlowTemplate<T, I> add(Class<? extends SpiBase<T, I>> spiClazz) {
        if (spiBaseList == null) {
            spiBaseList = new ArrayList<>();
        }

        spiBaseList.add(spiClazz);
        return this;
    }

    /**
     * 注册业务流程
     */
    public void register() {
        validate();

        FlowManager.register(this);
    }

    private void validate() {
        if (spiBaseList == null || spiBaseList.isEmpty()) {
            throw new IllegalStateException(String.format("spi list is empty! bizName:%s, bizIdentity:%s", bizName, bizIdentity));
        }

        //流程中的所有spi接口签名要一致
        validateSpiInterfacesSignature(spiBaseList);

        //校验相同接口重复注册的情况
        Set<Class<? extends SpiBase<?, ? extends BizIdentity>>> spiBaseSet = new HashSet<>(spiBaseList);
        if (spiBaseSet.size() != spiBaseList.size()) {
            throw new IllegalStateException(this.toString() + " has duplicate spi interfaces!");
        }
    }

    /**
     * 校验同一个业务流程中各个SPI节点的签名： 需要拥有相同的泛型参数
     * @param spiBaseList
     */
    private void validateSpiInterfacesSignature(List<Class<? extends SpiBase<T, I>>> spiBaseList) {
        //校验SPI接口是否具有相同的签名（泛型参数)
        Type contextType = null;
        Type bizIdentityType = null;
        for (Class<?> spiBase : spiBaseList) {
            Type spiBaseClass = ReflectionUtils.getGenericInterfaceType(spiBase, SpiBase.class);
            if (spiBaseClass == null) {
                throw new IllegalStateException(String.format("spi instance class:%s do not implement SPIBase", spiBase.getName()));
            }

            Type[] typeParams = ((ParameterizedType)spiBaseClass).getActualTypeArguments();
            if (typeParams.length != 2) {
                throw new IllegalStateException("wrong interface signature!");
            }

            if (contextType == null || bizIdentityType == null) {
                contextType = typeParams[0];
                bizIdentityType = typeParams[1];
            } else if (!contextType.equals(typeParams[0]) || !bizIdentityType.equals(typeParams[1])) {
                throw new IllegalStateException(toString() + " has spi interface with different generic types!");
            }
        }
    }

    @Override
    public String toString() {
        return "FlowTemplate{" +
                "bizName='" + bizName + '\'' +
                ", bizIdentity=" + bizIdentity.bizName() +
                '}';
    }
}
