package twino.test.loan.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class LoanDao {

    private final EntityManager entityManager;

    @Autowired
    public LoanDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public List<Loan> getAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Loan> query = criteriaBuilder.createQuery(Loan.class);
        Root<Loan> variableRoot = query.from(Loan.class);
        query.select(variableRoot);
        return entityManager.createQuery(query).getResultList();
    }

    public List<Loan> getLoansForPerson(long personId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Loan> query = criteriaBuilder.createQuery(Loan.class);
        Root<Loan> from = query.from(Loan.class);
        query.where(criteriaBuilder.equal(from.get("personId"), personId));
        return entityManager.createQuery(query).getResultList();
    }

    public void insertLoan(Loan loan) {
        entityManager.merge(loan);
        entityManager.flush();
    }

}
