package com.nixagh.classicmodels.dto;

import lombok.*;
import org.springframework.core.serializer.DefaultSerializer;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractResponse implements Serializable {
    private PageResponseInfo pageResponseInfo;
}
