package models;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class Etat extends BaseModel {
    private String nom;
    private Double montant_prev;
    private Double montant_dep;
    private Double reste;

    public Etat(){

    };
    private Etat(String n,Double p,Double d,Double r)
    {
        this.nom=n;
        this.montant_prev=p;
        this.montant_dep=d;
        this.reste=r;
    }

    public String getNom(){
        return  this.nom;
    }
    public Double getPrev(){
        return  this.montant_prev;
    }
    public Double getDep(){
        return  this.montant_dep;
    }
    public Double getReste(){
        return  this.reste;
    }

    public static  List<Etat> listEtat(Connection connection)
    {
        List<Etat> rep=new ArrayList<Etat>();
        try {
            List<Prevision> prev=Prevision.findAll(connection, Prevision.class);
            
            for(Prevision p:prev){
                Double m_p=p.getMontant();
                Double reste=m_p;
                List<Depense> dep=Depense.findByColumnId(connection, Depense.class, p.getId());
                if(dep.size()!=0){
                    for(Depense d:dep)
                    {
                        reste =reste-d.getMontant();
                        Etat e=new Etat(p.getLibelle(),m_p,d.getMontant(),reste);
                        rep.add(e);
                    }
                    
                }else{
                double d=(double) 0;
                Etat e=new Etat(p.getLibelle(),m_p,d,reste);
                rep.add(e);
                }
                

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return  rep;
    } 
}
