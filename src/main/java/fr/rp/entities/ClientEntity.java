package fr.rp.entities;


import fr.rp.DtoIn.ClientDtoIn;
import fr.rp.service.ApiKeyGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name ="APIKEYCLIENT")
@Getter
@Setter
@NoArgsConstructor
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ID_CLIENT")
    private Integer idClient;
    @Column(name ="API_KEY")
    private String apiKey;
    @Column(name ="EMAIL")
    private String email;
    @Column(name ="NOM_CLIENT")
    private String nomClient;
    @Column(name = "QUOTA")
    private Integer quota;
    @Column(name= "ACTIVE")
    private boolean statut;

    public ClientEntity(ClientDtoIn client) {
        this.apiKey = ApiKeyGenerator.generateApiKey();
        this.email = client.getEmail();
        this.nomClient = client.getNomClient();
        this.quota = client.getQuota();
        this.statut = true;
    }

    @Override
    public String toString() {
        return "ClientEntity{" +
                "idClient=" + idClient +
                ", apiKey='" + apiKey + '\'' +
                ", email='" + email + '\'' +
                ", nomClient='" + nomClient + '\'' +
                ", quota=" + quota +
                ", statut=" + statut +
                '}';
    }
}
