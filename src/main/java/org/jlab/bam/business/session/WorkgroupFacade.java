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

import org.jlab.bam.persistence.entity.Staff;
import org.jlab.bam.persistence.entity.Workgroup;
import org.jlab.smoothness.persistence.util.JPAUtil;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
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

    @PermitAll
    public Workgroup findWithLeaders(BigInteger groupId) {
        Workgroup group = find(groupId);

        if(group != null) {
            String roleName = group.getLeaderRoleName();

            String keycloakServiceUrl = System.getenv("KEYCLOAK_SERVICE_URL");

            if(keycloakServiceUrl == null) {
                throw new RuntimeException("KEYCLOAK_SERVICE_URL env required");
            }

            String realm = System.getenv("KEYCLOAK_REALM");

            if(realm == null) {
                throw new RuntimeException("KEYCLOAK_REALM env required");
            }

            String resource = System.getenv("KEYCLOAK_RESOURCE");

            if(resource == null) {
                throw new RuntimeException("KEYCLOAK_RESOURCE env required");
            }

            String secret = System.getenv("KEYCLOAK_SECRET");

            if(secret == null) {
                throw new RuntimeException("KEYCLOAK_SECRET env required");
            }

            Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakServiceUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(resource)
                .clientSecret(secret)
                .build();

            RolesResource roles = keycloak.realm(realm).roles();

            RoleResource roleResource = roles.get(roleName);

            Set<UserRepresentation> members = roleResource.getRoleUserMembers();

            List<Staff> staffList = new ArrayList<>();

            for(UserRepresentation rep: members) {
                Staff staff = new Staff();

                staff.setFirstname(rep.getFirstName());
                staff.setLastname(rep.getLastName());
                staff.setUsername(rep.getUsername());

                staffList.add(staff);
            }

            staffList.sort(Comparator.comparing(Staff::getLastname));

            group.setGroupLeaderList(staffList);
        }
        
        return group;
    }
    
}
