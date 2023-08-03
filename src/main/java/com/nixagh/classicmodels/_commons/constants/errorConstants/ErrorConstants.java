package com.nixagh.classicmodels._commons.constants.errorConstants;

public class ErrorConstants {
    public static final BaseError INTERNAL_SERVER_ERROR = new BaseError("500", "Internal Server Error");
    public static final BaseError PRODUCT_NOT_FOUND = new BaseError("404", "Product not found");
    public static final BaseError PRODUCT_ALREADY_EXISTS = new BaseError("400", "Product already exists");
    public static final BaseError PRODUCT_NOT_ENOUGH_QUANTITY = new BaseError("400", "Product not enough quantity");

    public static final BaseError USER_NOT_FOUND = new BaseError("404", "User not found");
    public static final BaseError USER_ALREADY_EXISTS = new BaseError("400", "User already exists");
    public static final BaseError USER_NOT_ENOUGH_BALANCE = new BaseError("400", "User not enough balance");

    public static final BaseError ORDER_NOT_FOUND = new BaseError("404", "Order not found");
    public static final BaseError ORDER_ALREADY_EXISTS = new BaseError("400", "Order already exists");

    public static final BaseError ORDER_ITEM_NOT_FOUND = new BaseError("404", "Order item not found");
    public static final BaseError ORDER_ITEM_ALREADY_EXISTS = new BaseError("400", "Order item already exists");

    public static final BaseError ACCESS_DENIED = new BaseError("403", "Access denied");
    public static final BaseError INVALID_CREDENTIALS = new BaseError("400", "Invalid credentials");
    public static final BaseError INVALID_TOKEN = new BaseError("498", "Invalid token");
    public static final BaseError INVALID_TOKEN_TYPE = new BaseError("400", "Invalid token type");
    public static final BaseError INVALID_TOKEN_FORMAT = new BaseError("400", "Invalid token format");
    public static final BaseError INVALID_TOKEN_LENGTH = new BaseError("400", "Invalid token length");
    public static final BaseError INVALID_TOKEN_EXPIRED = new BaseError("400", "Invalid token expired");
    public static final BaseError INVALID_TOKEN_SIGNATURE = new BaseError("400", "Invalid token signature");
    public static final BaseError INVALID_TOKEN_ALGORITHM = new BaseError("400", "Invalid token algorithm");
    public static final BaseError INVALID_TOKEN_ISSUER = new BaseError("400", "Invalid token issuer");

    public static final BaseError UN_AUTHORIZATION = new BaseError("401", "Un authorization");

    public static final BaseError INVALID_REQUEST_URL = new BaseError("400", "Invalid request url");
    public static final BaseError INVALID_REQUEST_METHOD = new BaseError("400", "Invalid request method");
    public static final BaseError INVALID_REQUEST = new BaseError("400", "Invalid request");
    public static final BaseError INVALID_REQUEST_BODY = new BaseError("400", "Invalid request body");
    public static final BaseError INVALID_REQUEST_PARAMETER = new BaseError("400", "Invalid request parameter");
    public static final BaseError INVALID_REQUEST_QUERY = new BaseError("400", "Invalid request query");
    public static final BaseError INVALID_REQUEST_HEADER = new BaseError("400", "Invalid request header");
    public static final BaseError INVALID_REQUEST_FILE = new BaseError("400", "Invalid request file");
    public static final BaseError INVALID_REQUEST_FILE_SIZE = new BaseError("400", "Invalid request file size");
    public static final BaseError INVALID_REQUEST_FILE_TYPE = new BaseError("400", "Invalid request file type");
    public static final BaseError INVALID_REQUEST_FILE_FORMAT = new BaseError("400", "Invalid request file format");
    public static final BaseError INVALID_REQUEST_FILE_CONTENT = new BaseError("400", "Invalid request file content");
    public static final BaseError INVALID_REQUEST_FILE_CONTENT_TYPE = new BaseError("400", "Invalid request file content type");
    public static final BaseError INVALID_REQUEST_FILE_CONTENT_FORMAT = new BaseError("400", "Invalid request file content format");
    public static final BaseError INVALID_REQUEST_FILE_CONTENT_SIZE = new BaseError("400", "Invalid request file content size");
    public static final BaseError INVALID_REQUEST_FILE_CONTENT_LENGTH = new BaseError("400", "Invalid request file content length");
    public static final BaseError INVALID_REQUEST_FILE_CONTENT_DIMENSION = new BaseError("400", "Invalid request file content dimension");
    public static final BaseError INVALID_REQUEST_FILE_CONTENT_RESOLUTION = new BaseError("400", "Invalid request file content resolution");
    public static final BaseError INVALID_REQUEST_FILE_CONTENT_DURATION = new BaseError("400", "Invalid request file content duration");
    public static final BaseError INVALID_REQUEST_FILE_CONTENT_BITRATE = new BaseError("400", "Invalid request file content bitrate");

}
