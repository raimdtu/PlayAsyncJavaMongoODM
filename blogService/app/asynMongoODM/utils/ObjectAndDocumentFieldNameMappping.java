package asynMongoODM.utils;

import asynMongoODM.customannotations.EnclosedGenericClass;
import asynMongoODM.customannotations.FieldName;
import asynMongoODM.customannotations.Id;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by roshan on 18/03/16.
 */
public class ObjectAndDocumentFieldNameMappping {

    private static Map<Class<?>,IdAndFieldData> classFieldMappings=new HashMap<>();
    private static Map<Class<?>,Map<String,FieldData>> classdocumentFieldMappings=new HashMap<>();

    public static List<Field> getObjectFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            fields = getObjectFields(fields, type.getSuperclass());
        }

        return fields;
    }

    public static IdAndFieldData getObjectDocumentFieldMappings(IdAndFieldData idAndFieldData, Class<?> type) {
        if(classFieldMappings.containsKey(type)){
            return classFieldMappings.get(type);
        }
        Map<String,String> fieldsMapping=idAndFieldData.getFieldDocumentMappings();
        IdData idData=idAndFieldData.getIdData();
        Field[] fields=type.getDeclaredFields();
        if(fields==null)
            return null;
        for(Field field : fields){
            Annotation[] annotations=field.getAnnotations();
            if(annotations==null)
                continue;
            String documentName=null;
            String name=field.getName();
            String idDocumentName=null;
            for(Annotation annotation :annotations){
                if(annotation instanceof FieldName){
                    documentName=((FieldName)annotation).value();
                }
                if(annotation instanceof Id){
                    idDocumentName=((Id)annotation).value();
                    idData.setDocumentIdName(idDocumentName);
                    idData.setFieldIdName(name);
                }
            }
            if(documentName!=null)
                fieldsMapping.put(name,documentName);
        }
        if (type.getSuperclass() != null) {
             idAndFieldData= getObjectDocumentFieldMappings(idAndFieldData, type.getSuperclass());
        }
        if(!classFieldMappings.containsKey(type) && ! (type instanceof Object)){
            classFieldMappings.put(type, idAndFieldData);
        }
        return idAndFieldData;
    }
    public static Map<String,FieldData> getDocumentObjectFieldMappings(Map<String,FieldData> fieldsMapping, Class<?> type) {
        if(classdocumentFieldMappings.containsKey(type)){
            return classdocumentFieldMappings.get(type);
        }
        Field[] fields=type.getDeclaredFields();
        if(fields==null)
            return null;
        for(Field field : fields){
            Annotation[] annotations=field.getAnnotations();
            if(annotations==null)
                continue;
            String documentName=null;
            String name=field.getName();
            FieldData fieldData=new FieldData();
            for(Annotation annotation :annotations){
                if(annotation instanceof  FieldName){
                    documentName=((FieldName)annotation).value();
                }
                if(annotation instanceof EnclosedGenericClass){
                   fieldData.setEnclosedGenericClass(((EnclosedGenericClass) annotation).value());
                }
                if(annotation instanceof Id){
                   documentName=((Id)annotation).value();
                }
                fieldData.setFieldName(name);
                fieldData.setField(field);
            }
            if(documentName!=null)
            fieldsMapping.put(documentName,fieldData);
        }
        if (type.getSuperclass() != null) {
            fieldsMapping= getDocumentObjectFieldMappings(fieldsMapping, type.getSuperclass());
        }
        if(!classdocumentFieldMappings.containsKey(type) && ! (type instanceof Object)){
            classdocumentFieldMappings.put(type,fieldsMapping);
        }
        return fieldsMapping;
    }



}
