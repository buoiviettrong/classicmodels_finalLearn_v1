package com.nixagh.classicmodels.dto.orders;

import com.nixagh.classicmodels.dto.AbstractRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.core.serializer.DefaultSerializer;

import java.io.Serializable;

@Getter
@Setter
public class OrderFilterRequest extends AbstractRequest {
    private OrderFilter orderFilter = null;

    @Override
    public String toString() {
        return """
                OrderFilterRequest{
                    orderFilter=%s,
                    pageInfo=%s
                }
                """.formatted(orderFilter, getPageInfo());
    }
}
