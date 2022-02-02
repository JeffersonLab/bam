package org.jlab.bam.presentation.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jlab.bam.persistence.entity.BeamDestination;
import org.jlab.bam.persistence.entity.Staff;

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

    public static String formatStaff(Staff staff) {
        StringBuilder builder = new StringBuilder();

        if (staff != null) {
            builder.append(staff.getLastname());
            builder.append(", ");
            builder.append(staff.getFirstname());
            builder.append(" (");
            builder.append(staff.getUsername());
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
