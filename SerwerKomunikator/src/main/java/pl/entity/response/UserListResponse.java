package pl.entity.response;

import pl.wrzesien.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Micha� Wrzesie� on 2015-04-14.
 */
public class UserListResponse  implements Serializable {
    private List<User> userList;

    public UserListResponse(List<User> userList) {
        this.userList = userList;
    }

    public List<User> getUserList() {
        return userList;
    }

    @Override
    public String toString() {
        return "UserListResponse{" +
                "userList=" + userList +
                '}';
    }
}
