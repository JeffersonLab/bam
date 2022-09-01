package org.jlab.bam.presentation.util;

import org.jlab.smoothness.business.service.UserAuthorizationService;
import org.jlab.smoothness.persistence.view.User;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author ryans
 */
public final class BeamAuthFunctions {

    private static final Logger LOGGER = Logger.getLogger(
            BeamAuthFunctions.class.getName());

    private static final List<String> CEBAF_LIST = Arrays.asList("None", "Tune", "CW");
    private static final List<String> LERF_LIST = Arrays.asList("None", "Ceramic Viewer", "Viewer Limited", "High Duty Cycle", "BLM Checkout", "CW");
    private static final List<String> UITF_LIST = Arrays.asList("None", "Viewer Limited", "Tune", "CW");

    private BeamAuthFunctions() {
        // cannot instantiate publicly
    }

    public static User lookupUserByUsername(String username) {
        UserAuthorizationService auth = UserAuthorizationService.getInstance();

        return auth.getUserFromUsername(username);
    }

    public static String formatUsername(String username) {
        User user = lookupUserByUsername(username);

        if(user != null) {
            return formatUser(user);
        } else {
            return username;
        }
    }

    public static String formatUser(User user) {
        StringBuilder builder = new StringBuilder();

        if (user != null) {
            builder.append(user.getLastname());
            builder.append(", ");
            builder.append(user.getFirstname());
            builder.append(" (");
            builder.append(user.getUsername());
            builder.append(")");
        }

        return builder.toString();
    }

    public static List<String> beamModeList(String facility) {
        List<String> modes = null;

        switch(facility){
            case "cebaf":
                modes = CEBAF_LIST;
                break;
            case "lerf":
                modes = LERF_LIST;
                break;
            case "uitf":
                modes = UITF_LIST;
                break;
        }
        return modes;
    }
    
    public static List<String> laseModeList() {
        return Arrays.asList("None", "UV", "IR");
    }    
    
    public static Date now() {
        return new Date();
    }     
    
    public static Date twoDaysFromNow() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 2);
        return cal.getTime();
    }        
}
