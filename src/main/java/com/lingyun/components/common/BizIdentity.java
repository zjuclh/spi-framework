package com.lingyun.components.common;

/**
 * 业务身份接口
 *
 * @author caolianghong
 * @date 2022/8/4 11:59 上午
 */
public interface BizIdentity {
    /**
     * 身份名称
     *
     * @return
     */
    String bizName();

    /**
     * 是否默认业务
     *
     * @return
     */
    boolean isDefault();
}
