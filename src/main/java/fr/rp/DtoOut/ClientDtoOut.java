package fr.rp.DtoOut;


import fr.rp.entities.ClientEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClientDtoOut {

    private Integer idClient;
    private String email;
    private String nomClient;
    private Integer quota;
    private boolean statut;

    // Contructs

    public ClientDtoOut(ClientEntity client) {
        this.idClient = client.getIdClient();
        this.email = client.getEmail();
        this.nomClient = client.getNomClient();
        this.quota = client.getQuota();
        this.statut = client.isStatut();
    }

    private ClientDtoOut() {}

    // Methods

    public static List<ClientDtoOut> toDtoListClient(List<ClientEntity> clientEntityList){
        List<ClientDtoOut> listeClientDtoOutEntrant = new ArrayList<>();
        for (ClientEntity clientEntity : clientEntityList){
            listeClientDtoOutEntrant.add(new ClientDtoOut(clientEntity));
        }
        return listeClientDtoOutEntrant;
    }

}
