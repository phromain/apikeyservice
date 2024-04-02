package fr.rp.restClient;

import fr.rp.dto.MailDto;
import jakarta.ws.rs.*;
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
    Response envoyerMail(@HeaderParam ("API-KEY") String apiKey, MailDto mailDto);


}


