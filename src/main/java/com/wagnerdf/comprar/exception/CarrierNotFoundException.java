package com.wagnerdf.comprar.exception;

public class CarrierNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public CarrierNotFoundException(String id) {
        super("Carrier not found: " + id);
    }

}
