package asynMongoODM.codecs;

import asynMongoODM.utils.*;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by roshan on 19/03/16.
 */
public interface GenericCodec {

    default void writeDocument(BsonWriter writer, Object value, EncoderContext encoderContext){
        IdAndFieldData idAndFieldData=new IdAndFieldData();
        Map<String, String> objectDocumentMapping = new HashMap<String, String>();
        IdData idData=new IdData();
        idAndFieldData.setFieldDocumentMappings(objectDocumentMapping);
        idAndFieldData.setIdData(idData);
        idAndFieldData= ObjectAndDocumentFieldNameMappping.getObjectDocumentFieldMappings(idAndFieldData, getEncoderClass());
        Set<String> objectField = objectDocumentMapping.keySet();
        Iterator<String> objectFieldIterator = objectField.iterator();
        writer.writeStartDocument();
        beforeFields(writer,encoderContext,idData,value);
        while (objectFieldIterator.hasNext()) {
            String fieldName = objectFieldIterator.next();
            String documentName=objectDocumentMapping.get(fieldName);
            Object object = InvokeGetterSetter.invokeGetter(value, fieldName);
            writeValue(writer,encoderContext,object,documentName);
        }
        writer.writeEndDocument();
    }


    default void writeArrayDocument(final BsonWriter writer, final Iterable<Object> list, final EncoderContext encoderContext) {
        writer.writeStartArray();
        for (final Object value : list) {
            writeValue(writer, encoderContext, value,null);
        }
        writer.writeEndArray();
    }


    default void writeValue(final BsonWriter writer, final EncoderContext encoderContext, final Object value,
                            String documentName) {
        if(value==null)
            return;
        if(documentName !=null )
            writer.writeName(documentName);
         if (value instanceof String) {
            writer.writeString((String) value);
        } else if (value instanceof Boolean) {
            writer.writeBoolean((Boolean) value);
        } else if (Iterable.class.isAssignableFrom(value.getClass())) {
            writeArrayDocument(writer, (Iterable<Object>) value, encoderContext.getChildContext());
        } else if (value instanceof Integer) {
            writer.writeInt32((Integer) value);
        } else if (value instanceof Long) {
            writer.writeInt64((Long) value);
        } else if (value instanceof ObjectId) {
            writer.writeObjectId((ObjectId) value);
        } else if (value instanceof Date) {
            writer.writeDateTime(((Date) value).getTime());
        } else {
            Codec codec = getCodecRegistry().get(value.getClass());
            encoderContext.encodeWithChildContext(codec, writer, value);
        }
    }

    default Object readDocument(BsonReader reader, DecoderContext decoderContext, Object object) {
        Map<String, FieldData> objectDocumentMapping = new HashMap<String, FieldData>();
        objectDocumentMapping=ObjectAndDocumentFieldNameMappping.getDocumentObjectFieldMappings(objectDocumentMapping, getEncoderClass());
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String documentName = reader.readName();
            FieldData fieldData = objectDocumentMapping.get(documentName);
            if(fieldData==null){
                reader.skipValue();
                continue;
            }
            String fieldName = fieldData.getFieldName();
            Object value = readValue(reader, decoderContext, fieldData);
            InvokeGetterSetter.invokeSetter(object, fieldName, value);
        }
        reader.readEndDocument();
        return object;
    }

    default Object readValue(BsonReader reader, DecoderContext decoderContext, FieldData fieldData) {
        switch (reader.getCurrentBsonType()) {
            case INT32:
                Integer intValue = reader.readInt32();
                return intValue;
            case INT64:
                Long longValue = reader.readInt64();
                return longValue;
            case STRING:
                String stringValue = reader.readString();
                return stringValue;
            case OBJECT_ID:
                ObjectId objectId = reader.readObjectId();
                return objectId;
            case BOOLEAN:
                Boolean booleanValue = reader.readBoolean();
                return booleanValue;
            case DATE_TIME:
                Date dateValue = new Date(reader.readDateTime());
                return dateValue;
            case DOCUMENT:
                Class<?> clazz=null;
                if(fieldData.getEnclosedGenericClass()!=null){
                    clazz=fieldData.getEnclosedGenericClass();
                }else {
                     clazz=fieldData.getField().getType();
                }
                Object embeddedObject = getCodecRegistry().get(clazz).decode(reader, decoderContext);
                return embeddedObject;
            case ARRAY:
                List<Object> embeddedList = readArrayDocument(reader, decoderContext, fieldData);
                return embeddedList;
            case NULL:
                reader.readNull();
                return null;
        }
        reader.skipValue();
        return null;
    }

    default List<Object> readArrayDocument(BsonReader reader, DecoderContext decoderContext, FieldData fieldData) {
        List<Object> listObject = new ArrayList<>();
        reader.readStartArray();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            Object value = readValue(reader, decoderContext, fieldData);
            listObject.add(value);
        }
        reader.readEndArray();
        return listObject;
    }

    Class<?> getEncoderClass();

    CodecRegistry getCodecRegistry();


    default void beforeFields(final BsonWriter bsonWriter, final EncoderContext encoderContext,
                              final IdData idData,Object value) {
         if (encoderContext.isEncodingCollectibleDocument() && idData.getDocumentIdName()!=null) {
             Object object = InvokeGetterSetter.invokeGetter(value, idData.getFieldIdName());
            writeValue(bsonWriter, encoderContext, object,idData.getDocumentIdName());
            }
        }
}
