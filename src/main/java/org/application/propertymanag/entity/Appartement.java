package org.application.propertymanag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
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
    private Integer id_appart;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_loc", nullable = true)
    private Locataire id_loc;
    private String adresse;
    private String adresse_comp;
    private String ville;
    private Integer code_postal;
    private Integer montant_loyer;
    private Integer montant_charges;
    private Integer montant_depot_garantie;
    private LocalDate date_creation;
    private Integer montant_frais_agence;

    public Integer getId_appart() {
        return id_appart;
    }

    public void setId_appart(Integer id_appart) {
        this.id_appart = id_appart;
    }

    public Locataire getId_loc() {
        return id_loc;
    }

    public void setId_loc(Locataire id_loc) {
        this.id_loc = id_loc;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getAdresse_comp() {
        return adresse_comp;
    }

    public void setAdresse_comp(String adresse_comp) {
        this.adresse_comp = adresse_comp;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Integer getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(Integer code_postal) {
        this.code_postal = code_postal;
    }

    public Integer getMontant_loyer() {
        return montant_loyer;
    }

    public void setMontant_loyer(Integer montant_loyer) {
        this.montant_loyer = montant_loyer;
    }

    public Integer getMontant_charges() {
        return montant_charges;
    }

    public void setMontant_charges(Integer montant_charges) {
        this.montant_charges = montant_charges;
    }

    public Integer getMontant_depot_garantie() {
        return montant_depot_garantie;
    }

    public void setMontant_depot_garantie(Integer montant_depot_garantie) {
        this.montant_depot_garantie = montant_depot_garantie;
    }

    public LocalDate getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(LocalDate date_creation) {
        this.date_creation = date_creation;
    }

    public Integer getMontant_frais_agence() {
        return montant_frais_agence;
    }

    public void setMontant_frais_agence(Integer montant_frais_agence) {
        this.montant_frais_agence = montant_frais_agence;
    }
}