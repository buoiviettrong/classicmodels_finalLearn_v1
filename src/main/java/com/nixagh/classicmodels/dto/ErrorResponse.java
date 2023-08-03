package com.nixagh.classicmodels.dto;

import com.nixagh.classicmodels.ClassicModelsApplication;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorResponse {
    private String message = "Something went wrong";
    private String code = "500";
    private String type = "Internal Server Error";
    private String detail = ClassicModelsApplication.class.getSimpleName();
    private String moreInfo = "";
}
