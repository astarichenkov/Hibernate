package ru.job4j.hibernate.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class VacancyRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();

        Candidate candidate;
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Vacancy one = Vacancy.of("work for profi");
            Vacancy two = Vacancy.of("work for junior");
            session.save(one);
            session.save(two);

            Base base = new Base();
            base.addVacancy(one);
            base.addVacancy(two);

            session.save(base);
            session.save(base);

            Candidate can1 = Candidate.of("Alex", 2, 2000);
            Candidate can2 = Candidate.of("Nikolay", 4, 3000);
            can1.setBase(base);
            can2.setBase(base);

            session.save(can1);
            session.save(can2);
            session.getTransaction().commit();

            candidate = session.createQuery(
                    "select distinct can from Candidate can "
                            + "join fetch can.base b "
                            + "join fetch b.vacancies v "
                            + "where can.id = :cId", Candidate.class
            ).setParameter("cId", 2).uniqueResult();

            System.out.println(candidate);

            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}