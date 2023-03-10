package org.application.propertymanag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appartement")
public class Appartement {
    @Id
    @SequenceGenerator(name="appart_seq", sequenceName="appart_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="appart_seq")
    @Column(name="id_appart", updatable = false)
    private Integer idAppart;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_loc", nullable = true)
    private Locataire idLoc;
    private String adresse;
    @Column(name = "adresse_comp")
    private String adresseComp;
    private String ville;
    @Column(name = "code_postal")
    private Integer codePostal;
    @Column(name = "montant_loyer")
    private Integer montantLoyer;
    @Column(name = "montant_charges")
    private Integer montantCharges;
    @Column(name = "montant_depot_garantie")
    private Integer montantDepotGarantie;
    @Column(name = "date_creation")
    private LocalDate dateCreation;
    @Column(name = "montant_frais_agence")
    private Integer montantFraisAgence;

    public Integer getIdAppart() {
        return idAppart;
    }

    public void setIdAppart(Integer idAppart) {
        this.idAppart = idAppart;
    }

    public Locataire getIdLoc() {
        return idLoc;
    }

    public void setIdLoc(Locataire idLoc) {
        this.idLoc = idLoc;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getAdresseComp() {
        return adresseComp;
    }

    public String getVille() {
        return ville;
    }

    public Integer getCodePostal() {
        return codePostal;
    }

    public Integer getMontantLoyer() {
        return montantLoyer;
    }

    public void setMontantLoyer(Integer montantLoyer) {
        this.montantLoyer = montantLoyer;
    }

    public Integer getMontantCharges() {
        return montantCharges;
    }

    public void setMontantCharges(Integer montantCharges) {
        this.montantCharges = montantCharges;
    }

    public Integer getMontantDepotGarantie() {
        return montantDepotGarantie;
    }

    public void setMontantDepotGarantie(Integer montantDepotGarantie) {
        this.montantDepotGarantie = montantDepotGarantie;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setMontantFraisAgence(Integer montantFraisAgence) {
        this.montantFraisAgence = montantFraisAgence;
    }
}