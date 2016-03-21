package asynMongoODM.customannotations;

/**
 * Created by roshan on 19/03/16.
 */
import java.lang.annotation.*;

/**
 * Created by roshan on 18/03/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
    String value() default "";

}

