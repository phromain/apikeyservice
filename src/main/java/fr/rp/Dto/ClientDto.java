package fr.rp.Dto;


import fr.rp.entities.ClientEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClientDto {

    private Integer idClient;
    private String email;
    private String nomClient;
    private Integer quota;
    private boolean statut;

    public ClientDto(ClientEntity client) {
        this.idClient = client.getIdClient();
        this.email = client.getEmail();
        this.nomClient = client.getNomClient();
        this.quota = client.getQuota();
        this.statut = client.isStatut();
    }

    public static List<ClientDto> toDtoListClient(List<ClientEntity> clientEntityList){
        List<ClientDto> listeClientDtoEntrant = new ArrayList<>();
        for (ClientEntity clientEntity : clientEntityList){
            listeClientDtoEntrant.add(new ClientDto(clientEntity));
        }
        return listeClientDtoEntrant;
    }

}
