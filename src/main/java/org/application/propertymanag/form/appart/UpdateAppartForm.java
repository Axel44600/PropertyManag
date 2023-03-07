package org.application.propertymanag.form.appart;

import jakarta.validation.constraints.*;

public class UpdateAppartForm {

    private Integer idLoc;
    @NotEmpty(message = "Veuillez saisir l'adresse de l'appartement.")
    @Size(max = 50, message = "L'adresse ne peut pas dépasser 50 caractères.")
    private String adressForm;
    @NotNull(message = "Veuillez saisir le montant du loyer.")
    @Min(value = 0, message = "Les valeurs négatives ne sont pas autorisées.")
    private Integer loyerForm;
    @NotNull(message = "Veuillez saisir le montant des charges.")
    @Min(value = 0, message = "Les valeurs négatives ne sont pas autorisées.")
    private Integer chargesForm;
    @NotNull(message = "Veuillez saisir le montant du dépôt de garantie.")
    @Min(value = 0, message = "Les valeurs négatives ne sont pas autorisées.")
    private Integer depotGForm;


    public Integer getIdLoc() {
        return idLoc;
    }

    public void setIdLoc(Integer idLoc) {
        this.idLoc = idLoc;
    }

    public String getAdressForm() {
        return adressForm;
    }

    public void setAdressForm(String adressForm) {
        this.adressForm = adressForm;
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
}
