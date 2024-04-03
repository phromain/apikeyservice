package fr.rp.DtoOut;


import fr.rp.entities.ClientEntity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KeyDtoOut {

    private String apikey;
    private Integer quota;

// Attribus


    private KeyDtoOut() {
    }

    public KeyDtoOut(ClientEntity clientEntity) {
        this.apikey = clientEntity.getApiKey();
        this.quota = clientEntity.getQuota();
    }

}
