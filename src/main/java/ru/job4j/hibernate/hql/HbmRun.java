package ru.job4j.hibernate.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Candidate one = Candidate.of("Alex", 2, 2000);
            Candidate two = Candidate.of("Nikolay", 4, 3000);
            Candidate three = Candidate.of("Nikita", 7, 5000);

            session.save(one);
            session.save(two);
            session.save(three);
            session.getTransaction().commit();


            Query query = session.createQuery("from Candidate ");
            for (Object st : query.list()) {
                System.out.println(st);
            }


            query = session.createQuery("from Candidate c where c.id = :fId");
            query.setParameter("fId", 2);
            System.out.println("CandidateById: " + query.uniqueResult());

            query = session.createQuery("from Candidate c where c.name = :fName");
            query.setParameter("fName", "Nikolay");
            System.out.println("CandidateByName: " + query.uniqueResult());


            Transaction txn = session.beginTransaction();
            session.createQuery("update Candidate c set c.experience = :newExp, c.salary = :newSalary where c.id = :fId")
                    .setParameter("newExp", 4)
                    .setParameter("newSalary", 4000)
                    .setParameter("fId", 1)
                    .executeUpdate();
            txn.commit();

            txn = session.beginTransaction();
            session.createQuery("delete from Candidate where id = :fId")
                    .setParameter("fId", 3)
                    .executeUpdate();
            txn.commit();


            txn = session.beginTransaction();
            session.createQuery("insert into Candidate (name, experience, salary) "
                            + "select concat(c.name, 'S'),  c.experience + 1, c.salary + 1000  "
                            + "from Candidate c where c.id = :fId")
                    .setParameter("fId", 2)
                    .executeUpdate();
            txn.commit();

            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}