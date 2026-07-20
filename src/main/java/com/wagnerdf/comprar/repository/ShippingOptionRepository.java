package com.wagnerdf.comprar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.comprar.entity.Carrier;
import com.wagnerdf.comprar.entity.ShippingOption;

public interface ShippingOptionRepository
        extends JpaRepository<ShippingOption, String> {

    List<ShippingOption> findByCarrierAndActiveTrue(Carrier carrier);

}