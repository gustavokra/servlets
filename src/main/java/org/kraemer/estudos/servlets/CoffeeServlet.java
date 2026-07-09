package org.kraemer.estudos.servlets;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/coffees_servlet/*")
public class CoffeeServlet extends HttpServlet {

    @Inject
    private CafeRepository repository;
    ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo(); // "/1"

        if (pathInfo == null || pathInfo.equals("/")) {
            findAll(req, resp);
        } else {
            findById(req, resp);
        }

    }

    private void findAll(HttpServletRequest req, HttpServletResponse resp)
            throws StreamWriteException, DatabindException, IOException {
        var allCoffees = repository.listall();

        resp.setContentType("application/json");

        mapper.writeValue(resp.getWriter(), allCoffees);
    }

    private void findById(HttpServletRequest req, HttpServletResponse resp)
            throws StreamWriteException, DatabindException, IOException {

        String pathInfo = req.getPathInfo();

        Long id = Long.parseLong(pathInfo.substring(1));

        Optional<Coffee> coffee = repository.findById(id);

        if (coffee.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setContentType("application/json");

        mapper.writeValue(resp.getWriter(), coffee.get());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        var coffee = mapper.readValue(
                req.getInputStream(),
                Coffee.class);

        var cafeSalvado = repository.create(coffee);
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_CREATED);

        mapper.writeValue(resp.getWriter(), cafeSalvado);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        var coffee = mapper.readValue(
                req.getInputStream(),
                Coffee.class);

        var cafeAtualizado = repository.update(coffee);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);

        mapper.writeValue(resp.getWriter(), cafeAtualizado);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Long id = Long.parseLong(pathInfo.substring(1));

        repository.delete(id);

        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

}