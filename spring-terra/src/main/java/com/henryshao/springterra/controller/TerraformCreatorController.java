package com.henryshao.springterra.controller;

import com.henryshao.springterra.dto.*;
import com.henryshao.springterra.service.TerraformCreatorService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TerraformCreatorController {
    private TerraformCreatorService creatorService;

    public TerraformCreatorController(TerraformCreatorService creatorService) {
        this.creatorService = creatorService;
    }

    public void handleSubnetRequest(SubnetDTO subnetDTO) {
        creatorService.generateSubnetFile(subnetDTO);
    }

    public void handleInternetGatewayRequest(InternetGatewayDTO internetGatewayDTO) {
        creatorService.generateInternetGatewayFile(internetGatewayDTO);
    }

    public void handleVpcRequest(VpcDTO vpcDTO) {
        creatorService.generateVpcFile(vpcDTO);
    }

    public void handleProviderRequest(ProviderDTO providerDTO) {
        creatorService.generateProviderFile(providerDTO);
    }

    public void handleVariablesRequest(VariablesDTO variablesDTO) {
        creatorService.generateVariablesFile(variablesDTO);
    }
}
