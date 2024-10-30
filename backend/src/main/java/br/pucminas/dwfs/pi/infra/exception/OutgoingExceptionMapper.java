package br.pucminas.dwfs.pi.infra.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.jbosslog.JBossLog;

/**
 * Provider that maps an exception into a HTTP response.
 * 
 * @author Ricardo Giovani Piantavinha Perandré (RicardoGPP)
 * @version 1.0
 * @since 30/10/2024
 */
@JBossLog
@Provider
public class OutgoingExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        log.errorf(exception, "Outgoing exception intercepted.");

        int httpStatus = AppException.DEFAULT_HTTP_STATUS;

        if (exception instanceof AppException) {
            httpStatus = ((AppException) exception).getHttpStatus();
        }

        AppError error = new AppError();

        error.setMessage(exception.getMessage());

        return Response
            .status(httpStatus)
            .entity(error)
            .build();
    }
}