package it.polimi.ProgettoTIW.model;

import javax.servlet.annotation.WebServlet;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.ProgettoTIW.beans.Album;
import it.polimi.ProgettoTIW.beans.Image;
import it.polimi.ProgettoTIW.beans.User;

import it.polimi.ProgettoTIW.DAO.albumDAO;
import it.polimi.ProgettoTIW.DAO.imageDAO;
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
        
        String folderPath = getServletContext().getRealPath("/images/");
        String folderPathToCopyFrom = getServletContext().getInitParameter("outputpath");

        File imagesDir = new File(folderPath);
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }
         
        Path sourceDir = Paths.get(folderPathToCopyFrom);
        Path targetDir = Paths.get(folderPath);
         
        CopyFileToDeployedFolder(sourceDir, targetDir);
        
        
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<User> UserList;
        
        String loginpath = getServletContext().getContextPath() + "/index.html";
        
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		}
        if (user == null) {
            response.sendRedirect(loginpath);
            return;
        }
        
        userDAO userDao = new userDAO(connection);
        imageDAO imageDao = new imageDAO(connection);
        albumDAO albumDao = new albumDAO(connection);
        List<Image> imagesUser = new ArrayList<>();
        List<Album> UserAlbum = new ArrayList<>();

        Map<User, List<Album>> OtherUserAlbum = new HashMap<>();
        
        try {
        	
        	UserList = userDao.getAllUsers();
            imagesUser = imageDao.RetrieveAllImagesByUser(user);
            UserAlbum = albumDao.findAlbumsByUser(user.getUsername());
            //TODO verify the order of the user inside the userlist, to solve that annoying problem related to refreshing the page
            for (User u : UserList) {
                if (!u.getUsername().equals(user.getUsername())) {
                    List<Album> albums = albumDao.findAlbumsByUser(u.getUsername());
                    OtherUserAlbum.put(u, albums);
                  //  System.out.println("User: " + u.getUsername() + " Albums: " + albums.size()); // Debug statement
                }
            }
        } catch (SQLException e) {
        		
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Unable to retrieve albums"+ e.getMessage());
            return;
        }	
        
        String path = "/WEB-INF/HomePage.html";
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("ImagesUser", imagesUser);
		ctx.setVariable("UserAlbum", UserAlbum);
		ctx.setVariable("OtherUserAlbum", OtherUserAlbum);
		templateEngine.process(path, ctx, response.getWriter());
        
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException sqle) {
        }
    }
    
    private void CopyFileToDeployedFolder(Path sourceDir, Path targetDir)
    {
        try {
            // Create the target directory if it doesn't exist
            Files.createDirectories(targetDir);

            // Copy all files from the source directory to the target directory
            Files.walk(sourceDir)
                 .filter(Files::isRegularFile)
                 .forEach(source -> {
                     Path target = targetDir.resolve(sourceDir.relativize(source));
                     try {
                         Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                     } catch (IOException e) {
                         System.err.println("Failed to copy " + source + " to " + target + ": " + e);
                     }
                 });

            System.out.println("All files copied successfully!");
        } catch (IOException e) {
            System.err.println("Failed to copy files: " + e);
        }
    }
}