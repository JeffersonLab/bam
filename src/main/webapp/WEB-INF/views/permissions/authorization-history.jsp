<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="http://jlab.org/jsp/smoothness" %>
<%@taglib prefix="beamauth" uri="http://jlab.org/beamauth/functions"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%> 
<t:page title="Authorization History"> 
    <jsp:attribute name="stylesheets">
    </jsp:attribute>
    <jsp:attribute name="scripts">
        <script type="text/javascript">
            $(document).on("click", "#next-button, #previous-button", function () {
                $("#offset-input").val($(this).attr("data-offset"));
                $("#filter-form").submit();
            });
        </script>
    </jsp:attribute>        
    <jsp:body>
        <div class="breadbox">
            <ul class="breadcrumb">
                <li>
                    <a href="${pageContext.request.contextPath}/permissions">Director's Authorization</a>
                </li>
                <li>
                    <span>Historic Authorizations</span>
                </li>
            </ul>
        </div>        
        <section>
            <div class="dialog-content">
                <c:choose>
                    <c:when test="${fn:length(historyList) < 1}">
                        <div class="message-box">None</div>
                    </c:when>
                    <c:otherwise>
                        <div class="message-box"><c:out value="${selectionMessage}"/></div>
                        <table id="authorization-table" class="data-table stripped-table">
                            <thead>
                                <tr>
                                    <th>Modified Date</th>
                                    <th>Modified By</th>
                                    <th>Authorization Date</th>
                                    <th>Authorized By</th>
                                    <th>Notes</th>
                                    <th>Destination Authorizations</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${historyList}" var="history">
                                    <tr>
                                        <td><fmt:formatDate pattern="${s:getFriendlyDateTimePattern()}" value="${history.modifiedDate}"/></td>
                                        <td><c:out value="${beamauth:formatUsername(history.modifiedBy)}"/></td>
                                        <td><fmt:formatDate pattern="${s:getFriendlyDateTimePattern()}" value="${history.authorizationDate}"/></td>
                                        <td><c:out value="${beamauth:formatUsername(history.authorizedBy)}"/></td>
                                        <td><c:out value="${history.comments}"/></td>
                                        <td><a href="destinations-authorization-history?authorizationId=${history.authorizationId}">Destination Details</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <form id="filter-form" action="authorization-history" method="get">
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
