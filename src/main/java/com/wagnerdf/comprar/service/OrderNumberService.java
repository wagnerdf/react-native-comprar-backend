package com.wagnerdf.comprar.service;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.wagnerdf.comprar.enums.State;
import com.wagnerdf.comprar.repository.SequenceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderNumberService {

    private final SequenceRepository sequenceRepository;

    public String generateOrderNumber(State state) {

        Long sequence = sequenceRepository.nextOrderSequence();

        String year = String.valueOf(LocalDate.now().getYear());
        
        String uf = state.name();

        int random =
                ThreadLocalRandom.current()
                        .nextInt(100, 1000);

        return "ORD"
        + year
        + uf
        + String.format("%03d", random)
        + sequence;
    }

}