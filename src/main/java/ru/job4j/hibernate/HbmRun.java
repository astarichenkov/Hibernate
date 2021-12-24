package ru.job4j.hibernate;

import ru.job4j.hibernate.model.Brand;
import ru.job4j.hibernate.model.Model;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRun {

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Model one = Model.of("Gazon");
            Model two = Model.of("Sobol");
            Model three = Model.of("Volga");
            Model four = Model.of("Sadko");
            Model five = Model.of("Gazelle");
            session.save(one);
            session.save(two);
            session.save(three);
            session.save(four);
            session.save(five);

            Brand gaz = Brand.of("GAZ");
            gaz.addModel(session.load(Model.class, one.getId()));
            gaz.addModel(session.load(Model.class, two.getId()));
            gaz.addModel(session.load(Model.class, three.getId()));
            gaz.addModel(session.load(Model.class, four.getId()));
            gaz.addModel(session.load(Model.class, five.getId()));

            session.save(gaz);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}