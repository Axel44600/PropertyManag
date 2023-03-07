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
@Table(name = "bilan")
public class Bilan {

    @Id
    @SequenceGenerator(name="bilan_seq", sequenceName="bilan_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="bilan_seq")
    @Column(name="id_bilan", updatable = false)
    private Integer idBilan;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_appart", nullable = false)
    private Appartement idAppart;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_loc", nullable = false)
    private Locataire idLoc;
    @Column(name="date_debut")
    private LocalDate dateDebut;
    @Column(name="date_fin")
    private LocalDate dateFin;
    @Column(name="nb_loyers")
    private Integer nbLoyers;
    @Column(name="montant_total")
    private Long montantTotal;

    public Integer getIdBilan() {
        return idBilan;
    }

    public void setIdBilan(Integer idBilan) {
        this.idBilan = idBilan;
    }

    public Appartement getIdAppart() {
        return idAppart;
    }

    public void setIdAppart(Appartement idAppart) {
        this.idAppart = idAppart;
    }

    public Locataire getIdLoc() {
        return idLoc;
    }

    public void setIdLoc(Locataire idLoc) {
        this.idLoc = idLoc;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Integer getNbLoyers() {
        return nbLoyers;
    }

    public void setNbLoyers(Integer nbLoyers) {
        this.nbLoyers = nbLoyers;
    }

    public Long getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Long montantTotal) {
        this.montantTotal = montantTotal;
    }
}