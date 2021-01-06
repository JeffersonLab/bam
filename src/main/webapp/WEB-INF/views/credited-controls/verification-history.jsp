<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="http://jlab.org/jsp/smoothness" %>
<%@taglib prefix="beamauth" uri="http://jlab.org/beamauth/functions"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%> 
<t:page title="Credited Controls > Verification History"> 
    <jsp:attribute name="stylesheets">
    </jsp:attribute>
    <jsp:attribute name="scripts">
    </jsp:attribute>        
    <jsp:body>
        <div class="breadbox">
            <ul class="breadcrumb">
                <li>
                    <a href="${pageContext.request.contextPath}/credited-controls">Credited Controls</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/credited-controls?creditedControlId=${verification.creditedControl.creditedControlId}">${verification.creditedControl.name}</a>
                </li>
                <li>
                    <c:out value="${beamauth:formatDestination(verification.beamDestination)}"/> <span>History</span>
                </li>
            </ul>
        </div>        
        <section>
            <div class="dialog-content">
                <h3>Name</h3>
                <c:out value="${verification.creditedControl.name}"/>
                <h3>Beam Destination</h3>
                <c:out value="${beamauth:formatDestination(verification.beamDestination)}"/>
                <h3>Group</h3>
                <c:out value="${verification.creditedControl.group.name}"/>
                <h3>History</h3>
                <c:choose>
                    <c:when test="${fn:length(historyList) < 1}">
                        <div class="message-box">None</div>
                    </c:when>
                    <c:otherwise>
                        <div class="message-box"><c:out value="${selectionMessage}"/></div>
                        <table id="verification-table" class="data-table stripped-table">
                            <thead>
                                <tr>
                                    <th>Modified Date</th>
                                    <th>Modified By</th>
                                    <th>Verified Date</th>
                                    <th>Verified By</th>
                                    <th>Verified</th>                                            
                                    <th>Comments</th>
                                    <th>Expiration Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${historyList}" var="history">
                                    <tr>
                                        <td><fmt:formatDate pattern="${s:getFriendlyDateTimePattern()}" value="${history.modifiedDate}"/></td>
                                        <td><c:out value="${beamauth:formatStaff(history.modifiedBy)}"/></td>
                                        <td><fmt:formatDate pattern="${s:getFriendlyDateTimePattern()}" value="${history.verificationDate}"/></td>
                                        <td><c:out value="${beamauth:formatStaff(history.verifiedBy)}"/></td>
                                        <td class="icon-cell"><span title="${history.verificationId eq 1 ? 'Verified' : (history.verificationId eq 50 ? 'Provisionally Verified' : 'Not Verified')}" class="small-icon baseline-small-icon ${history.verificationId eq 1 ? 'verified-icon' : (history.verificationId eq 50 ? 'provisional-icon' : 'not-verified-icon')}"></span></td>
                                        <td><c:out value="${history.comments}"/></td>
                                        <td><fmt:formatDate pattern="${s:getFriendlyDateTimePattern()}" value="${history.expirationDate}"/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <form id="filter-form" action="${pageContext.request.contextPath}/credited-controls/verification-history" method="get">
                            <input type="hidden" name="controlVerificationId" value="${param.controlVerificationId}"/>
                            <input type="hidden" id="offset-input" name="offset" value="0"/>
                        </form>
                        <button id="previous-button" type="button" data-offset="${paginator.previousOffset}" value="Previous"${paginator.previous ? '' : ' disabled="disabled"'}>Previous</button>                        
                        <button id="next-button" type="button" data-offset="${paginator.nextOffset}" value="Next"${paginator.next ? '' : ' disabled="disabled"'}>Next</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>          
    </jsp:body>         
</t:page>
