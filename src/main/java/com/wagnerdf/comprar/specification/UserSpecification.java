package com.wagnerdf.comprar.specification;

import com.wagnerdf.comprar.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> nameContains(String name) {
        return (root, query, cb) ->
                name == null ? null :
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<User> emailContains(String email) {
        return (root, query, cb) ->
                email == null ? null :
                cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }
    
    public static Specification<User> isActive() {
        return (root, query, cb) ->
                cb.isTrue(root.get("active"));
    }
}
