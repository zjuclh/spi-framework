package com.lingyun.components.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caolianghong
 * @date 2022/8/8 8:35 下午
 */
class PluginGroup<T> {
    private final List<SpiPlugin<T>> spiPluginList;

    public PluginGroup(SpiPlugin<T> spiPlugin) {
        if (spiPlugin == null) {
            throw new NullPointerException("spiPlugin is null!");
        }

        spiPluginList = new ArrayList<>();
        spiPluginList.add(spiPlugin);
    }

    public void add(SpiPlugin<T> spiPlugin) {
        spiPluginList.add(spiPlugin);
    }

    public List<SpiPlugin<T>> getSpiPluginList() {
        return this.spiPluginList;
    }
}
