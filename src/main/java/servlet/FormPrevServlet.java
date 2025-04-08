package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import connection.MySQLConnection;
import models.Prevision;
import models.Depense;

public class FormPrevServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = MySQLConnection.getConnection()) {
            RequestDispatcher disp = request.getRequestDispatcher("formPrev.jsp");
            disp.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupérer les données du formulaire
        String nom = request.getParameter("libelle");
        Double montant = Double.parseDouble(request.getParameter("montant"));

        try (Connection connection = MySQLConnection.getConnection()) {
            // Ajouter une nouvelle prévision
            Prevision newPrev = new Prevision(nom, montant);
            newPrev.save(connection);

            request.setAttribute("message", "Prevision ajouté avec succès !");
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }

        // Recharger la liste des départements et retourner au formulaire
        doGet(request, response);
    }
}