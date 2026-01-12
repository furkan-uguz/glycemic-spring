package com.glycemic.services.glycemic.model.category.list;

import com.glycemic.core.model.BaseResponse;
import com.glycemic.entity.model.glycemic.Category;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryListResponse extends BaseResponse {
    private List<Category> categoryList;
}
