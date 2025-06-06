package it.polimi.ProgettoTIW.model;

import it.polimi.ProgettoTIW.DAO.albumDAO;
import it.polimi.ProgettoTIW.DAO.imageDAO;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;

@WebServlet("/CreateAlbum")
@MultipartConfig
public class CreateAlbum extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private String folderPath = "";
    private String folderPathToCopyFrom = "";

    public void init() throws ServletException {
        try {
            ServletContext context = getServletContext();
            String driver = context.getInitParameter("dbDriver");
            String url = context.getInitParameter("dbUrl");
            String user = context.getInitParameter("dbUser");
            String password = context.getInitParameter("dbPassword");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);

            // Ensure the directory path is properly initialized and accessible
            folderPath = context.getRealPath("/images/");
            folderPathToCopyFrom = getServletContext().getInitParameter("outputpath");
            File imagesDir = new File(folderPath);
            if (!imagesDir.exists()) {
                imagesDir.mkdirs();
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new UnavailableException("Cannot load database driver or establish connection: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = getServletContext().getContextPath() + "/GoToHomePage";
        String[] selectedImages = request.getParameterValues("selectedImages");
        String image_title = request.getParameter("image_title");
        String description = request.getParameter("description");
        
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

        //add images taken from the checkbox
        Album album = new Album();
        album.setTitle(title);
        album.setUser_id(user.getId());
        album.setUsername(user.getUsername());
        album.setCreation_Date(LocalDateTime.now());
        albumDAO albumDao = new albumDAO(connection);
        try {
            albumDao.createAlbum(album);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error while creating album: " + e.getMessage());
            return;
        }
        
        if(selectedImages != null)
        {
            int[] Images_Id = new int[selectedImages.length];
            try {
    	        	int i;
    		        for(i = 0; i < Images_Id.length; i++)
    		        {
    					Images_Id[i] = Integer.parseInt(selectedImages[i]);
    					System.out.println(Images_Id[i]);
    		        }
    		        
              	
    		        imageDAO imageDao = new imageDAO(connection);
                	for(i = 0; i < selectedImages.length; i++)
                	{
                		Image ImageToAdd = imageDao.findImageById(Images_Id[i]);
                		imageDao.AddImagesToAlbum(ImageToAdd.getImage_Id(), user.getId(), title);
                	}
                	
            }
            catch (NumberFormatException e)
            {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("An error occurs while parsing the selected images vector" + e.getMessage());
                return;
            }
            
	        catch (SQLException e) {
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            response.getWriter().println("Error while creating album: " + e.getMessage());
	            return;
	        }
	       
        }

        handleImageUpload(request, response, user);
        response.sendRedirect(path);
    }

    private void handleImageUpload(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        Part filePart = request.getPart("file"); // Assume the file input name is "file"
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            String outputPath = folderPath + uniqueFileName;
            String outputPathBackup = folderPathToCopyFrom + uniqueFileName;
            File file = new File(outputPath);
            File fileBackup = new File(outputPathBackup);

            try (InputStream input = filePart.getInputStream()) {
     //       	Files.createDirectories(Paths.get(outputPath).getParent()); // Ensure parent directories exist
                Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                storeImageDetails(fileName, "/images/" + uniqueFileName, user.getId(), request);
            } catch (IOException e) {
                throw new ServletException("Error while saving file: " + e.getMessage(), e);
            }
            
            try (InputStream input = filePart.getInputStream()) {
     //       	Files.createDirectories(Paths.get(outputPath).getParent()); // Ensure parent directories exists   
                Files.copy(input, fileBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new ServletException("Error while saving file: " + e.getMessage(), e);
            }
        }
    }

    private void storeImageDetails(String fileName, String path, int userId, HttpServletRequest request)
            throws ServletException {
        String image_title = request.getParameter("image_title");
        String description = request.getParameter("description");
        imageDAO imageDao = new imageDAO(connection);
        Image image = new Image();
        image.setCreation_Date(new Date());
        image.setTitle(image_title);
        image.setDescription(description);
        image.setSystem_Path(path);

        try {
            imageDao.addImage(image);
            int imageId = imageDao.RetrieveLastImageId();
            imageDao.AddImagesToAlbum(imageId, userId, request.getParameter("title"));
        } catch (SQLException e) {
            throw new ServletException("Error while storing image details or linking image to album: " + e.getMessage(), e);
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
