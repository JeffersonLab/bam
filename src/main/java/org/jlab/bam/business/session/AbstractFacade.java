package org.jlab.bam.business.session;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJBAccessException;
import javax.ejb.SessionContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.jlab.bam.persistence.entity.Workgroup;
import org.jlab.bam.persistence.entity.Staff;

/**
 *
 * @author ryans
 */
@DeclareRoles({"oability"})
public abstract class AbstractFacade<T> {
    @Resource
    private SessionContext context;
    
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    @PermitAll
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    @PermitAll
    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    @PermitAll
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    @PermitAll
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    @PermitAll
    public List<T> findAll() {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @PermitAll
    public List<T> findAll(OrderDirective... directives) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        List<Order> orders = new ArrayList<Order>();
        for (OrderDirective ob : directives) {
            Order o;

            Path p = root.get(ob.field);

            if (ob.asc) {
                o = cb.asc(p);
            } else {
                o = cb.desc(p);
            }

            orders.add(o);
        }
        cq.orderBy(orders);
        return getEntityManager().createQuery(cq).getResultList();
    }

    @PermitAll
    public List<T> findRange(int[] range) {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        TypedQuery<T> q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    @PermitAll
    public long count() {
        CriteriaQuery<Long> cq = getEntityManager().getCriteriaBuilder().createQuery(Long.class);
        Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        TypedQuery<Long> q = getEntityManager().createQuery(cq);
        return q.getSingleResult();
    }

    public static class OrderDirective {

        private String field;
        private boolean asc;

        public OrderDirective(String field) {
            this(field, true);
        }

        public OrderDirective(String field, boolean asc) {
            this.field = field;
            this.asc = asc;
        }

        public String getField() {
            return field;
        }

        public boolean isAsc() {
            return asc;
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    protected String checkAuthenticated() {
        String username = context.getCallerPrincipal().getName();
        if (username == null || username.isEmpty() || username.equalsIgnoreCase("ANONYMOUS")) {
            throw new EJBAccessException("You must be authenticated to perform the requested operation");
        } else {
            String[] tokens = username.split(":");
            if(tokens.length > 1) {
                username = tokens[2];
            }
        }
        return username;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    protected void checkAdminOrGroupLeader(String username, Workgroup group) {
        boolean isAdminOrLeader = isAdminOrGroupLeader(username, group);

        if (!isAdminOrLeader) {
            throw new EJBAccessException("You must be an admin or group leader to perform the requested operation");
        }
    }

    protected boolean isAdminOrGroupLeader(String username, Workgroup group) {
        boolean isAdminOrLeader = false;

        boolean isAdmin = context.isCallerInRole("oability");
        if (isAdmin) {
            isAdminOrLeader = true;
        } else {
            List<Staff> leaders = group.getGroupLeaderList();
            boolean isLeader = false;
            for (Staff leader : leaders) {
                if (leader.getUsername().equals(username)) {
                    isLeader = true;
                    break;
                }
            }
            if (isLeader) {
                isAdminOrLeader = true;
            }
        }

        return isAdminOrLeader;
    }

    @PermitAll
    public boolean isAdminOrGroupLeader(String username, BigInteger groupId) {
        if (username == null || groupId == null) {
            return false;
        }

        Workgroup group = getEntityManager().find(Workgroup.class, groupId);

        if(group == null) {
            return false;
        }
        
        return isAdminOrGroupLeader(username, group);
    }    
}
