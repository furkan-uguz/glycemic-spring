package com.glycemic.services.glycemic.model.category.from;

import com.glycemic.core.model.BaseResponse;
import com.glycemic.entity.model.glycemic.Category;
import com.glycemic.entity.model.glycemic.Food;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryByResponse extends BaseResponse {
    private Category category;

    private List<Food> foodList;

    private Long totalElements;

    private Integer totalPage;

    private Integer page;
}
