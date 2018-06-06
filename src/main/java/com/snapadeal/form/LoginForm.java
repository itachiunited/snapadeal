package com.snapadeal.form;

import javax.validation.constraints.NotEmpty;

public class LoginForm {
    @NotEmpty(message="Please enter a valid Login")
    private String login;

    @NotEmpty(message = "Please enter a valid Password")
    private String password;


    public String getLogin ( ) {
        return login;
    }

    public void setLogin ( String login ) {
        this.login = login;
    }

    public String getPassword ( ) {
        return password;
    }

    public void setPassword ( String password ) {
        this.password = password;
    }


}
