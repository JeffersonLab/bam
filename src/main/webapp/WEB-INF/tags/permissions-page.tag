<%@tag description="Destination Table Tag" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="http://jlab.org/jsp/smoothness" %>
<%@taglib prefix="beamauth" uri="http://jlab.org/beamauth/functions"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@attribute name="cebafDestinationList" required="true" type="java.util.List"%>
<%@attribute name="lerfDestinationList" required="true" type="java.util.List"%>
<%@attribute name="isEditable" required="true" type="java.lang.Boolean"%>
<%@attribute name="isHistory" required="true" type="java.lang.Boolean"%>
<form id="authorization-form" method="post" action="permissions">
    <div class="chart-wrap-backdrop">
        <h2>Permissions
            <c:if test="${isEditable}">
                <span class="readonly-field"><button id="edit-button" type="button">Edit</button></span>
            </c:if>            
        </h2>
        <div class="editable-field power-limited-note">Note: Blank/Empty Current Limit results in "Dump Power Limited"</div>
        <h3>CEBAF</h3>
        <t:destination-permissions-table destinationList="${cebafDestinationList}" isHistory="${isHistory}" facility="cebaf"/>
        <h3>LERF</h3>
        <t:destination-permissions-table destinationList="${lerfDestinationList}" isHistory="${isHistory}" facility="lerf"/>
        <h3>UITF</h3>
        <t:destination-permissions-table destinationList="${uitfDestinationList}" isHistory="${isHistory}" facility="uitf"/>
        <h3>Notes</h3>
        <div class="notes-field">
            <span class="readonly-field">
                <c:out value="${fn:length(authorization.comments) == 0 ? 'None' : authorization.comments}"/>
            </span>
            <span class="editable-field">
                <textarea id="comments" name="comments"><c:out value="${authorization.comments}"/></textarea>
            </span>
        </div>
        <h3>Digital Signature</h3>
        <div class="footer">
            <div class="footer-row">
                <div class="signature-field">
                    <c:choose>
                        <c:when test="${authorization ne null}">
                            <div class="readonly-field">Authorized by ${beamauth:formatStaff(authorization.authorizedBy)} on <fmt:formatDate value="${authorization.authorizationDate}" pattern="${s:getFriendlyDateTimePattern()}"/></div>
                        </c:when>
                        <c:otherwise>
                            <div class="readonly-field">None</div>
                        </c:otherwise>
                    </c:choose>
                    <div class="editable-field notification-option-panel">
                        <p>
                            <label for="generate-elog-checkbox">Generate elog:</label>
                            <input id="generate-elog-checkbox" type="checkbox" name="notification" value="Y" checked="checked"/>
                        </p>
                    </div>     
                    <div class="editable-field">Click the Save button to sign:
                        <span>
                            <button id="save-button" class="ajax-button inline-button" type="button">Save</button>
                            <span class="cancel-text">
                                or 
                                <a id="cancel-button" href="#">Cancel</a>
                            </span>
                        </span>
                    </div>
                </div>
                <div class="history-panel">
                    <c:if test="${not isHistory}">
                        <a data-dialog-title="Authorization History" href="permissions/authorization-history" title="Click for authorization history">History</a>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</form>