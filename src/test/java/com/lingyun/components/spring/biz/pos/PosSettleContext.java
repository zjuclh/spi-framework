package com.lingyun.components.spring.biz.pos;

import com.lingyun.components.spring.biz.SettleContext;
import lombok.Data;

/**
 * @author caolianghong
 * @date 2022/8/25 7:06 下午
 */
@Data
public class PosSettleContext extends SettleContext {
    private Long tradeLocationId;
}
