package org.kraemer.estudos.servlets.jax_rs;

import java.net.URI;

import org.kraemer.estudos.servlets.Coffee;
import org.kraemer.estudos.servlets.CoffeeRepository;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("cafe")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CofeeResourcec {
    
    @Inject
    private CoffeeRepository repo;


    @GET
    public Response listAll() {
        return Response.ok(repo.listAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        var cafe = repo.findById(id);
        if(cafe.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(cafe.get()).build();
    }

    @POST
    public Response criar(@Context UriInfo uriInfo, Coffee coffee) {
        var created = repo.create(coffee);

        URI location = uriInfo.getAbsolutePathBuilder()
            .path(created.getId()
            .toString()).build();


        return Response.created(location).entity(created).build();
    }

    @PUT 
    public Response update(Coffee coffee) {
        var exists = repo.findById(coffee.getId());

        if(exists.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var updated = repo.update(coffee);

        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        var exists = repo.findById(id);

        if(exists.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        repo.remove(id);

        return Response.noContent().build();
    }

}
