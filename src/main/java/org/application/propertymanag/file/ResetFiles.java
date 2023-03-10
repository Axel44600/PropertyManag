package org.application.propertymanag.file;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Configuration
@EnableScheduling
public class ResetFiles {

    private final String absolutePath = new File("pom.xml").getAbsolutePath();
    private final String filePath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
    private final String url = filePath+"\\pdf\\quittance";
    private static final String dirName = "quittance";

    public static void delete(File f) throws IOException {
        if(f.isDirectory()) {
            if(Objects.requireNonNull(f.list()).length == 0){
                System.out.println("Le dossier "+dirName+" a été supprimé : "+ f.getAbsolutePath());
            } else {
                String[] files = f.list();
                assert files != null;
                for (String tmp : files) {
                    File file = new File(f, tmp);
                    delete(file);
                }
                if(Objects.requireNonNull(f.list()).length == 0){
                    f.delete();
                    System.out.println("Le dossier "+dirName+" a été supprimé : "+ f.getAbsolutePath());
                }
            }
        } else {
            f.delete();
            System.out.println("Les fichiers du dossier "+dirName+" ont été supprimés : " + f.getAbsolutePath());
        }
    }

    // Remise à zéro du package tous les soirs à 22h
    @Scheduled(cron = "0 0 22 * * ?", zone = "Europe/Paris")
    public void timerReset() throws IOException {
        System.out.println("Remise à zéro quotidienne des fichiers PDF stockée enclenchée -> "+ LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute());

        delete(new File(url+"\\"));
        File dossier = new File(url);
        boolean res = dossier.mkdir();
            if(res) {
                System.out.println("Le dossier "+dirName+" a été créé.");
            }
            else {
                System.out.println("Le dossier "+dirName+" existe déjà.");
            }
    }

}