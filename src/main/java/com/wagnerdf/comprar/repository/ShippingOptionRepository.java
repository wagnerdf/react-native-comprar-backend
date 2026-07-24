package com.wagnerdf.comprar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.comprar.entity.Carrier;
import com.wagnerdf.comprar.entity.ShippingOption;

public interface ShippingOptionRepository
        extends JpaRepository<ShippingOption, String> {

    List<ShippingOption> findByCarrierAndActiveTrue(Carrier carrier);
    
    Optional<ShippingOption> findByCarrierIdAndServiceNameIgnoreCase(
            String carrierId,
            String serviceName);

}