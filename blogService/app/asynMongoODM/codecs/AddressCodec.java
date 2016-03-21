package asynMongoODM.codecs;

import asynMongoODM.models.Address;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * Created by roshan on 19/03/16.
 */
public class AddressCodec implements Codec<Address>,GenericCodec {
    private CodecRegistry codecRegistry;

    public CodecRegistry getCodecRegistry() {
        return codecRegistry;
    }

    public AddressCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public void encode(BsonWriter writer, Address value, EncoderContext encoderContext) {
        writeDocument(writer,value,encoderContext);
    }


    @Override
    public Address decode(BsonReader reader, DecoderContext decoderContext) {
        Address address = new Address();
        address = (Address) readDocument(reader, decoderContext, address);
        return address;
    }

    @Override
    public Class<Address> getEncoderClass() {
        return Address.class;
    }
}
