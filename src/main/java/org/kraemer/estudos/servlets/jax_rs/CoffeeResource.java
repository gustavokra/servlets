package org.kraemer.estudos.servlets.jax_rs;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.kraemer.estudos.servlets.Coffee;
import org.kraemer.estudos.servlets.CoffeeRepository;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/coffees")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CoffeeResource {

    @Inject
    private CoffeeRepository repo;

    @GET
    public List<Coffee> findAll() {
        return repo.listAll();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {

        Optional<Coffee> coffee = repo.findById(id);

        if (coffee.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(coffee.get()).build();
    }

    @POST
    public Response create(Coffee coffee, @Context UriInfo uriInfo) {

        Coffee created = repo.create(coffee);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(created.getId().toString())
                .build();

        return Response.created(location)
                .entity(created)
                .build();
    }

    @PUT
    public Response update(Coffee coffee) {

        Optional<Coffee> coffeeExists = repo.findById(coffee.getId());

        if (coffeeExists.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Coffee updated = repo.update(coffee);

        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {

        Optional<Coffee> coffeeExists = repo.findById(id);

        if (coffeeExists.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        repo.remove(id);

        return Response.noContent().build();
    }
}