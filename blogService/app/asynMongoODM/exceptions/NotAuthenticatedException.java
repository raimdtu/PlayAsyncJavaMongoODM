package asynMongoODM.exceptions;

/**
 * Created by roshan on 29/02/16.
 */
public class NotAuthenticatedException extends RuntimeException{

        private static final long serialVersionUID = 42L;

        public NotAuthenticatedException (String s) {
            super(s);
        }


}
