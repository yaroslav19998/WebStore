package WebStoreGroup.WebStore.Exceptions;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyExistException extends AuthenticationException {
    public UserAlreadyExistException(String message) {
        super(message);
    }

}
