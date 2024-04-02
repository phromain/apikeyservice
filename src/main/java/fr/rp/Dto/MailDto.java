package fr.rp.Dto;

import fr.rp.entities.ClientEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDto {

    //Attributs

    private String destinataire;
    private String objet;
    private String body;

    // Contructs

    public MailDto(ClientEntity clientEntity) {
        this.destinataire = clientEntity.getEmail();
    }

    // Methods


    public void mailClientCreated(String apiKey, MailDto mailDto){
        mailDto.setObjet("Création de votre clé API-KEY");
        mailDto.setBody( "Bonjour\n" +
                "Pour pouvoir utiliser notre service Mail, vous allez avoir besoin de votre clé Apikey: " + apiKey + "\n" +
                "Veuillez la saisir dans l'entête avec la  notion API-KEY\n"+
                "Cordialement");
    }

    public void mailClientRenew(String apiKey, MailDto mailDto){
        mailDto.setObjet("Renouvellement de votre clé API-KEY");
        mailDto.setBody( "Bonjour\n" +
                "Suite à votre demande de renouvellement de clé Apikey voici la nouvelle : " + apiKey + "\n" +
                "Cordialement");
    }

    @Override
    public String toString() {
        return "MailDto{" +
                "destinataire='" + destinataire + '\'' +
                ", objet='" + objet + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
