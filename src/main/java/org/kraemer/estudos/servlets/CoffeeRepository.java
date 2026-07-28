package org.kraemer.estudos.servlets;

import java.util.List;
import java.util.Optional;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class CoffeeRepository {
    
    @PersistenceContext
    private EntityManager em;

    public Optional<Coffee> findById(Long id) {
        return Optional.ofNullable(em.find(Coffee.class, id));
    }

    public List<Coffee> listAll() {
        return em.createQuery(
            "SELECT c FROM Coffee c", 
            Coffee.class)
            .getResultList();
    }

    public Coffee create (Coffee coffee) {
        em.persist(coffee);
        return coffee;
    }

    public Coffee update(Coffee coffee) {
        return em.merge(coffee);
    }

    public void remove(Long id) {
        var coffee = findById(id).orElseThrow(() -> new IllegalArgumentException("Id não existe: " + id));
        em.remove(coffee);
    }
}