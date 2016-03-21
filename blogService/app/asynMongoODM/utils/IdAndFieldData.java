package asynMongoODM.utils;

import java.util.Map;

/**
 * Created by roshan on 19/03/16.
 */
public class IdAndFieldData {

    Map<String,String> fieldDocumentMappings;
    IdData idData;

    public Map<String, String> getFieldDocumentMappings() {
        return fieldDocumentMappings;
    }

    public void setFieldDocumentMappings(Map<String, String> fieldDocumentMappings) {
        this.fieldDocumentMappings = fieldDocumentMappings;
    }

    public IdData getIdData() {
        return idData;
    }

    public void setIdData(IdData idData) {
        this.idData = idData;
    }
}
