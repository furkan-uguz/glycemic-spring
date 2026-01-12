package com.glycemic.services.glycemic.model.nutritional.list;

import com.glycemic.core.model.BaseResponse;
import com.glycemic.entity.model.glycemic.Nutritional;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class NutritionalListResponse extends BaseResponse {
    private List<Nutritional> nutritionalList;
}
