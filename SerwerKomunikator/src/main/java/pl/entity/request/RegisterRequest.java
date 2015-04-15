package pl.entity.request;

import java.io.Serializable;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-14.
 */
public class RegisterRequest  implements Serializable {
    private static final long serialVersionUID = 1;

    private String login;
    private String password;

    public RegisterRequest(String login, String password) {
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
        return "RegisterRequest{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
