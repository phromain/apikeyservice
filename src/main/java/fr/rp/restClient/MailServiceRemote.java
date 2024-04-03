package fr.rp.restClient;


import fr.rp.DtoOut.MailDtoOut;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.HeaderParam;

@Path("/mail")
@RegisterRestClient(baseUri = "MailServiceRemote")
public interface MailServiceRemote {


    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    Response envoyerMail(@HeaderParam ("API-KEY") String apiKey, MailDtoOut mailDtoOut);


}


