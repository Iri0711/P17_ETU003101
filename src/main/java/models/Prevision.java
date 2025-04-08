package models;

public class Prevision extends BaseModel {
    private String libelle;
    private Double montant;

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String name) {
        this.libelle = name;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double monta) {
        this.montant = monta;
    }

    public Prevision(){
        
    }

    public Prevision(String name,Double monta) {
        this.setLibelle(name);
        this.setMontant(monta);
    }
    
}
