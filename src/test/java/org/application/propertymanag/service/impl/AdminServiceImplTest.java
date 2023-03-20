package org.application.propertymanag.service.impl;

import org.application.propertymanag.auth.repository.UserRepository;
import org.application.propertymanag.entity.Role;
import org.application.propertymanag.entity.Users;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Users user;

    @BeforeEach
    public void init() {
        user = Users.builder()
                .id(1)
                .pseudo("User")
                .password("PropertyManag123.")
                .nom("Delon")
                .prenom("Alain")
                .role(Role.EMPLOYE)
                .registerKey(null)
                .build();
    }


    @Test
    void testGetUserById() {
        Integer id = user.getId();
        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user));
        Optional<Users> u = userRepository.findById(id);

        u.ifPresent(users ->  assertThat(users.getId(), is(adminService.getUserById(id).getId())));
        u.ifPresent(users ->  assertThat(users.getPseudo(), is(adminService.getUserById(id).getPseudo())));
        u.ifPresent(users ->  assertThat(users.getPassword(), is(adminService.getUserById(id).getPassword())));
        u.ifPresent(users ->  assertThat(users.getNom(), is(adminService.getUserById(id).getNom())));
        u.ifPresent(users ->  assertThat(users.getPrenom(), is(adminService.getUserById(id).getPrenom())));
        u.ifPresent(users ->  assertThat(users.getRole(), is(adminService.getUserById(id).getRole())));
        u.ifPresent(users ->  assertThat(users.getRegisterKey(), is(adminService.getUserById(id).getRegisterKey())));
    }

    @Test
    void testGetUserByNom() {
        String nom = user.getNom();
        when(userRepository.findByNom(nom)).thenReturn(user);
        Users u = userRepository.findByNom(nom);

        assertThat(u.getId(), is(adminService.getUserByNom(nom).getId()));
        assertThat(u.getPseudo(), is(adminService.getUserByNom(nom).getPseudo()));
        assertThat(u.getPassword(), is(adminService.getUserByNom(nom).getPassword()));
        assertThat(u.getNom(), is(adminService.getUserByNom(nom).getNom()));
        assertThat(u.getPrenom(), is(adminService.getUserByNom(nom).getPrenom()));
        assertThat(u.getRole(), is(adminService.getUserByNom(nom).getRole()));
        assertThat(u.getRegisterKey(), is(adminService.getUserByNom(nom).getRegisterKey()));
    }

    @Test
    void testGetUserByPseudo() {
        String pseudo = user.getPseudo();
        when(userRepository.findByPseudo(pseudo)).thenReturn(Optional.ofNullable(user));
        Optional<Users> u = userRepository.findByPseudo(pseudo);

        u.ifPresent(users ->  assertThat(users.getId(), is(adminService.getUserByPseudo(pseudo).getId())));
        u.ifPresent(users ->  assertThat(users.getPseudo(), is(adminService.getUserByPseudo(pseudo).getPseudo())));
        u.ifPresent(users ->  assertThat(users.getPassword(), is(adminService.getUserByPseudo(pseudo).getPassword())));
        u.ifPresent(users ->  assertThat(users.getNom(), is(adminService.getUserByPseudo(pseudo).getNom())));
        u.ifPresent(users ->  assertThat(users.getPrenom(), is(adminService.getUserByPseudo(pseudo).getPrenom())));
        u.ifPresent(users ->  assertThat(users.getRole(), is(adminService.getUserByPseudo(pseudo).getRole())));
        u.ifPresent(users ->  assertThat(users.getRegisterKey(), is(adminService.getUserByPseudo(pseudo).getRegisterKey())));
    }

    @Test
    void testGetListOfUsers() {
        List<Users> listOfUsers = new ArrayList<>();
        listOfUsers.add(user);
        when(userRepository.findAll()).thenReturn(listOfUsers);

        assertThat(listOfUsers.get(0).getId(), is(adminService.getListOfUsers().get(0).getId()));
        assertThat(listOfUsers.get(0).getPseudo(), is(adminService.getListOfUsers().get(0).getPseudo()));
        assertThat(listOfUsers.get(0).getPassword(), is(adminService.getListOfUsers().get(0).getPassword()));
        assertThat(listOfUsers.get(0).getNom(), is(adminService.getListOfUsers().get(0).getNom()));
        assertThat(listOfUsers.get(0).getPrenom(), is(adminService.getListOfUsers().get(0).getPrenom()));
        assertThat(listOfUsers.get(0).getRole(), is(adminService.getListOfUsers().get(0).getRole()));
        assertThat(listOfUsers.get(0).getRegisterKey(), is(adminService.getListOfUsers().get(0).getRegisterKey()));
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(user)).thenReturn(user);
        adminService.createUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUser() {
        willDoNothing().given(userRepository).delete(user);
        adminService.deleteUser(user);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testGetRandomStr() {
        int n = 25;
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder s = new StringBuilder(n);
        String s1 = adminService.getRandomStr(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(str.length() * Math.random());
            s.append(str.charAt(index));
        }

        assertEquals(s.toString().length(), n);
        assertNotEquals(s.toString(), s1);
    }

}
