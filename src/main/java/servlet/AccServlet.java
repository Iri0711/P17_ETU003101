package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import connection.MySQLConnection;
import java.io.PrintWriter;
import models.Depense;
import models.Prevision;
import models.Etat;

public class AccServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = MySQLConnection.getConnection()) {
      
            RequestDispatcher disp = request.getRequestDispatcher("index.jsp");
            disp.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}