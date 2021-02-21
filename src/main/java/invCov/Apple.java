package invCov;

public class Apple extends Fruit{
    // 返回值可以协变
    @Override
    public Integer fun(Number o){
        return o.intValue();
    }

    // 返回值不能逆变 报冲突错误
//            @Override
//            public Object fun(Number o){
//                return o;
//            }


    // 参数（输入值）可以协变，
    // 但是被认为是重载，
    // 而非重写，
    // 故不能带@Override
//            @Override
    public Number fun(Integer o){
        return o;
    }

    // 参数（输入值）可以逆变，
    // 但是被认为是重载，
    // 而非重写，
    // 故不能带@Override
//            @Override
    public Number fun(Object o){
        return 0; // 这里看作是赋值的协变，即Number resources = 0;return resources;
    }

    /**
     * 上两种总结起来就是，不能实现参数值的协变与逆变
     * 强行实现，只能算作重载。
     */
}
