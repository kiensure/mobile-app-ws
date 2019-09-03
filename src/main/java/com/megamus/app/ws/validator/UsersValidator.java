/**
 * 
 */
package com.megamus.app.ws.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.megamus.app.ws.model.request.UsersRequestModel;

/**
 * @author mrens
 *
 */
public class UsersValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        // TODO Auto-generated method stub
        return UsersRequestModel.class.isAssignableFrom(UsersRequestModel.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        UsersRequestModel requestModel = (UsersRequestModel) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "page", "field.required", requestModel.getClass().getName());
    }

}
