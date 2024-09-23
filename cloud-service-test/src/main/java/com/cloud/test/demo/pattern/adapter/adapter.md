# 适配器模式（Adapter Design Pattern）
    适配器就是将不兼容的接口转换为可兼容的接口。分为类适配器和对象适配器。
    类适配器：基于集成
    对象适配器：基于组合

如何选择？
    判断的标准主要有两个，一个是 Adaptee 接口的个数，另一个是 Adaptee 和 ITarget 的契合程度。
    1、如果 Adaptee 接口并不多，那两种实现方式都可以。
    2、如果 Adaptee 接口很多，而且 Adaptee 和 ITarget 接口定义大部分都相同，那我们推荐使用类适配器，
        因为 Adaptor 复用父类 Adaptee 的接口，比起对象适配器的实现方式，Adaptor 的代码量要少一些。
    3、如果 Adaptee 接口很多，而且 Adaptee 和 ITarget 接口定义大部分都不相同，那我们推荐使用对象适配器，因为组合结构相对于继承更加灵活。
 

其中有一个被适配类Adaptee。做适配的时候，先定义一个接口Target（如果Adaptee中有很多方法不需要适配，那么就集成Adaptee,实现接口Target），并且
 Target中方法的命名和不需要适配的方法一致，这样就不需要重写方法了
