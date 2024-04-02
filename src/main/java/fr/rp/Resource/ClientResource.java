package fr.rp.resources;


import fr.rp.dto.ClientDtoEntrant;
import fr.rp.dto.ClientDtoSortant;
import fr.rp.dto.MailDto;
import fr.rp.entities.ClientEntity;
import fr.rp.repositories.ClientRepository;
import fr.rp.restClient.MailServiceRemote;
import fr.rp.service.ApiKeyGenerator;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;


import java.util.List;

@Path("/clients")
public class ClientResource {
    @RestClient
    MailServiceRemote mailServiceRemote;
    @Inject
    ClientRepository clientRepository;

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Affiche la liste des clients", description = "Retourne une liste de client")
    @APIResponse(responseCode = "200", description = " Liste Client")
    public Response getListClient (){
        List<ClientEntity> listeClients = clientRepository.listAll();
        return Response.ok(ClientDtoSortant.toDtoListContinent(listeClients)).build();
    }


    @POST
    @Transactional
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Crée un client", description = " Crée un client")
    @APIResponse(responseCode = "200", description = "Client Créer")
    @APIResponse(responseCode = "200", description = "API-KEY créer et Erreur lors de l'envoi du mail")
    @APIResponse(responseCode = "400", description = "paramètre absent")
    @APIResponse(responseCode = "400", description = "informations manquantes")
    @APIResponse(responseCode = "500", description = "Une erreur est survenue")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createClient (ClientDtoEntrant clientDtoEntrant){
        if(clientDtoEntrant == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("paramètre absent")
                    .build();
        }
        if (!clientDtoEntrant.champsValide()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("informations manquantes")
                    .build();
        }
        try{
            ClientEntity client = new ClientEntity(clientDtoEntrant);
            System.out.println(client);
            clientRepository.persist(client);

            MailDto mailDto = new MailDto(client);
            mailDto.mailClientCreated(client.getApiKey(),mailDto);
            Response responseMail = mailServiceRemote.envoyerMail("azerty",mailDto);
            if (responseMail.getStatus() != 200) {
                return Response.ok()
                        .entity("API-KEY créer et Erreur lors de l'envoi du mail").build();
            }

            return Response.ok()
                    .entity("Client Créer").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Une erreur est survenue : " + e.getMessage())
                    .build();
        }

    }




    @GET
    @Operation(summary = "Retourne les info du client", description = "Retourne les info du client ")
    @APIResponse(responseCode = "200", description = "Client trouvé")
    @APIResponse(responseCode = "404", description = "Client non trouvée")
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Response findClientById(@PathParam("id") Integer id){
        ClientEntity client = clientRepository.findById(id);

        if (client != null ){
            ClientDtoSortant clientDtoSortant = new ClientDtoSortant(client);
            return Response.ok(clientDtoSortant).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Key non trouvée")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }

    @PUT
    @Transactional
    @Operation(summary = "Met à jour le Quota", description = "Modifie le Quota ")
    @APIResponse(responseCode = "200", description = "Quota mis à jour ")
    @APIResponse(responseCode = "400", description = "informations manquantes ou incorrecte (nombre)")
    @APIResponse(responseCode = "404", description = "Client non trouvée")
    @APIResponse(responseCode = "500", description = "Une erreur est survenue")
    @Path("/{id}/quota")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateQuotaById(@PathParam("id") Integer id, String newQuota){
        ClientEntity client = clientRepository.findById(id);

        if (client == null ){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Client non trouvée")
                    .build();
        }
        try {
            int quota = Integer.parseInt(newQuota);
            client.setQuota(quota);
            clientRepository.persist(client);
            return Response.ok()
                    .entity("Quota mis à jour")
                    .build();

        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("informations manquantes ou incorrecte (nombre)")
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Une erreur est survenue : " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Transactional
    @Operation(summary = "Reinitialise la clé API-KEY", description = " Régénere une nouvelle API-KEY")
    @APIResponse(responseCode = "200", description = " API-KEY régénérée ")
    @APIResponse(responseCode = "200", description = " API-KEY régénérée et Erreur lors de l'envoi du mail")
    @APIResponse(responseCode = "404", description = "Client non trouvée")
    @APIResponse(responseCode = "500", description = "Une erreur est survenue")
    @Path("/{id}/newkey")
    @Produces(MediaType.TEXT_PLAIN)
    public Response renewKeyById(@PathParam("id") Integer id){
        ClientEntity client = clientRepository.findById(id);

        if (client == null ){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Client non trouvée")
                    .build();
        }

        try{
            String newKey = ApiKeyGenerator.generateApiKey();
            client.setApiKey(newKey);
            clientRepository.persist(client);

            MailDto mailDto = new MailDto(client);
            mailDto.mailClientRenew(client.getApiKey(),mailDto);
            Response responseMail = mailServiceRemote.envoyerMail("azerty",mailDto);
            if (responseMail.getStatus() != 200) {
                return Response.ok()
                        .entity("API-KEY régénérée et Erreur lors de l'envoi du mail")
                        .build();
            }

            return Response.ok()
                    .entity("API-KEY régénérée").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Une erreur est survenue : " + e.getMessage())
                    .build();
        }
    }

}
