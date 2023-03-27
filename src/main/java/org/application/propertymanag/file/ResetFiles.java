package org.application.propertymanag.file;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.application.propertymanag.configuration.PathConfig.NAME_BUCKET;
import static org.application.propertymanag.configuration.PathConfig.SERVICE_ACCOUNT_JSON_PATH;

@Configuration
@EnableScheduling
public class ResetFiles {

    private final Storage storage = StorageOptions.newBuilder().setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(SERVICE_ACCOUNT_JSON_PATH))).build().getService();

    public ResetFiles() throws IOException {
    }

    // Remise à zéro du stockage GCP tous les soirs à 22h
    @Scheduled(cron = "0 0 22 * * ?", zone = "Europe/Paris")
    public void timerReset() {
        System.out.println("Remise à zéro quotidienne des fichiers PDF stockée sur le cloud -> "+ LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute());

        Iterable<Blob> blobs = storage.list(NAME_BUCKET, Storage.BlobListOption.prefix("")).iterateAll();
        if(blobs != null) {
            for (Blob blob : blobs) {
                blob.delete(Blob.BlobSourceOption.generationMatch());
            }
        }
    }

}