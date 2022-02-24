package org.jlab.bam.business.session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.jlab.bam.business.util.SecurityUtil;
import org.jlab.bam.persistence.entity.Authorization;
import org.jlab.bam.persistence.entity.BeamDestination;
import org.jlab.bam.persistence.entity.DestinationAuthorization;
import org.jlab.bam.persistence.entity.Staff;
import org.jlab.bam.presentation.util.BeamAuthFunctions;
import org.jlab.jlog.Body;
import org.jlab.jlog.Library;
import org.jlab.jlog.LogEntry;
import org.jlab.jlog.LogEntryAdminExtension;
import org.jlab.jlog.exception.AttachmentSizeException;
import org.jlab.jlog.exception.LogCertificateException;
import org.jlab.jlog.exception.LogIOException;
import org.jlab.jlog.exception.LogRuntimeException;
import org.jlab.smoothness.business.exception.UserFriendlyException;
import org.jlab.smoothness.business.service.EmailService;
import org.jlab.smoothness.business.util.IOUtil;

/**
 *
 * @author ryans
 */
@Stateless
@DeclareRoles({"bam-admin"})
public class AuthorizationFacade extends AbstractFacade<Authorization> {

    private static final Logger LOGGER = Logger.getLogger(
            AuthorizationFacade.class.getName());
    @PersistenceContext(unitName = "beam-authorizationPU")
    private EntityManager em;
    @EJB
    StaffFacade staffFacade;
    @EJB
    BeamDestinationFacade destinationFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AuthorizationFacade() {
        super(Authorization.class);
    }

    @SuppressWarnings("unchecked")
    @PermitAll
    public HashMap<BigInteger, String> getUnitsMap() {
        HashMap<BigInteger, String> units = new HashMap<>();

        Query q = em.createNativeQuery("select a.BEAM_DESTINATION_ID, a.CURRENT_LIMIT_UNITS from beam_destination a where a.ACTIVE_YN = 'Y'");

        List<Object[]> results = q.getResultList();

        for(Object[] result: results) {
            Object[] row = result;
            Number id = (Number)row[0];
            String unit = (String)row[1];
            //LOGGER.log(Level.WARNING, "ID: {0}, Unit: {1}", new Object[]{id, unit});
            units.put(BigInteger.valueOf(id.longValue()), unit);
        }

        return units;
    }

    @SuppressWarnings("unchecked")
    @PermitAll
    public Authorization findCurrent() {
        Query q = em.createNativeQuery(
                "select * from (select * from authorization order by modified_date desc) where rownum <= 1",
                Authorization.class);

        List<Authorization> authorizationList = q.getResultList();

        Authorization authorization = null;

        if (authorizationList != null && !authorizationList.isEmpty()) {
            authorization = authorizationList.get(0);
        }

        return authorization;
    }

    @SuppressWarnings("unchecked")
    @PermitAll
    public List<Authorization> findHistory(int offset, int maxPerPage) {
        Query q = em.createNativeQuery(
                "select * from authorization order by authorization_date desc",
                Authorization.class);

        return q.setFirstResult(offset).setMaxResults(maxPerPage).getResultList();
    }

    @PermitAll
    public Long countHistory() {
        TypedQuery<Long> q = em.createQuery("select count(a) from Authorization a", Long.class);
        
        return q.getSingleResult();
    }

    @PermitAll
    public Map<BigInteger, DestinationAuthorization> createDestinationAuthorizationMap(
            Authorization authorization) {
        Map<BigInteger, DestinationAuthorization> destinationAuthorizationMap
                = new HashMap<>();

        if (authorization != null && authorization.getDestinationAuthorizationList() != null) {
            for (DestinationAuthorization destinationAuthorization : authorization.getDestinationAuthorizationList()) {
                destinationAuthorizationMap.put(
                        destinationAuthorization.getDestinationAuthorizationPK().getBeamDestinationId(),
                        destinationAuthorization);
            }
        }

        return destinationAuthorizationMap;
    }

    @RolesAllowed("bam-admin")
    public void saveAuthorization(String comments,
            List<DestinationAuthorization> destinationAuthorizationList) throws UserFriendlyException {
        String username = checkAuthenticated();

        Staff staff = staffFacade.findByUsername(username);

        Authorization authorization = new Authorization();
        authorization.setComments(comments);
        authorization.setAuthorizationDate(new Date());
        authorization.setAuthorizedBy(staff);
        authorization.setModifiedDate(authorization.getAuthorizationDate());
        authorization.setModifiedBy(staff);

        create(authorization);

        for (DestinationAuthorization da : destinationAuthorizationList) {

            BeamDestination destination = destinationFacade.find(
                    da.getDestinationAuthorizationPK().getBeamDestinationId());
            if (!"None".equals(da.getBeamMode())) { // CW or Tune

                // Check if credited control agrees
                if (!(destination.getVerification().getVerificationId() <= 50)) {
                    throw new UserFriendlyException("Beam Destination \"" + destination.getName()
                            + "\" cannot have beam when credited controls are not verified");
                }

                // If provisional then there better be a comment
                if (destination.getVerification().getVerificationId() == 50 && (da.getComments()
                        == null || da.getComments().trim().isEmpty())) {
                    throw new UserFriendlyException("Beam Destination \"" + destination.getName()
                            + "\" must have a comment to explain why beam is permitted with provisional credited control status");
                }

                // Must provide an expiration date since CW or Tune
                if (da.getExpirationDate() == null) {
                    throw new UserFriendlyException("Beam Destination \"" + destination.getName()
                            + "\" must have an expiration date since beam is allowed");
                }

                // Expiration must be in the future
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY, 1);
                if (da.getExpirationDate().before(cal.getTime())) {
                    throw new UserFriendlyException("Beam Destination \"" + destination.getName()
                            + "\" must have a future expiration date and minimum expiration is 1 hour from now");
                }
            }

            da.setAuthorization(authorization);
            da.getDestinationAuthorizationPK().setAuthorizationId(authorization.getAuthorizationId());
            em.persist(da);
        }

        LOGGER.log(Level.FINE, "Director's Authorization saved successfully");
    }


    @RolesAllowed("bam-admin")
    public void sendOpsNewAuthorizationEmail(String linkHostName) throws UserFriendlyException {

        String toCsv = System.getenv("BAM_PERMISSIONS_EMAIL_CSV");

        if (toCsv == null || toCsv.isEmpty()) {
            LOGGER.log(Level.WARNING,
                    "Environment variable 'BAM_PERMISSIONS_EMAIL_CSV' not found, aborting");
            return;
        }

        String subject = System.getenv("BAM_PERMISSIONS_SUBJECT");

        String body = "<a href=\"https://" + linkHostName + "/beam-auth\">https://" + linkHostName + "/beam-auth</a>";

        String sender = System.getenv("BAM_EMAIL_SENDER");

        if (sender == null || sender.isEmpty()) {
            LOGGER.log(Level.WARNING,
                    "Environment variable 'BAM_EMAIL_SENDER' not found, aborting");
            return;
        }

        EmailService emailService = new EmailService();

        emailService.sendEmail(sender, sender, toCsv, subject, body, true);
    }

    @RolesAllowed("bam-admin")
    public long sendELog(String proxyServerName, String logbookServerName) throws UserFriendlyException {
        String username = checkAuthenticated();

        Authorization authorization = findCurrent();

        if (authorization == null) {
            throw new UserFriendlyException("No authorizations found");
        }

        //String body = getELogHTMLBody(authorization);
        String body = getAlternateELogHTMLBody(proxyServerName);

        String subject = System.getenv("BAM_PERMISSIONS_SUBJECT");

        String logbooks = System.getenv("BAM_BOOKS_CSV");

        if (logbooks == null || logbooks.isEmpty()) {
            logbooks = "TLOG";
            LOGGER.log(Level.WARNING,
                    "Environment variable 'BAM_BOOKS_CSV' not found, using default TLOG");
        }

        Properties config = Library.getConfiguration();

        config.setProperty("SUBMIT_URL", "https://" + logbookServerName + "/incoming");
        config.setProperty("FETCH_URL", "https://" + logbookServerName + "/entry");

        LogEntry entry = new LogEntry(subject, logbooks);

        entry.setBody(body, Body.ContentType.HTML);
        entry.setTags("Readme");

        LogEntryAdminExtension extension = new LogEntryAdminExtension(entry);
        extension.setAuthor(username);

        long logId;

        //System.out.println(entry.getXML());
        File tmpFile = null;

        try {
            SecurityUtil.disableServerCertificateCheck();

            tmpFile = grabPermissionsScreenshot(proxyServerName);
            entry.addAttachment(tmpFile.getAbsolutePath());
            logId = entry.submitNow();

            SecurityUtil.disableServerCertificateCheck();
        } catch (IOException | AttachmentSizeException | LogIOException | LogRuntimeException | LogCertificateException | KeyManagementException | NoSuchAlgorithmException e) {
            throw new UserFriendlyException("Unable to send elog", e);
        } finally {
            if (tmpFile != null) {
                boolean deleted = tmpFile.delete();
                if (!deleted) {
                    LOGGER.log(Level.WARNING, "Temporary image file was not deleted {0}",
                            tmpFile.getAbsolutePath());
                }
            }
        }

        return logId;
    }

    private String getELogHTMLBody(Authorization authorization) {
        StringBuilder builder = new StringBuilder();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        List<DestinationAuthorization> destinationAuthorizationList
                = authorization.getDestinationAuthorizationList();

        builder.append("<div><b>Beam Authorizations:</b>\n");
        if (destinationAuthorizationList != null && !destinationAuthorizationList.isEmpty()) {
            builder.append(
                    "<table><thead><tr><th>Beam Destination</th><th>Beam Mode</th><th>CW Current Limit</th></tr></thead><tbody>\n");
            for (DestinationAuthorization da : destinationAuthorizationList) {
                BeamDestination destination = destinationFacade.find(
                        da.getDestinationAuthorizationPK().getBeamDestinationId());

                Map<BigInteger, String> unitsMap = this.getUnitsMap();
                String units;

                if (da.getCwLimit() == null) {
                    units = "";
                } else {
                    units = unitsMap.get(destination.getBeamDestinationId());
                    if (units == null) {
                        units = "uA";
                    }
                }

                builder.append("<tr><td>");
                builder.append(IOUtil.escapeXml(destination.getName()));
                builder.append("</td><td>");
                builder.append(da.getBeamMode());
                builder.append("</td><td>");
                builder.append(
                        da.getCwLimit() == null ? ("CW".equals(da.getBeamMode()) ? "No Limit" : "No CW") : da.getCwLimit());
                builder.append(units);
                builder.append("</td></tr>\n");
            }
            builder.append("</tbody></table>\n");
        } else {
            builder.append("None\n");
        }
        builder.append("</div><div>\n<b>Comments:</b> ");
        builder.append(IOUtil.escapeXml(authorization.getComments()));
        builder.append("</div><div>\n\n<b>Authorized By:</b> ");
        builder.append(BeamAuthFunctions.formatStaff(authorization.getAuthorizedBy()));
        builder.append("</div><div>\n\n<b>Authorized On:</b> ");
        builder.append(formatter.format(authorization.getAuthorizationDate()));
        builder.append(
                "</div><div>\n\n<b>See:</b> <a href=\"http://accweb/beam-auth/\">Beam Authorization</a></div>\n");

        return builder.toString();
    }

    private File grabPermissionsScreenshot(String serviceHostname) throws
            IOException {
        URL url = new URL("https://" + serviceHostname + "/puppet-show/screenshot?url=https%3A%2F%2F"
                + serviceHostname
                + "%2Fbeam-auth%2Fpermissions%3Fprint%3DY&fullPage=true&filename=beam-auth.png&ignoreHTTPSErrors=true");

        LOGGER.log(Level.FINEST, "Fetching URL: {0}", url.toString());

        File tmpFile = null;
        InputStream in = null;
        OutputStream out = null;

        try {
            URLConnection con = url.openConnection();
            in = con.getInputStream();

            tmpFile = File.createTempFile("beam-auth", ".png");
            out = new FileOutputStream(tmpFile);
            IOUtil.copy(in, out);

        } finally {
            IOUtil.close(in, out);
        }
        return tmpFile;
    }

    private String getAlternateELogHTMLBody(String serviceHostname) {
        StringBuilder builder = new StringBuilder();

        builder.append(
                "[figure:1]<div>\n\n<b><span style=\"color: red;\">Always check the Beam Authorization web application for the latest credited controls status:</span></b> ");
        builder.append("<a href=\"https://");
        builder.append(serviceHostname);
        builder.append("/beam-auth/\">Beam Authorization</a></div>\n");

        return builder.toString();
    }
}
