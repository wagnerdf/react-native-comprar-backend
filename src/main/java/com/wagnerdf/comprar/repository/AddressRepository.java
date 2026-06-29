package com.wagnerdf.comprar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.comprar.entity.Address;
import com.wagnerdf.comprar.entity.User;

public interface AddressRepository extends JpaRepository<Address, String> {

    long countByUser(User user);

    List<Address> findByUser(User user);
    
    List<Address> findByUserOrderByDefaultAddressDescCreatedAtAsc(User user);
    
    Optional<Address> findByIdAndUser(String id, User user);
    
    Optional<Address> findFirstByUserOrderByCreatedAtDesc(User user);
}
