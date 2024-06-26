package fr.rp.repositories;

import fr.rp.DtoIn.MailDtoIn;
import fr.rp.entities.ClientEntity;
import fr.rp.entities.MailEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;

import java.time.LocalDate;

@RequestScoped
public class MailRepository implements PanacheRepositoryBase<MailEntity, Integer> {

    public void enregistrerMail(MailDtoIn mailDtoIn, ClientEntity client){
        MailEntity mailEntity = new MailEntity(mailDtoIn,client);
        this.persist(mailEntity);
    }


    public int countMailParMois(Integer idclient) {
        LocalDate dateActuel = LocalDate.now();
        int mois = dateActuel.getMonthValue();
        Integer nbreMailClientMoisEnCour = Math.toIntExact(this.find("client.idClient = ?1 and MONTH(dateEnvoi) = ?2", idclient, mois).count());
        return nbreMailClientMoisEnCour;

    }


}
