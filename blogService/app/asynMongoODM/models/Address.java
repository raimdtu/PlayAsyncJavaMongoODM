package asynMongoODM.models;

import asynMongoODM.customannotations.FieldName;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

/**
 * Created by roshan on 19/03/16.
 */
public class Address implements Bson{

    @FieldName(value = "phone")
    private String phoneNo;

    @FieldName(value = "ismale")
    private Boolean isMale;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Boolean getIsMale() {
        return isMale;
    }

    public void setIsMale(Boolean isMale) {
        this.isMale = isMale;
    }

    @Override
    public <TDocument> BsonDocument toBsonDocument(Class<TDocument> aClass, CodecRegistry codecRegistry) {
        return new BsonDocumentWrapper<Address>(this, codecRegistry.get(Address.class));
    }
}
