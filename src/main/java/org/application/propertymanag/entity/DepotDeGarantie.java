package org.application.propertymanag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "garantie")
public class DepotDeGarantie {

    @Id
    @SequenceGenerator(name="depot_seq", sequenceName="depot_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="depot_seq")
    @Column(name="id_depot", updatable = false)
    private Integer idDepot;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_appart", nullable = false)
    private Appartement idAppart;
    private Integer montant;
    private Boolean statut;
    private String ref;

    public Integer getIdDepot() {
        return idDepot;
    }

    public void setIdDepot(Integer idDepot) {
        this.idDepot = idDepot;
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

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}