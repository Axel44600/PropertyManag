package org.application.propertymanag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "locataire")
public class Locataire {

    @Id
    @SequenceGenerator(name="loc_seq", sequenceName="loc_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="loc_seq")
    @Column(name="id_loc", updatable = false)
    private Integer idLoc;
    private String nom;
    private String prenom;
    private String email;
    private String tel;
    private Integer solde;

    public Integer getIdLoc() {
        return idLoc;
    }

    public void setIdLoc(Integer idLoc) {
        this.idLoc = idLoc;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getSolde() {
        return solde;
    }

    public void setSolde(Integer solde) {
        this.solde = solde;
    }
}