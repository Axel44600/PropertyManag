package org.application.propertymanag.form.appart;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
public class CreateAppartForm {

    @NotEmpty(message = "Veuillez saisir l'adresse de l'appartement.")
    @Size(max = 50, message = "L'adresse ne peut pas dépasser 50 caractères.")
    private String adressForm;
    @Size(max = 20, message = "Le complèment d'adresse ne peut pas dépasser 20 caractères.")
    private String adressCompForm;
    @NotEmpty(message = "Veuillez saisir une ville.")
    @Size(max = 45, message = "Le nom de la ville ne peut pas dépasser 45 caractères.")
    private String villeForm;
    @NotNull(message = "Veuillez saisir un code postal.")
    @Min(value = 01000, message = "Le code postal saisie est invalide.")
    @Max(value = 99000, message = "Le code postal saisie est invalide.")
    private Integer cPostalForm;
    @NotNull(message = "Veuillez saisir le montant du loyer.")
    @Min(value = 0, message = "Les valeurs négatives ne sont pas autorisées.")
    private Integer loyerForm;
    @NotNull(message = "Veuillez saisir le montant des charges.")
    @Min(value = 0, message = "Les valeurs négatives ne sont pas autorisées.")
    private Integer chargesForm;
    @NotNull(message = "Veuillez saisir le montant du dépôt de garantie.")
    @Min(value = 0, message = "Les valeurs négatives ne sont pas autorisées.")
    private Integer depotGForm;

    @NotNull(message = "Veuillez saisir la date de construction de l'appartement.")
    @Past(message="La date de construction de l'appartement doit être inférieur à aujourd'hui.")
    private @DateTimeFormat(pattern= "yyyy-MM-dd") LocalDate dateForm;

    public String getAdressForm() {
        return adressForm;
    }

    public void setAdressForm(String adressForm) {
        this.adressForm = adressForm;
    }

    public String getAdressCompForm() {
        return adressCompForm;
    }

    public void setAdressCompForm(String adressCompForm) {
        this.adressCompForm = adressCompForm;
    }
    public String getVilleForm() {
        return villeForm;
    }

    public void setVilleForm(String villeForm) {
        this.villeForm = villeForm;
    }

    public Integer getcPostalForm() {
        return cPostalForm;
    }

    public void setcPostalForm(Integer cPostalForm) {
        this.cPostalForm = cPostalForm;
    }

    public Integer getLoyerForm() {
        return loyerForm;
    }

    public void setLoyerForm(Integer loyerForm) {
        this.loyerForm = loyerForm;
    }

    public Integer getChargesForm() {
        return chargesForm;
    }

    public void setChargesForm(Integer chargesForm) {
        this.chargesForm = chargesForm;
    }

    public Integer getDepotGForm() {
        return depotGForm;
    }

    public void setDepotGForm(Integer depotGForm) {
        this.depotGForm = depotGForm;
    }

    public LocalDate getDateForm() {
        return dateForm;
    }

    public void setDateForm(LocalDate dateForm) {
        this.dateForm = dateForm;
    }
}
