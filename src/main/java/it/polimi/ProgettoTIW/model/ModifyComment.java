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
import it.polimi.ProgettoTIW.DAO.commentsDAO;


@WebServlet("/ModifyComment")
public class ModifyComment extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    
    public ModifyComment()
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
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("User not logged in");
            return;
        }

        int commentId;
        try {
            commentId = Integer.parseInt(request.getParameter("commentId"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid comment ID format");
            return;
        }

        String newText = request.getParameter("newText");
        if (newText == null || newText.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Comment text cannot be empty");
            return;
        }

        commentsDAO commentDao = new commentsDAO(connection);
        try {
            // Check if the current user is authorized to modify the comment
            int authorId = commentDao.getAuthorIdByCommentId(commentId);
            if (authorId != user.getId()) { // Assuming User has a getId() method
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().println("User is not authorized to modify this comment");
                return;
            }

            boolean updated = commentDao.updateComment(commentId, newText);
            if (!updated) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Unable to update the comment");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Comment updated successfully");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error while updating comment");
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