/**
 * 
 */
package com.megamus.app.ws.model.response;

/**
 * @author mrens
 *
 */
public class OperationStatusModel {

    private String operationName;

    private String operationStatus;

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

}
