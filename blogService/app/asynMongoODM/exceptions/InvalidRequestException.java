package asynMongoODM.exceptions;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class InvalidRequestException extends RuntimeException{
	
	private static final long serialVersionUID = 42L;
	
    private List<String> constraintViolations;

    public static InvalidRequestException fromConstraintViolations(Set constraintViolations){
        ArrayList<String> errorMessages = new ArrayList<String>();
        for (Object constraintViolation : constraintViolations) {
            ConstraintViolation constraintViolation1 = (ConstraintViolation) constraintViolation;
            errorMessages.add(constraintViolation1.getPropertyPath() + " " + constraintViolation1.getMessage());
        }
        return new InvalidRequestException(errorMessages);
    }

    public InvalidRequestException(List<String> constraintViolations) {
        super("The request format is invalid.");
        this.constraintViolations = constraintViolations;
    }

    public InvalidRequestException(final String constraintViolation) {
        super("The request format is invalid.");
        this.constraintViolations = new ArrayList<String>(){{add(constraintViolation);}};
    }

    public InvalidRequestException() {
        super("The request format is invalid.");

    }

    public List<String> getErrorMessages(){
        return constraintViolations;
    }
}
