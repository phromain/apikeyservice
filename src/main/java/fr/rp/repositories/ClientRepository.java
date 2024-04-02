package fr.rp.repositories;

import fr.rp.entities.ClientEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class ClientRepository implements PanacheRepositoryBase<ClientEntity, Integer> {

    public ClientEntity clientByApiKey(String apiKey){
        ClientEntity client = find("apiKey", apiKey).firstResult();
        return client;
    }

}
