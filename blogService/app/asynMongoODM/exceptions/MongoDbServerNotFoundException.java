package asynMongoODM.exceptions;

/**
 * Created by roshan on 12/03/16.
 */
public class MongoDbServerNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 42L;

    public MongoDbServerNotFoundException(String s) {
        super(s);
    }
}

