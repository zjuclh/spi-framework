package com.lingyun.components.spring;

/**
 * 业务方需要实现这个接口并声明为spring bean，在register方法里面注册各种业务对应的SPI模板
 *
 * @author caolianghong
 * @date 2022/8/10 8:13 下午
 */
public interface FlowTemplateRegister {
    /**
     * 注册业务流程
     */
    void register();
}
