package com.nixagh.classicmodels.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseAbstractResponse<T> {
    private boolean success;
    private ErrorResponse error;
    private T data;

    private BaseAbstractResponse(Builder<T> builder) {
        this.success = builder.success;
        this.error = builder.error;
        this.data = builder.data;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }
    @ToString
    @Getter
    @Setter
    public static class Builder<T> {
        private boolean success = true;
        private ErrorResponse error = new ErrorResponse();
        private T data;

        public Builder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder<T> error(ErrorResponse error) {
            this.error = error;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public BaseAbstractResponse<T> build() {
            if (this.success) this.error = null;
            else this.data = null;
            return new BaseAbstractResponse<>(this);
        }
    }
}
