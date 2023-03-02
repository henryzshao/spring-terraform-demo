package com.henryshao.springterra.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubnetDTO {
    private String name;
    private String public1_cidrBlock;
    private String private1_cidrBlock;
    private String availabilityZone;
}
