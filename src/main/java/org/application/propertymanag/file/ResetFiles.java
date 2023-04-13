package org.application.propertymanag.file;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class ResetFiles {

    @Value("${config.gcp.name-bucket}")
    public String NAME_BUCKET;

    @Value("${config.gcp.service-account-json-path}")
    public String SERVICE_ACCOUNT_JSON_PATH;

    // RESET du stockage GCP tous les soirs à 22h
    @Scheduled(cron = "0 0 22 * * ?", zone = "Europe/Paris")
    public void timerReset() throws IOException {

        Storage storage = StorageOptions.newBuilder().setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(System.getProperty("user.dir")+SERVICE_ACCOUNT_JSON_PATH))).build().getService();

        Iterable<Blob> blobs = storage.list(NAME_BUCKET, Storage.BlobListOption.prefix("")).iterateAll();
        if(blobs != null) {
            for (Blob blob : blobs) {
                blob.delete(Blob.BlobSourceOption.generationMatch());
            }
        }

        System.out.println("Remise à zéro quotidienne des fichiers PDF stockée sur le cloud -> "+ LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute());
    }

}