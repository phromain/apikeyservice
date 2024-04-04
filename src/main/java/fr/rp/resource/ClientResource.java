package fr.rp.resource;


import fr.rp.DtoIn.ClientDtoIn;
import fr.rp.DtoOut.ClientDtoOut;
import fr.rp.DtoOut.MailDtoOut;
import fr.rp.repositories.ClientRepository;
import fr.rp.restClient.MailServiceRemote;
import fr.rp.entities.ClientEntity;
import fr.rp.service.ApiKeyGenerator;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
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
        return Response.ok(ClientDtoOut.toDtoListClient(listeClients)).build();
    }


    @POST
    @Transactional
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Crée un client", description = " Crée un client")
    @APIResponse(responseCode = "200", description = "Client Créer")
    @APIResponse(responseCode = "400", description = "paramètre absent")
    @APIResponse(responseCode = "400", description = "Retourne l'erreur sur les champs")
    @APIResponse(responseCode = "500", description = "Une erreur est survenue")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createClient (@Valid ClientDtoIn client){
        if(client == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("paramètre absent")
                    .build();
        }
        try{
            ClientEntity clientEntity = new ClientEntity(client);
            clientRepository.persist(clientEntity);
            MailDtoOut mailDtoOut = new MailDtoOut(clientEntity);
            mailDtoOut.mailClientCreated(clientEntity.getApiKey(), mailDtoOut);

            mailServiceRemote.envoyerMail("azerty", mailDtoOut);

            return Response.ok()
                    .entity("Client Créer").build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
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
            ClientDtoOut clientDtoOut = new ClientDtoOut(client);
            return Response.ok(clientDtoOut).build();
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
    @APIResponse(responseCode = "400", description = "Valeur vide ou inexistante")
    @APIResponse(responseCode = "400", description = "nombre incorrect ex: 500")
    @APIResponse(responseCode = "404", description = "Client non trouvée")
    @APIResponse(responseCode = "500", description = "Une erreur est survenue")
    @Path("/{id}/quota")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateQuotaById(@PathParam("id") Integer id, String newQuota){
        ClientEntity client = clientRepository.findById(id);

        if (client == null ){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Client non trouvé")
                    .build();
        }

        if (newQuota.isEmpty()){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Valeur vide ou inexistante")
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
                    .entity("nombre incorrect ex: 500")
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
    @APIResponse(responseCode = "404", description = "Client non trouvé")
    @APIResponse(responseCode = "500", description = "Une erreur est survenue")
    @Path("/{id}/newkey")
    @Produces(MediaType.TEXT_PLAIN)
    public Response renewKeyById(@PathParam("id") Integer id){
        ClientEntity client = clientRepository.findById(id);
        if (client == null ){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Client non trouvé")
                    .build();
        }
        try{
            String newKey = ApiKeyGenerator.generateApiKey();
            client.setApiKey(newKey);
            clientRepository.persist(client);

            MailDtoOut mailDtoOut = new MailDtoOut(client);
            mailDtoOut.mailClientRenew(client.getApiKey(), mailDtoOut);
            mailServiceRemote.envoyerMail("azerty", mailDtoOut);
            return Response.ok()
                    .entity("API-KEY régénérée").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Une erreur est survenue")
                    .build();
        }
    }

}
