package org.application.propertymanag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "etat_des_lieux")
public class EtatDesLieux {

    @Id
    @SequenceGenerator(name="etat_seq", sequenceName="etat_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="etat_seq")
    @Column(name="id_etat", updatable = false)
    private Integer idEtat;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_appart", nullable = false)
    private Appartement idAppart;
    private String type;
    private LocalDateTime date;
    private String remarques;
    private String ref;

    public Integer getIdEtat() {
        return idEtat;
    }

    public Appartement getIdAppart() {
        return idAppart;
    }

    public void setIdAppart(Appartement idAppart) {
        this.idAppart = idAppart;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}