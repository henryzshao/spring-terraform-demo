package com.henryshao.springterra.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderDTO {
    private String region;
    private String accessKey;
    private String secretKey;
}