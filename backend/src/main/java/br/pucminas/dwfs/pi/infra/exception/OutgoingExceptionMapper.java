package br.pucminas.dwfs.pi.infra.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.jbosslog.JBossLog;

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

        String message = exception.getMessage();

        return Response
            .status(httpStatus)
            .entity(message)
            .build();
    }
}