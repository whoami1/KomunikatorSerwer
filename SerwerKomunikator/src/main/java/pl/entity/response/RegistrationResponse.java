package pl.entity.response;

import java.io.Serializable;

/**
 * Created by Micha� Wrzesie� on 2015-04-14.
 */
public class RegistrationResponse implements Serializable {
    private static final long serialVersionUID = 1;
    private boolean succes;

    public RegistrationResponse(boolean succes) {
        this.succes = succes;
    }

    public boolean isSucces() {
        return succes;
    }

    @Override
    public String toString() {
        return "RegistrationResponse{" +
                "succes=" + succes +
                '}';
    }
}
