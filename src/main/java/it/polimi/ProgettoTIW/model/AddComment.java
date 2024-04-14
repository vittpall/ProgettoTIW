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



import it.polimi.ProgettoTIW.beans.User;
import it.polimi.ProgettoTIW.beans.Comment;

import it.polimi.ProgettoTIW.DAO.commentsDAO;

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
            comment.setUser_id(user.getId()); // Assuming Comment has an authorId field
            commentDao.addComment(comment);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Comment added successfully");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error while adding comment");
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