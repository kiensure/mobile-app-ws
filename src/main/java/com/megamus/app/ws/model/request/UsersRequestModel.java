/**
 * 
 */
package com.megamus.app.ws.model.request;

/**
 * @author mrens
 *
 */
public class UsersRequestModel {

    private String page;

    private String perpage;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPerpage() {
        return perpage;
    }

    public void setPerpage(String perpage) {
        this.perpage = perpage;
    }

}
