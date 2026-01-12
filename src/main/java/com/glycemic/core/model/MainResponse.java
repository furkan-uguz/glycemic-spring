package com.glycemic.core.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MainResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 8290255678254854521L;

    private Boolean status;
    private HttpStatus errors;
    private Integer errorCode;
    private String message;
    private BaseResponse result;
}
