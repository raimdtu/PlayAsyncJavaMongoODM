package asynMongoODM.utils;

import asynMongoODM.exceptions.MongoDbServerNotFoundException;
import asynMongoODM.codecproviders.CustomCodecProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.ServerAddress;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.async.client.MongoClients;
import com.mongodb.client.model.geojson.codecs.GeoJsonCodecProvider;
import com.mongodb.connection.ClusterSettings;
import org.bson.codecs.BsonValueCodecProvider;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.ValueCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import play.Configuration;
import play.Logger;
import play.inject.ApplicationLifecycle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by roshan on 12/03/16.
 */
@Singleton
public class MongoClientInstance {

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    private  MongoClient mongoClient=null;


    private final Configuration configuration;


    private final ApplicationLifecycle lifecycle;


    @Inject
    public MongoClientInstance(Configuration configuration,ApplicationLifecycle lifecycle){
        this.configuration=configuration;
        this.lifecycle=lifecycle;
        setMongoClientInstance();
    }

    public  void setMongoClientInstance() throws MongoDbServerNotFoundException {
            List<String> servers= configuration.getStringList("blogMongoDBServers");
            List<ServerAddress> serverAddresses =new ArrayList<ServerAddress>();
            if(servers==null){
                Logger.error("server address is not specified");
                throw new MongoDbServerNotFoundException("Server Not found Exception");
            }

            for(String string:servers){
                if(string.contains(":")){
                    String[] parts = string.split(":");
                    String host = parts[0];
                    String portString = parts[1];
                    int port =Integer.parseInt(portString);
                    ServerAddress serverAddress=new ServerAddress(host,port);
                    serverAddresses.add(serverAddress);
                }else{
                   ServerAddress serverAddress=new ServerAddress(string);
                   serverAddresses.add(serverAddress);
                }

            }
            ClusterSettings clusterSettings = ClusterSettings.builder().hosts(serverAddresses).build();
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(CodecRegistries.fromProviders
                (Arrays.asList(new CustomCodecProvider(), new ValueCodecProvider(),
                        new DocumentCodecProvider(),
                        new BsonValueCodecProvider(),
                        new GeoJsonCodecProvider())));
        MongoClientSettings settings = MongoClientSettings.builder().clusterSettings(clusterSettings).codecRegistry(codecRegistry).build();
            this.mongoClient = MongoClients.create(settings);
            lifecycle.addStopHook(() -> {
                mongoClient.close();
                return CompletableFuture.completedFuture(null);
            });

    }

}
