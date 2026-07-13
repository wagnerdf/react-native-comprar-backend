package com.wagnerdf.comprar.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.wagnerdf.comprar.enums.Gender;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * ============================================================================
 * GlobalExceptionHandler
 * ============================================================================
 *
 * Centraliza o tratamento de exceções da API REST.
 *
 * Responsabilidades:
 * - Padronizar respostas de erro.
 * - Evitar exposição de stacktrace ao cliente.
 * - Retornar códigos HTTP apropriados.
 * - Fornecer mensagens consistentes para consumo da API.
 *
 * Todas as exceções da aplicação devem possuir um tratamento
 * específico antes de recorrer ao tratamento genérico (HTTP 500).
 *
 * ============================================================================
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 🔐 Trata falhas de autenticação.
	 *
	 * Utilizado quando:
	 * - Login com credenciais inválidas;
	 * - Refresh Token inválido;
	 * - Refresh Token expirado;
	 * - Token JWT inválido.
	 *
	 * Retorna:
	 * HTTP 401 - Unauthorized
	 */
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiError> handleAuthException(
	        AuthenticationException ex,
	        HttpServletRequest request
    ) {

		ApiError error = ApiError.builder()
	            .timestamp(LocalDateTime.now())
	            .status(HttpStatus.UNAUTHORIZED.value())
	            .error("Unauthorized")
	            .message(ex.getMessage())
	            .path(request.getRequestURI())
	            .build();

	    return ResponseEntity
	            .status(HttpStatus.UNAUTHORIZED)
	            .body(error);
    }

	/**
	 * 🚨 Trata exceções não previstas pela aplicação.
	 *
	 * Este é o último tratamento executado quando nenhuma
	 * Exception específica foi capturada.
	 *
	 * Utilizado para evitar que erros internos sejam
	 * expostos ao cliente.
	 *
	 * Retorna:
	 * HTTP 500 - Internal Server Error
	 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("Erro interno no servidor")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
    
    /**
     * 📋 Trata regras de negócio da aplicação.
     *
     * Exemplos:
     * - SKU já cadastrado;
     * - Email já existente;
     * - Username duplicado;
     * - Categoria já cadastrada.
     *
     * Retorna:
     * HTTP 400 - Bad Request
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request
    ) {

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    
    /**
     * ✅ Trata erros de validação dos DTOs.
     *
     * Disparado automaticamente pelas anotações Bean Validation.
     *
     * Exemplos:
     * - @NotBlank
     * - @NotNull
     * - @Size
     * - @Positive
     *
     * Retorna:
     * HTTP 400 - Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Erro de validação");

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .message(message)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    
    /**
     * 🔎 Obtém todos os valores possíveis de um Enum.
     *
     * Utilizado para montar mensagens amigáveis quando
     * o cliente informa um valor inválido.
     *
     * Exemplo:
     * Gender: MALE, FEMALE
     */
    public static String getEnumValues(Class<? extends Enum<?>> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
    
    /**
     * 🔤 Trata valores inválidos enviados para campos Enum.
     *
     * Exemplo:
     *
     * Gender = "MASCULINO"
     *
     * Quando o esperado seria:
     *
     * MALE
     * FEMALE
     *
     * Retorna:
     * HTTP 400 - Bad Request
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleInvalidEnum(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {

    	String validValues = getEnumValues(Gender.class);
    	
        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Request")
                .message("Gender inválido. Valores aceitos: " + validValues)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    
    /**
     * 👤 Trata usuário não encontrado.
     *
     * Utilizado quando um usuário informado por ID
     * não existe na base de dados.
     *
     * Retorna:
     * HTTP 404 - Not Found
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(
            UserNotFoundException ex,
            HttpServletRequest request
    ) {

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
    
    /**
     * 📍 Trata endereço não encontrado.
     *
     * Utilizado quando um endereço informado por ID
     * não existe.
     *
     * Retorna:
     * HTTP 404 - Not Found
     */
    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ApiError> handleAddressNotFound(
            AddressNotFoundException ex,
            HttpServletRequest request
    ) {

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
    
    /**
     * 🚫 Trata regras de acesso da aplicação.
     *
     * Utilizado quando o usuário autenticado tenta
     * executar uma operação permitida tecnicamente,
     * porém proibida pelas regras de negócio.
     *
     * Exemplo:
     * - Usuário tentando alterar cadastro de outro usuário.
     *
     * Retorna:
     * HTTP 403 - Forbidden
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleForbidden(
            ForbiddenException ex,
            HttpServletRequest request
    ) {

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Forbidden")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);
    }
    
    /**
     * 📂 Trata categoria não encontrada.
     *
     * Utilizado quando uma categoria informada
     * não existe na base.
     *
     * Retorna:
     * HTTP 404 - Not Found
     */
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiError> handleCategoryNotFound(
            CategoryNotFoundException ex,
            HttpServletRequest request
    ) {

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
    
    /**
     * 👨‍💼 Trata funcionário não encontrado.
     *
     * Utilizado quando um funcionário informado
     * por ID não existe.
     *
     * Retorna:
     * HTTP 404 - Not Found
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ApiError> handleEmployeeNotFound(
            EmployeeNotFoundException ex,
            HttpServletRequest request
    ) {

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
    
    /**
     * 🔒 Trata acessos negados pelo Spring Security.
     *
     * Disparado automaticamente quando o usuário
     * autenticado não possui a Role ou Permission
     * necessária para executar determinada operação.
     *
     * Exemplos:
     * - USER tentando criar Product.
     * - EMPLOYEE tentando criar Employee.
     * - USER tentando acessar endpoints administrativos.
     *
     * Retorna:
     * HTTP 403 - Forbidden
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Forbidden")
                .message("Você não possui permissão para executar esta operação.")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);

    }
    
    /**
     * 📦 Trata produto não encontrado.
     *
     * Utilizado quando um produto informado
     * por ID não existe na base.
     *
     * Retorna:
     * HTTP 404 - Not Found
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handleProductNotFound(
            ProductNotFoundException ex,
            HttpServletRequest request
    ) {

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);

    }
    
    /**
     * ==========================================================
     * ORDER NOT FOUND
     * ==========================================================
     *
     * Trata exceções quando um pedido não é encontrado.
     *
     * Retorno:
     *
     * HTTP 404 - NOT FOUND
     *
     * Exemplo:
     *
     * {
     *   "timestamp": "...",
     *   "status":404,
     *   "error":"Not Found",
     *   "message":"Pedido não encontrado.",
     *   "path":"/orders/{id}"
     * }
     *
     */
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiError> handleOrderNotFound(
            OrderNotFoundException ex,
            HttpServletRequest request
    ) {

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);

    }
}