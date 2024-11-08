package br.pucminas.dwfs.pi.infra.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;

/**
 * Tests the outgoing exception mapper class.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (rperandre)
 * @version 1.0
 * @since 08/11/2024
 */
@QuarkusTest
class OutgoingExceptionMapperTest {

    private static final OutgoingExceptionMapper MAPPER = new OutgoingExceptionMapper();

    @Test
    void testToResponse_whenItIsAnAppException_thenMustReturnAResponseWithAppExceptionHttpStatus() {
        int httpStatus = 404;
        String message = "An app exception";

        AppException exception = new AppException(httpStatus, message);

        Response response = MAPPER.toResponse(exception);

        assertEquals(httpStatus, response.getStatus());
        assertEquals(message, ((AppError) response.getEntity()).getMessage());
    }

    @Test
    void testToResponse_whenItIsAnAppException_thenMustReturnAResponseWithADefaultHttpStatus() {
        int httpStatus = AppException.DEFAULT_HTTP_STATUS;
        String message = "A regular exception";

        Exception exception = new Exception(message);

        Response response = MAPPER.toResponse(exception);

        assertEquals(httpStatus, response.getStatus());
        assertEquals(message, ((AppError) response.getEntity()).getMessage());
    }
}