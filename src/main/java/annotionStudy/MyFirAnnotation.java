package annotionStudy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class MyFirAnnotation {
    @myAnnotation
    public void test(){

    }

    @Target(value = {ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface myAnnotation{
        /**
         *
         *          注解的参数：
         *          参数类型 + 参数名() default "";
         *          其中参数名后必须跟()，default值应与前面的类型匹配
         *          而且default的存在并不是必须的
         *          若无default，使用注解时必须添加参数
         *
         *          当参数只有一个是，将其命名为value
         *          可以在使用时直接输入参数，而无需输入
         *          参数名=xxx
         */
        String[] value() default {"cqu", "nju"};
    }
}
