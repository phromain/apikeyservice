package fr.rp.repositories;

import fr.rp.dto.MailDto;
import fr.rp.entities.ClientEntity;
import fr.rp.entities.MailEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;

@RequestScoped
public class MailRepository implements PanacheRepositoryBase<MailEntity, Integer> {

    public void enregistrerMail(MailDto mailDto, ClientEntity client){
        MailEntity mail = new MailEntity(mailDto,client);
        this.persist(mail);
    }


    public int countMailParMois(Integer idclient) {
        LocalDate dateActuel = LocalDate.now();
        int mois = dateActuel.getMonthValue();
        Integer nbreMailClientMoisEnCour = Math.toIntExact(this.find("client.idClient = ?1 and MONTH(dateEnvoi) = ?2", idclient, mois).count());
        return nbreMailClientMoisEnCour;

    }


}
