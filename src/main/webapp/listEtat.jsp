<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Depense" %>
<%@ page import="models.Etat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulaire Employé</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            padding: 20px;
        }
        td{
            padding: 30px;
        }
    </style>
</head>
<body>
    <a href="acceuil">index</a>
    <% List<Etat> etat=(List<Etat>) request.getAttribute("listEtat"); %>
    <table border="1" >
        <tr>
            <th>Description</th>
            <th>Prevision</th>
            <th>Depense</th>
            <th>Reste</th>
        </tr>
        <% for(Etat e:etat) { %>
            <tr>
                <td><%=e.getNom()%></td>
                <td><%=e.getPrev()%></td>
                <td><%=e.getDep()%></td>
                <td><%=e.getReste()%></td>
            </tr>
        <% } %>
    </table>
</body>
</html>
