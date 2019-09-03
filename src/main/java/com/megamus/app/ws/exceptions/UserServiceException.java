/**
 * 
 */
package com.megamus.app.ws.exceptions;

/**
 * @author mrens
 *
 */
public class UserServiceException extends RuntimeException {

    private static final long serialVersionUID = -4549042228374847068L;

    public UserServiceException(String message) {
        super(message);
    }
}
