package com.glycemic.services.auth.model.login;

import com.glycemic.core.model.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoginResponse extends BaseResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -4985856907026299901L;

    private Long id;
    private String token;
    private String email;
    private String fullname;
    private String name;
    private String surname;
    private Boolean enable;
}
