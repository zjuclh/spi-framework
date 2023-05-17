package com.lingyun.components.plugin;

import com.lingyun.components.common.BizIdentity;
import com.lingyun.components.spi.SpiBase;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author caolianghong
 * @date 2022/8/8 7:37 下午
 */
public class PluginManager {
    public static final Map<Class<? extends SpiBase<?, ? extends BizIdentity>>, PluginGroup<?>> PLUGINS_MAP = new HashMap<>();

    /**
     * 注册插件
     * @param spiClass
     * @param plugin
     * @param <T>
     * @param <I>
     */
    public static <T, I extends BizIdentity> void add(Class<? extends SpiBase<T, I>> spiClass, SpiPlugin<T> plugin) {
        PluginGroup<T> pluginGroup = getPluginGroup(spiClass);
        if (pluginGroup == null) {
            pluginGroup = new PluginGroup<>(plugin);
            PLUGINS_MAP.put(spiClass, pluginGroup);
        } else {
            pluginGroup.add(plugin);
        }
    }

    public static <T, I extends BizIdentity> List<SpiPlugin<T>> get(Class<? extends SpiBase<T, I>> spiClass) {
        PluginGroup<T> pluginGroup = getPluginGroup(spiClass);
        if (pluginGroup == null) {
            return Collections.emptyList();
        }

        return pluginGroup.getSpiPluginList();
    }

    @SuppressWarnings("unchecked")
    private static <T, I extends BizIdentity> PluginGroup<T> getPluginGroup(Class<? extends SpiBase<T, I>> spiClass) {
        return (PluginGroup<T>)PLUGINS_MAP.get(spiClass);
    }
}
