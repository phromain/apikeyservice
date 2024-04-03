package fr.rp.entities;


import fr.rp.DtoIn.MailDtoIn;
import jakarta.persistence.*;


import java.time.LocalDate;

@Entity(name ="MAIL")

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

    public MailEntity(MailDtoIn mailDtoIn, ClientEntity client) {
        this.destinataire = mailDtoIn.getDestinataire();
        this.objet = mailDtoIn.getObjet();
        this.dateEnvoi = LocalDate.now();
        this.client = client;
    }

    public MailEntity() {}
}
