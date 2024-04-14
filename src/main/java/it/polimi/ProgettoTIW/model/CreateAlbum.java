package it.polimi.ProgettoTIW.model;

import it.polimi.ProgettoTIW.DAO.albumDAO;
import it.polimi.ProgettoTIW.DAO.imageDAO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


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
    String folderPath = "";
    
    
    public CreateAlbum()
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
            folderPath = getServletContext().getInitParameter("outputpath");

        } catch (ClassNotFoundException e) {
            throw new UnavailableException("Can't load database driver");
        } catch (SQLException e) {
            throw new UnavailableException("Couldn't get db connection");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    	int last_id = 0;
    	
        User user = (User) request.getSession().getAttribute("user");
        
        String title = request.getParameter("title");
         
        String description = request.getParameter("description");
        String uploaded_image_title = request.getParameter("uploaded_image_title");
        
		imageDAO imageDao = new imageDAO(connection);
        albumDAO albumDao = new albumDAO(connection);
        
        
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("User not logged in");
            return;
        }

        if (title == null || title.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Album title cannot be empty, title is required");
            return;
        }
        
        //create a new album adding only the title

        try {
            Album album = new Album();
            album.setTitle(title);
            album.setUser_id(user.getId());
            album.setCreation_Date(new Date());
            albumDao.createAlbum(album);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Album created successfully");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal server error while creating album");
        }
        
        
        String[] images_id = request.getParameterValues("selectedImages");
        
        int[] images_id_parsed = new int[images_id.length];
        
        // Convert each string element to an integer and store it in the int array
        try {
        	for (int i = 0; i < images_id.length; i++) {
        		images_id_parsed[i] = Integer.parseInt(images_id[i]);
        	}
        }
        catch (NumberFormatException e)
        {
        	System.err.println("An error occurs while parsing the array");
        	e.printStackTrace();
        }
        
        //TODO: another way to implement the view was to create a dedicated page for adding and uploading images to the previously created album. Every time a photo is uploaded the form used to select photos.
        
        if (images_id.length == 0 || images_id == null)
        	System.out.println("No images selected");
        else
        {
        	try {
        		for(int i = 0; i < images_id.length; i++)
        		{
        			albumDao.AddImagesToAlbum(images_id_parsed[i], user.getId(), title);
        		}
        	}
        	catch (SQLException e)
        	{
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Internal server error while adding images to the album");
        	}
        }
        
        
        Part filePart = request.getPart("file");
        
		if (filePart == null || filePart.getSize() <= 0) {
			System.out.println("No image uploaded");
		}
		else
		{
			String contentType = filePart.getContentType();
			System.out.println("Type " + contentType);
			if (!contentType.startsWith("image")) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File format not permitted");
				return;
			}
			
			String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
			System.out.println("Filename: " + fileName);
			
			//TODO deal with possible duplicates in the same. I retrieve the last image_id avaiable in the images db
			

			
			try
			{
				last_id = imageDao.RetrieveNextImageId();
			}
			catch (SQLException e)
			{
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("An error occurs while retriving the last image id");
			}
			
			
			
			String outputPath = folderPath + fileName + last_id;
			System.out.println("Output path: " + outputPath);

			File file = new File(outputPath);
			
			try (InputStream fileContent = filePart.getInputStream()) {
				// TODO: WHAT HAPPENS IF A FILE WITH THE SAME NAME ALREADY EXISTS?
				// you could override it, send an error or 
				// rename it, for example, if I need to upload images to an album, and for each image I also save other data, I could save the image as {image_id}.jpg using the id of the db

				Files.copy(fileContent, file.toPath());
				System.out.println("File saved correctly!");

				response.sendRedirect("ShowImage?filename=" + fileName);
			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while saving file");
			}
			
			
			Image ImageToAdd = new Image();
			ImageToAdd.setCreation_Date(new Date());
			ImageToAdd.setDescription(description);
			ImageToAdd.setSystem_Path(outputPath);
			ImageToAdd.setTitle(uploaded_image_title);
			//i dont pass the image id because it's meant to be auto-inserted
			
			try
			{
				imageDao.addImage(ImageToAdd);
				albumDao.AddImagesToAlbum(last_id, user.getId(), title);
			}
			catch (SQLException e)
			{
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("An error occurs while adding to the db the uploaded image");
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
