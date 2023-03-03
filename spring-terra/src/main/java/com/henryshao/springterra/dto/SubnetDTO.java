package com.henryshao.springterra.dto;

import lombok.Data;

@Data
public class SubnetDTO {
    private String name;
    private String cidrBlock;
    private String availabilityZone;
    private boolean isPublic;
    private String resourceId = "";
}
