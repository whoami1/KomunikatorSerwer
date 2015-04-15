package pl.entity.request;

import java.io.Serializable;

/**
 * Created by Micha� Wrzesie� on 2015-04-14.
 */
public class LoginRequest  implements Serializable {
    private static final long serialVersionUID = 1;

    private String login;
    private String password;


    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
