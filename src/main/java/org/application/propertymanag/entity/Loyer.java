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
@Table(name = "loyer")
public class Loyer {

    @Id
    @SequenceGenerator(name="loyer_seq", sequenceName="loyer_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="loyer_seq")
    @Column(name="id_loyer", updatable = false)
    private Integer idLoyer;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_appart", nullable = false)
    private Appartement idAppart;
    private Integer montant;
    private Boolean statut;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "origine_paiement")
    private String originePaiement;
    private String ref;

    public Integer getIdLoyer() {
        return idLoyer;
    }

    public void setIdLoyer(Integer idLoyer) {
        this.idLoyer = idLoyer;
    }

    public Appartement getIdAppart() {
        return idAppart;
    }

    public void setIdAppart(Appartement idAppart) {
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

    public String getOriginePaiement() {
        return originePaiement;
    }

    public void setOriginePaiement(String originePaiement) {
        this.originePaiement = originePaiement;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}