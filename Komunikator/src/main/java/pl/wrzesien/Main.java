package pl.wrzesien;

import org.hibernate.Hibernate;
import org.hibernate.classic.Session;

/**
 * Created by Michał Wrzesień on 2015-03-28.
 */
public class Main {

    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        User user = new User("testtttt", "pass");
        session.save(user);

        session.getTransaction().commit();


    }
}
