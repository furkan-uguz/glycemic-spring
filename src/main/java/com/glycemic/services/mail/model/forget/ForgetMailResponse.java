package com.glycemic.services.mail.model.forget;

import com.glycemic.core.model.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ForgetMailResponse extends BaseResponse {
    private Integer result;
}
