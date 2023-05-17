package com.lingyun.components;

import com.lingyun.components.common.BizIdentity;
import com.lingyun.components.flow.FlowManager;
import com.lingyun.components.flow.FlowCreator;
import com.lingyun.components.plugin.PluginManager;
import com.lingyun.components.plugin.SpiPlugin;
import com.lingyun.components.spi.SpiBase;
import com.lingyun.components.spi.SpiManager;
import org.junit.Test;

/**
 * @author caolianghong
 * @date 2022/8/4 10:53 下午
 */
public class SPITest {
    enum BizType implements BizIdentity {
        TYPE1("type1", true),
        TYPE2("type2", false)
        ;

        BizType(String bizName, boolean isDefault) {
            this.bizName = bizName;
            this.isDefault = isDefault;
        }

        private final String bizName;
        private final boolean isDefault;

        @Override
        public String bizName() {
            return bizName;
        }

        @Override
        public boolean isDefault() {
            return isDefault;
        }
    }

    enum OtherBizType implements BizIdentity {
        TYPE1("type1", true),
        TYPE2("type2", false);

        private final String bizName;
        private final boolean isDefault;

        OtherBizType(String bizName, boolean isDefault) {
            this.bizName = bizName;
            this.isDefault = isDefault;
        }

        @Override
        public String bizName() {
            return null;
        }

        @Override
        public boolean isDefault() {
            return false;
        }
    }

    static class TestContext {}
    static class OtherTestContext {}

    interface SPIDemoInterface extends SpiBase<TestContext, BizType> {}

    interface Action1 extends SPIDemoInterface {}

    interface Action2 extends SPIDemoInterface {}

    interface Action3 extends SPIDemoInterface {}

    interface OtherAction extends SpiBase<TestContext, OtherBizType> {

    }

    static class Type1Action1 implements Action1 {
        @Override
        public void execute(TestContext bizContext) {
            System.out.println("action1 executed for type1");
        }

        @Override
        public BizType identity() {
            return BizType.TYPE1;
        }
    }

    static class Type1Action2 implements Action2 {
        @Override
        public void execute(TestContext bizContext) {
            System.out.println("action2 executed for type1");
        }

        @Override
        public BizType identity() {
            return BizType.TYPE1;
        }
    }

    static class Type1Action3 implements Action3 {
        @Override
        public void execute(TestContext bizContext) {
            System.out.println("action3 executed for type1");
        }

        @Override
        public BizType identity() {
            return BizType.TYPE1;
        }
    }

    static class Type2Action2 implements Action2 {
        @Override
        public void execute(TestContext bizContext) {
            System.out.println("action2 executed for type2");
        }

        @Override
        public BizType identity() {
            return BizType.TYPE2;
        }
    }

    static class TestPlugin implements SpiPlugin<TestContext> {

        @Override
        public Class<? extends SpiBase<TestContext, ? extends BizIdentity>> getSpiClass() {
            return Action3.class;
        }

        @Override
        public void before(TestContext context) {
            System.out.println("TestPlugin for executed before Action2!");
        }

        @Override
        public void after(TestContext context) {
            System.out.println("TestPlugin for executed After Action2!");
        }
    }

    @Test
    public void test() {
        //初始化spi和flow
        SpiManager.register(new Type1Action1());
        SpiManager.register(new Type1Action2());
        SpiManager.register(new Type1Action3());
        SpiManager.register(new Type2Action2());

        //注册插件(针对包含Action2的不同的业务流程都会生效——业务AOP)
        PluginManager.add(Action2.class, new TestPlugin());

        FlowCreator.create("test1", BizType.TYPE1).add(Action1.class).add(Action2.class).add(Action3.class).register();
        FlowCreator.create("test1", BizType.TYPE2).add(Action1.class).add(Action2.class).add(Action3.class).register();

        //执行flow
        TestContext testContext = new TestContext();
        FlowManager.execute("test1", BizType.TYPE1, testContext);
        FlowManager.execute("test1", BizType.TYPE2, testContext);

    }

    @Test(expected = IllegalStateException.class)
    public void testForFlowDuplicateCheck() {
        FlowCreator.create("test1", BizType.TYPE1).add(Action1.class).add(Action1.class).add(Action3.class).register();
    }

    @Test(expected = IllegalStateException.class)
    public void testFlowManagerRegister() {
        SpiManager.register(new Type1Action1());
        SpiManager.register(new Type1Action2());
        SpiManager.register(new Type1Action3());
        SpiManager.register(new Type2Action2());

        FlowCreator.create("test1", BizType.TYPE1).add(Action1.class).add(Action2.class).add(Action3.class).register();
        FlowCreator.create("test1", BizType.TYPE1).add(Action1.class).add(Action2.class).add(Action3.class).register();
    }
}
