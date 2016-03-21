package asynMongoODM.utils;

import java.lang.reflect.Field;

/**
 * Created by roshan on 19/03/16.
 */
public class FieldData {

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Field getField() {
        return field;
    }
    public void setField(Field field) {
        this.field = field;
    }

    private String fieldName;

    private Field field;

    private Class<?> enclosedGenericClass;

    public Class<?> getEnclosedGenericClass() {
        return enclosedGenericClass;
    }

    public void setEnclosedGenericClass(Class<?> enclosedGenericClass) {
        this.enclosedGenericClass = enclosedGenericClass;
    }
}
