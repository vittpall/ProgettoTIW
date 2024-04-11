package it.polimi.ProgettoTIW.model;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.catalina.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.ProgettoTIW.beans.album;
import it.polimi.ProgettoTIW.beans.user;
import it.polimi.ProgettoTIW.DAO.albumDAO;
import it.polimi.ProgettoTIW.DAO.userDAO;

@WebServlet("/GoToHomePage")
public class GoToHomePage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
	private TemplateEngine templateEngine;

	
    
    public GoToHomePage()
    {
    	super();
    }
    

    public void init() throws ServletException {
        try {
        		ServletContext servletContext = getServletContext();
        		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        		templateResolver.setTemplateMode(TemplateMode.HTML);
        		this.templateEngine = new TemplateEngine();
        		this.templateEngine.setTemplateResolver(templateResolver);
        		templateResolver.setSuffix(".html");
        		String driver = servletContext.getInitParameter("dbDriver");
        		String url = servletContext.getInitParameter("dbUrl");
        		String user = servletContext.getInitParameter("dbUser");
        		String password = servletContext.getInitParameter("dbPassword");
        		Class.forName(driver);
        		connection = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            throw new UnavailableException("Couldn't get db connection");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (user) request.getSession().getAttribute("user");
        
        String loginpath = getServletContext().getContextPath() + "/index.html";
        
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		}
        if (user == null) {
            response.sendRedirect(loginpath);
            return;
        }
        
        userDAO userDao = new userDAO(connection);
        try {
            userlist = userDao.getAllUser();
            request.setAttribute("users", users);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Unable to retrieve albums");
            return;
        }

        albumDAO albumDao = new albumDAO(connection);
        List<Album> Users;
        List<List<Album>> OtherAlbum;
        try {
            UserAlbum = albumDao.findAlbumsByUser(user.getUsername());
            OtherAlbum = albumDao.findAlbumOtherUser();
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Unable to retrieve albums");
            return;
        }
        
        for(User u : Users)
        {
        	if(u.getUsername().equals(user.getUsername()))
        	OtherAlbum.add(albumDao.findAlbumsByUser(u.getUsername()));
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