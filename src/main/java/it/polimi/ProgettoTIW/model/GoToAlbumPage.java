package it.polimi.ProgettoTIW.model;


import it.polimi.ProgettoTIW.DAO.imageDAO;

import it.polimi.ProgettoTIW.beans.Image;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/GoToAlbumPage")
public class GoToAlbumPage extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
	private TemplateEngine templateEngine;
	int Offset = 0;
    
    
    public GoToAlbumPage()
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

    	String AlbumTitle = request.getParameter("albumTitle");
    	String AlbumCreator = request.getParameter("albumCreator");
    	String NextPage = request.getParameter("Next");
    	String PrevPage = request.getParameter("Prev");
    	boolean AvailableNext = false;
    	boolean AvailablePrev = false;
    	
    	String albumTitleCurrent = (String) request.getSession().getAttribute("albumTitle");
        if(albumTitleCurrent != null && albumTitleCurrent.equals(AlbumTitle)) {
            if (NextPage != null && "true".equals(NextPage)) {
                this.Offset += 5;
            } else if (PrevPage != null && "true".equals(PrevPage)) {
                this.Offset = Math.max(0, this.Offset - 5); // Ensure Offset doesn't go negative
            }
        } else {
            this.Offset = 0;
            request.getSession().setAttribute("albumTitle", AlbumTitle);
        }
    	
    	/*
    	String albumTitleCurrent = (String) request.getSession().getAttribute("albumTitle");
    	if(albumTitleCurrent != null)
    	{
    		if(!albumTitleCurrent.equals(AlbumCreator))
    			Offset = 0;
    	}
    	else
    		request.getSession().setAttribute("albumTitle", AlbumTitle);
    		
    		
    	if(NextPage != null && NextPage.equals("true"))
    	{
    		this.Offset += 5;
    	}
    	else
    	{
    		if(PrevPage != null && PrevPage.equals("true"))
    		{
    			this.Offset -= M;
    		}
    	}
    	*/

        imageDAO imageDao = new imageDAO(connection);
        List<Image> images;
        int idAlbumCreator;
        
        try {
            idAlbumCreator = Integer.parseInt(AlbumCreator);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Invalid albumCreator id: " + e.getMessage());
            return;
        }

        try { 	
            images = imageDao.findImagesByAlbum(AlbumTitle, idAlbumCreator, Offset);
            System.out.println(images.size());
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal server error while retrieving album data: "+ e.getMessage());
            return;
        }

        //implements the logic used to add the button prev or next
        if(Offset == 0)
        {
        	AvailablePrev = false;
        	if(images.size() > 5)
        		AvailableNext = true;
        	else
        		AvailableNext = false;
        }
        else
        {
        	if(Offset > 0)
        	{
              	AvailablePrev = true;
            	if(images.size() > 5)
            		AvailableNext = true;
            	else
            		AvailableNext = false;
        	}
        }
        
		String path = "/WEB-INF/AlbumPage.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("AvailableNext", AvailableNext);
		ctx.setVariable("AvailablePrev", AvailablePrev);
		ctx.setVariable("albumCreator", idAlbumCreator );
		ctx.setVariable("images", images);
		ctx.setVariable("albumTitle", AlbumTitle);
		templateEngine.process(path, ctx, response.getWriter());
            
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
