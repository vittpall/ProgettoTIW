package it.polimi.ProgettoTIW.model;

import it.polimi.ProgettoTIW.DAO.imageDAO;
import it.polimi.ProgettoTIW.DAO.commentsDAO;
import it.polimi.ProgettoTIW.beans.Image;
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

@WebServlet("/ImageDetails")
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

        String imageIdParam = request.getParameter("id");
        if (imageIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Image ID is required");
            return;
        }

        int imageId;
        try {
            imageId = Integer.parseInt(imageIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid image ID format");
            return;
        }

        imageDAO imageDao = new imageDAO(connection);
        commentsDAO commentsDao = new commentsDAO(connection);
        Image image;
        List<Comment> comments;

        try {
            image = imageDao.findImageById(imageId);
            if (image == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
                return;
            }
            comments = commentsDao.findCommentsByImage(imageId);
            
            String path = getServletContext().getContextPath() + "/ImagePage.html";
    		ServletContext servletContext = getServletContext();
            WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            ctx.setVariable("image", image);
            ctx.setVariable("comments", comments);
            templateEngine.process(path, ctx, response.getWriter());
            

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database access error");
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
