package com.nixagh.classicmodels.utils.excel;

import com.nixagh.classicmodels.controller.StatisticalController;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Transactional
public class ExcelUtil {
    public static <T> StatisticalController.ByteArrayInputStreamResponse writeExcel(ExcelConfig config, List<T> data) throws IOException, NoSuchFieldException, IllegalAccessException {
        // create excel file
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // create workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(config.getSheetName());

        // write header
        writeHeader(sheet, config.getHeader(), config.getStartRow());

        int rowNum = config.getStartRow() + 1;

        // get fields from data
        String[] fields = Arrays.stream(config.getHeader()).map(h -> {
            String temp = h.replaceAll(" ", "");
            return temp.substring(0, 1).toLowerCase() + temp.substring(1);
        }).toArray(String[]::new);

        // write data
        for (T d : data) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            // get data from fields
            for (String f : fields) {
                Field field = d.getClass().getDeclaredField(f);
                field.setAccessible(true);
                Cell cell = row.createCell(colNum++);
                if (field.get(d) == null) {
                    cell.setCellValue("null");
                } else {
                    // get type of field
                    var type = field.getType().getName();
                    switch (type) {
                        case "java.lang.Integer" -> cell.setCellValue(field.getInt(d));
                        case "java.lang.Double" -> cell.setCellValue((Double) field.get(d));
                        case "java.lang.Float" -> cell.setCellValue(field.getFloat(d));
                        case "java.lang.Long" -> cell.setCellValue(field.getLong(d));
                        case "java.lang.Short" -> cell.setCellValue(field.getShort(d));
                        case "java.lang.Byte" -> cell.setCellValue(field.getByte(d));
                        case "java.lang.Boolean" -> cell.setCellValue(field.getBoolean(d));
                        case "java.lang.Character" -> cell.setCellValue(field.getChar(d));
                        default -> cell.setCellValue(field.get(d).toString());
                    }
                }
            }
        }


        workbook.write(out);
        // return output stream
        return new StatisticalController.ByteArrayInputStreamResponse(new ByteArrayInputStream(out.toByteArray()), config.getFileName());
    }

    private static void writeHeader(Sheet sheet, String[] header, int startRow) {
        Row row = sheet.createRow(startRow);
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        cellStyle.setFont(font);

        for (int i = 0; i < header.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(header[i]);
        }
    }
}
