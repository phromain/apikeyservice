package fr.rp.resource;


import fr.rp.DtoIn.Client;
import fr.rp.DtoOut.ClientDtoOut;
import fr.rp.DtoOut.MailDtoOut;
import fr.rp.entities.ClientEntity;
import fr.rp.repositories.ClientRepository;
import fr.rp.restClient.MailServiceRemote;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTest
class ClientResourceTest {

    @Mock
    MailServiceRemote mailServiceRemote;

    @Inject
    ClientRepository clientRepository;
    List<ClientEntity> clients = new ArrayList<>();
    List<ClientDtoOut> clientsDto = new ArrayList<>();
    Client client;
    ClientEntity clientEntity;
    ClientDtoOut clientDtoOut;
    MailDtoOut mailDtoOut;

    String oldKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clients = clientRepository.listAll();
        clientsDto = ClientDtoOut.toDtoListClient(clients);
        client = new Client("test@mail.fr","TEST",0);
        clientEntity = clientRepository.findById(1);
        clientDtoOut = new ClientDtoOut(clientEntity);
        oldKey = clientEntity.getApiKey();
    }

    @Test
    void getListClient() {
        List<ClientDtoOut> responseList =
                given()
                        .when()
                        .get("clients/")
                        .then()
                        .statusCode(200)
                        .extract().body().as(new TypeRef<List<ClientDtoOut>>() {});

        assertEquals(clientsDto.size(), responseList.size());

        ClientDtoOut clientDto = clientsDto.get(0);
        ClientDtoOut responseClient = responseList.get(0);

        assertEquals(clientDto.getIdClient(), responseClient.getIdClient());
        assertEquals(clientDto.getEmail(), responseClient.getEmail());
        assertEquals(clientDto.getNomClient(), responseClient.getNomClient());
        assertEquals(clientDto.getQuota(), responseClient.getQuota());
        assertEquals(clientDto.isStatut(), responseClient.isStatut());

    }


    @Test
    @Transactional
    void createClient400() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("clients/")
                .then()
                .statusCode(400)
                .body(equalTo("paramètre absent"));
    }

    @Test
    @Transactional
    void createClient400_1() {
        client.setEmail("test");
        given()
                .contentType(ContentType.JSON)
                .body(client)
                .when()
                .post("clients/")
                .then()
                .statusCode(400);
    }

    @Test
    @Transactional
    void createClient400_2() {
        client.setEmail("test@");
        given()
                .contentType(ContentType.JSON)
                .body(client)
                .when()
                .post("clients/")
                .then()
                .statusCode(400);
    }


    @Test
    @Transactional
    void createClient400_3() {
        client.setEmail("test@.");
        given()
                .contentType(ContentType.JSON)
                .body(client)
                .when()
                .post("clients/")
                .then()
                .statusCode(400);
    }

    @Test
    @Transactional
    void createClient400_4() {
        client.setEmail("test@mail.");
        given()
                .contentType(ContentType.JSON)
                .body(client)
                .when()
                .post("clients/")
                .then()
                .statusCode(400);
    }

    @Test
    @Transactional
    void createClient400_5() {
        client.setEmail(null);
        given()
                .contentType(ContentType.JSON)
                .body(client)
                .when()
                .post("clients/")
                .then()
                .statusCode(400);
    }


    @Test
    @Transactional
    void createClient400_6() {
        client.setNomClient(null);
        given()
                .contentType(ContentType.JSON)
                .body(client)
                .when()
                .post("clients/")
                .then()
                .statusCode(400);
    }

    @Test
    @Transactional
    void createClient400_7() {
        client.setNomClient("");
        given()
                .contentType(ContentType.JSON)
                .body(client)
                .when()
                .post("clients/")
                .then()
                .statusCode(400);
    }

    @Test
    @Transactional
    void createClient400_8() {
        client.setQuota(null);
        given()
                .contentType(ContentType.JSON)
                .body(client)
                .when()
                .post("clients/")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(1)
    @Transactional
    void createClient500() {
        mailDtoOut = new MailDtoOut();
        when(mailServiceRemote.envoyerMail("azerty", mailDtoOut)).thenReturn(Response.status(500).build());

        given()
                .contentType(ContentType.JSON)
                .body(client)
                .when()
                .post("clients/")
                .then()
                .statusCode(500);

    }

    @Test
    @Order(2)
    @Transactional
    void deleteClient500() {
        ClientEntity clientEntity = clientRepository.find("nomClient", client.getNomClient()).firstResult();
        clientRepository.delete(clientEntity);
    }

    @Test
    void findClientById200() {
        Integer id = 1;
        ClientDtoOut clientDtoOutTest = given()
                .when()
                .get("clients/"+ id)
                .then()
                .statusCode(200)
                .extract().as(ClientDtoOut.class);

        assertNotNull(clientDtoOutTest);
        assertEquals(clientDtoOut.getIdClient(), clientDtoOutTest.getIdClient());
        assertEquals(clientDtoOut.getNomClient(), clientDtoOutTest.getNomClient());
        assertEquals(clientDtoOut.getQuota(), clientDtoOutTest.getQuota());
        assertEquals(clientDtoOut.getEmail(), clientDtoOutTest.getEmail());
    }

    @Test
    void findClientById404() {
        Integer id = 9999999;
        given()
                .when()
                .get("clients/"+ id)
                .then()
                .statusCode(404)
                .body(equalTo("Key non trouvée"));
    }

    @Test
    @Transactional
    void updateQuotaById404() {
        Integer id = 9999999;
        given()
                .contentType(ContentType.TEXT)
                .body(5)
                .when()
                .put("clients/"+ id + "/quota")
                .then()
                .statusCode(404)
                .body(equalTo("Client non trouvé"));
    }

    @Test
    @Transactional
    void updateQuotaById400_1() {
        Integer id = clientEntity.getIdClient();
        given()
                .contentType(ContentType.TEXT)
                .body("e")
                .when()
                .put("clients/"+ id + "/quota")
                .then()
                .statusCode(400)
                .body(equalTo("nombre incorrect ex: 500"));


    }
    @Test
    @Transactional
    void updateQuotaById400_2() {
        Integer id = clientEntity.getIdClient();
        given()
                .contentType(ContentType.TEXT)
                .body("")
                .when()
                .put("clients/"+ id + "/quota")
                .then()
                .statusCode(400)
                .body(equalTo("Valeur vide ou inexistante"));
    }

    @Test
    @Transactional
    void updateQuotaById400_3() {
        Integer id = clientEntity.getIdClient();
        given()
                .contentType(ContentType.TEXT)
                .body("2z")
                .when()
                .put("clients/"+ id + "/quota")
                .then()
                .statusCode(400)
                .body(equalTo("nombre incorrect ex: 500"));


    }

    @Test
    @Transactional
    void updateQuotaById400_4() {
        Integer id = clientEntity.getIdClient();
        given()
                .contentType(ContentType.TEXT)
                .body("cinquante")
                .when()
                .put("clients/"+ id + "/quota")
                .then()
                .statusCode(400)
                .body(equalTo("nombre incorrect ex: 500"));
    }

    @Test
    @Transactional
    void updateQuotaById400_5() {
        Integer id = clientEntity.getIdClient();
        given()
                .contentType(ContentType.TEXT)
                .when()
                .put("clients/"+ id + "/quota")
                .then()
                .statusCode(400)
                .body(equalTo("Valeur vide ou inexistante"));


    }


    @Test
    @Order(3)
    @Transactional
    void updateQuotaById200_1() {
        Integer id = clientEntity.getIdClient();
        given()
                .contentType(ContentType.TEXT)
                .body("100")
                .when()
                .put("clients/"+ id + "/quota")
                .then()
                .statusCode(200)
                .body(equalTo("Quota mis à jour"));
    }

    @Test
    @Order(5)
    @Transactional
    void confirmeChangeQuota100() {
        clientEntity.getQuota();
        assertEquals(clientEntity.getQuota(), 100);
    }
    @Test
    @Order(6)
    @Transactional
    void quotaReset() {
        clientEntity = clientRepository.findById(1);
        clientEntity.setQuota(0);
    }

    @Test
    @Transactional
    void renewKeyById404(){
        Integer id = 9999999;
        given()
                .when()
                .put("clients/"+ id + "/newkey")
                .then()
                .statusCode(404)
                .body(equalTo("Client non trouvé"));
    }

    @Test
    @Transactional
    void renewKeyById500() {
        when(mailServiceRemote.envoyerMail("azerty", mailDtoOut)).thenReturn(Response.status(500).build());
        Integer id = clientEntity.getIdClient();
        given()
                .when()
                .put("clients/"+ id + "/newkey")
                .then()
                .statusCode(500)
                .body(equalTo("Une erreur est survenue"));
    }

}