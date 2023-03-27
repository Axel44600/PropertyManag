package org.application.propertymanag.file;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.application.propertymanag.entity.Appartement;
import org.application.propertymanag.entity.Locataire;
import org.application.propertymanag.entity.Loyer;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.application.propertymanag.configuration.PathConfig.*;

public class QuittancePDF {

    private static final DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final LocalDate date = LocalDate.now();
    private static final Font catFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
    private static final Font subFont = new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL);
    private static final Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font small = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private final Storage storage = StorageOptions.newBuilder().setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(SERVICE_ACCOUNT_JSON_PATH))).build().getService();

    public QuittancePDF() throws IOException {
    }

    public String createQuittance(Appartement appart, Locataire locataire, LocalDate dateDebut, LocalDate dateFin, String nameFile, List<Loyer> selectLoyers) {
        String urlFile = null;
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Document document = new Document();
                PdfWriter.getInstance(document, byteArrayOutputStream);
                document.open();
                addMetaData(document);
                List<Double> listOfLoyers = new ArrayList<>();
                for(Loyer l : selectLoyers) {
                    Float loyer = Float.valueOf(l.getMontant());
                    listOfLoyers.add(Double.valueOf(loyer));
                }
                addPage(document, appart, locataire, dateDebut, dateFin, listOfLoyers);
                document.close();

                byte[] bytes = byteArrayOutputStream.toByteArray();
                BlobId blobId = BlobId.of(NAME_BUCKET, nameFile);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
                storage.create(blobInfo, bytes);

                URL url = storage.signUrl(blobInfo, 1, TimeUnit.HOURS,
                        Storage.SignUrlOption.httpMethod(HttpMethod.GET),
                        Storage.SignUrlOption.signWith(ServiceAccountCredentials.fromStream(new FileInputStream(SERVICE_ACCOUNT_JSON_PATH))));

                urlFile = String.valueOf(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
         return urlFile;
    }

    private static void addMetaData(Document document) {
            document.addTitle("Quittance de loyer");
    }

    private static void addPage(Document document, Appartement appart, Locataire locataire, LocalDate dateDebut, LocalDate dateFin, List<Double> listOfLoyers) throws DocumentException, IOException {
            Float loyer = Float.valueOf(appart.getMontantLoyer());
            Double totalLoyer = listOfLoyers.stream().mapToDouble(Double::doubleValue).sum();
            Float charges = Float.valueOf(appart.getMontantCharges());
            Double totalCharges = (double) (appart.getMontantCharges() * listOfLoyers.size());
            Double total = totalLoyer + totalCharges;

            Paragraph preface = new Paragraph();
                preface.setAlignment(Element.ALIGN_CENTER);
                    Phrase title = new Phrase("QUITTANCE DE LOYER", catFont);
                    Paragraph periode = new Paragraph("Période : du " +dateDebut.format(formatters)+" au "+dateFin.format(formatters), smallBold);
                        periode.setAlignment(Element.ALIGN_CENTER);
                    Paragraph adresse = new Paragraph("Adresse du logement : "+appart.getAdresse()+", "+appart.getCodePostal()+" "+appart.getVille(), smallBold);
                        adresse.setAlignment(Element.ALIGN_CENTER);

                preface.add(title);
            addEmptyLine(preface, 1);
                preface.add(adresse);
            addEmptyLine(preface, 1);
                preface.add(periode);
            addEmptyLine(preface, 2);

            // BLOC PROPRIETAIRE

            PdfPTable tablePr = new PdfPTable(1);
            tablePr.setHorizontalAlignment(Element.ALIGN_LEFT);
            tablePr.setWidthPercentage(45);

            PdfPCell c1 = new PdfPCell(new Phrase("Propriétaire", subFont));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                c1.setMinimumHeight(25);
            tablePr.addCell(c1);
            tablePr.setHeaderRows(1);

            PdfPCell c2 = new PdfPCell(new Phrase(APP_NAME));
                c2.setHorizontalAlignment(Element.ALIGN_LEFT);
                c2.setPadding(6);
                c2.setBorderWidthTop(0);
                c2.setBorderWidthRight(1);
                c2.setBorderWidthBottom(0);
                c2.setBorderWidthLeft(1);

            tablePr.addCell(c2);

            PdfPCell c3 = new PdfPCell(new Phrase("5 Avenue Anatole France"));
                c3.setHorizontalAlignment(Element.ALIGN_LEFT);
                c3.setPadding(6);
                c3.setBorderWidthTop(0);
                c3.setBorderWidthRight(1);
                c3.setBorderWidthBottom(0);
                c3.setBorderWidthLeft(1);
            tablePr.addCell(c3);

            PdfPCell c4 = new PdfPCell(new Phrase("75007 Paris"));
                c4.setHorizontalAlignment(Element.ALIGN_LEFT);
                c4.setPadding(6);
                c4.setBorderWidthTop(0);
                c4.setBorderWidthRight(1);
                c4.setBorderWidthBottom(0);
                c4.setBorderWidthLeft(1);
            tablePr.addCell(c4);
            tablePr.addCell(new Phrase(""));

            // BLOC LOCATAIRE

            PdfPTable tableLoc = new PdfPTable(1);
            tableLoc.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tableLoc.setWidthPercentage(45);

            PdfPCell d1 = new PdfPCell(new Phrase("Locataire", subFont));
                d1.setHorizontalAlignment(Element.ALIGN_CENTER);
                d1.setMinimumHeight(25);
            tableLoc.addCell(d1);
            tableLoc.setHeaderRows(1);

            PdfPCell d2 = new PdfPCell(new Phrase(locataire.getNom().toUpperCase()+" "+locataire.getPrenom()));
                d2.setHorizontalAlignment(Element.ALIGN_LEFT);
                d2.setPadding(6);
                d2.setBorderWidthTop(0);
                d2.setBorderWidthRight(1);
                d2.setBorderWidthBottom(0);
                d2.setBorderWidthLeft(1);
            tableLoc.addCell(d2);

            PdfPCell d3 = new PdfPCell(new Phrase(appart.getAdresse()));
                d3.setHorizontalAlignment(Element.ALIGN_LEFT);
                d3.setPadding(6);
                d3.setBorderWidthTop(0);
                d3.setBorderWidthRight(1);
                d3.setBorderWidthBottom(0);
                d3.setBorderWidthLeft(1);
            tableLoc.addCell(d3);

            PdfPCell d4 = new PdfPCell(new Phrase(appart.getCodePostal()+" "+appart.getVille()));
                d4.setHorizontalAlignment(Element.ALIGN_LEFT);
                d4.setPadding(6);
                d4.setBorderWidthTop(0);
                d4.setBorderWidthRight(1);
                d4.setBorderWidthBottom(1);
                d4.setBorderWidthLeft(1);
            tableLoc.addCell(d4);

            Paragraph space = new Paragraph();
            addEmptyLine(space, 2);

            addEmptyLine(preface, 1);
            document.add(preface);
            document.add(tablePr);
            document.add(tableLoc);
            document.add(space);

            // BLOC PRINCIPAL

            PdfPTable tableMain = new PdfPTable(2);
            tableMain.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableMain.setWidthPercentage(100);
            tableMain.setHeaderRows(1);

            PdfPCell m1 = new PdfPCell(new Phrase("PROPRIÉTAIRE"));
            m1.setHorizontalAlignment(Element.ALIGN_CENTER);
            m1.setPadding(6);
            tableMain.addCell(m1);

            PdfPCell m2 = new PdfPCell(new Phrase("LOCATAIRE"));
            m2.setHorizontalAlignment(Element.ALIGN_CENTER);
            m2.setPadding(6);
            tableMain.addCell(m2);
            tableMain.setHeaderRows(1);

            PdfPCell m1L1 = new PdfPCell(new Phrase("Détail du règlement"));
            m1L1.setHorizontalAlignment(Element.ALIGN_LEFT);
            m1L1.setPadding(6);
            tableMain.addCell(m1L1);

            PdfPCell m2L1 = new PdfPCell(new Phrase("Montant"));
            m2L1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            m2L1.setPadding(6);
            tableMain.addCell(m2L1);

            PdfPCell m1L2 = new PdfPCell(new Phrase("Loyer"));
            m1L2.setHorizontalAlignment(Element.ALIGN_LEFT);
            m1L2.setPadding(6);
            tableMain.addCell(m1L2);

            PdfPCell m2L2 = new PdfPCell(new Phrase(loyer+" €"));
            m2L2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            m2L2.setPadding(6);
            tableMain.addCell(m2L2);

            PdfPCell m1L3 = new PdfPCell(new Phrase("Charges"));
            m1L3.setHorizontalAlignment(Element.ALIGN_LEFT);
            m1L3.setPadding(6);
            tableMain.addCell(m1L3);

            PdfPCell m2L3 = new PdfPCell(new Phrase(charges+" €"));
            m2L3.setHorizontalAlignment(Element.ALIGN_RIGHT);
            m2L3.setPadding(6);
            tableMain.addCell(m2L3);

            PdfPCell m1L4 = new PdfPCell(new Phrase("Total", smallBold));
            m1L4.setHorizontalAlignment(Element.ALIGN_LEFT);
            m1L4.setPadding(6);
            tableMain.addCell(m1L4);

            PdfPCell m2L4 = new PdfPCell(new Phrase(total+" €", smallBold));
            m2L4.setHorizontalAlignment(Element.ALIGN_RIGHT);
            m2L4.setPadding(6);
            tableMain.addCell(m2L4);

            document.add(tableMain);

            Paragraph txt = new Paragraph();
            addEmptyLine(txt, 2);
            txt.add("Je soussigné(e) "+APP_NAME+" propriétaire du logement désigné ci-dessus, déclare avoir reçu de la part" +
                    " du locataire l'ensemble des sommes mentionnées au titre du loyer et des charges.");
            addEmptyLine(txt, 1);
            txt.setAlignment(Element.ALIGN_LEFT);

            Phrase ph = new Phrase("Fait à Paris, le " + date.format(formatters));

            document.add(txt);
            document.add(ph);

            Paragraph txt1 = new Paragraph();
            txt1.setAlignment(Element.ALIGN_RIGHT);
            addEmptyLine(txt1, 2);
            Image image = Image.getInstance(Objects.requireNonNull(QuittancePDF.class.getClassLoader().getResource("static/img/signature.png")));
            image.scaleToFit(150, 60);
            image.setAlignment(Element.ALIGN_RIGHT);

            document.add(txt1);
            document.add(image);

            Paragraph space1 = new Paragraph();
            addEmptyLine(space1, 1);

            Paragraph textEnd = new Paragraph("Cette quittance annule tous les reçus qui auraient pu être donnés pour acomptes versés au titre du " +
                    "loyer et des charges pour l'échéance correspondante. Le paiement de la présente quittance ne présume pas du paiement des termes " +
                    "précédents. À conserver 3 ans après échéance du bail.", small);

            document.add(space1);
            document.add(textEnd);
        }

    private static void addEmptyLine(Paragraph paragraph, int number) {
            for (int i = 0; i < number; i++) {
                paragraph.add(new Paragraph(" "));
            }
        }

}
