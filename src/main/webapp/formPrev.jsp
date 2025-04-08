<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Prevision" %>
<%@ page import="models.Depense" %>
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
        form {
            width: 300px;
            margin: auto;
        }
        label {
            font-weight: bold;
            display: block;
            margin-top: 10px;
        }
        input, select {
            width: 100%;
            padding: 8px;
            margin: 5px 0;
            box-sizing: border-box;
        }
        button {
            background-color: blue;
            color: white;
            padding: 10px;
            width: 100%;
            border: none;
            margin-top: 10px;
            cursor: pointer;
        }
        button:hover {
            background-color: darkblue;
        }
    </style>
</head>
<body>
    <a href="acceuil">index</a>
    <form action="formPrev" method="post">
        <div>
            <label for="lib">Libelle</label>
            <input type="text" name="libelle" id="lib" placeholder="Ex.Sakafo">
        </div>
        <div>
            <label for="montant">Montant</label>
            <input type="number" name="montant" id="montant">
        </div>
        <input type="submit" value="Ajouter">
    </form>

    <% String mess=(String)request.getAttribute("message"); %>
    <% if(mess!=null) { %>
        <p><%=mess %></p>
    <% } %>
</body>
</html>
