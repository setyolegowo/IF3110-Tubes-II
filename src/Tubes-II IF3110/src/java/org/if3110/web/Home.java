/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.if3110.web;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.if3110.model.UserManagement;

/**
 *
 * @author Setyo Legowo <setyo.legowo@live.com>
 */
public class Home extends HttpServlet {
    /**
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doPost(HttpServletRequest request, 
                      HttpServletResponse response) 
                      throws IOException, ServletException {
        String c = request.getParameter("type");

        // Now use our Coffee Model above 
        UserManagement ce = new UserManagement();

        List result = ce.getTypes(c);

        // Use the below code to debug the program if you get problems 
        //response.setContentType("text/html"):
        //PrintWriter out = response.getWriter();
        //out.println("Coffee Selection Advise<br>");

        //Iterator it = result.iterator();
        //while(it.hasNext()) {
        //  out.print("<br>try: " + it.next());
        //}

        // The results will be passed back (as an attribute) to the JSP view
        // The attribute will be a name/value pair, the value in this case will be a List object 
        request.setAttribute("styles", result);
        RequestDispatcher view = request.getRequestDispatcher("include/result.jsp");
        view.forward(request, response); 
    }
}
