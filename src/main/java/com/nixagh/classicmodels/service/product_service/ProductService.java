package com.nixagh.classicmodels.service.product_service;

import com.nixagh.classicmodels.config.excel.ExcelConfig;
import com.nixagh.classicmodels.controller.ProductController;
import com.nixagh.classicmodels.controller.StatisticalController;
import com.nixagh.classicmodels.dto._statistic.details.DetailsOverview;
import com.nixagh.classicmodels.dto._statistic.details.DetailsProduct;
import com.nixagh.classicmodels.dto._statistic.synthetic.details.SyntheticProduct;
import com.nixagh.classicmodels.dto._statistic.synthetic.overview.OverviewTop;
import com.nixagh.classicmodels.dto._statistic.synthetic.overview.OverviewTotal;
import com.nixagh.classicmodels.dto.page.PageResponseInfo;
import com.nixagh.classicmodels.dto.product.ProductAddRequest;
import com.nixagh.classicmodels.dto.product.edit.ProductUpdateRequest;
import com.nixagh.classicmodels.dto.product.manager.search.request.ProductManagerSearchRequest;
import com.nixagh.classicmodels.dto.product.search.ProductSearchRequest;
import com.nixagh.classicmodels.dto.product.search.ProductSearchResponse;
import com.nixagh.classicmodels.dto.product.search.ProductSearchResponseDTO;
import com.nixagh.classicmodels.dto.product.search.QuantityInStock;
import com.nixagh.classicmodels.dto.statistical.request.ProductStatisticDTO;
import com.nixagh.classicmodels.dto.statistical.request.ProductsEachMonthInYear;
import com.nixagh.classicmodels.dto.statistical.request.StatisticDTO;
import com.nixagh.classicmodels.dto.statistical.request.StatisticalRequest;
import com.nixagh.classicmodels.dto.statistical.response.ProductEachMonth;
import com.nixagh.classicmodels.dto.statistical.response.ProductStatisticResponse;
import com.nixagh.classicmodels.dto.statistical.response.Top10ProductResponse;
import com.nixagh.classicmodels.entity.Product;
import com.nixagh.classicmodels.entity.ProductLinee;
import com.nixagh.classicmodels.entity.firebase.NotificationMessage;
import com.nixagh.classicmodels.exception.exceptions.BadRequestException;
import com.nixagh.classicmodels.exception.exceptions.NotFoundEntity;
import com.nixagh.classicmodels.repository.product.ProductNoDSLRepository;
import com.nixagh.classicmodels.repository.product.ProductRepository;
import com.nixagh.classicmodels.service.web_socket_service.IWebSocketService;
import com.nixagh.classicmodels.utils.excel.ExcelUtil;
import com.nixagh.classicmodels.utils.math.RoundUtil;
import com.nixagh.classicmodels.utils.page.PageUtil;
import com.querydsl.core.Tuple;
import jakarta.persistence.EntityManager;
import lombok.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductNoDSLRepository productNoDSLRepository;
    private final EntityManager entityManager;
    private final ProductRepository productRepository;
    private final IWebSocketService webSocketService;

    @Override
    public List<Top10ProductResponse> getTop10Products(Date from, Date to) {
        return productNoDSLRepository.getTop10Products(from, to).stream().map(
                tuple -> Top10ProductResponse.builder()
                        .productCode(tuple.get("productCode", String.class))
                        .productName(tuple.get("productName", String.class))
                        .totalSoldQuantity(tuple.get("totalSoldQuantity", BigDecimal.class).longValue())
                        .totalProfit(tuple.get("totalProfit", Double.class))
                        .build()
        ).toList();
    }

    @Override
    public Map<Integer, ProductsEachMonthInYear> getProductsEachMonthInYear(int year) {
        // tạo map với key là tháng, value là ProductsEachMonthInYear (chứa danh sách sản phẩm và tổng profit của tháng đó)
        Map<Integer, ProductsEachMonthInYear> products = new HashMap<>();
        // thêm 12 tháng vào map với value là ProductsEachMonthInYear rỗng
        for (int i = 1; i <= 12; i++) products.put(i, new ProductsEachMonthInYear());

        // lấy danh sách các tuple (tháng, productCode, productName, totalSoldQuantity, totalProfit)
        List<Tuple> tuples = productRepository.findProductsEachMonthInYear_(year);

        // duyệt danh sách các tuple và thêm vào map
        tuples.forEach(tuple -> {
            Integer month = tuple.get(0, Integer.class);
            ProductsEachMonthInYear list = products.get(month);
            Top10ProductResponse product = Top10ProductResponse.builder()
                    .productCode(tuple.get(1, String.class))
                    .productName(tuple.get(2, String.class))
                    .totalSoldQuantity(tuple.get(3, Long.class))
                    .totalProfit(tuple.get(4, Double.class))
                    .build();
            list.getProducts().add(product);
            products.put(month, list);
        });
        // tính tổng profit của mỗi tháng
        products.forEach((key, value) -> {
            value.setTotalProfit(value.getProducts().stream().mapToDouble(Top10ProductResponse::getTotalProfit).sum());
        });
        // trả về map
        return products;
    }

    @Override
    public StatisticDTO getAllStatistical() {
        // lấy danh sách các tuple (tháng, customerNumber, customerName,  productCode, productName, totalSoldQuantity, totalProfit)
//        List<Tuple> tuples = productRepository.findAllStatistic();
        // duyệt danh sách các tuple và thêm vào StatisticDTO
        return new StatisticDTO();
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductStatisticResponse getProductStatistical(StatisticalRequest statisticalRequest) {
        ProductStatisticResponse productStatisticResponse = new ProductStatisticResponse();

        Date from = statisticalRequest.getFrom();


        Date to = statisticalRequest.getTo();


        if (from == null) {
            from = new Date(0);
        }
        if (to == null) {
            to = new Date(new Date().getTime() + 86400000);
        }

        // increase 1 day to date to include the last day
        to = new Date(to.getTime() + 86400000);

        long pageNumber = statisticalRequest.getPageInfo().getPageNumber();
        long pageSize = statisticalRequest.getPageInfo().getPageSize();

        long offset = pageSize * (pageNumber - 1);

        List<ProductStatisticDTO> products = productNoDSLRepository.getProductStatistical(from, to, offset, pageSize)
                .stream()
                .map(tuple -> ProductStatisticDTO.builder()
                        .productCode(tuple.get("productCode", String.class))
                        .productName(tuple.get("productName", String.class))
                        .totalSoldQuantity(tuple.get("totalSoldQuantity", BigDecimal.class).longValue())
                        .totalAmount(tuple.get("totalAmount", Double.class))
                        .build())
                .toList();
        long totalItems = productNoDSLRepository.countProductStatistical(from, to);

        productStatisticResponse.setProducts(products);
        productStatisticResponse.setPageResponseInfo(PageUtil.getResponse(pageNumber, pageSize, totalItems, (long) products.size()));

        return productStatisticResponse;
    }


    @Override
    public ProductEachMonth getProductEachMonth(int year, int month, long pageNumber, long pageSize) {
        ProductEachMonth productEachMonth = new ProductEachMonth();
        long offset = pageSize * (pageNumber - 1);
        List<ProductStatisticDTO> products = productNoDSLRepository.getProductEachMonth(year, month, offset, pageSize)
                .stream()
                .map(tuple -> ProductStatisticDTO.builder()
                        .productCode(tuple.get("productCode", String.class))
                        .productName(tuple.get("productName", String.class))
                        .totalSoldQuantity(
                                tuple.get("totalSoldQuantity", BigDecimal.class) == null
                                        ? 0
                                        : tuple.get("totalSoldQuantity", BigDecimal.class).longValue())
                        .soldPrice(tuple.get("soldPrice", Double.class))
                        .totalAmount(tuple.get("totalAmount", Double.class))
                        .buyPrice(tuple.get("buyPrice", Double.class))
                        .totalProfit(tuple.get("totalProfit", Double.class))
                        .build())
                .toList();
        PageResponseInfo pageResponseInfo = PageUtil.getResponse(
                pageNumber,
                pageSize,
                (long) productNoDSLRepository.countProductEachMonth(year, month).size(),
                (long) products.size()
        );

        productEachMonth.setProducts(products);
        productEachMonth.setPageResponseInfo(pageResponseInfo);
        return productEachMonth;
    }

    @Override
    public Map<String, String> addProduct(ProductAddRequest product) {
        String productCode = "S" + product.getProductScale() + "_" + product.getProductCode();
        ProductLinee productLine = checkField(
                product.getProductLine(),
                product.getProductScale(),
                product.getProductVendor(),
                null
        );
        Product saveProduct = Product.builder()
                .productCode(productCode)
                .productName(product.getProductName())
                .productLine(productLine)
                .productScale("1:" + product.getProductScale())
                .productVendor(product.getProductVendor())
                .productDescription(product.getProductDescription())
                .quantityInStock(0)
                .buyPrice(product.getBuyPrice())
                .msrp(product.getMsrp())
                .build();

        return new HashMap<>(Map.of("productCode", productRepository.save(saveProduct).getProductCode()));
    }

    @Override
    public ProductSearchResponse filterProducts(ProductSearchRequest request) {
        String productLine = request.getFilter().getProductLine();
        Integer productScale = request.getFilter().getProductScale();
        String productVendor = request.getFilter().getProductVendor();
        QuantityInStock quantityInStock = request.getFilter().getQuantityInStock();
        String search = request.getFilter().getSearch();

        Long offset = request.getPageInfo().getPageSize() * (request.getPageInfo().getPageNumber() - 1);
        Long pageSize = request.getPageInfo().getPageSize();

        List<ProductSearchResponseDTO> filterProducts = productRepository.filterProducts(
                search,
                productLine,
                productScale,
                productVendor,
                quantityInStock,
                offset,
                pageSize);
        Long totalItems = productRepository.countFilterProducts(
                search,
                productLine,
                productScale,
                productVendor,
                quantityInStock);

        PageResponseInfo pageResponseInfo = PageUtil.getResponse(
                request.getPageInfo().getPageNumber(),
                request.getPageInfo().getPageSize(),
                totalItems,
                (long) filterProducts.size()
        );

        ProductSearchResponse productSearchResponse = new ProductSearchResponse();
        productSearchResponse.setProducts(filterProducts);
        productSearchResponse.setPageResponseInfo(pageResponseInfo);

        return productSearchResponse;
    }

    @Override
    public Map<String, String> deleteProduct(String productCode) {
        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new NotFoundEntity("Product code is not existed"));
        productRepository.delete(product);
        return new HashMap<>(Map.of("productCode", productCode));
    }

    @Override
    public ProductSearchResponseDTO getProduct(String productCode) {
        ProductSearchResponseDTO product = productRepository.getProduct(productCode);
        if (product == null) {
            throw new NotFoundEntity("Product code is not existed");
        }
        return product;
    }

    @Override
    public Map<String, String> updateProduct(String productCode, ProductUpdateRequest product) {
        Product productInStore = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new NotFoundEntity("Product code is not existed"));
        ProductLinee productLine = checkField(
                product.getProductLine(),
                product.getProductScale(),
                product.getProductVendor(),
                product.getQuantityInStock()
        );
        productInStore.setProductName(product.getProductName());
        productInStore.setProductLine(productLine);
        productInStore.setProductScale("1:" + product.getProductScale());
        productInStore.setProductVendor(product.getProductVendor());
        productInStore.setProductDescription(product.getProductDescription());
        productInStore.setQuantityInStock(product.getQuantityInStock());
        productInStore.setBuyPrice(product.getBuyPrice());
        productInStore.setMsrp(product.getMsrp());

        webSocketService.sendGlobalNotification(
                NotificationMessage.builder()
                        .title("Product updated")
                        .body("Product " + productCode + " has been updated")
                        .build()
        );
        return new HashMap<>(Map.of("productCode", productRepository.save(productInStore).getProductCode()));
    }

    @Override
    public ProductLinee checkField(String productLine, Integer productScale, String productVendor, Integer quantityInStock) {
        ProductLinee productLinee = entityManager.getReference(ProductLinee.class, productLine);
        if (productLinee == null) {
            throw new BadRequestException("Product line is not existed");
        }
        if (productScale != null) {
            if (productScale < 1 || productScale > 1000) {
                throw new BadRequestException("Product scale must be between 1 and 1000");
            }
        }
        if (productVendor != null) {
            if (productVendor.length() > 50) {
                throw new BadRequestException("Product vendor must be less than 50 characters");
            }
        }
        if (quantityInStock != null) {
            if (quantityInStock < 0) {
                throw new BadRequestException("Quantity in stock must be greater than 0");
            }
        }
        return productLinee;
    }

    @Override
    public ProductSearchResponse managerSearch(ProductManagerSearchRequest request) {
        Long offset = request.getPageInfo().getPageSize() * (request.getPageInfo().getPageNumber() - 1);
        Long pageSize = request.getPageInfo().getPageSize();
        String search = request.getSearch();
        String productLine = request.getProductLine();

        // check product line
        if (productLine != null) {
            ProductLinee productLinee = entityManager.getReference(ProductLinee.class, productLine);
            if (productLinee == null) {
                throw new BadRequestException("Product line is not existed");
            }
        }

        List<ProductSearchResponseDTO> products = productRepository.managerSearch(
                search,
                productLine,
                offset,
                pageSize
        );

        Long totalItems = productRepository.countManagerSearch(search, productLine);

        ProductSearchResponse response = new ProductSearchResponse();
        response.setProducts(products);
        response.setPageResponseInfo(PageUtil.getResponse(
                request.getPageInfo().getPageNumber(),
                request.getPageInfo().getPageSize(),
                totalItems,
                (long) products.size()
        ));
        return response;
    }

    @Override
    public List<ProductController.ProductOutOfStockResponse> getOutOfStockProducts() {
        return productRepository.getOutOfStockProducts();
    }

    @Override
    public Map<String, String> updateQuantityInStock(String productCode, Integer quantityInStock) {
        // quantity in stock must be greater than 0 and greater than 100
        if (quantityInStock == null || quantityInStock < 0 || quantityInStock <= 100)
            throw new BadRequestException("Quantity in stock must be greater than 0 and greater than 100");

        Product product = productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new NotFoundEntity("Product code is not existed"));

        product.setQuantityInStock(quantityInStock);
        productRepository.save(product);
        return new HashMap<>(Map.of("productCode", productCode));
    }

    @Override
    public StatisticalController.ByteArrayInputStreamResponse getExportProduct(int year, int month)
            throws IOException, NoSuchFieldException, IllegalAccessException {

        // get data from database
        List<ProductExportResponse> products = productNoDSLRepository.getExportProduct(year, month)
                .stream()
                .map(product -> new ProductExportResponse(
                        product.get("productCode", String.class),
                        product.get("productName", String.class),
                        product.get("totalSoldQuantity", BigDecimal.class),
                        product.get("soldPrice", Double.class),
                        product.get("totalAmount", Double.class),
                        product.get("buyPrice", Double.class),
                        product.get("totalProfit", Double.class)
                ))
                .toList();

        // check if no product to export
        if (products.isEmpty()) {
            throw new NotFoundEntity("No product to export");
        }

        // set config for excel
        String[] columns = {"Product Code", "Product Name", "Total Sold Quantity", "Sold Price", "Total Amount", "Buy Price", "Total Profit"};
        String excelFile = "src/main/resources/excel/";

        ExcelConfig excelConfig = new ExcelConfig();
        excelConfig.setSheetName("Product");
        excelConfig.setExcelPath(excelFile);
        excelConfig.setFileName("product_" + new SimpleDateFormat("yyyyMMddHHss").format(new Date()) + ".xlsx");
        excelConfig.setHeader(columns);
        excelConfig.setStartRow(1);

        // write excel
        return ExcelUtil.writeExcel(excelConfig, products);
    }

    @Override
    public OverviewTotal getTotalSoldProductAndProfit(java.sql.Date from, java.sql.Date to) {
        Tuple tuple = productRepository.getTotalSoldProductAndProfit(from, to);

        if (tuple == null) return OverviewTotal.builder().build();

        Long totalSoldProduct = tuple.get(0, Long.class);
        Double totalProfit = tuple.get(1, Double.class) == null ? 0.0 : tuple.get(1, Double.class);


        assert totalProfit != null;
        return OverviewTotal.builder()
                .totalSoldProduct(totalSoldProduct)
                .totalMoney(RoundUtil.convert(totalProfit, 2))
                .build();
    }

    @Override
    public OverviewTop.Product getTop1Product(Date from, Date to) {
        Tuple tuple = productRepository.getTop1Product(from, to);
        if (tuple == null)
            return OverviewTop.Product.builder()
                    .productCode("Product code is not existed")
                    .productName("Product name is not existed")
                    .quantity(0L)
                    .build();
        return OverviewTop.Product.builder()
                .productCode(tuple.get(0, String.class) == null ? "Product code is not existed" : tuple.get(0, String.class))
                .productName(tuple.get(1, String.class) == null ? "Product name is not existed" : tuple.get(1, String.class))
                .quantity(tuple.get(2, Long.class) == null ? 0L : tuple.get(2, Long.class))
                .build();
    }

    @Override
    public OverviewTop.ProductLine getTop1ProductLine(Date from, Date to) {
        Tuple tuple = productRepository.getTop1ProductLine(from, to);
        if (tuple == null)
            return OverviewTop.ProductLine.builder()
                    .ProductLineCode("Product line code is not existed")
                    .quantity(0L)
                    .build();
        return OverviewTop.ProductLine.builder()
                .ProductLineCode(tuple.get(0, String.class))
                .quantity(tuple.get(1, Long.class))
                .build();
    }

    @Override
    public Long getTotalProduct(java.sql.Date from, java.sql.Date to) {
        return productRepository.getTotalProduct(from, to);
    }

    @Override
    public List<SyntheticProduct.SyntheticProductLine> getSyntheticProductLine(java.sql.Date from, java.sql.Date to) {
        return productNoDSLRepository.getSyntheticProductLine(from, to)
                .stream()
                .map(SyntheticProduct.SyntheticProductLine::fromTuple)
                .collect(Collectors.toList());
    }

    @Override
    public DetailsOverview getTotalSoldProductAndProfit(java.sql.Date from,
                                                        java.sql.Date to,
                                                        String typeProductLine,
                                                        String search) {
        Tuple tuple2 = productRepository.getTotalSoldProductAndProfit(from, to, typeProductLine, search);
        return DetailsOverview.fromTuple(tuple2);
    }

    @Override
    public List<DetailsProduct> getDetailStatisticDetail(java.sql.Date from,
                                                         java.sql.Date to,
                                                         String typeProductLine,
                                                         String search,
                                                         long offset,
                                                         long pageSize) {
        List<Tuple> result = productRepository.getDetailStatisticDetail(from, to, typeProductLine, search, offset, pageSize);
        return result.stream().map(DetailsProduct::fromTuple).collect(Collectors.toList());
    }

    @Override
    public Long countDetailStatisticDetail(java.sql.Date sqlFrom,
                                           java.sql.Date sqlTo,
                                           String typeProductLine,
                                           String search,
                                           long offset,
                                           long pageSize) {
        return productRepository.countDetailStatisticDetail(sqlFrom, sqlTo, typeProductLine, search);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class ProductExportResponse {
        private String productCode;
        private String productName;
        private BigDecimal totalSoldQuantity;
        private Double soldPrice;
        private Double totalAmount;
        private Double buyPrice;
        private Double totalProfit;
    }

}
