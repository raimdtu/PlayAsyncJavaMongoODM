package asynMongoODM.codecproviders;

import asynMongoODM.codecs.UserCodec;
import asynMongoODM.models.Address;
import asynMongoODM.codecs.AddressCodec;
import asynMongoODM.models.User;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * Created by roshan on 16/03/16.
 */
public class CustomCodecProvider implements CodecProvider {



    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if(clazz==User.class){
            return (Codec<T>) new UserCodec(registry);
        }else if(clazz== Address.class){
            return (Codec<T>) new AddressCodec(registry);
        }
        return  null;
    }
}
