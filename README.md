## 简介

助力业务应用实现业务主流程和业务细节的解耦和不同业务之间的解耦，提高系统的可理解性、可维护性和可扩展性，提高开发效率。

## 背景

#### 架构是如何腐化的

- 阶段一：单一流程，随着业务迭代，不断加入条件判断、逻辑分支和额外处理逻辑，导致代码逐渐复杂，越来越难以理解，并且不同业务逻辑耦合在一起，变更的影响难以控制。

- 阶段二：某个新的业务需求对原有流程的改动太大，于是选择“另起炉灶”，新开接口，新写一个流程，彻底和原有流程解耦。随着这种需求越来越多，接口也变得越来越多，导致一些其他变更需要同时考虑和修改不同接口，改动量大增，同时也存在遗漏的风险。

#### 常见业务系统的架构痛点

- 业务不清晰，难以理解。 架构的各个层次没有体现出清晰的业务含义，大到一个模块，小到一个方法，经过不断迭代，早已经面目全非，很难看出是做什么的，更难知道为什么这么做。不管是垂直业务主流程，还是某个水平业务规则，都很不直观。
- 业务不收敛，难以维护。某个业务需求的改动散落各处，后续很难维护，且各种业务之间没有解耦，经常相互影响，变更很容易出问题。
- 业务非标准，难以复用。垂直方向上，各个层次（方法、类和模块）上的组件，逐渐加入各种不同的业务逻辑，导致难以复用；水平方向上，各种差异化业务重写同一业务流程，导致烟囱式架构。

## 架构目标

总的来说，就是提升架构和代码的可理解性、可维护性和可扩展性，提升开发效率。具体体现下以下几个方面：

- 架构和代码充体现清晰的业务语义，便于理解和传承业务知识
- 高层业务主流程抽象稳定，和底层业务技术细节解耦
- 不同垂直业务逻辑之间独立、解耦，尽量减少互相影响
- 一些特殊水平业务规则收敛，降低耦合，便于理解

## 基本概念

#### 业务身份

每个需要定制自身业务流程的业务都需要一个明确的业务身份，根据业务身份在运行前就编排好该业务的流程。相比于运行时基于上下文数据动态判断执行分支来说，提前根据业务身份编排流程使得业务更易于理解，也能够更好地实现变更在业务间的隔离。以创建订单这个业务流程来说，有主站C端、Admin后台、一页商店、POS等业务身份。

#### SPI接口

SPI接口抽象了流程中的一个个业务处理节点，代表了可复用的业务能力，比如查询某个资源（比如查询商品信息）、执行某个校验（校验商品和库存）、执行某个处理（扣减资源）等等。利用SPI接口来扩展业务，实现了单一职责原则和开闭原则，有利于系统的理解与维护。

#### 业务流程

流程是通过编排SPI业务节点来创建的，每个业务身份都需要定制自己的流程，运行时通过流程隔离避免不同业务身份之间的相互影响。流程必须有一个明确的业务身份标识，可以看成是SPI节点的容器，类似Pipeline模式。通过这种方式，限制了不能随意在主流程中进行修改，任何一段变更逻辑，必须要先找到合适的地方，在某个具体的SPI实现中进行处理，避免了无序和混乱，确保了主流程的清晰和稳定。

#### 业务插件

在SPI业务节点执行前后，提供hook能力，以便执行一些水平业务逻辑，“横切”所有垂直业务，而不改变流程结构。

采用了SPI业务框架的应用，各个概念之间的关系如下：

![](https://markdownforipic.oss-cn-hangzhou.aliyuncs.com/2022-08-11-145754.jpg)

## 如何使用

添加依赖：

```xml
<dependency>
    <groupId>com.lingyun.components</groupId>
    <artifactId>spi-framework</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```


#### 1、定义业务身份

定义业务枚举，实现BizIdentity接口，如下所示：

```java
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
```

通过isDefault属性指定默认业务身份，以便在执行业务流程和SPI扩展点时，在没有匹配到具体业务身份的实现时，执行默认的实现（默认身份对应的流程，或者某个SPI接口的默认业务身份对应的实现类）

#### 2 定义业务SPI接口和实现

定义扩展自SpiBase<T, I extends BizIdentity> 业务接口，指定上下文类和业务身份类型，比如计价接口：

```java
public interface CalcPrice extends SpiBase<SettleContext,SettleBizIdentity> {
}
```

计价接口的默认实现：

```java
@Component
public class DefaultCalcPrice implements CalcPrice {
    @Override
    public SettleBizIdentity identity() {
      	//在NORMAL这个业务身份下生效
        return SettleBizIdentity.NORMAL;
    }

    /**
     * 这里实现默认的计价逻辑
     * @param bizContext 上下文数据
     */
    @Override
    public void execute(SettleContext bizContext) {
        System.out.println("|--DefaultCalcPrice success");
    }
}
```

#### 3、定义业务流程

实现FlowTemplateRegister接口，在register()方法中，使用FlowCreator定义业务流程。比如定义普通结算页流程：

```java
@Component
public class DefaultFlowTemplateRegister implements FlowTemplateRegister {
    @Override
    public void register() {
        FlowCreator.create("settle", SettleBizIdentity.NORMAL)
                .add(QueryResource.class)
                .add(CheckProduct.class)
                .add(QueryReceiverInfo.class)
                .add(QueryAddressTemplate.class)
                .add(NoProductsProcessor.class)
                .add(CalcPrice.class)
                .add(QueryPayInfo.class)
                .add(CheckUserAction.class)
                .add(UpdateAbandonedOrder.class)
                .add(AssembleResult.class)
                .register();
    }
}
```

#### 4、定义业务插件

实现SpiPlugin接口，比如，针对结算页流程，B2B插件需要额外的商品校验逻辑——校验起批量，可以实现如下的插件：

```java
@Component
public class B2BPluginForSettle implements SpiPlugin<SettleContext> {
    @Override
    public Class<? extends SpiBase<SettleContext, ? extends BizIdentity>> getSpiClass() {
    	//指定需要“横切”的SPI接口，针对所有使用到该SPI接口的流程，都会生效
        return CheckProduct.class;
    }

    @Override
    public void before(SettleContext context) {
    }

    @Override
    public void after(SettleContext context) {
        System.out.println("   |--B2BPluginForSettle checked");
    }
}
```

#### 5、执行指定的业务流程

使用FlowManager.execute()方法执行流程，如下所示，执行普通结算流程和POS结算流程：

```java
@Test
    public void test() {
        SettleContext settleContext = new SettleContext();
        System.out.println("default flow start:");
        FlowManager.execute("settle", SettleBizIdentity.NORMAL, settleContext);

        System.out.println("-------------------------------");

        System.out.println("pos flow start:");
        //POS业务可以传入默认的上下文的扩展类，以便承载扩展数据，支持个性化业务逻辑
        PosSettleContext posSettleContext = new PosSettleContext();
        FlowManager.execute("settle", SettleBizIdentity.POS, posSettleContext);
    }
```

#### 6、其他

- 提前中断流程

流程会依次执行完所有SPI节点，除非某个SPI节点的执行抛出运行时异常，或者使用如下方式中断流程：

```java
ContextUtil.finish()
```

- 如何进行业务扩展

  框架支持三种扩展业务的方式，可以根据新需求的定制化程度，选择不同的扩展方式：

  - 新增插件（定义SpiPlugin）。适用于扩展水平业务规则，实现针对不同业务身份的横切机制，比如B2B、赠品等业务
  - 扩展SPI实现。适用于某个垂直业务身份在整个流程中某个（或者某几个）SPI具有比较大的差异，需要自定义实现，比如订阅电商，计费逻辑自定义
  - 定义业务流程。适用于某个垂直业务身份在主流程上相比默认流程具有较大差异的情况，可以为该业务定制个性化流程。比如POS成单流程，就可以进行自定义，去除一些不需要的流程节点，比如查询地址模板等。

  这三种方式并非互斥，可以叠加使用。

- 不同业务流程复用同一个SPI接口

实现SpiBase接口的flowName()方法，指定某个SPI实现类针对哪个流程生效，比如结算页首屏和结算页详情，会复用一些SPI接口（查询资源、计价等），但是其中某些SPI接口首屏和详情的实现逻辑有差异，因此需要分别定义各自的实现类，比如针对QueryResource这个SPI接口，分别定义了首屏和详情的实现：
```java
@Component
public class DefaultQueryResourceFirstLoad implements QueryResource {
    @Override
    public SettleBizIdentity identity() {
        return SettleBizIdentity.NORMAL;
    }

    @Override
    public String flowName() {
        return "settle_firstLoad";
    }

    @Override
    public void execute(SettleContext bizContext) {
        System.out.println("|--DefaultQueryResource for first load success");
    }
}
```
```java
@Component
public class DefaultQueryResourceDetail implements QueryResource {
    @Override
    public SettleBizIdentity identity() {
        return SettleBizIdentity.NORMAL;
    }

    @Override
    public String flowName() {
        return "settle_detail";
    }

    @Override
    public void execute(SettleContext bizContext) {
        System.out.println("|--DefaultQueryResource for detail success");
    }
}
```
如果某个新的业务身份没有实现自己的QueryResource，那么在执行到QueryResource节点时，具体走上述两个默认实现中的哪个，取决于当前执行的流程是结算首屏还是详情