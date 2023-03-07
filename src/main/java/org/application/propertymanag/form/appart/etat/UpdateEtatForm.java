package org.application.propertymanag.form.appart.etat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
public class UpdateEtatForm {
    private Integer idEtat;
    @NotNull(message = "Veuillez saisir une date pour l'état des lieux.")
    @Future(message = "La date de l'état des lieux ne peut pas être inférieur à la date de location.")
    private @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime date;
    private String remarques;

    public Integer getIdEtat() {
        return idEtat;
    }
    public void setIdEtat(Integer idEtat) {
        this.idEtat = idEtat;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public String getRemarques() {
        return remarques;
    }
    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }
}
