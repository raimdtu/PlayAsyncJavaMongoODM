# PlayAsyncJavaMongoODM
Java Asyn Mongo driver generic  Object Document Mapper For Play (Including some Mongo Handler operation)

No mongo ODM has developed for java which usage Async Java driver.
Morphia and jongo both use syncronous java driver .

Advantage :

1.Marshalling and unmarshalling is done from directly Bson stream to object or vice-versa (Instead of convert from Bson to string then object)

2.Mapping of document field and Object field using Annotation and Reflection

using  three annotation https://github.com/raimdtu/PlayAsyncJavaMongoODM/tree/master/blogService/app/asynMongoODM/customannotations

a>Id -Mapping b/w document id which is _id and object Id name

b>FieldName - all field name except id mapping of object and document

c>EnclosedGenericClass 

why we need EnclosedGenericClass ?? 

Because as we know in case of collection (ex List<String>) the actual object type is erased at compile time and we don't have information of actual type at runtime so during unmarshalling of bson (binary from of json) we cant read List<User> type of actual Object at runtime through reflection So for that case we need EnclosedGenericClass . 

Example :-
    @EnclosedGenericClass(value = Address.class)
    @FieldName(value = "houseaddress")
    private List<List<Address>> address;
    
    @Id(value = "_id")
    private ObjectId id;
    
3.For each Object class we read mapping of document and object just for first of their usage through reflection and kept it in memory so
we need not read every time

https://github.com/raimdtu/PlayAsyncJavaMongoODM/blob/master/blogService/app/asynMongoODM/utils/ObjectAndDocumentFieldNameMappping.java

https://github.com/raimdtu/PlayAsyncJavaMongoODM/blob/master/blogService/app/asynMongoODM/utils/ObjectAndDocumentFieldNameMappping.java#L32

https://github.com/raimdtu/PlayAsyncJavaMongoODM/blob/master/blogService/app/asynMongoODM/utils/ObjectAndDocumentFieldNameMappping.java#L63

4. To generically set or get property value of object we have used PropertyDescriptor

https://github.com/raimdtu/PlayAsyncJavaMongoODM/blob/master/blogService/app/asynMongoODM/utils/InvokeGetterSetter.java  

5. Generic Custom Mongohandler which will give you result asynchronously

https://github.com/raimdtu/PlayAsyncJavaMongoODM/blob/master/blogService/app/asynMongoODM/utils/MongoHandler.java

6.Define GenericCodec which has default implementation you just need to implement to your ObjectCodec as it  Marshall and unmarshall all object type

https://github.com/raimdtu/PlayAsyncJavaMongoODM/blob/master/blogService/app/asynMongoODM/codecs/GenericCodec.java

If it is top document you just need to implement CollectibleCodec<User>,GenericCodec like

https://github.com/raimdtu/PlayAsyncJavaMongoODM/blob/master/blogService/app/asynMongoODM/codecs/UserCodec.java

As it generate Id if it doesnot exist

for inside document just need to implement Codec<Address>,GenericCodec like
https://github.com/raimdtu/PlayAsyncJavaMongoODM/blob/master/blogService/app/asynMongoODM/codecs/AddressCodec.java

you need to registed your codec to genricCodecprovider by 

        if(clazz== Address.class){
        
            return (Codec<T>) new AddressCodec(registry);
            
        }
        
        
https://github.com/raimdtu/PlayAsyncJavaMongoODM/blob/master/blogService/app/asynMongoODM/codecproviders/CustomCodecProvider.java


MongoClientInstance which is singlton as single mongoClient contain no of connection also

When creating multiple instances: All resource usage limits (max connections, etc) apply per MongoClient instance

https://github.com/raimdtu/PlayAsyncJavaMongoODM/blob/master/blogService/app/asynMongoODM/utils/MongoClientInstance.java


for server cluster you need to just define it in application.conf 
blogMongoDBServers=["localhost:27017"]

MongoClient Connection is closed as application is closed 

lifecycle.addStopHook(() -> {

                mongoClient.close();
                
                return CompletableFuture.completedFuture(null);
                
            });

Also all the codec registry Including our(new CustomCodecProvider()) has been registered at

https://github.com/raimdtu/PlayAsyncJavaMongoODM/blob/master/blogService/app/asynMongoODM/utils/MongoClientInstance.java#L76

Also MongoClientInstance  bind at https://github.com/raimdtu/PlayAsyncJavaMongoODM/blob/master/blogService/app/Module.java#L34

so in your controller you just need to 

@Inject

MongoClientInstance mongoClientInstance;

To map collection "col" of datatbase "testdb" to User object you need to define

MongoCollection<User> collection=
                mongoClientInstance.getMongoClient().getDatabase("testdb").getCollection("col",User.class);
                

Why we need to implement Bson to object in

https://github.com/raimdtu/PlayAsyncJavaMongoODM/blob/master/blogService/app/asynMongoODM/models/User.java#L17 ??

As in java async driver all the filter are type of bson and If you want to use object as a filter .
Like all User whose name is x .

You have two option 
CompletionStage<?> completionStage=mongoHandler.readOneDocument(collection,eq("name","x"));
In this case you need to use document name instead of object name .

other option is
User user1=new User();
user1.setName("x");
CompletionStage<?> completionStage=mongoHandler.readDocuments(collection, user1);
In this case you need to just deal with object which is again awesome thing.

Improvement :-
All other MongoOperation need to be written in Mongohandler .

Caching of frequent document data in memory.

Join of Document at application level.

All other type in generic codec.

more Testing .



If you want to contribute or refactor or optimized or find Bug or use it you are most welcomed .
