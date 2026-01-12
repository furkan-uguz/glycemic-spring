package com.glycemic.services.auth.model.activation;

import com.glycemic.core.model.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ActivationResponse extends BaseResponse {
    int result;
}
