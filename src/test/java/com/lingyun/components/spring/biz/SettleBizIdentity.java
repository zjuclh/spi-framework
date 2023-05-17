package com.lingyun.components.spring.biz;

import com.lingyun.components.common.BizIdentity;

/**
 * @author caolianghong
 * @date 2022/8/11 2:58 下午
 */
public enum SettleBizIdentity implements BizIdentity {
    NORMAL("normal", true),
    FIRST_LOAD("first_load", false),
    ONE_SHOP("one_shop", false),
    POS("pos", false)
    ;
    private final String bizName;
    private final boolean isDefault;

    SettleBizIdentity(String bizName, boolean isDefault) {
        this.bizName = bizName;
        this.isDefault = isDefault;
    }

    @Override
    public String bizName() {
        return bizName;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }
}
