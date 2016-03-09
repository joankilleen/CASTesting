package org.greetings.rest.service;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.greetings.service.Greetings;

@Path("greetings")
public class GreetingsResource {

    @EJB
    Greetings greetings;

    @GET
    @Path("{userId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response greeting(@PathParam("userId") int userId) {
        String greetingMessage = greetings.generatesGreeting(userId);
        return Response.ok(greetingMessage).build();
    }

}