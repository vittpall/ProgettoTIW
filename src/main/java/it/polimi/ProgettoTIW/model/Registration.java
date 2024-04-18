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
//import java.time.LocalDateTime;
//import java.sql.Date;
import java.util.Date;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polimi.ProgettoTIW.beans.User;
import it.polimi.ProgettoTIW.DAO.userDAO;


@WebServlet("/Register")
public class Registration extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;

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
        String usrn = request.getParameter("username");
        String email = request.getParameter("email");
        String pwd1 = request.getParameter("password");
        String pwd2 = request.getParameter("confirmPassword");
        boolean testpwd = true;
        boolean testemail = true;
        boolean testusnunivocity = true;
        int testusnunivocity_count = 0;

        //LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDateTime = new Date();
        User new_user = new User();

      
        //check if pass and rep pass are equals
        testpwd = pwd1.equals(pwd2);
        if (!testpwd) {
            new_user.setEmail(email);
            new_user.setPassword(pwd1);
            new_user.setUsername(usrn);
            request.getSession().setAttribute("user", new_user);
            request.getSession().setAttribute("pwdError", "The pass aren't equals");
        }

        testemail = CheckEmail(email);
        //check email syntax
        if (!testemail) {
            new_user.setEmail(email);
            new_user.setPassword(pwd1);
            new_user.setUsername(usrn);
            request.getSession().setAttribute("user", new_user);
            request.getSession().setAttribute("emailError", "The email syntax isnt right");
        }

        userDAO userDao = new userDAO(connection);
        try {
            testusnunivocity_count = userDao.checkUsrn(usrn);
            if (testusnunivocity_count > 0) {
                testusnunivocity = false;
                request.getSession().setAttribute("usnError", "The usn already exists");
            }

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Internal server error, retry later");
            e.printStackTrace();
            return;
        }

        if (testpwd && testemail && testusnunivocity) {
            try {
                new_user.setEmail(email);
                new_user.setPassword(pwd1);
                new_user.setUsername(usrn);
                new_user.setReg_Date(currentDateTime);
                userDao.registerUser(new_user);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("Registration successful");
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Internal server error");
                e.printStackTrace();
            }

            response.sendRedirect(getServletContext().getContextPath() + "/GoToHomePage");
        } else {
            response.sendRedirect(getServletContext().getContextPath() + "/Registration.html");
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
    
    private boolean CheckEmail(String email) {
        //Regular expression for basic email validation
        //i suppose that emails which contain '.' or '-' are acceptable
        String pattern = "^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }
}