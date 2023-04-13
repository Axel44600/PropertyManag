package org.application.propertymanag.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "agence")
public class Agence {

    @Id
    @SequenceGenerator(name="agence_seq", sequenceName="agence_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="agence_seq")
    @Column(name="id_agence", updatable = false)
    private Integer idAgence;

    @Column(name="nom_agence")
    private String nomAgence;

    @Column(name="frais_agence")
    private Integer fraisAgence;
}