package com.henryshao.springterra.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.henryshao.springterra.dto.*;
import com.henryshao.springterra.service.TerraformCreatorService;
import com.henryshao.springterra.utils.JsonTester;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@RestController
public class TerraformCreatorController {

    private TerraformCreatorService creatorService;

    public TerraformCreatorController(TerraformCreatorService creatorService) {
        this.creatorService = creatorService;
    }

    @PostMapping("terraform/attribute/subnet")
    public void handleSubnetRequest(@RequestBody SubnetDTO[] subnetDTOs) throws JsonProcessingException {

        log.info("Generating subnet and route tables configuration files");
        if(creatorService.generateSubnetFile(subnetDTOs))
        {
            log.info("Subnet generation successful");
        } else {
            log.info("Subnet generation successful");
        }
    }

    @PostMapping("terraform/attribute/igw")
    public void handleInternetGatewayRequest(@RequestBody InternetGatewayDTO internetGatewayDTO) {

        log.info("Generating internet gateway configuration files");
        if(creatorService.generateInternetGatewayFile(internetGatewayDTO))
        {
            log.info("Igw file generation successful");
        } else {
            log.info("Igw file generation successful");
        }
    }

    @PostMapping("terraform/attribute/vpc")
    public void handleVpcRequest(@RequestBody VpcDTO vpcDTO) {

        log.info("Generating vpc configuration files");
        if(creatorService.generateVpcFile(vpcDTO))
        {
            log.info("Vpc file generation successful");
        } else {
            log.info("Vpc file generation successful");
        }
    }

    @PostMapping("terraform/attribute/provider")
    public void handleProviderRequest() {
        creatorService.generateProviderFile();

        log.info("Generating provider configuration files");
        if(creatorService.generateProviderFile())
        {
            log.info("Providers file generation successful");
        } else {
            log.info("Providers file generation successful");
        }
    }

    @PostMapping("terraform/attribute/variables")
    public void handleVariablesRequest(@RequestBody VariablesDTO variablesDTO) {

        log.info("Generating variables files");
        if(creatorService.generateVariablesFile(variablesDTO))
        {
            log.info("Variables file generation successful");
        } else {
            log.info("Variables file generation successful");
        }
    }
}
