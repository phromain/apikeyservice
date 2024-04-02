package fr.rp.entrant;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Mail {

    @NotNull(message = "L'email ne peut pas être null")
    @NotBlank(message = "L'email ne peut pas être vide")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Format mail non valide exemple@mail.fr")
    @JsonProperty("destinataire")
    private String destinataire;


    @NotNull(message = "L'objet ne peut pas être null")
    @NotBlank(message = "L'objet ne peut pas être vide")
    @JsonProperty("objet")
    private String objet;

}
