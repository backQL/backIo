package io.backQL.BackIo.utils;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.backQL.BackIo.domain.HttpResponse;
import io.backQL.BackIo.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.io.OutputStream;
import java.time.LocalTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Slf4j
public class ExceptionUtils {
    public static void processError(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        if(exception instanceof ApiException || exception instanceof DisabledException || exception instanceof LockedException ||
           exception instanceof InvalidClaimException || exception instanceof BadCredentialsException ||
           exception instanceof TokenExpiredException) {
                HttpResponse httpResponse = getHttpResponse(response, exception.getMessage(), BAD_REQUEST);
                writeResponse(response, httpResponse);
        } else {HttpResponse httpResponse = getHttpResponse(response, "An error occured, please try again.", INTERNAL_SERVER_ERROR);
            writeResponse(response, httpResponse);}
        log.error(exception.getMessage());
    }
    private static void writeResponse(HttpServletResponse response, HttpResponse httpResponse) {
        OutputStream out;
        try{
            out = response.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, httpResponse);
            out.flush();
        }catch (Exception exception) {
            log.error(exception.getMessage());
            exception.printStackTrace();
        }
    }
    public static HttpResponse getHttpResponse(HttpServletResponse response, String message, HttpStatus httpStatus) {
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(LocalTime.now().toString())
                .reason(message)
                .status(httpStatus)
                .statusCode(httpStatus.value())
                .build();
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        return httpResponse;
    }
}