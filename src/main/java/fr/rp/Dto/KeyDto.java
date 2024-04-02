package fr.rp.Dto;


import fr.rp.entities.ClientEntity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KeyDto {

    private String apikey;
    private Integer quota;

    public KeyDto() {
    }
    public KeyDto(ClientEntity clientEntity) {
        this.apikey = clientEntity.getApiKey();
        this.quota = clientEntity.getQuota();
    }

}
