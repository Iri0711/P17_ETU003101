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

public class FormDepServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = MySQLConnection.getConnection()) {
            List<Prevision> list;
            list = Prevision.findAll(connection, Prevision.class);
            request.setAttribute("listPrevision", list);
            RequestDispatcher disp = request.getRequestDispatcher("formDep.jsp");
            disp.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer prev = Integer.parseInt(request.getParameter("id_prev"));
        Double montant = Double.parseDouble(request.getParameter("montant"));

        try (Connection connection = MySQLConnection.getConnection()) {
            Prevision p = Prevision.findById(connection, Prevision.class, prev);
            double somme = Depense.sommeDep(connection, Depense.class, prev);
            if (p.getMontant() < somme + montant) {
                request.setAttribute("message", "le montant est invalide");
            } else {
                Depense newDep = new Depense(prev, montant);
                newDep.save(connection);
                request.setAttribute("message", "Depense ajouté avec succès !");
            }
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
        // Recharger la liste des départements et retourner au formulaire
        doGet(request, response);
    }
}