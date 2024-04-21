package it.polimi.ProgettoTIW.model;

import it.polimi.ProgettoTIW.DAO.albumDAO;
import it.polimi.ProgettoTIW.DAO.imageDAO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import it.polimi.ProgettoTIW.beans.Album;
import it.polimi.ProgettoTIW.beans.Image;
import it.polimi.ProgettoTIW.beans.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

@WebServlet("/CreateAlbum")
@MultipartConfig
public class CreateAlbum extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private String folderPath = "";

    public CreateAlbum() {
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
            folderPath = context.getRealPath("/");
        } catch (ClassNotFoundException | SQLException e) {
            throw new UnavailableException("Cannot load database driver or establish connection: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
		String path = getServletContext().getContextPath() + "/GoToHomePage";

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("User not logged in");
            return;
        }

        String title = request.getParameter("title");
        if (title == null || title.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Album title cannot be empty");
            return;
        }

        try {
            Album album = new Album();
            album.setTitle(title);
            album.setUser_id(user.getId());
            album.setCreation_Date(new Date());
            album.setUsername(user.getUsername());
            albumDAO albumDao = new albumDAO(connection);
            albumDao.createAlbum(album);
            System.out.println("Album created");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error while creating album: " + e.getMessage());
            return;
        }

        try
        {
        	handleImageUpload(request, response, user, title);
        	response.sendRedirect(path);
        }
        catch(ServletException e1)
        {
        	e1.printStackTrace();
        	response.sendError(HttpServletResponse.SC_CONFLICT, "An error occurs");
        }
        catch(IOException e2)
        {
        	response.sendError(HttpServletResponse.SC_CONFLICT, "An error occurs while uploading the file");
        }
        
        
        
    }

    private void handleImageUpload(HttpServletRequest request, HttpServletResponse response, User user, String title)
            throws ServletException, IOException {
        Part filePart = request.getPart("file");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String outputPath = getUniqueFilePath(folderPath, fileName);
            File file = new File(outputPath);
            if (!file.exists()) {
                try (InputStream fileContent = filePart.getInputStream()) {
                    Files.copy(fileContent, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    storeImageDetails(filePart, fileName, outputPath, title, user.getId());
                   // response.sendRedirect("ShowImage?filename=" + fileName);
                } catch (IOException e) {
                    throw new ServletException("Error while saving file: " + e.getMessage(), e);
                }
            } else {
                response.sendError(HttpServletResponse.SC_CONFLICT, "File already exists.");
            }
        } else {
            System.out.println("No image uploaded");
        }
    }

    private String getUniqueFilePath(String folderPath, String fileName) {
        long timestamp = System.currentTimeMillis();
        return folderPath +'/'+ timestamp + "_" + fileName;
    }

    private void storeImageDetails(Part filePart, String fileName, String path, String title, int userId)
            throws ServletException {
        imageDAO imageDao = new imageDAO(connection);
        Image image = new Image();
        image.setCreation_Date(new Date());
        image.setTitle(fileName);
        image.setSystem_Path(path);
        //String contextPath = getServletContext().getContextPath();
        //image.setSystem_Path(contextPath + "/css/" + fileName);
        
        System.out.println(image.getSystem_Path());
        image.setDescription(filePart.getContentType()); // Example usage, replace with actual description if available

        try {
            imageDao.addImage(image);
            int imageId = imageDao.RetrieveNextImageId(); // Assume last added image's ID
            imageDao.AddImagesToAlbum(imageId, userId, title);
        } catch (SQLException e) {
            throw new ServletException("Error while storing image details or linking image to album: " + e.getMessage(),e);
            
        }
    }

    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

