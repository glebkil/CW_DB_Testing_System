/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.*;
import java.util.List;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author Gleb
 */
public class Service {

    private static SessionFactory sessionFactory;
    private static ServiceRegistry registry;

//    private static final Byte Normal = 0;
//    private static final Byte Deleted = 1;

    private Service() {
        try {
            Configuration conf = new Configuration().configure();
            registry = new StandardServiceRegistryBuilder().applySettings(
                    conf.getProperties()).build();
            sessionFactory = conf.buildSessionFactory(registry);
        } catch (Throwable ex) {
            System.err.println("Failed to create session factory object");
            throw new ExceptionInInitializerError(ex);
        }
    }

    private final static class SingletonHolder {

        private final static Service instance = new Service();
    }

    public static Service getInstane() {
        return SingletonHolder.instance;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    //do before exit
    public void destroyRegistry() {
        StandardServiceRegistryBuilder.destroy(registry);
        System.out.println("Destroing registry");
    }

    public String AddSubject(String title) throws CustomException{
        Validator.validateSubjectLength(title);
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        String uuid = null;
        try {
            tx = session.beginTransaction();            
            uuid = randomUUID().toString();
            session.save(new Subject(uuid, title));
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } catch (Exception e) {
            System.out.println("super oops...");
            System.out.println(e.getMessage());
        } finally {
            session.close();
            return uuid;
        }
    }
    
    //returns uuid of added user or null if failed
    public String AddUser(Role role, String login, String password, String fullName) throws CustomException {
        Validator.validateFullNameLength(fullName);
        Validator.validateLoginLength(login);
        Validator.validatePasswordLength(password);
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        String uuid = null;
        try {
            tx = session.beginTransaction();            
            uuid = randomUUID().toString();
            session.save(new User(uuid, role, login, password, fullName));
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
        } catch (Exception e) {
            System.out.println("super oops...");
            System.out.println(e.getMessage());
        } finally {
            session.close();
            return uuid;
        }
    }

    public Role getRoleByName(String roleName) throws CustomException {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        Role role = null;
        try {
            //tx = session.beginTransaction();
            Criteria cr = session.createCriteria(Role.class);
            cr.add(Restrictions.ilike("name", roleName));
            List res = cr.list();
            if(res.size() != 0)
            {
                role = (Role) cr.list().get(0);
            }           
            //tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } catch (Exception e) {            
            System.out.println(e.getMessage());            
        } finally {
            session.close();
            return role;
        }
    }

    public List<Subject> getSubjects(){
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        List<Subject> subjects = null;
        try {                    
            Criteria cr = session.createCriteria(Subject.class);
            subjects = cr.list();               
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("super oops...");
            System.out.println(e.getMessage());
        } finally {
            session.close();
            return subjects;
        }
    }
    
    public User getUserByLogin(String login) throws CustomException {        
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        User user = null;
        try {
            //tx = session.beginTransaction();            
            Criteria cr = session.createCriteria(User.class);
            cr.add(Restrictions.like("login", login));
            user = (User) cr.list().get(0);
            //tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("super oops...");
            System.out.println(e.getMessage());
        } finally {
            session.close();
            return user;
        }
    }
}

//            List<User> users = cr.list();
//            for(User usr : (List<User>)users)
//            {
//                System.out.println(usr.getLoginMail());
//            }

/*
Session session = sessionFactory.openSession();
        Transaction tx = null;
       
        try{
            tx = session.beginTransaction();
            //Do stuff
            tx.commit();
        }catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("super oops...");
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
 */
