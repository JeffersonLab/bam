package org.jlab.bam.business.session;

import java.math.BigInteger;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jlab.bam.persistence.entity.Workgroup;
import org.jlab.smoothness.persistence.util.JPAUtil;

/**
 *
 * @author ryans
 */
@Stateless
public class WorkgroupFacade extends AbstractFacade<Workgroup> {
    @PersistenceContext(unitName = "beam-authorizationPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WorkgroupFacade() {
        super(Workgroup.class);
    }

    @PermitAll
    public Workgroup findWithLeaders(BigInteger groupId) {
        Workgroup group = find(groupId);
        
        if(group != null) {
            JPAUtil.initialize(group.getGroupLeaderList());
        }
        
        return group;
    }
    
}
