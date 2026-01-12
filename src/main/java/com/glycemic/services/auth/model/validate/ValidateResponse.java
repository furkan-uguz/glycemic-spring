package com.glycemic.services.auth.model.validate;

import com.glycemic.core.model.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidateResponse extends BaseResponse {
    private Integer result;
}
