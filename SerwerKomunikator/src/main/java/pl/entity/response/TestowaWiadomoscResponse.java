package pl.entity.response;

import java.io.Serializable;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-16.
 */
public class TestowaWiadomoscResponse implements Serializable {
    private static final long serialVersionUID = 1;
    private boolean succes;

    public TestowaWiadomoscResponse(boolean succes) {

        this.succes = succes;
    }

    public boolean isSucces() {
        return succes;
    }

    @Override
    public String toString() {
        return "TestowaWiadomoscResponse{" +
                "succes=" + succes +
                '}';
    }

}
