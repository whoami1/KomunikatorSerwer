package pl.entity.request;

import java.io.Serializable;

/**
 * Created by Micha� Wrzesie� on 2015-04-16.
 */
public class TestowaWiadomoscRequest implements Serializable
{
    private static final long serialVersionUID = 1;
    private String username;
    private String text;

    public TestowaWiadomoscRequest(String username, String text)
    {
        this.username = username;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "TestowaWiadomoscRequest{" +
                "username='" + username + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
