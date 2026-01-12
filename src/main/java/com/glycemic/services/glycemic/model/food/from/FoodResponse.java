package com.glycemic.services.glycemic.model.food.from;

import com.glycemic.core.model.BaseResponse;
import com.glycemic.entity.model.glycemic.Food;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FoodResponse extends BaseResponse {

    private Food food;
}
