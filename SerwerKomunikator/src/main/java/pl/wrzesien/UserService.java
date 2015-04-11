package pl.wrzesien;

import org.hibernate.classic.Session;

import java.util.List;

/**
 * Created by Michał Wrzesień on 2015-03-28.
 */
public class UserService {
    private Session session;

    public UserService() {
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
    }

    public void close() {
        HibernateUtil.getSessionFactory().close();
    }

    public void newUser(String userNick, String userPassword) {
        session.beginTransaction();

        User user = new User(userNick, userPassword);
        session.save(user);

        session.getTransaction().commit();
    }

    private User getUser(Integer userId) {
        session.beginTransaction();

        User result = (User) session.get(User.class, userId);

        session.getTransaction().commit();
        return result;
    }

    private void deleteUser(Integer userId) {
        session.beginTransaction();

        //jeśli będą jakieś relacje, na początek je usunąć
        User user = new User(userId);
        session.delete(user);

        session.getTransaction().commit();
    }

    public List<User> showUsers() {
        session.beginTransaction();

        List<User> result = session.createQuery("FROM User").list();

        session.getTransaction().commit();
        return result;
    }
}
