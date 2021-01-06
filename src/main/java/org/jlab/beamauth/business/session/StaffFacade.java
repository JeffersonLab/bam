package org.jlab.beamauth.business.session;

import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.jlab.beamauth.persistence.entity.Staff;

/**
 *
 * @author ryans
 */
@Stateless
public class StaffFacade extends AbstractFacade<Staff> {

    @PersistenceContext(unitName = "beam-authorizationPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StaffFacade() {
        super(Staff.class);
    }

    @PermitAll
    public List<Staff> search(String term, int maxResults) {
        TypedQuery<Staff> q = em.createQuery("select s from Staff s where upper(username) like :term order by username asc", Staff.class);

        q.setParameter("term", term.toUpperCase() + "%");

        return q.setMaxResults(maxResults).getResultList();
    }

    @PermitAll
    public Staff findByUsername(String username) {
        TypedQuery<Staff> q = em.createQuery("select s from Staff s where username = :username", Staff.class);

        q.setParameter("username", username);

        Staff staff = null;

        List<Staff> resultList = q.getResultList();
        
        if (resultList != null && !resultList.isEmpty()) {
            staff = resultList.get(0);
        }

        return staff;
    }
}
