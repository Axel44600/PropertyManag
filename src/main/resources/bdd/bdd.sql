CREATE TABLE appartement (
                                    id_appart integer NOT NULL,
                                    id_loc integer,
                                    adresse character varying(255) NOT NULL,
                                    adresse_comp character varying(255),
                                    ville character varying(100) NOT NULL,
                                    code_postal integer NOT NULL,
                                    montant_loyer integer NOT NULL,
                                    montant_charges integer NOT NULL,
                                    montant_depot_garantie integer NOT NULL,
                                    date_creation date NOT NULL,
                                    montant_frais_agence integer NOT NULL
);


CREATE TABLE bilan (
                              id_bilan integer NOT NULL,
                              id_appart integer NOT NULL,
                              id_loc integer NOT NULL,
                              date_debut date,
                              date_fin date,
                              nb_loyers integer NOT NULL,
                              montant_total bigint NOT NULL
);


CREATE TABLE etat_des_lieux (
                                       id_etat integer NOT NULL,
                                       id_appart integer NOT NULL,
                                       type character varying(50) NOT NULL,
                                       remarques character varying(255),
                                       ref character varying(255) NOT NULL,
                                       date timestamp with time zone
);


CREATE TABLE garantie (
                                 id_depot integer NOT NULL,
                                 id_appart integer NOT NULL,
                                 montant integer NOT NULL,
                                 statut boolean NOT NULL,
                                 ref character(255) NOT NULL
);


CREATE TABLE locataire (
                                  id_loc integer NOT NULL,
                                  nom character varying(50) NOT NULL,
                                  prenom character varying(50) NOT NULL,
                                  email character varying(100) NOT NULL,
                                  tel character varying(50) NOT NULL,
                                  solde integer NOT NULL
);


CREATE TABLE loyer (
                              id_loyer integer NOT NULL,
                              id_appart integer NOT NULL,
                              montant integer NOT NULL,
                              statut boolean NOT NULL,
                              date date,
                              origine_paiement character varying(255),
                              ref character varying(255) NOT NULL
);


CREATE TABLE users (
                              id integer NOT NULL,
                              pseudo character varying(50),
                              password character varying,
                              nom character varying(50),
                              prenom character varying(50),
                              role character varying(50),
                              register_key character varying(255)
);
