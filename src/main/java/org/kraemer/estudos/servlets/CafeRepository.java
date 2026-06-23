package org.kraemer.estudos.servlets;

import java.util.List;
import java.util.Optional;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class CafeRepository {

    @PersistenceContext
    private EntityManager em;

    public Coffee create(Coffee coffee) {
        em.persist(coffee);

        return coffee;
    }

    public List<Coffee> listall() {
        return em.createQuery(
                "SELECT c FROM Coffee c",
                Coffee.class).getResultList();
    }

    public Optional<Coffee> findById(Long id) {
        return Optional.ofNullable(em.find(Coffee.class, id));
    }

    public void delete(Long id) {
        var coffee = findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Coffee id: " + id));
        em.remove(coffee);
    }

    public Coffee update(Coffee coffee) {
        return em.merge(coffee);
    }
}
