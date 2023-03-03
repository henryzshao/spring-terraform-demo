package com.henryshao.springterra.controller;

import com.henryshao.springterra.dto.*;
import com.henryshao.springterra.service.TerraformCreatorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TerraformCreatorController {

    private TerraformCreatorService creatorService;

    public TerraformCreatorController(TerraformCreatorService creatorService) {
        this.creatorService = creatorService;
    }

    @PostMapping("terraform/attribute/subnet")
    public void handleSubnetRequest(@RequestBody SubnetDTO subnetDTO) {

        System.out.println("cidrBlock: " + subnetDTO.getCidrBlock());
        System.out.println("availabilityZone: " + subnetDTO.getAvailabilityZone());
        System.out.println("name: " + subnetDTO.getName());


        //creatorService.generateSubnetFile(subnetDTO);
    }

    @PostMapping("terraform/attribute/igw")
    public void handleInternetGatewayRequest(@RequestBody InternetGatewayDTO internetGatewayDTO) {
        creatorService.generateInternetGatewayFile(internetGatewayDTO);
    }

    @PostMapping("terraform/attribute/vpc")
    public void handleVpcRequest(@RequestBody VpcDTO vpcDTO) {
        creatorService.generateVpcFile(vpcDTO);
    }

    @PostMapping("terraform/attribute/provider")
    public void handleProviderRequest() {
        creatorService.generateProviderFile();
    }

    @PostMapping("terraform/attribute/variables")
    public void handleVariablesRequest(@RequestBody VariablesDTO variablesDTO) {
        creatorService.generateVariablesFile(variablesDTO);
    }
}
