package com.wagnerdf.comprar.exception;

public class ShippingOptionNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ShippingOptionNotFoundException(String id) {
        super("Shipping option not found: " + id);
    }

}