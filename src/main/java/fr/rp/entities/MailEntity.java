package fr.rp.entities;

import fr.rp.dto.MailDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name ="MAIL")
@Getter
@Setter
public class MailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="ID_MAIL")
    private Integer idMail;
    @Column(name= "DESTINATAIRE")
    private String destinataire;
    @Column(name= "OBJET")
    private String objet;
    @Column(name= "DATE_ENVOI")
    private LocalDate dateEnvoi;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENT")
    private ClientEntity client;

    public MailEntity(MailDto mailDto, ClientEntity client) {
        this.destinataire = mailDto.getDestinataire();
        this.objet = mailDto.getObjet();
        this.dateEnvoi = LocalDate.now();
        this.client = client;
    }

    public MailEntity() {}
}
