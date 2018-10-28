package uniask.controller;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import uniask.model.User;

public class UserForm {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min=5, max=25, message="the size of password should in [5,25]")
    private String pwd;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public User toUser() {
        return new User(email, pwd);
    }
}
