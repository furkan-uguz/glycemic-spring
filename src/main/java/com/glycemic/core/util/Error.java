package com.glycemic.core.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Error implements Serializable {

    @Serial
    private static final long serialVersionUID = -7256538724438256922L;

    private HttpStatus httpStatus;

    private ErrorHandleType errorType;

    private String message;

    private String details;

    private String date;

    private String time;

    @Override
    public String toString() {
        return "{\n\"httpStatus\":\"" + httpStatus.value() + "\",\n\"error\":\"" + errorType.value() + "\",\n\"message\":\"" + message + "\",\n\"details\":\"" + details + "\",\n\"date\":\"" + date + "\",\n\"time\":\"" + time + "\"\n}";
    }
}
