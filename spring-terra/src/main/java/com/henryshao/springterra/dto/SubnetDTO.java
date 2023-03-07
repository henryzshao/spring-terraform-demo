package com.henryshao.springterra.dto;

import lombok.Data;

@Data
public class SubnetDTO {
    private String name;
    private String cidrBlock;
    private String availabilityZone;
    private boolean isPublic;
    private String resourceId = "";

    public SubnetDTO(String name, String cidrBlock, String availabilityZone, boolean isPublic) {
        this.name = name;
        this.cidrBlock = cidrBlock;
        this.availabilityZone = availabilityZone;
        this.isPublic = isPublic;
    }
}
