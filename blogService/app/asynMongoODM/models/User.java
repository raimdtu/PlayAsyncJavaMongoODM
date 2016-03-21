package asynMongoODM.models;

import asynMongoODM.customannotations.EnclosedGenericClass;
import asynMongoODM.customannotations.FieldName;
import asynMongoODM.customannotations.Id;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by roshan on 16/03/16.
 */
public class User implements Bson{

    public User(){

    }

    public  User(ObjectId id,String name,String type){
        this.name=name;
        this.type=type;
        this.id=id;
    }


    @Id(value = "_id")
    private ObjectId id;

    public ObjectId getId() {
        return this.id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId generateId() {
        if (this.id == null) {
            id = new ObjectId();
        }
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @FieldName(value = "name")
    private String name;

    @FieldName(value = "type")
    private String type;

    @FieldName(value = "listcheck")
    private List<String> listCheck;


    public List<String> getListCheck() {
        return listCheck;
    }

    public void setListCheck(List<String> listCheck) {
        this.listCheck = listCheck;
    }

    @EnclosedGenericClass(value = Address.class)
    @FieldName(value = "houseaddress")
    private List<List<Address>> address;

    public List<List<Address>> getAddress() {
        return address;
    }

    public void setAddress(List<List<Address>> address) {
        this.address = address;
    }

    @Override
    public <TDocument> BsonDocument toBsonDocument(Class<TDocument> aClass, CodecRegistry codecRegistry) {
        return new BsonDocumentWrapper<User>(this, codecRegistry.get(User.class));
    }
}
