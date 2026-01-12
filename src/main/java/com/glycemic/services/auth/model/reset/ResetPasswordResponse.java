package com.glycemic.services.auth.model.reset;

import com.glycemic.core.model.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResetPasswordResponse extends BaseResponse {
    private Integer result;
}
