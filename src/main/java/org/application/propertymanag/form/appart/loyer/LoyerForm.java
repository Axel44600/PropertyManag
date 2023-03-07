package org.application.propertymanag.form.appart.loyer;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class LoyerForm {

    private Integer idLoyer;
    private Integer idAppart;
    @NotNull(message = "Veuillez saisir le montant du loyer.")
    private Integer montant;

    @NotNull(message = "Veuillez renseigner le statut du loyer.")
    private Boolean statut;
    @NotNull(message = "Veuillez saisir la date du loyer.")
    private @DateTimeFormat(pattern= "yyyy-MM-dd") LocalDate date;
    @NotNull(message = "Veuillez saisir l'origine du paiement.")
    @Pattern(regexp = "^[A-Za-z]*$", message = "Les chiffres ainsi que les caractères spéciaux ne sont pas autorisés.")
    private String origine;

    public Integer getIdLoyer() {
        return idLoyer;
    }

    public void setIdLoyer(Integer idLoyer) {
        this.idLoyer = idLoyer;
    }

    public Integer getIdAppart() {
        return idAppart;
    }

    public void setIdAppart(Integer idAppart) {
        this.idAppart = idAppart;
    }

    public Integer getMontant() {
        return montant;
    }

    public void setMontant(Integer montant) {
        this.montant = montant;
    }

    public Boolean getStatut() {
        return statut;
    }

    public void setStatut(Boolean statut) {
        this.statut = statut;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getOrigine() {
        return origine;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }
}
