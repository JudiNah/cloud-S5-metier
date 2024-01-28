CREATE OR REPLACE FUNCTION public.custom_seq(in_prefix character varying, in_sequence_name character varying, in_digit_count integer)
    RETURNS character varying
    LANGUAGE plpgsql
    AS $function$
    DECLARE
        seq_value INT;
        result VARCHAR;
    BEGIN
        EXECUTE 'SELECT nextval(''' || in_sequence_name || '''::regclass)' INTO seq_value;
        result := in_prefix || LPAD(seq_value::TEXT, in_digit_count, '0');
    RETURN result;
    END;
    $function$;

CREATE SEQUENCE personne_id_seq;
CREATE SEQUENCE personne_autentification_id_seq;
CREATE SEQUENCE categorie_voiture_id_seq;
CREATE SEQUENCE marque_voiture_id_seq;
CREATE SEQUENCE type_carburant_voiture_id_seq;
CREATE SEQUENCE transmission_voiture_id_seq;
CREATE SEQUENCE freignage_voiture_id_seq;
CREATE SEQUENCE equipement_interne_id_seq;
CREATE SEQUENCE photos_id_seq;
CREATE SEQUENCE voiture_id_seq;
CREATE SEQUENCE voiture_prix_id_seq;
CREATE SEQUENCE annonce_id_seq;
CREATE SEQUENCE annonce_validee_id_seq;
CREATE SEQUENCE commision_id_seq;
CREATE SEQUENCE remise_id_seq;
CREATE SEQUENCE remise_voiture_id_seq;
CREATE SEQUENCE vente_id_seq;
CREATE SEQUENCE commission_id_seq;

CREATE TABLE personne(
    id VARCHAR PRIMARY KEY DEFAULT custom_seq('PER'::character varying, 'personne_id_seq'::character varying, 4) NOT NULL ,
    nom VARCHAR (50) NOT NULL ,
    prenom VARCHAR (50) NOT NULL ,
    date_naissance DATE NOT NULL ,
    sexe INTEGER ,
    telephone VARCHAR (10) ,
    address VARCHAR (30)
);

CREATE TABLE personne_autentification(
    id  VARCHAR  PRIMARY KEY DEFAULT custom_seq('PER_AT'::character varying, 'personne_autentification_id_seq'::character varying, 4) NOT NULL ,
    email VARCHAR(50) UNIQUE CHECK (email ~* '^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$') ,
    mot_passe VARCHAR (50) NOT NULL ,
    is_admin BOOLEAN ,
    personne_id VARCHAR REFERENCES personne
);


CREATE TABLE categorie_voiture(
    id VARCHAR  PRIMARY KEY DEFAULT custom_seq('CAT'::character varying, 'categorie_voiture_id_seq'::character varying, 4) NOT NULL ,
    nom VARCHAR (30) NOT NULL ,
    description TEXT
);



CREATE TABLE marque_voiture (
    id VARCHAR  PRIMARY KEY DEFAULT custom_seq('MRQ'::character varying, 'marque_voiture_id_seq'::character varying, 4) NOT NULL ,
    nom VARCHAR (30) NOT NULL ,
    description TEXT ,
    date_creation DATE
);

CREATE TABLE type_carburant_voiture(
    id VARCHAR PRIMARY KEY DEFAULT custom_seq('TYPE_CAR'::character varying, 'type_carburant_voiture_id_seq'::character varying, 4) NOT NULL ,
    nom VARCHAR (30) NOT NULL
);

CREATE TABLE transmission_voiture(
    id VARCHAR PRIMARY KEY DEFAULT custom_seq('TRSM'::character varying, 'transmission_voiture_id_seq'::character varying, 4) NOT NULL ,
    nom VARCHAR (30)
);

CREATE TABLE freignage_voiture(
    id VARCHAR PRIMARY KEY DEFAULT custom_seq('FRGN'::character varying, 'freignage_voiture_id_seq'::character varying, 4) NOT NULL ,
    nom VARCHAR (30)
);

CREATE TABLE equipement_interne(
    id VARCHAR PRIMARY KEY DEFAULT custom_seq('EQP_IN'::character varying, 'equipement_interne_id_seq'::character varying, 4) NOT NULL ,
    nom VARCHAR (30)
);

CREATE TABLE photos(
    id VARCHAR PRIMARY KEY DEFAULT custom_seq('PH'::character varying, 'photos_id_seq'::character varying, 4) NOT NULL ,
    nom VARCHAR (255)
);

CREATE TABLE voiture(
    id VARCHAR PRIMARY KEY DEFAULT custom_seq('VOIT'::character varying, 'voiture_id_seq'::character varying, 4) NOT NULL ,
    annee_fabrication DATE NOT NULL ,
    couleur VARCHAR ,
    consommation DECIMAL (10, 2) ,
    categorie_voiture_id VARCHAR REFERENCES categorie_voiture ,
    marque_voiture_id VARCHAR REFERENCES marque_voiture ,
    type_carburant_id VARCHAR REFERENCES type_carburant_voiture ,
    transmission_voiture_id VARCHAR REFERENCES transmission_voiture ,
    freignage_voiture_id VARCHAR REFERENCES freignage_voiture
);

CREATE TABLE voiture_equipement_interne(
    voiture_id VARCHAR REFERENCES voiture ,
    equipement_interne_id VARCHAR REFERENCES equipement_interne
);

CREATE TABLE voiture_prix(
    id VARCHAR PRIMARY KEY DEFAULT custom_seq('VOIT_PR'::character varying, 'voiture_prix_id_seq'::character varying, 4) NOT NULL ,
    prix DECIMAL (10, 2) NOT NULL ,
    date DATE  NOT NULL ,
    voiture_id VARCHAR REFERENCES voiture
);

CREATE TABLE voiture_photos(
    voiture_id VARCHAR REFERENCES voiture ,
    photos_id VARCHAR REFERENCES photos
);

CREATE TABLE annonce(
    id VARCHAR PRIMARY KEY DEFAULT custom_seq('ANNC'::character varying, 'annonce_id_seq'::character varying, 4) NOT NULL ,
    date_debut DATE NOT NULL DEFAULT CURRENT_DATE ,
    date_fin DATE ,
    code_annonce VARCHAR(20) NOT NULL ,
    voiture_id VARCHAR REFERENCES voiture ,
    personne_autentification_id VARCHAR REFERENCES personne_autentification
);

CREATE TABLE annonce_validee(
    id VARCHAR PRIMARY KEY DEFAULT custom_seq('ANNC_VLD'::character varying, 'annonce_validee_id_seq'::character varying, 4) NOT NULL ,
    date_validation DATE NOT NULL ,
    annonce_id VARCHAR REFERENCES annonce ,
    personne_autentification_id VARCHAR REFERENCES personne_autentification
);

CREATE TABLE commission(
    id VARCHAR PRIMARY KEY DEFAULT custom_seq('CMSS'::character varying, 'commission_id_seq'::character varying, 4) NOT NULL ,
    prix_min DECIMAL (10, 2) NOT NULL ,
    prix_max DECIMAL (10, 2) NOT NULL ,
    taux_commission DECIMAL (10, 2) ,
    date DATE NOT NULL
);

CREATE TABLE remise(
    id VARCHAR PRIMARY KEY DEFAULT custom_seq('RMS'::character varying, 'remise_id_seq'::character varying, 4) NOT NULL ,
    taux_remise DECIMAL (10, 2) ,
    date_debut DATE NOT NULL ,
    date_fin DATE NOT NULL ,
    personne_autentification_id VARCHAR REFERENCES personne_autentification
);

CREATE TABLE remise_voiture(
    id VARCHAR PRIMARY KEY DEFAULT custom_seq('RMS_VOIT'::character varying, 'remise_voiture_id_seq'::character varying, 4) NOT NULL ,
    remise_id VARCHAR REFERENCES remise ,
    voiture_id VARCHAR REFERENCES voiture
);

CREATE TABLE vente(
    id VARCHAR PRIMARY KEY DEFAULT custom_seq('VT'::character varying, 'vente_id_seq'::character varying, 4) NOT NULL ,
    prix_vente DECIMAL (10, 2) NOT NULL ,
    prix_final DECIMAL (10, 2) NOT NULL ,
    taux_commission DECIMAL (10, 2) ,
    taux_remise DECIMAL (10, 2) ,
    date DATE NOT NULL ,
    annonce_validee_id VARCHAR REFERENCES annonce_validee ,
    personne_autentification_id VARCHAR REFERENCES personne_autentification
);

CREATE VIEW voiture_prix_actuel AS
    SELECT *FROM (
     SELECT prix, "date", voiture_id, ROW_NUMBER() OVER
         (PARTITION BY voiture_id ORDER BY "date" DESC) AS row_num
     FROM voiture_prix)
     AS ranked
    WHERE row_num = 1;

CREATE or REPLACE VIEW catalog_voiture AS
    SELECT v.id as voiture_id, v.annee_fabrication, v.couleur, v.consommation ,
           cat.nom as nom_categorie, cat.description as description_categorie,
           marq.nom as nom_marque, marq.description as description_marque, marq.date_creation as date_creation_marque,
           tcb.nom as nom_type_carburant,
           trans.nom as nom_transmission ,
           fre.nom as nom_freignage
    FROM voiture v
    JOIN categorie_voiture cat ON cat.id = v.categorie_voiture_id
    JOIN marque_voiture marq ON marq.id = v.marque_voiture_id
    JOIN type_carburant_voiture tcb ON tcb.id = v.type_carburant_id
    JOIN transmission_voiture trans ON trans.id = v.transmission_voiture_id
    JOIN freignage_voiture fre ON fre.id = v.freignage_voiture_id;

CREATE or REPLACE VIEW v_annonce AS
    SELECT aa.date_validation,
           per.nom as nom_client, per.prenom as prenom_client, per.address as address_client,
           per_au.id as utilisateur_id,
           a.date_debut, a.date_fin , a.id as annonce_id, a.prix_commission prix_commission,
           voit_pr.prix ,
           catalog_voit.*
    FROM annonce a
    LEFT JOIN annonce_validee aa ON aa.annonce_id = a.id
    JOIN personne_autentification per_au ON per_au.id = a.personne_autentification_id
    JOIN personne per ON per.id = per_au.personne_id
    JOIN catalog_voiture catalog_voit ON catalog_voit.voiture_id = a.voiture_id
    JOIN voiture_prix_actuel voit_pr ON voit_pr.voiture_id = a.voiture_id;





