package asynMongoODM.customannotations;

import java.lang.annotation.*;

/**
 * Created by roshan on 18/03/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldName {
        String value() default "";

}
