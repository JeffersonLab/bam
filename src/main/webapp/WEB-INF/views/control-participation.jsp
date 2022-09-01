<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="beamauth" uri="http://jlab.org/beamauth/functions" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="title" value="Control Participation"/>
<t:page title="${title}">  
    <jsp:attribute name="stylesheets">
        <link rel="stylesheet" type="text/css"
              href="${pageContext.request.contextPath}/resources/v${initParam.releaseNumber}/css/credited-controls.css"/>
        <link rel="stylesheet" type="text/css"
              href="${pageContext.request.contextPath}/resources/v${initParam.releaseNumber}/css/control-participation.css"/>
    </jsp:attribute>
    <jsp:attribute name="scripts">              
        <script type="text/javascript"
                src="${pageContext.request.contextPath}/resources/v${initParam.releaseNumber}/js/control-participation.js"></script>
    </jsp:attribute>
    <jsp:body>
        <section>
            <h2><c:out value="${title}"/></h2>
            <div class="participation-scroll-pane">
                <table class="data-table stripped-table fixed-table control-participation-table ${pageContext.request.isUserInRole('bam-admin') ? 'editable' : ''}">
                    <thead>
                    <tr>
                        <th rowspan="2" class="component-header">Credited Control</th>
                        <th colspan="${fn:length(destinationList)}">Beam Destination</th>
                    </tr>
                    <tr>
                        <c:forEach items="${destinationList}" var="destination">
                            <th class="destination-header">
                                <c:out value="${destination.name}"/>
                            </th>
                        </c:forEach>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${ccList}" var="cc">
                        <tr data-cc-id="${cc.creditedControlId}">
                            <th><a data-dialog-title="${fn:escapeXml(cc.name)} Information" class="dialog-ready"
                                   href="${pageContext.request.contextPath}/credited-controls?creditedControlId=${cc.creditedControlId}&amp;notEditable=1"><c:out
                                    value="${cc.name}"/></a></th>
                            <c:forEach items="${destinationList}" var="destination">
                                <td data-destination-id="${destination.beamDestinationId}">
                                    <c:if test="${cc.hasBeamDestination(destination)}">
                                        ✔
                                    </c:if>
                                </td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>
    </jsp:body>
</t:page>
