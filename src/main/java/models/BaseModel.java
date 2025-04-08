package models;

import com.sun.source.doctree.SummaryTree;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de base pour les entités avec des méthodes CRUD simples.
 * Les entités concrètes hériteront de cette classe.
 */
public  class BaseModel {

    // Identifiant de l'entité
    private Integer id;

    public BaseModel(){

    }

    public BaseModel(Integer id){
        setId(id);
    }

    /**
     * Getter pour l'identifiant de l'entité.
     * 
     * @return L'identifiant
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter pour l'identifiant de l'entité.
     * 
     * @param id L'identifiant à définir
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retourne le nom de la table correspondant à l'entité.
     * Par défaut, c'est le nom de la classe en minuscules.
     * 
     * @return Le nom de la table
     */
    protected String getTableName() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    /**
     * Sauvegarde l'entité dans la base de données (insertion ou mise à jour).
     * 
     * @param connection La connexion à la base de données
     * @return L'entité sauvegardée
     * @throws Exception En cas d'erreur lors de la sauvegarde
     */
    public BaseModel save(Connection connection) throws Exception {
        if (connection == null) {
            throw new IllegalArgumentException("La connexion à la base de données ne peut pas être nulle.");
        }

        Field[] fields = this.getAllFields();

        if (getId() == null) {
            // Insertion
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();
            List<Object> paramValues = new ArrayList<>();

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();

                if (!fieldName.equals("id")) {
                    Object value = field.get(this);

                    if (columns.length() > 0) {
                        columns.append(", ");
                    }
                    columns.append(fieldName.toLowerCase());

                    if (values.length() > 0) {
                        values.append(", ");
                    }
                    values.append("?");

                    paramValues.add(value);
                }
            }

            String sql = "INSERT INTO " + getTableName() + " (" + columns + ") VALUES (" + values + ")";

            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                setParameters(stmt, paramValues);
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        setId(generatedKeys.getInt(1));
                    }
                }
            }
            catch(Exception e){
                throw  e;
            }
        } else {
            // Mise à jour
            StringBuilder updates = new StringBuilder();
            List<Object> paramValues = new ArrayList<>();

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();

                if (!fieldName.equals("id")) {
                    Object value = field.get(this);

                    if (updates.length() > 0) {
                        updates.append(", ");
                    }
                    updates.append(fieldName.toLowerCase() + " = ?");
                    paramValues.add(value);
                }
            }

            String sql = "UPDATE " + getTableName() + " SET " + updates + " WHERE id = ?";
            paramValues.add(getId());

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                setParameters(stmt, paramValues);
                stmt.executeUpdate();
            }
        }

        return this;
    }

    /**
     * Supprime l'entité de la base de données.
     * 
     * @param connection La connexion à la base de données
     * @throws Exception En cas d'erreur lors de la suppression
     */
    public void delete(Connection connection) throws Exception {
        if (connection == null) {
            throw new IllegalArgumentException("La connexion à la base de données ne peut pas être nulle.");
        }

        if (getId() == null) {
            throw new IllegalStateException("Impossible de supprimer une entité sans identifiant.");
        }

        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, getId());
            stmt.executeUpdate();
            stmt.close();
        }
    }

    /**
     * Trouve une entité par son identifiant.
     * 
     * @param connection  La connexion à la base de données
     * @param entityClass La classe de l'entité
     * @param id          L'identifiant de l'entité
     * @return L'entité trouvée ou null si non trouvée
     * @throws Exception En cas d'erreur lors de la recherche
     */
    public static <E extends BaseModel> E findById(Connection connection, Class<E> entityClass, Integer id)
            throws Exception {
        if (connection == null) {
            throw new IllegalArgumentException("La connexion à la base de données ne peut pas être nulle.");
        }

        String tableName = entityClass.getSimpleName().toLowerCase();
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs, entityClass);
                }
            }
        }
        catch(Exception e){
            throw  e;
        }

        return null;
    }

    public static <E extends BaseModel> List<E> findByColumnId(Connection connection, Class<E> entityClass,Integer id) throws Exception {
        if (connection == null) {
            throw new IllegalArgumentException("La connexion à la base de données ne peut pas être nulle.");
        }

        
        String sql = "SELECT * FROM depense Where id_prev="+id+"";
        List<E> entities = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs, entityClass));
                System.out.println("PASTA:"+mapResultSetToEntity(rs, entityClass));
            }
        }
        return entities;

    }

    public static <E extends BaseModel> double sommeDep(Connection connection, Class<E> entityClass,Integer id) throws Exception {
        if (connection == null) {
            throw new IllegalArgumentException("La connexion à la base de données ne peut pas être nulle.");
        }

        
        String sql = "SELECT SUM(montant) as somme FROM depense Where id_prev="+id+"";
        double rep=0;
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                rep=rs.getDouble("somme");
            }
        }
        return rep;

    }

    /**
     * Retourne toutes les entités de la table.
     * 
     * @param connection  La connexion à la base de données
     * @param entityClass La classe de l'entité
     * @return Une liste d'entités
     * @throws Exception En cas d'erreur lors de la récupération
     */
    public static <E extends BaseModel> List<E> findAll(Connection connection, Class<E> entityClass) throws Exception {
        if (connection == null) {
            throw new IllegalArgumentException("La connexion à la base de données ne peut pas être nulle.");
        }

        String tableName = entityClass.getSimpleName().toLowerCase();
        String sql = "SELECT * FROM " + tableName;
        List<E> entities = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs, entityClass));
                System.out.println("PASTA:"+mapResultSetToEntity(rs, entityClass));
            }
        }

        

        return entities;
    }

    public Field[] getAllFields() {
        Class<?> class1 =this.getClass();
        List<Field> list = new ArrayList<Field>();
        while (class1 != null) { 
            Field[] foFields = class1.getDeclaredFields();
            for (Field field : foFields) {
                list.add(field);
            }
            class1 = class1.getSuperclass();
        }
        return list.toArray(new Field[list.size()]);
    }
    /**
     * Mappe un ResultSet à une entité.
     * 
     * @param rs          Le ResultSet à mapper
     * @param entityClass La classe de l'entité
     * @return L'entité créée à partir du ResultSet
     * @throws Exception En cas d'erreur lors du mapping
     */
    protected static <E extends BaseModel> E mapResultSetToEntity(ResultSet rs, Class<E> entityClass)
            throws Exception {
        E entity=entityClass.getDeclaredConstructor().newInstance();
       Field [] fields=entity.getAllFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName().toLowerCase();

            try {
                Object value = rs.getObject(fieldName);
                if (value != null) {
                    if (field.getType() == Long.class || field.getType() == long.class) {
                        if (value instanceof Number) {
                            field.set(entity, ((Number) value).longValue());
                        } else {
                            field.set(entity, Long.parseLong(value.toString()));
                        }
                    } else if (field.getType() == Integer.class || field.getType() == int.class) {
                        if (value instanceof Number) {
                            field.set(entity, ((Number) value).intValue());
                        } else {
                            field.set(entity, Integer.parseInt(value.toString()));
                        }
                    } else if (field.getType() == Double.class || field.getType() == double.class) {
                        if (value instanceof Number) {
                            field.set(entity, ((Number) value).doubleValue());
                        } else {
                            field.set(entity, Double.parseDouble(value.toString()));
                        }
                    } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                        if (value instanceof Boolean) {
                            field.set(entity, value);
                        } else if (value instanceof Number) {
                            field.set(entity, ((Number) value).intValue() != 0);
                        } else {
                            field.set(entity, Boolean.parseBoolean(value.toString()));
                        }
                    } else {
                        field.set(entity, value);
                    }
                }
            } catch (SQLException e) {
                // Ignorer les champs qui ne correspondent pas aux colonnes
            }
        }

        return entity;
    }

    /**
     * Configure les paramètres d'une PreparedStatement.
     * 
     * @param stmt   La PreparedStatement à configurer
     * @param params Les paramètres à définir
     * @throws SQLException En cas d'erreur lors de la configuration
     */
    private void setParameters(PreparedStatement stmt, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i));
        }
    }

    public void update(Connection connection, Integer id, BaseModel newEmp) throws Exception {
        if (connection == null) {
            throw new IllegalArgumentException("La connexion à la base de données ne peut pas être nulle.");
        }
    
        if (id == null) {
            throw new IllegalArgumentException("L'identifiant ne peut pas être nul.");
        }
    
        if (newEmp == null) {
            throw new IllegalArgumentException("Les nouvelles valeurs ne peuvent pas être nulles.");
        }
    
        StringBuilder updates = new StringBuilder();
        List<Object> paramValues = new ArrayList<>();
        System.out.println("EMp:"+newEmp.getId());
        Field[] fields = this.getAllFields();  // Récupérer tous les champs de l'entité
        Field[] newEmpFields = newEmp.getAllFields();  // Récupérer tous les champs de newEmp
    
        for (Field newEmpField : newEmpFields) {
            newEmpField.setAccessible(true);
            String fieldName = newEmpField.getName();
    
            // Chercher le champ correspondant dans l'entité actuelle
            for (Field field : fields) {
                field.setAccessible(true);
    
                if (field.getName().equals(fieldName) && !fieldName.equals("id")) {  // Ne pas inclure l'id dans les mises à jour
                    Object newValue = newEmpField.get(newEmp);
    
                    // Comparer les anciennes et nouvelles valeurs
                    Object currentValue = field.get(this);
                    if ((currentValue == null && newValue != null) || (currentValue != null && !currentValue.equals(newValue))) {
                        if (updates.length() > 0) {
                            updates.append(", ");
                        }
                        updates.append(field.getName().toLowerCase()).append(" = ?");
                        paramValues.add(newValue);
                    }
                    break;
                }
            }
        }
    
        if (updates.length() == 0) {
            throw new IllegalArgumentException("Aucune valeur à mettre à jour.");
        }
    
        String sql = "UPDATE " + getTableName() + " SET " + updates + " WHERE id = ?";
        paramValues.add(id);
    
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setParameters(stmt, paramValues);
            stmt.executeUpdate();
        }
    }
    
    
}