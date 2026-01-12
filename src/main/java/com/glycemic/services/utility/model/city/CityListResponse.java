package com.glycemic.services.utility.model.city;

import com.glycemic.core.model.BaseResponse;
import com.glycemic.entity.model.glycemic.City;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CityListResponse extends BaseResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 8290255678413854521L;

    private List<City> cityList;
}
