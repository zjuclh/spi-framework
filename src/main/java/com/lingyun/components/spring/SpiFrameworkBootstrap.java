package com.lingyun.components.spring;

import com.lingyun.components.flow.FlowManager;
import com.lingyun.components.plugin.PluginManager;
import com.lingyun.components.plugin.SpiPlugin;
import com.lingyun.components.spi.SpiBase;
import com.lingyun.components.spi.SpiManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SPI框架启动类
 *
 * @author caolianghong
 * @date 2022/8/10 8:08 下午
 */
public class SpiFrameworkBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);

    @Override
    @SuppressWarnings(value = {"rawtypes", "unchecked"})
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        /*
        1、加载所有SPI实现类，通过SpiBase接口
        2、加载所有Plugin，通过SpiPlugin接口
        3、加载所有FlowTemplate，通过FlowTemplateRegister
        4、执行校验，确保问题在启动时发现
         */

        if (!INITIALIZED.compareAndSet(false, true)) {
            return;
        }

        ApplicationContext applicationContext = event.getApplicationContext();
        Map<String, SpiBase> spiInstanceMap = applicationContext.getBeansOfType(SpiBase.class);
        if (CollectionUtils.isEmpty(spiInstanceMap)) {
            return;
        }

        for (SpiBase spiInstance : spiInstanceMap.values()) {
            SpiManager.register(spiInstance);
        }

        Map<String, SpiPlugin> pluginInstanceMap = applicationContext.getBeansOfType(SpiPlugin.class);
        if (!CollectionUtils.isEmpty(pluginInstanceMap)) {
            for (SpiPlugin pluginInstance : pluginInstanceMap.values()) {
                PluginManager.add(pluginInstance.getSpiClass(), pluginInstance);
            }
        }

        Map<String, FlowTemplateRegister> flowTemplateRegisterMap = applicationContext.getBeansOfType(FlowTemplateRegister.class);
        if (CollectionUtils.isEmpty(flowTemplateRegisterMap)) {
            return;
        }

        for (FlowTemplateRegister flowTemplateRegister : flowTemplateRegisterMap.values()) {
            flowTemplateRegister.register();
        }

        FlowManager.validateFlows();
    }
}
