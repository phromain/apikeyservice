package fr.rp.resource;

import fr.rp.Dto.KeyDto;
import fr.rp.entities.ClientEntity;
import fr.rp.repositories.ClientRepository;
import fr.rp.repositories.MailRepository;
import fr.rp.restClient.MailServiceRemote;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


@QuarkusTest
class ApikeyResourceTest {

    @Inject
    MailRepository mailRepository;
    @Inject
    ClientRepository clientRepository;
    String apikeyFalse = "toto";
    String apikeyTrue;
    Integer count;

    @InjectMocks
    ApikeyResource apikeyResource;
    @Mock
    MailServiceRemote mailServiceRemote;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ClientEntity client = clientRepository.findById(1);
        apikeyTrue = client.getApiKey();
        count = mailRepository.countMailParMois(client.getIdClient());

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
        KeyDto key = given()
                .when()
                .get("apikey/"+ apikeyTrue)
                .then()
                .statusCode(200)
                .extract().as(KeyDto.class);
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
    

}