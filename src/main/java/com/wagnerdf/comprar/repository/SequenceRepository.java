package com.wagnerdf.comprar.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class SequenceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Long nextOrderSequence() {

        return ((Number) entityManager
                .createNativeQuery("SELECT nextval('order_sequence')")
                .getSingleResult())
                .longValue();

    }

}