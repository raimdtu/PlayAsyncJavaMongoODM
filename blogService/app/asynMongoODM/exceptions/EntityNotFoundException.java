package asynMongoODM.exceptions;

public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 42L;
	
    public EntityNotFoundException(String s) {
        super(s);
    }
}
