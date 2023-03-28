package org.application.propertymanag.configuration;

public interface PathConfig {

    String URL_PATH = "https://property-manage.herokuapp.com";

    String APP_NAME = "PropertyManag";
    String APP_PATH = "/app";
    Integer FRAIS_AGENCE = 8;

    // GCP CONFIG
    String NAME_BUCKET = "property-manag";
    String SERVICE_ACCOUNT_JSON_PATH = System.getProperty("user.dir")+"/target/classes/gcp-account-file.json";



}
