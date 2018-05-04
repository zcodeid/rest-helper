package id.zcode.resthelper.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    protected ResponseEntity generalHandling(RuntimeException ex, WebRequest request) {
        Map<String, String> map = new HashMap();
        map.put("message", "Mohon maaf, ada kesalahan pada sistem kami.");
        map.put("engineMessage", ex.getMessage());
        return handleExceptionInternal(ex, map,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler
    protected ResponseEntity generalHandling(ZCodeException ex, WebRequest request) {
        Map<String, String> map = new HashMap();
        map.put("message", ex.getMessage());
        map.put("engineMessage", null);
        return handleExceptionInternal(ex, map,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


}