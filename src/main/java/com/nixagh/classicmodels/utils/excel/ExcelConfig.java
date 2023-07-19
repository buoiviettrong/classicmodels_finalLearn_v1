package com.nixagh.classicmodels.utils.excel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExcelConfig {
    private String excelPath;
    private String fileName;
    private String sheetName;
    private String[] header;
    private int startRow;
}
