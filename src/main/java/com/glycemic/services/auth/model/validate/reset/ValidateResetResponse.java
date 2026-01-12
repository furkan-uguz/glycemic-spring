package com.glycemic.services.auth.model.validate.reset;

import com.glycemic.core.model.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidateResetResponse extends BaseResponse {
    private Integer result;
}
