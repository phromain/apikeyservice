package fr.rp.DtoIn;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDtoIn {

    @NotNull(message = "L'email ne peut pas être null")
    @NotBlank(message = "L'email ne peut pas être vide")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Format mail non valide exemple@mail.fr")
    @JsonProperty("email")
    private String email;

    @NotNull(message = "Le nom client ne peut pas être null")
    @NotBlank(message = "Le nom client ne peut pas être vide")
    @JsonProperty("nomClient")
    private String nomClient;

    @NotNull(message = "Le quota ne peut pas être null")
    @JsonProperty("quota")
    private Integer quota;

    // Contructs

    public ClientDtoIn(String email, String nomClient, Integer quota) {
        this.email = email;
        this.nomClient = nomClient;
        this.quota = quota;
    }
}
