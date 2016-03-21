package asynMongoODM.utils;

import play.Logger;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by roshan on 18/03/16.
 */

public class InvokeGetterSetter {

    public static void invokeSetter(Object obj, String variableName, Object variableValue){
      /* variableValue is Object because value can be an Object, Integer, String, etc... */
        try {
            /**
             * Get object of PropertyDescriptor using variable name and class
             * Note: To use PropertyDescriptor on any field/variable, the field must have both `Setter` and `Getter` method.
             */
            PropertyDescriptor objPropertyDescriptor = new PropertyDescriptor(variableName, obj.getClass());
         /* Set field/variable value using getWriteMethod() */
            objPropertyDescriptor.getWriteMethod().invoke(obj, variableValue);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | IntrospectionException e) {
            Logger.error("Setter has not been associated with "+obj+" with variable "+variableName);
        }
    }

    public static Object invokeGetter(Object obj, String variableName){
        Object variableValue =null;
        try {
            /**
             * Get object of PropertyDescriptor using variable name and class
             * Note: To use PropertyDescriptor on any field/variable, the field must have both `Setter` and `Getter` method.
             */
            PropertyDescriptor objPropertyDescriptor = new PropertyDescriptor(variableName, obj.getClass());
            /**
             * Get field/variable value using getReadMethod()
             * variableValue is Object because value can be an Object, Integer, String, etc...
             */
            variableValue = objPropertyDescriptor.getReadMethod().invoke(obj);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | IntrospectionException e) {
              Logger.error("Getter has not been associated with "+obj+" with variable "+variableName);
        }
        return variableValue;
    }
}
