package com.nixagh.classicmodels.dto._statistic.synthetic;

import com.nixagh.classicmodels.dto._statistic.synthetic.details.SyntheticProduct;
import com.nixagh.classicmodels.dto._statistic.synthetic.overview.Overview;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyntheticStatisticResponse {
    private Overview overview;  // Tổng quan
    private SyntheticProduct syntheticProduct;  // Danh sách Loai sản phẩm với số lượng bán được và doanh thu
}


