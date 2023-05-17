package com.lingyun.components.spi;

/**
 * 业务流程上下文工具
 *
 * @author caolianghong
 * @date 2022/8/4 11:44 上午
 */
public class ContextUtil {
    private static final ThreadLocal<Boolean> BOOLEAN_THREAD_LOCAL = ThreadLocal.withInitial(() -> false);

    /**
     * 提前结束业务流程
     */
    public static void finish() {
        BOOLEAN_THREAD_LOCAL.set(true);
    }

    /**
     * 判断业务流程是否已结束
     * @return
     */
    public static boolean isFinished() {
        return BOOLEAN_THREAD_LOCAL.get();
    }

    public static void clear() {
        BOOLEAN_THREAD_LOCAL.remove();
    }
}
