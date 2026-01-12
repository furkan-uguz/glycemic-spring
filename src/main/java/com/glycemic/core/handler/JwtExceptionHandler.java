package com.glycemic.core.handler;

import com.glycemic.core.util.Error;
import com.glycemic.core.util.ErrorHandleType;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class JwtExceptionHandler {

	public void jwtException(HttpServletResponse response, JwtException exception){
		String errorMessage = "Invalid json web token.";
		Error error = new Error(HttpStatus.BAD_REQUEST, ErrorHandleType.JWT_SIGNATURE, errorMessage, exception.getLocalizedMessage() ,LocalDateTime.now().toLocalDate().toString(), LocalDateTime.now().toLocalTime().toString());

		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(error.toString());
		} catch (IOException e) {
			log.error("Could not responded because the writer was not found or missing.");
		}
	}
}
