package twino.test.loan.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class BlacklistDao {

    private final EntityManager entityManager;

    @Autowired
    public BlacklistDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void insert(long personId) {
        Blacklist blacklist = new Blacklist();
        blacklist.setPersonId(personId);
        entityManager.merge(blacklist);
        entityManager.flush();
    }

    public boolean exist(long personId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Blacklist> query = criteriaBuilder.createQuery(Blacklist.class);
        Root<Blacklist> root = query.from(Blacklist.class);
        query.where(criteriaBuilder.equal(root.get("personId"), personId));

        return !entityManager.createQuery(query).getResultList().isEmpty();
    }
}
