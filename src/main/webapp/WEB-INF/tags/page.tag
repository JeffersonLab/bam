<%@tag description="The Site Page Template Tag" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="s" uri="http://jlab.org/jsp/smoothness" %>
<%@attribute name="title"%>
<%@attribute name="category" %>
<%@attribute name="stylesheets" fragment="true" %>
<%@attribute name="scripts" fragment="true" %>
<%@attribute name="secondaryNavigation" fragment="true" %>
<s:tabbed-page title="${title}" category="${category}">
    <jsp:attribute name="stylesheets">
        <link rel="stylesheet" type="text/css" href="${cdnContextPath}/jquery-plugins/timepicker/jquery-ui-timepicker-1.3.1.css"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/v${initParam.resourceVersionNumber}/css/beam-auth.css"/>
        <jsp:invoke fragment="stylesheets"/>
    </jsp:attribute>
    <jsp:attribute name="scripts">
        <script type="text/javascript" src="${cdnContextPath}/jquery-plugins/timepicker/jquery-ui-timepicker-1.3.1.js"></script>
        <script type="text/javascript" src="${cdnContextPath}/jquery-plugins/maskedinput/jquery.maskedinput-1.3.1.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/v${initParam.resourceVersionNumber}/js/beam-auth.js"></script>
        <jsp:invoke fragment="scripts"/>
    </jsp:attribute>
    <jsp:attribute name="primaryNavigation">
                    <ul>
                        <li${fn:startsWith(currentPath, '/permissions') ? ' class="current-primary"' : ''}><a href="${pageContext.request.contextPath}/permissions">Permissions</a></li>
                        <li${fn:startsWith(currentPath, '/credited-controls') ? ' class="current-primary"' : ''}><a href="${pageContext.request.contextPath}/credited-controls">Credited Controls</a></li>
                        <li${'/destinations' eq currentPath ? ' class="current-primary"' : ''}><a href="${pageContext.request.contextPath}/destinations">Beam Destinations</a></li>
                        <li${'/control-participation' eq currentPath ? ' class="current-primary"' : ''}><a href="${pageContext.request.contextPath}/control-participation">Control Participation</a></li>
                        <li${'/help' eq currentPath ? ' class="current-primary"' : ''}><a href="${pageContext.request.contextPath}/help">Help</a></li>
                    </ul>
    </jsp:attribute>
    <jsp:attribute name="secondaryNavigation">
        <jsp:invoke fragment="secondaryNavigation"/>
    </jsp:attribute>
    <jsp:body>
        <jsp:doBody/>
    </jsp:body>
</s:tabbed-page>