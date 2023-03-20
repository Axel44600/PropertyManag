package org.application.propertymanag.service.impl;

import org.application.propertymanag.entity.*;
import org.application.propertymanag.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class AppartServiceImplTest {

    @Mock
    private AppartRepository appartRepository;

    @Mock
    private EtatRepository etatRepository;

    @Mock
    private LoyerRepository loyerRepository;

    @Mock
    private GarantieRepository garantieRepository;

    @Mock
    private BilanRepository bilanRepository;

    @InjectMocks
    private AppartServiceImpl appartService;

    private Appartement appart;

    private EtatDesLieux etatDesLieux;

    private Loyer loyer;

    private DepotDeGarantie depotDeGarantie;

    private Bilan bilan;

    @BeforeEach
    public void init() {
        Locataire locataire = Locataire.builder()
                .idLoc(1)
                .nom("Delon")
                .prenom("Alain")
                .email("alaindelon@gmail.com")
                .tel("0637001551")
                .solde(0)
                .build();

        appart = Appartement.builder()
                .idAppart(1)
                .idLoc(locataire)
                .adresse("adresseTest")
                .adresseComp("RDC")
                .ville("Paris")
                .codePostal(75000)
                .montantLoyer(400)
                .montantCharges(200)
                .montantDepotGarantie(1100)
                .montantFraisAgence(65)
                .dateCreation(LocalDate.now().minusDays(1))
                .build();

        etatDesLieux = EtatDesLieux.builder()
                .idEtat(4)
                .idAppart(appart)
                .type("Emménagement")
                .ref("REFERENCE")
                .date(LocalDate.now().atStartOfDay())
                .build();

        loyer = Loyer.builder()
                .idLoyer(1)
                .idAppart(appart)
                .date(LocalDate.now())
                .statut(false)
                .ref("REFERENCE")
                .montant(appart.getMontantLoyer())
                .originePaiement("Locataire")
                .build();

        depotDeGarantie = DepotDeGarantie.builder()
                .idDepot(1)
                .idAppart(appart)
                .montant(appart.getMontantDepotGarantie())
                .statut(false)
                .ref("REFERENCE")
                .build();

        bilan = Bilan.builder()
                .idBilan(1)
                .idAppart(appart)
                .dateDebut(LocalDate.now().minusMonths(3))
                .dateFin(LocalDate.now())
                .montantTotal(appart.getMontantLoyer()*3L)
                .nbLoyers(3)
                .build();
    }


    // APPARTEMENT
    @Test
    void testGetAppartById() {
        Integer idAppart = appart.getIdAppart();
        when(appartRepository.findById(idAppart)).thenReturn(Optional.ofNullable(appart));
        Optional<Appartement> a = appartRepository.findById(idAppart);

        a.ifPresent(appartement ->  assertThat(appartement.getIdAppart(), is(appartService.getAppartById(idAppart).getIdAppart())));
        a.ifPresent(appartement ->  assertThat(appartement.getIdLoc(), is(appartService.getAppartById(idAppart).getIdLoc())));
        a.ifPresent(appartement ->  assertThat(appartement.getAdresse(), is(appartService.getAppartById(idAppart).getAdresse())));
        a.ifPresent(appartement ->  assertThat(appartement.getAdresseComp(), is(appartService.getAppartById(idAppart).getAdresseComp())));
        a.ifPresent(appartement ->  assertThat(appartement.getVille(), is(appartService.getAppartById(idAppart).getVille())));
        a.ifPresent(appartement ->  assertThat(appartement.getCodePostal(), is(appartService.getAppartById(idAppart).getCodePostal())));
        a.ifPresent(appartement ->  assertThat(appartement.getMontantLoyer(), is(appartService.getAppartById(idAppart).getMontantLoyer())));
        a.ifPresent(appartement ->  assertThat(appartement.getMontantCharges(), is(appartService.getAppartById(idAppart).getMontantCharges())));
        a.ifPresent(appartement ->  assertThat(appartement.getMontantDepotGarantie(), is(appartService.getAppartById(idAppart).getMontantDepotGarantie())));
        a.ifPresent(appartement ->  assertThat(appartement.getDateCreation(), is(appartService.getAppartById(idAppart).getDateCreation())));
        a.ifPresent(appartement ->  assertThat(appartement.getMontantFraisAgence(), is(appartService.getAppartById(idAppart).getMontantFraisAgence())));
    }

    @Test
    void testGetAppartByAdresse() {
        String adresse = appart.getAdresse();
        when(appartRepository.findByAdresse(adresse)).thenReturn(appart);
        Appartement a = appartRepository.findByAdresse(adresse);

        assertThat(a.getIdAppart(), is(appartService.getAppartByAdresse(adresse).getIdAppart()));
        assertThat(a.getIdLoc(), is(appartService.getAppartByAdresse(adresse).getIdLoc()));
        assertThat(a.getAdresse(), is(appartService.getAppartByAdresse(adresse).getAdresse()));
        assertThat(a.getAdresseComp(), is(appartService.getAppartByAdresse(adresse).getAdresseComp()));
        assertThat(a.getVille(), is(appartService.getAppartByAdresse(adresse).getVille()));
        assertThat(a.getCodePostal(), is(appartService.getAppartByAdresse(adresse).getCodePostal()));
        assertThat(a.getMontantLoyer(), is(appartService.getAppartByAdresse(adresse).getMontantLoyer()));
        assertThat(a.getMontantCharges(), is(appartService.getAppartByAdresse(adresse).getMontantCharges()));
        assertThat(a.getMontantDepotGarantie(), is(appartService.getAppartByAdresse(adresse).getMontantDepotGarantie()));
        assertThat(a.getDateCreation(), is(appartService.getAppartByAdresse(adresse).getDateCreation()));
        assertThat(a.getMontantFraisAgence(), is(appartService.getAppartByAdresse(adresse).getMontantFraisAgence()));
    }

    @Test
    void testGetListOfApparts() {
        List<Appartement> listOfApparts = new ArrayList<>();
        listOfApparts.add(appart);
        when(appartRepository.findAll()).thenReturn(listOfApparts);

        assertThat(listOfApparts.get(0).getIdAppart(), is(appartService.getListOfApparts().get(0).getIdAppart()));
        assertThat(listOfApparts.get(0).getIdLoc(), is(appartService.getListOfApparts().get(0).getIdLoc()));
        assertThat(listOfApparts.get(0).getAdresse(), is(appartService.getListOfApparts().get(0).getAdresse()));
        assertThat(listOfApparts.get(0).getAdresseComp(), is(appartService.getListOfApparts().get(0).getAdresseComp()));
        assertThat(listOfApparts.get(0).getVille(), is(appartService.getListOfApparts().get(0).getVille()));
        assertThat(listOfApparts.get(0).getCodePostal(), is(appartService.getListOfApparts().get(0).getCodePostal()));
        assertThat(listOfApparts.get(0).getMontantLoyer(), is(appartService.getListOfApparts().get(0).getMontantLoyer()));
        assertThat(listOfApparts.get(0).getMontantCharges(), is(appartService.getListOfApparts().get(0).getMontantCharges()));
        assertThat(listOfApparts.get(0).getMontantDepotGarantie(), is(appartService.getListOfApparts().get(0).getMontantDepotGarantie()));
        assertThat(listOfApparts.get(0).getDateCreation(), is(appartService.getListOfApparts().get(0).getDateCreation()));
        assertThat(listOfApparts.get(0).getMontantFraisAgence(), is(appartService.getListOfApparts().get(0).getMontantFraisAgence()));
    }

    @Test
    void testCreateAppart() {
        when(appartRepository.save(appart)).thenReturn(appart);
        appartService.createAppart(appart);

        verify(appartRepository, times(1)).save(appart);
    }

    @Test
    void testDeleteAppart() {
        given(etatRepository.findAll()).willReturn(new ArrayList<>());
        given(loyerRepository.findAll()).willReturn(new ArrayList<>());
        given(garantieRepository.findAll()).willReturn(new ArrayList<>());
        given(bilanRepository.findAll()).willReturn(new ArrayList<>());

        willDoNothing().given(appartRepository).delete(appart);
        appartService.deleteAppart(appart);

        verify(appartRepository, times(1)).delete(appart);
    }


    // ETAT DES LIEUX
    @Test
    void testGetEtatById() {
        Integer idEtat = appart.getIdAppart();
        when(etatRepository.findById(idEtat)).thenReturn(Optional.ofNullable(etatDesLieux));
        Optional<EtatDesLieux> e = etatRepository.findById(idEtat);

        e.ifPresent(etatDesLieux ->  assertThat(etatDesLieux.getIdEtat(), is(appartService.getEtatById(idEtat).getIdEtat())));
        e.ifPresent(etatDesLieux ->  assertThat(etatDesLieux.getIdAppart(), is(appartService.getEtatById(idEtat).getIdAppart())));
        e.ifPresent(etatDesLieux ->  assertThat(etatDesLieux.getType(), is(appartService.getEtatById(idEtat).getType())));
        e.ifPresent(etatDesLieux ->  assertThat(etatDesLieux.getRemarques(), is(appartService.getEtatById(idEtat).getRemarques())));
        e.ifPresent(etatDesLieux ->  assertThat(etatDesLieux.getRef(), is(appartService.getEtatById(idEtat).getRef())));
        e.ifPresent(etatDesLieux ->  assertThat(etatDesLieux.getDate(), is(appartService.getEtatById(idEtat).getDate())));
    }

    @Test
    void testGetEtatByRef() {
        String ref = etatDesLieux.getRef();
        when(etatRepository.findByRef(ref)).thenReturn(etatDesLieux);
        EtatDesLieux e = etatRepository.findByRef(ref);

        assertThat(e.getIdEtat(), is(appartService.getEtatByRef(ref).getIdEtat()));
        assertThat(e.getIdAppart(), is(appartService.getEtatByRef(ref).getIdAppart()));
        assertThat(e.getType(), is(appartService.getEtatByRef(ref).getType()));
        assertThat(e.getRemarques(), is(appartService.getEtatByRef(ref).getRemarques()));
        assertThat(e.getRef(), is(appartService.getEtatByRef(ref).getRef()));
        assertThat(e.getDate(), is(appartService.getEtatByRef(ref).getDate()));
    }

    @Test
    void testGetListOfEtats() {
        List<EtatDesLieux> listOfEtats = new ArrayList<>();
        listOfEtats.add(etatDesLieux);
        when(etatRepository.findAll()).thenReturn(listOfEtats);

        assertThat(listOfEtats.get(0).getIdEtat(), is(appartService.getListOfEtats().get(0).getIdEtat()));
        assertThat(listOfEtats.get(0).getIdAppart(), is(appartService.getListOfEtats().get(0).getIdAppart()));
        assertThat(listOfEtats.get(0).getType(), is(appartService.getListOfEtats().get(0).getType()));
        assertThat(listOfEtats.get(0).getRemarques(), is(appartService.getListOfEtats().get(0).getRemarques()));
        assertThat(listOfEtats.get(0).getRef(), is(appartService.getListOfEtats().get(0).getRef()));
        assertThat(listOfEtats.get(0).getDate(), is(appartService.getListOfEtats().get(0).getDate()));
    }

    @Test
    void testCreateEtat() {
        when(etatRepository.save(etatDesLieux)).thenReturn(etatDesLieux);
        appartService.createEtat(etatDesLieux);

        verify(etatRepository, times(1)).save(etatDesLieux);
    }


    // LOYER
    @Test
    void testGetLoyerById() {
        Integer idLoyer = loyer.getIdLoyer();
        when(loyerRepository.findById(idLoyer)).thenReturn(Optional.ofNullable(loyer));
        Optional<Loyer> l = loyerRepository.findById(idLoyer);

        l.ifPresent(loyer ->  assertThat(loyer.getIdLoyer(), is(appartService.getLoyerById(idLoyer).getIdLoyer())));
        l.ifPresent(loyer ->  assertThat(loyer.getIdAppart(), is(appartService.getLoyerById(idLoyer).getIdAppart())));
        l.ifPresent(loyer ->  assertThat(loyer.getMontant(), is(appartService.getLoyerById(idLoyer).getMontant())));
        l.ifPresent(loyer ->  assertThat(loyer.getStatut(), is(appartService.getLoyerById(idLoyer).getStatut())));
        l.ifPresent(loyer ->  assertThat(loyer.getDate(), is(appartService.getLoyerById(idLoyer).getDate())));
        l.ifPresent(loyer ->  assertThat(loyer.getOriginePaiement(), is(appartService.getLoyerById(idLoyer).getOriginePaiement())));
        l.ifPresent(loyer ->  assertThat(loyer.getRef(), is(appartService.getLoyerById(idLoyer).getRef())));
    }

    @Test
    void testGetLoyerByDate() {
        LocalDate date = loyer.getDate();
        when(loyerRepository.findByDate(date)).thenReturn(loyer);
        Loyer l = loyerRepository.findByDate(date);

        assertThat(l.getIdLoyer(), is(appartService.getLoyerByDate(date).getIdLoyer()));
        assertThat(l.getIdAppart(), is(appartService.getLoyerByDate(date).getIdAppart()));
        assertThat(l.getMontant(), is(appartService.getLoyerByDate(date).getMontant()));
        assertThat(l.getStatut(), is(appartService.getLoyerByDate(date).getStatut()));
        assertThat(l.getDate(), is(appartService.getLoyerByDate(date).getDate()));
        assertThat(l.getOriginePaiement(), is(appartService.getLoyerByDate(date).getOriginePaiement()));
        assertThat(l.getRef(), is(appartService.getLoyerByDate(date).getRef()));
    }

    @Test
    void testGetListOfLoyers() {
        List<Loyer> listOfLoyers = new ArrayList<>();
        listOfLoyers.add(loyer);
        when(loyerRepository.findAll()).thenReturn(listOfLoyers);

        assertThat(listOfLoyers.get(0).getIdLoyer(), is(appartService.getListOfLoyers().get(0).getIdLoyer()));
        assertThat(listOfLoyers.get(0).getIdAppart(), is(appartService.getListOfLoyers().get(0).getIdAppart()));
        assertThat(listOfLoyers.get(0).getMontant(), is(appartService.getListOfLoyers().get(0).getMontant()));
        assertThat(listOfLoyers.get(0).getStatut(), is(appartService.getListOfLoyers().get(0).getStatut()));
        assertThat(listOfLoyers.get(0).getDate(), is(appartService.getListOfLoyers().get(0).getDate()));
        assertThat(listOfLoyers.get(0).getOriginePaiement(), is(appartService.getListOfLoyers().get(0).getOriginePaiement()));
        assertThat(listOfLoyers.get(0).getRef(), is(appartService.getListOfLoyers().get(0).getRef()));
    }

    @Test
    void testCreateLoyer() {
        when(loyerRepository.save(loyer)).thenReturn(loyer);
        appartService.createLoyer(loyer);

        verify(loyerRepository, times(1)).save(loyer);
    }

    @Test
    void testDeleteLoyer() {
        willDoNothing().given(loyerRepository).delete(loyer);
        appartService.deleteLoyer(loyer);

        verify(loyerRepository, times(1)).delete(loyer);
    }


    // DEPOT DE GARANTIE
    @Test
    void testGetDepotById() {
        Integer idDepot = depotDeGarantie.getIdDepot();
        when(garantieRepository.findById(idDepot)).thenReturn(Optional.ofNullable(depotDeGarantie));
        Optional<DepotDeGarantie> d = garantieRepository.findById(idDepot);

        d.ifPresent(depotDeGarantie ->  assertThat(depotDeGarantie.getIdDepot(), is(appartService.getDepotById(idDepot).getIdDepot())));
        d.ifPresent(depotDeGarantie ->  assertThat(depotDeGarantie.getIdAppart(), is(appartService.getDepotById(idDepot).getIdAppart())));
        d.ifPresent(depotDeGarantie ->  assertThat(depotDeGarantie.getMontant(), is(appartService.getDepotById(idDepot).getMontant())));
        d.ifPresent(depotDeGarantie ->  assertThat(depotDeGarantie.getStatut(), is(appartService.getDepotById(idDepot).getStatut())));
        d.ifPresent(depotDeGarantie ->  assertThat(depotDeGarantie.getRef(), is(appartService.getDepotById(idDepot).getRef())));
    }

    @Test
    void testGetListOfDepots() {
        List<DepotDeGarantie> listOfDepots = new ArrayList<>();
        listOfDepots.add(depotDeGarantie);
        when(garantieRepository.findAll()).thenReturn(listOfDepots);

        assertThat(listOfDepots.get(0).getIdDepot(), is(appartService.getListOfDepots().get(0).getIdDepot()));
        assertThat(listOfDepots.get(0).getIdAppart(), is(appartService.getListOfDepots().get(0).getIdAppart()));
        assertThat(listOfDepots.get(0).getMontant(), is(appartService.getListOfDepots().get(0).getMontant()));
        assertThat(listOfDepots.get(0).getStatut(), is(appartService.getListOfDepots().get(0).getStatut()));
        assertThat(listOfDepots.get(0).getRef(), is(appartService.getListOfDepots().get(0).getRef()));
    }

    @Test
    void testCreateDepot() {
        when(garantieRepository.save(depotDeGarantie)).thenReturn(depotDeGarantie);
        appartService.createDepot(depotDeGarantie);

        verify(garantieRepository, times(1)).save(depotDeGarantie);
    }


    // BILAN DES COMPTES DES LOYERS
    @Test
    void testGetListOfBilans() {
        List<Bilan> listOfBilans = new ArrayList<>();
        listOfBilans.add(bilan);
        when(bilanRepository.findAll()).thenReturn(listOfBilans);

        assertThat(listOfBilans.get(0).getIdBilan(), is(appartService.getListOfBilans().get(0).getIdBilan()));
        assertThat(listOfBilans.get(0).getIdAppart(), is(appartService.getListOfBilans().get(0).getIdAppart()));
        assertThat(listOfBilans.get(0).getIdLoc(), is(appartService.getListOfBilans().get(0).getIdLoc()));
        assertThat(listOfBilans.get(0).getDateDebut(), is(appartService.getListOfBilans().get(0).getDateDebut()));
        assertThat(listOfBilans.get(0).getDateFin(), is(appartService.getListOfBilans().get(0).getDateFin()));
        assertThat(listOfBilans.get(0).getNbLoyers(), is(appartService.getListOfBilans().get(0).getNbLoyers()));
        assertThat(listOfBilans.get(0).getMontantTotal(), is(appartService.getListOfBilans().get(0).getMontantTotal()));
    }

    @Test
    void testCreateBilan() {
        when(bilanRepository.save(bilan)).thenReturn(bilan);
        appartService.createBilan(bilan);

        verify(bilanRepository, times(1)).save(bilan);
    }

}
