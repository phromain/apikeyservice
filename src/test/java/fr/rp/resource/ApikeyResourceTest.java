package fr.rp.resource;

import fr.rp.DtoOut.KeyDtoOut;
import fr.rp.entities.ClientEntity;
import fr.rp.DtoIn.MailDtoIn;
import fr.rp.repositories.ClientRepository;
import fr.rp.repositories.MailRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTest
class ApikeyResourceTest {

    @Inject
    MailRepository mailRepository;
    @Inject
    ClientRepository clientRepository;
    String apikeyFalse = "toto";
    String apikeyTrue;
    Integer count;
    ClientEntity client;
    @Mock
    MailDtoIn mailDtoIn;

    KeyDtoOut keyDtoOut;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = clientRepository.findById(1);
        apikeyTrue = client.getApiKey();
        count = mailRepository.countMailParMois(client.getIdClient());
        mailDtoIn = new MailDtoIn("test@mail.fr","mail test");
        keyDtoOut = new KeyDtoOut(client);
    }

    @Test
    void findByApiKey404() {
        given()
                .when()
                .get("apikey/"+ apikeyFalse)
                .then()
                .statusCode(404)
                .body(equalTo("Key non trouvée"));
    }


    @Test
    void findByApiKey200() {
        KeyDtoOut key = given()
                .when()
                .get("apikey/"+ apikeyTrue)
                .then()
                .statusCode(200)
                .extract().as(KeyDtoOut.class);
        assertNotNull(key);
        assertEquals(key.getApikey(), keyDtoOut.getApikey());
        assertEquals(key.getQuota(), keyDtoOut.getQuota());

    }

    @Test
    void findByApiKeyCountMail404() {
        given()
                .when()
                .get("apikey/"+ apikeyFalse+"/count")
                .then()
                .statusCode(404)
                .body(equalTo("Key non trouvée"));
    }

    @Test
    void findByApiKeyCountMail200() {
        given()
                .when()
                .get("apikey/"+ apikeyTrue+"/count")
                .then()
                .statusCode(200)
                .body(equalTo(count.toString()));
    }



    @Test
    void findByApiKeyValidAccount404() {
        given()
                .when()
                .get("apikey/"+ apikeyFalse+"/statut")
                .then()
                .statusCode(404)
                .body(equalTo("Key non trouvée"));
    }


    @Test
    @Order(1)
    @Transactional
    void ApikeyStatusAccountDisabled(){
        client = clientRepository.findById(1);
        client.setStatut(false);
        System.out.println(client);
    }

    @Test
    @Order(2)
    @Transactional
    void findByApiKeyValidAccount403() {
        given()
                .when()
                .get("apikey/"+ apikeyTrue+"/statut")
                .then()
                .statusCode(403)
                .body(equalTo("Compte Desactivé."));

    }

    @Test
    @Order(3)
    @Transactional
    void ApikeyStatusAccountActive() {
        client = clientRepository.findById(1);
        client.setStatut(true);
        clientRepository.flush();
    }

    @Test
    @Order(4)
    void findByApiKeyValidAccount200() {
        given()
                .when()
                .get("apikey/" + apikeyTrue + "/statut")
                .then()
                .statusCode(200)
                .body(equalTo("Compte Activé."));
    }


    @Test
    @Order(1)
    @Transactional

    void saveMail200(){
        given()
                .contentType(ContentType.JSON)
                .body(mailDtoIn)
                .when()
                .post("apikey/" + apikeyTrue + "/mail")
                .then()
                .statusCode(200)
                .body(equalTo("Mail enregistré"));
    }

    @Test
    @Transactional
    void saveMail400_1(){
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("apikey/" + apikeyTrue + "/mail")
                .then()
                .statusCode(400)
                .body(equalTo("paramètre absent"));
    }

    @Test
    @Transactional
    void saveMail400_2(){
        mailDtoIn.setDestinataire("ErreurMail");
        given()
                .contentType(ContentType.JSON)
                .body(mailDtoIn)
                .when()
                .post("apikey/" + apikeyTrue + "/mail")
                .then()
                .statusCode(400);

    }

    @Test
    @Transactional
    void saveMail400_3(){
        mailDtoIn.setDestinataire("ErreurMail@test");
        given()
                .contentType(ContentType.JSON)
                .body(mailDtoIn)
                .when()
                .post("apikey/" + apikeyTrue + "/mail")
                .then()
                .statusCode(400);
    }
    @Test
    @Transactional
    void saveMail400_4(){
        mailDtoIn.setDestinataire(null);
        given()
                .contentType(ContentType.JSON)
                .body(mailDtoIn)
                .when()
                .post("apikey/" + apikeyTrue + "/mail")
                .then()
                .statusCode(400);
    }

    @Test
    @Transactional
    void saveMail400_5(){
        mailDtoIn.setObjet("");
        given()
                .contentType(ContentType.JSON)
                .body(mailDtoIn)
                .when()
                .post("apikey/" + apikeyTrue + "/mail")
                .then()
                .statusCode(400);
    }

    @Test
    @Transactional
    void saveMail400_6(){
        mailDtoIn.setObjet(null);
        given()
                .contentType(ContentType.JSON)
                .body(mailDtoIn)
                .when()
                .post("apikey/" + apikeyTrue + "/mail")
                .then()
                .statusCode(400);
    }
    @Test
    @Transactional
    void saveMail404(){
        given()
                .contentType(ContentType.JSON)
                .body(mailDtoIn)
                .when()
                .post("apikey/"+ apikeyFalse+"/mail")
                .then()
                .statusCode(404)
                .body(equalTo("Key non trouvée"));
    }




}