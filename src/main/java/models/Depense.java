package models;

import connection.MySQLConnection;
import java.sql.SQLException;

public class Depense extends BaseModel{
    Integer id_prev;
    Double montant;

    public Depense(){

    }

    public Depense(Integer pre,Double monta){
        this.setIdPrev(pre);
        this.setMontant(monta);
    }

    public Integer getIdPrev() {
        return id_prev;
    }

    public void setIdPrev(Integer id) {
        this.id_prev = id;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double monta) {
        this.montant = monta;
    }

    public Prevision getPrevision() throws SQLException, Exception{
        return Prevision.findById(MySQLConnection.getConnection(), Prevision.class, this.id_prev);
    }

    
}
