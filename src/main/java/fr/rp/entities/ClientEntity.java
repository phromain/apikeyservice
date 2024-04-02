package fr.rp.entities;

import fr.rp.dto.ClientDtoEntrant;
import fr.rp.service.ApiKeyGenerator;
import jakarta.persistence.*;
import lombok.*;

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

    public ClientEntity(ClientDtoEntrant clientDtoEntrant) {
        this.apiKey = ApiKeyGenerator.generateApiKey();
        this.email = clientDtoEntrant.getEmail();
        this.nomClient = clientDtoEntrant.getNomClient();
        this.quota = clientDtoEntrant.getQuota();
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
