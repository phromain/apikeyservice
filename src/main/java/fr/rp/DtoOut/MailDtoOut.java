package fr.rp.DtoOut;

import fr.rp.entities.ClientEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDtoOut {

    //Attributs

    private String destinataire;
    private String objet;
    private String body;

    // Contructs

    public MailDtoOut(ClientEntity clientEntity) {
        this.destinataire = clientEntity.getEmail();
    }

    public MailDtoOut() {
    }
    // Methods


    public void mailClientCreated(String apiKey, MailDtoOut mailDtoOut){
        mailDtoOut.setObjet("Création de votre clé API-KEY");
        mailDtoOut.setBody( "Bonjour\n" +
                "Pour pouvoir utiliser notre service Mail, vous allez avoir besoin de votre clé Apikey: " + apiKey + "\n" +
                "Veuillez la saisir dans l'entête avec la  notion API-KEY\n"+
                "Cordialement");
    }

    public void mailClientRenew(String apiKey, MailDtoOut mailDtoOut){
        mailDtoOut.setObjet("Renouvellement de votre clé API-KEY");
        mailDtoOut.setBody( "Bonjour\n" +
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
