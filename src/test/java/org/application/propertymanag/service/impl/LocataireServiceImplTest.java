package org.application.propertymanag.service.impl;

import org.application.propertymanag.entity.Locataire;
import org.application.propertymanag.repository.LocataireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class LocataireServiceImplTest {

    @Mock
    private LocataireRepository locataireRepository;

    @InjectMocks
    private LocataireServiceImpl locataireService;

    private Locataire locataire;

    @BeforeEach
    public void init() {
        locataire = Locataire.builder()
                .idLoc(1)
                .nom("Delon")
                .prenom("Alain")
                .email("alaindelon@gmail.com")
                .tel("0637001551")
                .solde(0)
                .build();
    }


    @Test
    void testGetLocataireById() {
        Integer idLoc = locataire.getIdLoc();
        when(locataireRepository.findById(idLoc)).thenReturn(Optional.ofNullable(locataire));
        Optional<Locataire> l = locataireRepository.findById(idLoc);

        l.ifPresent(locataire ->  assertThat(locataire.getIdLoc(), is(locataireService.getLocataireById(idLoc).getIdLoc())));
        l.ifPresent(locataire ->  assertThat(locataire.getNom(), is(locataireService.getLocataireById(idLoc).getNom())));
        l.ifPresent(locataire ->  assertThat(locataire.getPrenom(), is(locataireService.getLocataireById(idLoc).getPrenom())));
        l.ifPresent(locataire ->  assertThat(locataire.getEmail(), is(locataireService.getLocataireById(idLoc).getEmail())));
        l.ifPresent(locataire ->  assertThat(locataire.getTel(), is(locataireService.getLocataireById(idLoc).getTel())));
        l.ifPresent(locataire ->  assertThat(locataire.getSolde(), is(locataireService.getLocataireById(idLoc).getSolde())));
    }

    @Test
    void testGetLocataireByNom() {
        String nom = locataire.getNom();
        when(locataireRepository.findByNom(nom)).thenReturn(locataire);
        Locataire l = locataireRepository.findByNom(nom);

        assertThat(l.getIdLoc(), is(locataireService.getLocataireByNom(nom).getIdLoc()));
        assertThat(l.getNom(), is(locataireService.getLocataireByNom(nom).getNom()));
        assertThat(l.getPrenom(), is(locataireService.getLocataireByNom(nom).getPrenom()));
        assertThat(l.getEmail(), is(locataireService.getLocataireByNom(nom).getEmail()));
        assertThat(l.getTel(), is(locataireService.getLocataireByNom(nom).getTel()));
        assertThat(l.getSolde(), is(locataireService.getLocataireByNom(nom).getSolde()));
    }

    @Test
    void testGetListOfLocataires() {
        List<Locataire> listOfLocataires = new ArrayList<>();
        listOfLocataires.add(locataire);
        when(locataireRepository.findAll()).thenReturn(listOfLocataires);

        assertThat(listOfLocataires.get(0).getIdLoc(), is(locataireService.getListOfLocataires().get(0).getIdLoc()));
        assertThat(listOfLocataires.get(0).getNom(), is(locataireService.getListOfLocataires().get(0).getNom()));
        assertThat(listOfLocataires.get(0).getPrenom(), is(locataireService.getListOfLocataires().get(0).getPrenom()));
        assertThat(listOfLocataires.get(0).getEmail(), is(locataireService.getListOfLocataires().get(0).getEmail()));
        assertThat(listOfLocataires.get(0).getTel(), is(locataireService.getListOfLocataires().get(0).getTel()));
        assertThat(listOfLocataires.get(0).getSolde(), is(locataireService.getListOfLocataires().get(0).getSolde()));
    }

    @Test
    void testCreateLocataire() {
        when(locataireRepository.save(locataire)).thenReturn(locataire);
        locataireService.createLocataire(locataire);

        verify(locataireRepository, times(1)).save(locataire);
    }

}
