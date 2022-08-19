package org.jlab.bam.business.session;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jlab.bam.persistence.entity.Workgroup;
import org.jlab.bam.persistence.view.User;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.UserRepresentation;

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
    
}
