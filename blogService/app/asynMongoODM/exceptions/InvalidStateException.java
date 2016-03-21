package asynMongoODM.exceptions;


public class InvalidStateException extends RuntimeException {
	
	private static final long serialVersionUID = 42L;
	
    public InvalidStateException(String s) {
        super(s);
    }
}
