package it.polimi.ProgettoTIW.model;

package it.polimi.tiw.missions.controllers;

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
import it.polimi.tiw.missions.beans.Album;
import it.polimi.tiw.missions.beans.Image;
import it.polimi.tiw.missions.dao.AlbumDAO;
import it.polimi.tiw.missions.dao.ImageDAO;
import it.polimi.tiw.missions.utils.ConnectionHandler;

@WebServlet("/Album")
public class GoToAlbumPage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    
    
    public GoToAlbumPage()
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
        int albumId;
        try {
            albumId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid album ID format");
            return;
        }

        AlbumDAO albumDao = new AlbumDAO(connection);
        ImageDAO imageDao = new ImageDAO(connection);
        Album album;
        List<Image> images;

        try {
            album = albumDao.findAlbumById(albumId);
            if (album == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("Album not found");
                return;
            }

            images = imageDao.findImagesByAlbum(albumId);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal server error while retrieving album data");
            return;
        }

        request.setAttribute("album", album);
        request.setAttribute("images", images);

        RequestDispatcher dispatcher = request.getRequestDispatcher("albumView.jsp"); // Replace with actual view page
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
