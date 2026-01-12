package com.glycemic.services.glycemic.model.nutritional.cud;

import com.glycemic.core.model.BaseResponse;
import com.glycemic.entity.model.glycemic.Nutritional;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NutritionalCUDResponse extends BaseResponse {
    private Nutritional nutritional;
}
