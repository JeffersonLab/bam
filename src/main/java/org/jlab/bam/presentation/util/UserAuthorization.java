package org.jlab.bam.presentation.util;

import org.jlab.bam.persistence.view.User;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

public class UserAuthorization {
    private HashMap<String, List<User>> usersInRole = new HashMap<>();
    private HashMap<String, User> userFromUsername = new HashMap<>();
    private Keycloak keycloak;
    private String realm;
    private String resource;
    private String secret;

    private UserAuthorization() {
        // Private constructor; must use factory method

        String keycloakServiceUrl = System.getenv("KEYCLOAK_SERVICE_URL");

        if(keycloakServiceUrl == null) {
            throw new RuntimeException("KEYCLOAK_SERVICE_URL env required");
        }

       realm = System.getenv("KEYCLOAK_REALM");

        if(realm == null) {
            throw new RuntimeException("KEYCLOAK_REALM env required");
        }

        resource = System.getenv("KEYCLOAK_RESOURCE");

        if(resource == null) {
            throw new RuntimeException("KEYCLOAK_RESOURCE env required");
        }

        secret = System.getenv("KEYCLOAK_SECRET");

        if(secret == null) {
            throw new RuntimeException("KEYCLOAK_SECRET env required");
        }

        keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakServiceUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(resource)
                .clientSecret(secret)
                .build();
    }

    public static UserAuthorization getInstance(HttpServletRequest request) {

        HttpSession session = request.getSession();

        UserAuthorization auth = (UserAuthorization) session.getAttribute("userAuthorization");

        if(auth == null) {
            auth = new UserAuthorization();

            session.setAttribute("userAuthorization", auth);
        }

        return auth;
    }

    public List<User> getUsersInRole(String role) {

        List<User> users = usersInRole.get(role);

        if(users == null) {
            users = lookupUsersInRole(role);

            usersInRole.put(role, users);
        }

        return users;
    }

    public User getUserFromUsername(String username) {

        User user = userFromUsername.get(username);

        if(user == null) {
            user = lookupUserFromUsername(username);

            userFromUsername.put(username, user);
        }

        return user;
    }

    private List<User> lookupUsersInRole(String role) {
        List<User> users = new ArrayList<>();

        RolesResource roles = keycloak.realm(realm).roles();

        RoleResource roleResource = roles.get(role);

        Set<UserRepresentation> members = roleResource.getRoleUserMembers();

        for(UserRepresentation rep: members) {
            User user = new User();

            user.setFirstname(rep.getFirstName());
            user.setLastname(rep.getLastName());
            user.setUsername(rep.getUsername());

            users.add(user);
        }

        users.sort(Comparator.comparing(User::getLastname));

        return users;
    }

    private User lookupUserFromUsername(String username) {
        User user = new User();

        UserResource userResource = keycloak.realm(realm).users().get(username);

        if(userResource != null) {
            UserRepresentation rep = userResource.toRepresentation();
            user.setFirstname(rep.getFirstName());
            user.setLastname(rep.getLastName());
            user.setUsername(rep.getUsername());
        } else {
            System.err.println("No User resource found with ID: " + username);
        }

        return user;
    }
}
