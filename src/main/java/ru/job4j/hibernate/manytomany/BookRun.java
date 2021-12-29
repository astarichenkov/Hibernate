package ru.job4j.hibernate.manytomany;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class BookRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Book one = Book.of("12 Stulyev");
            Book two = Book.of("Zolotoy Telenok");
            Book three = Book.of("Zapisniye knizhki");
            Book four = Book.of("Mister Uolk");

            Author first = Author.of("Ilya Ilf");
            first.addBook(one);
            first.addBook(two);
            first.addBook(three);

            Author second = Author.of("Evgeniy Petrov");
            second.addBook(one);
            second.addBook(two);
            second.addBook(four);

            session.persist(first);
            session.persist(second);

            Author author = session.get(Author.class, 1);
            session.remove(author);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}