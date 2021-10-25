package org.jlab.bam.business.session;

import java.math.BigInteger;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jlab.bam.persistence.entity.ResponsibleGroup;
import org.jlab.smoothness.persistence.util.JPAUtil;

/**
 *
 * @author ryans
 */
@Stateless
public class ResponsibleGroupFacade extends AbstractFacade<ResponsibleGroup> {
    @PersistenceContext(unitName = "beam-authorizationPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ResponsibleGroupFacade() {
        super(ResponsibleGroup.class);
    }

    @PermitAll
    public ResponsibleGroup findWithLeaders(BigInteger groupId) {
        ResponsibleGroup group = find(groupId);
        
        if(group != null) {
            JPAUtil.initialize(group.getLeaderWorkgroup().getGroupLeaderList());
        }
        
        return group;
    }
    
}
