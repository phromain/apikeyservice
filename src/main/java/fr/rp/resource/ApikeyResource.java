package fr.rp.resource;

import fr.rp.DtoOut.KeyDtoOut;
import fr.rp.DtoIn.MailDtoIn;
import fr.rp.repositories.ClientRepository;
import fr.rp.repositories.MailRepository;
import fr.rp.entities.ClientEntity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;


@Path("/apikey")
public class ApikeyResource {

    @Inject
    ClientRepository clientRepository;

    @Inject
    MailRepository mailRepository;

    @GET
    @Operation(summary = "Verifie l API-KEY", description = "Verifie l API-KEY et retourne la Key + Quota")
    @APIResponse(responseCode = "200", description = "Key valide")
    @APIResponse(responseCode = "404", description = "Key non trouvée")
    @Path("/{apikey}")
    @Produces({MediaType.APPLICATION_JSON, (MediaType.TEXT_PLAIN)})
    public Response findByApiKey(@PathParam("apikey") String apikey){
        ClientEntity client = clientRepository.clientByApiKey(apikey);

        if (client != null ){

            KeyDtoOut keyDtoOut = new KeyDtoOut(client);
            return Response.ok(keyDtoOut)
                    .build();

        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Key non trouvée")
                .type(MediaType.TEXT_PLAIN)
                .build();

    }

    @GET
    @Operation(summary = "Retourne Count Mail", description = "Retourne le nombre de mail envoyé dans le mois en cours")
    @APIResponse(responseCode = "200", description = "Count Mail ok")
    @APIResponse(responseCode = "404", description = "Key non trouvée")
    @Path("/{apikey}/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response findByApiKeyCountMail(@PathParam("apikey") String apikey){
        ClientEntity client = clientRepository.clientByApiKey(apikey);

        if (client != null ){
            Integer count = mailRepository.countMailParMois(client.getIdClient());
            return Response.ok(count).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Key non trouvée")
                .build();
    }


    @Transactional
    @POST
    @Operation(summary = "Enregistrement Mail", description = "Enregistrement le mail dans la bdd")
    @APIResponse(responseCode = "200", description = "Mail enregistré")
    @APIResponse(responseCode = "404", description = "Key non trouvée")
    @APIResponse(responseCode = "400", description = "Erreur sur les champs")
    @APIResponse(responseCode = "500", description = "Une erreur est survenue Mail non enregistré")
    @Path("/{apikey}/mail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveMail(@PathParam("apikey") String apikey,@Valid MailDtoIn mailDtoIn){
        if(mailDtoIn == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("paramètre absent")
                    .build();
        }
        ClientEntity client = clientRepository.clientByApiKey(apikey);
        if (client != null ){
            try {
                mailRepository.enregistrerMail(mailDtoIn,client);
                return Response.ok()
                        .entity("Mail enregistré")
                        .build();
            } catch (ConstraintViolationException e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(e.getMessage())
                        .build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Une erreur est survenue Mail non enregistré")
                        .build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Key non trouvée")
                .build();
    }

    @GET
    @Operation(summary = "Verifie le statut du compte", description = "Verifie le statut du compte")
    @APIResponse(responseCode = "200", description = "Compte Activé")
    @APIResponse(responseCode = "403", description = "Compte Désactivé")
    @APIResponse(responseCode = "404", description = "Key non trouvée")
    @Path("/{apikey}/statut")
    @Produces(MediaType.TEXT_PLAIN)
    public Response findByApiKeyValidAccount(@PathParam("apikey") String apikey){
        ClientEntity client = clientRepository.clientByApiKey(apikey);

        if (client != null ) {
            if (client.isStatut()) {
                return Response.ok()
                        .entity("Compte Activé.")
                        .build();

            }
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Compte Desactivé.")
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Key non trouvée")
                .build();
    }
}
