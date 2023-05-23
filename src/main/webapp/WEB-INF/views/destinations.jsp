<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="http://jlab.org/jsp/smoothness" %>
<%@taglib prefix="beamauth" uri="http://jlab.org/beamauth/functions"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%> 
<t:page title="Destinations"> 
    <jsp:attribute name="stylesheets">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/v${initParam.releaseNumber}/css/credited-controls.css"/>
        <style type="text/css">
            .dialog-content {
                padding-bottom: 1em;
            }
            .dialog-content button {
                font-size: 14px;
            }
        </style>
    </jsp:attribute>
    <jsp:attribute name="scripts">
        <script type="text/javascript">
            jlab = jlab || {};
            jlab.verificationType = 'Control-Group';
        </script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/v${initParam.releaseNumber}/js/credited-controls.js"></script>
    </jsp:attribute>        
    <jsp:body>
        <c:if test="${destination ne null}">
            <div class="banner-breadbox">
                <ul>
                    <li>
                        <a href="destinations">Beam Destinations</a>
                    </li>
                    <li>
                        <form method="get" action="destinations">
                            <select name="destinationId" class="change-submit">
                                <c:forEach items="${destinationList}" var="destination">
                                    <option value="${destination.beamDestinationId}"${param.destinationId eq destination.beamDestinationId ? ' selected="selected"' : ''}><c:out value="${destination.name}"/></option>
                                </c:forEach>
                            </select>
                        </form>
                    </li>
                </ul>
            </div>
        </c:if>
        <section>
            <c:choose>
                <c:when test="${destination ne null}">
                    <div class="dialog-content">
                        <h3>Verifications</h3>
                        <c:choose>
                            <c:when test="${fn:length(destination.controlVerificationList) < 1}">
                                <div class="message-box">None</div>
                            </c:when>
                            <c:otherwise>
                                <c:if test="${adminOrLeader && param.notEditable eq null}">
                                    <button id="edit-selected-button" type="button" class="verify-button selected-row-action" disabled="disabled">Edit Selected</button>
                                </c:if>
                                <table id="verification-table" class="data-table stripped-table${(adminOrLeader && param.notEditable eq null) ? ' multicheck-table editable-row-table' : ''}">
                                    <thead>
                                        <tr>
                                            <c:if test="${adminOrLeader && param.notEditable eq null}">
                                                <th>
                                                    Select
                                                    <select id="check-select" name="check-select">
                                                        <option value="">&nbsp;</option>
                                                        <option value="all">All</option>
                                                        <option value="none">None</option>
                                                    </select>
                                                </th>
                                            </c:if>
                                            <th>Credited Control and Group</th>
                                            <th>Verified</th>
                                            <th>Verified Date</th>
                                            <th>Verified By</th>
                                            <th>Comments</th>
                                            <th>Expiration Date</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${destination.controlVerificationList}" var="verification">
                                            <tr data-control-verification-id="${verification.controlVerificationId}" data-verified-username="${verification.verifiedBy}" data-status-id="${verification.verificationId}">
                                                <c:if test="${adminOrLeader && param.notEditable eq null}">
                                                    <td>
                                                        <input class="destination-checkbox" type="checkbox" name="destination-checkbox" value="${verification.controlVerificationId}"/>
                                                    </td>
                                                </c:if>
                                                <td><c:out value="${verification.creditedControl.name}"/>; <c:out value="${verification.creditedControl.group.name}"/></td>
                                                <td class="icon-cell"><span title="${verification.verificationId eq 1 ? 'Verified' : (verification.verificationId eq 50 ? 'Provisionally Verified' : 'Not Verified')}" class="small-icon baseline-small-icon ${verification.verificationId eq 1 ? 'verified-icon' : (verification.verificationId eq 50 ? 'provisional-icon' : 'not-verified-icon')}"></span></td>
                                                <td><fmt:formatDate pattern="${s:getFriendlyDateTimePattern()}" value="${verification.verificationDate}"/></td>
                                                <td><c:out value="${s:formatUsername(verification.verifiedBy)}"/></td>
                                                <td><c:out value="${verification.comments}"/></td>
                                                <td><fmt:formatDate pattern="${s:getFriendlyDateTimePattern()}" value="${verification.expirationDate}"/></td>
                                                <td><a class="small-icon dialog-ready comment-icon" data-dialog-title="Verification History" href="credited-controls/verification-history?controlVerificationId=${verification.controlVerificationId}" title="Click for verification history"></a></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                                <c:if test="${adminOrLeader && param.notEditable eq null}">
                                    <div id="multi-instructions">Hold down the control (Ctrl) or shift key when clicking to select multiple.  Hold down the Command (⌘) key on Mac.</div> 
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:when>
                <c:otherwise>                   
                    <h2>Beam Destinations</h2>
                    <table class="data-table stripped-table">
                        <thead>
                            <tr>
                                <th>Select</th>
                                <th>Name</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${destinationList}" var="destination">
                                <tr data-destination-id="${destination.beamDestinationId}">
                                    <td>
                                        <form method="get" action="destinations">
                                            <input type="hidden" name="destinationId" value="${destination.beamDestinationId}"/>
                                            <button class="single-char-button" type="submit">&rarr;</button>
                                        </form>
                                    </td>
                                    <td><c:out value="${destination.name}"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>                              
                </c:otherwise>
            </c:choose>
            <div id="verify-dialog" class="dialog" title="Edit Credited Control Verification">
                <form>
                    <ul class="key-value-list">
                        <li>
                            <div class="li-key"><span id="edit-dialog-verification-label">Beam Destinations</span>:</div>
                            <div class="li-value">
                                <ul id="selected-verification-list">

                                </ul>
                                <span id="edit-dialog-verification-count"></span>
                            </div>
                        </li>                    
                        <li>
                            <div class="li-key">Status:</div>
                            <div class="li-value">
                                <select id="verificationId" name="verificationId">
                                    <option value="&nbsp;"> </option>
                                    <option value="100">Not Verified</option>
                                    <option value="50">Provisionally Verified</option>
                                    <option value="1">Verified</option>
                                </select>
                            </div>
                        </li>      
                        <li>
                            <div class="li-key">Verification Date:</div>
                            <div class="li-value">
                                <input id="verificationDate" name="verificationDate" type="text" class="date-time-field nowable-field" placeholder="${s:getFriendlyDateTimePlaceholder()}"/>
                            </div>
                        </li>
                        <li>
                            <div class="li-key">Verified By:</div>
                            <div class="li-value">
                                <input id="verifiedBy" name="verifiedBy" type="text" placeholder="username" class="username-autocomplete" maxlength="64"/>
                                <button class="me-button" type="button">Me</button>
                            </div>
                        </li>                        
                        <li>
                            <div class="li-key">Expiration Date:</div>
                            <div class="li-value">
                                <input id="expirationDate" name="expirationDate" type="text" class="date-time-field" placeholder="${s:getFriendlyDateTimePlaceholder()}"/>
                            </div>
                        </li>
                        <li>
                            <div class="li-key">Comments:</div>
                            <div class="li-value">
                                <textarea id="comments" name="comments"></textarea>
                            </div>
                        </li>                    
                    </ul>
                    <input type="hidden" id="creditedControlId" name="creditedControlId"/>
                    <div class="dialog-button-panel">
                        <span id="rows-differ-message">Note: One or more selected rows existing values differ</span>
                        <button id="verifySaveButton" class="dialog-submit ajax-button" type="button">Save</button>
                        <button class="dialog-close-button" type="button">Cancel</button>
                    </div>
                </form>
            </div>
        </section>
        <div id="success-dialog" class="dialog" title="Verification Saved Successfully">
            <span class="logentry-success">Verification contained downgrade so a new log entry was created: <a id="new-entry-url" href="#"></a></span>
            <div class="dialog-button-panel">
                <button class="dialog-close-button" type="button">OK</button>
            </div>
        </div>        
    </jsp:body>         
</t:page>
