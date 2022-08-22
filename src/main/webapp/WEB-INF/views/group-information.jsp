<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%> 
<%@taglib prefix="beamauth" uri="http://jlab.org/beamauth/functions"%>
<t:page title="Group Information: ${group.name}"> 
    <jsp:attribute name="stylesheets">
        <style type="text/css">
        </style>
    </jsp:attribute>
    <jsp:attribute name="scripts">       
    </jsp:attribute>        
    <jsp:body>
        <section>
            <h2>${group.name} Information</h2>
            <div class="dialog-content">
                <h3>Group Leaders</h3>
                <c:choose>
                    <c:when test="${fn:length(leaderList) > 0}">
                        <table class="data-table stripped-table">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${leaderList}" var="leader">
                                    <tr>
                                        <td><c:out value="${beamauth:formatUser(leader)}"/></td>
                                    </tr>
                                </c:forEach> 
                            </tbody>
                        </table>                        
                    </c:when>
                    <c:otherwise>
                        <div class="message-box">No Leaders</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </jsp:body>         
</t:page>
