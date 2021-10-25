package org.jlab.bam.presentation.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jlab.bam.business.session.AuthorizationFacade;
import org.jlab.bam.persistence.entity.Authorization;
import org.jlab.smoothness.presentation.util.Paginator;
import org.jlab.smoothness.presentation.util.ParamUtil;

/**
 *
 * @author ryans
 */
@WebServlet(name = "AuthorizationHistoryController", urlPatterns = {"/permissions/authorization-history"})
public class AuthorizationHistoryController extends HttpServlet {

    @EJB
    AuthorizationFacade historyFacade;
    
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int offset = ParamUtil.convertAndValidateNonNegativeInt(request, "offset", 0);
        int maxPerPage = 10;        
        
        List<Authorization> historyList = historyFacade.findHistory(offset, maxPerPage);
        Long totalRecords = historyFacade.countHistory();

        Paginator paginator = new Paginator(totalRecords.intValue(), offset, maxPerPage);
        
        DecimalFormat formatter = new DecimalFormat("###,###");
        
        String selectionMessage = "All Authorizations";
        
        if (paginator.getTotalRecords() < maxPerPage && offset == 0) {
            selectionMessage = selectionMessage + " {" + formatter.format(
                    paginator.getTotalRecords()) + "}";
        } else {
            selectionMessage = selectionMessage + " {"
                    + formatter.format(paginator.getStartNumber())
                    + " - " + formatter.format(paginator.getEndNumber())
                    + " of " + formatter.format(paginator.getTotalRecords()) + "}";
        }            
        
        request.setAttribute("selectionMessage", selectionMessage);
        request.setAttribute("historyList", historyList);
        request.setAttribute("paginator", paginator);

        request.getRequestDispatcher("/WEB-INF/views/permissions/authorization-history.jsp").forward(request, response);
    }
}
