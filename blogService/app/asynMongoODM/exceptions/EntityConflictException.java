package asynMongoODM.exceptions;

/**
 * Created by roshan on 19/01/16.
 */
public class EntityConflictException extends RuntimeException {

    private static final long serialVersionUID = 42L;

    public EntityConflictException(String s) {
        super(s);
    }
}

