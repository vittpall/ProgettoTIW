package it.polimi.ProgettoTIW.model;

import it.polimi.ProgettoTIW.DAO.imageDAO;
import it.polimi.ProgettoTIW.DAO.userDAO;
import it.polimi.ProgettoTIW.DAO.commentsDAO;
import it.polimi.ProgettoTIW.beans.Image;
import it.polimi.ProgettoTIW.beans.User;
import it.polimi.ProgettoTIW.beans.Comment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/GoToImagePage")
public class GoToImage extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private TemplateEngine templateEngine;

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

        String imageIdParam = request.getParameter("Image_id");
        String albumTitle = request.getParameter("albumTitle");
        String albumCreator = request.getParameter("albumCreator");
        
        System.out.println("albumCreator:"+albumCreator);
        int idAlbumCreator;
        try {
            idAlbumCreator = Integer.parseInt(albumCreator);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Invalid albumCreator id: " + e.getMessage());
            return;
        }
        
        if (imageIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Image ID is required");
            
            return;
        }

        int imageId;
        try {
            imageId = Integer.parseInt(imageIdParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Invalid photos format: " + e.getMessage());
            return;
        }
        
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("User not logged in");
            return;
        }

        imageDAO imageDao = new imageDAO(connection);
        commentsDAO commentsDao = new commentsDAO(connection);
        userDAO userDao = new userDAO(connection);
        Image image;
        List<Comment> comments;
        int userCreator = 0;
        
        try {
            image = imageDao.findImageById(imageId);
            if (image == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
                return;
            }
            comments = commentsDao.findCommentsByImage(imageId);
            //i forgot to add username as an attribute of comment
            for(Comment comment: comments)
            {
            	comment.setUsername(userDao.getUsernameById(comment.getUser_id()));
            }
            userCreator = imageDao.CheckCreator(image.getImage_Id());
            
            System.out.println("immagine è "+ userCreator);
            String path = "/WEB-INF/ImagePage.html";
    		ServletContext servletContext = getServletContext();
            WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            ctx.setVariable("userCreator", userCreator);
            ctx.setVariable("image", image);
            ctx.setVariable("comments", comments);
            ctx.setVariable("albumTitle", albumTitle);
            ctx.setVariable("albumCreator", idAlbumCreator);
            templateEngine.process(path, ctx, response.getWriter());
            

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database access error: " + e.getMessage());
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
