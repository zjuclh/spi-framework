package com.lingyun.components.spring;

import com.lingyun.components.TestApplication;
import com.lingyun.components.flow.FlowManager;
import com.lingyun.components.spring.biz.SettleBizIdentity;
import com.lingyun.components.spring.biz.SettleContext;
import com.lingyun.components.spring.biz.pos.PosSettleContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author caolianghong
 * @date 2022/8/11 2:52 下午
 */
@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringRunner.class)
public class SpiFrameworkSpringTest {
    @Test
    public void test() {
        SettleContext settleContext = new SettleContext();
        System.out.println("default flow start:");
        FlowManager.execute("settle_firstLoad", SettleBizIdentity.NORMAL, settleContext);
        System.out.println("-------------------------------");

        FlowManager.execute("settle_detail", SettleBizIdentity.NORMAL, settleContext);
        System.out.println("-------------------------------");

        System.out.println("pos flow start:");
        //POS业务可以传入默认的上下文的扩展类，以便承载扩展数据，支持个性化业务逻辑
        PosSettleContext posSettleContext = new PosSettleContext();
        //POS没有自定义QueryResource，因此QueryResource走settle_firstLoad对应的默认业务实现
        FlowManager.execute("settle_firstLoad", SettleBizIdentity.POS, posSettleContext);
        System.out.println("-------------------------------");
        //POS没有自定义QueryResource，QueryResource走settle_detail对应的默认业务实现
        FlowManager.execute("settle_detail", SettleBizIdentity.POS, posSettleContext);
    }
}
