package global;

import com.vvts.utiles.GlobalApiResponse;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @auther kul.paudel
 * @created at 2023-04-22
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@ComponentScan
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {


    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleMethodNotFound(Exception ex, WebRequest req) {
        return new ResponseEntity<>(new GlobalApiResponse(ex.getMessage(), false, new String[0]), HttpStatus.NOT_FOUND);
    }


    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ex.printStackTrace();
        return new ResponseEntity<>(new GlobalApiResponse(ex.getMessage(), false, new String[0]), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new GlobalApiResponse(ex.getMessage(), false, new String[0]), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @Bean
    protected GlobalApiResponse handleRunTimeException(RuntimeException ex, WebRequest request) {
        return new GlobalApiResponse(ex.getMessage(), false, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Bean
    protected GlobalApiResponse handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        return new GlobalApiResponse(ex.getMessage(), false, HttpStatus.BAD_REQUEST);
    }

}
