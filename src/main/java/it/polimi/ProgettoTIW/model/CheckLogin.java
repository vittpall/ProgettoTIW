package it.polimi.ProgettoTIW.model;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import it.polimi.ProgettoTIW.beans.User;
import it.polimi.ProgettoTIW.DAO.userDAO;

@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private TemplateEngine templateEngine;

    public void init() throws ServletException {
        try {
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String user = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            
            ServletContext servletContext = getServletContext();
    		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
    		templateResolver.setTemplateMode(TemplateMode.HTML);
    		this.templateEngine = new TemplateEngine();
    		this.templateEngine.setTemplateResolver(templateResolver);
    		templateResolver.setSuffix(".html");

        } catch (ClassNotFoundException e) {
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            throw new UnavailableException("Couldn't get db connection");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usrn = request.getParameter("username");
        String pwd = request.getParameter("password");
        HttpSession session = request.getSession(); 

        if (usrn == null || pwd == null || usrn.isEmpty() || pwd.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Credentials must be not null");
            return;
        }

        userDAO userDao = new userDAO(connection);
        User user = null;
        try {
            user = userDao.checkCredentials(usrn, pwd);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal server error, retry later");
            return;
        }
        String path;
        if (user == null) {
        	//String loginError = "User not registered. Create a new one";
        	path = "/index.html";
    		ServletContext servletContext = getServletContext();
    		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
    		ctx.setVariable("loginError", "Incorrect username or password" );
    		templateEngine.process(path, ctx, response.getWriter());
        } else {
            session.setAttribute("user", user);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(usrn);
            session.setAttribute("user", user);
            path = getServletContext().getContextPath() + "/GoToHomePage";
			response.sendRedirect(path);
            
           
        }
    }

    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException sqle) {
        }
    }
}