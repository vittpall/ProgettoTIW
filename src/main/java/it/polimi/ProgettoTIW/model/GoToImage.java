package it.polimi.ProgettoTIW.model;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import it.polimi.ProgettoTIW.beans.Image;
import it.polimi.ProgettoTIW.beans.Comment;
import it.polimi.ProgettoTIW.DAO.imageDAO;
import it.polimi.ProgettoTIW.DAO.commentsDAO;


@WebServlet("/ImageDetails")
public class GoToImage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    
    public GoToImage()
    {
    	super();
    }

    public void init() throws ServletException {
        try {
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String user = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
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
        int imageId;
        try {
            imageId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid image ID format");
            return;
        }

        imageDAO imageDao = new imageDAO(connection);
        commentsDAO commentDao = new commentsDAO(connection);
        Image image;
        List<Comment> comments;

        try {
            image = imageDao.findImageById(imageId);
            if (image == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("Image not found");
                return;
            }

            comments = commentDao.findCommentsByImage(imageId);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal server error while retrieving image data");
            return;
        }

        request.setAttribute("image", image);
        request.setAttribute("comments", comments);

        RequestDispatcher dispatcher = request.getRequestDispatcher("imageView.jsp"); // Replace with actual view page
        dispatcher.forward(request, response);
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