package com.glycemic.services.glycemic.model.category.cud;

import com.glycemic.core.model.BaseResponse;
import com.glycemic.entity.model.glycemic.Category;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryCUDResponse extends BaseResponse {
    private Category category;
}
