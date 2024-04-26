package it.polimi.ProgettoTIW.model;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

import it.polimi.ProgettoTIW.beans.User;
import it.polimi.ProgettoTIW.beans.Comment;

import it.polimi.ProgettoTIW.DAO.commentsDAO;
import it.polimi.ProgettoTIW.DAO.imageDAO;

@WebServlet("/AddComment")
public class AddComment extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    
    public AddComment()
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String commentText = request.getParameter("comment");
        String albumTitle = request.getParameter("albumTitle");
        int imageId;
        try {
            imageId = Integer.parseInt(request.getParameter("imageId"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid image ID format: " +e.getMessage());
            return;
        }

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("User not logged in");
            return;
        }

        if (commentText == null || commentText.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Comment cannot be empty");
            return;
        }

        commentsDAO commentDao = new commentsDAO(connection);
        try {
            Comment comment = new Comment();
            comment.setText(commentText);
            comment.setImage_id(imageId);
            comment.setUser_id(user.getId());
            comment.setPublication_date(new Date());
            commentDao.addComment(comment); 
            System.out.println("Comment added succesfully");
            response.sendRedirect(getServletContext().getContextPath() + "/GoToImagePage?Image_id=imageId&albumTitle=albumTitle");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error while adding comment: " + e.getMessage());
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	//consider that i cannot retriever userId from the image id
    	imageDAO imageDao = new imageDAO(connection);
    	commentsDAO commentsDao = new commentsDAO(connection);
    	String albumTitle = request.getParameter("albumTitle");
    	
    	int imageCreator = 0;
    	
    	int imageId;
        
        try {
            imageId = Integer.parseInt(request.getParameter("imageId"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid image ID format");
            return;
        }
        
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("User not logged in");
            return;
        }
        
    	try {
			imageCreator = imageDao.CheckCreator(imageId);
		} catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error adding the comment: " + e.getMessage());
		}
    	
    	if(user.getId() != imageCreator)
    	{
    		System.out.println("users not authorized to delete the image");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("User not logged in");
            return;
    	}
    	else
    	{
            try {
            	
            	commentsDao.deleteAllComment(imageId);
            	imageDao.deleteImage(imageId);
            	imageDao.DeleteFromAlbum(imageId, albumTitle);
            	
            	System.out.println("Comment succesfully deleted");
            	response.sendRedirect(getServletContext().getContextPath() + "/GoToAlbumPage?albumTitle=albumTitle");
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Error while deleting comment");
            }
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