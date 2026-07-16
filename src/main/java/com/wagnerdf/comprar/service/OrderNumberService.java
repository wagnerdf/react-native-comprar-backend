package com.wagnerdf.comprar.service;

import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.wagnerdf.comprar.repository.SequenceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderNumberService {

    private final SequenceRepository sequenceRepository;

    public String generate() {

        Long sequence = sequenceRepository.nextOrderSequence();

        int year = Year.now().getValue();

        int random =
                ThreadLocalRandom.current()
                        .nextInt(100, 1000);

        return String.format(
                "ORD-%d-%03d-%d",
                year,
                random,
                sequence
        );
    }

}