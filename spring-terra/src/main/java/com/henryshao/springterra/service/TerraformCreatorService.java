package com.henryshao.springterra.service;

import com.henryshao.springterra.dto.*;
import com.henryshao.springterra.service.helper.RouteTableHelper;
import com.henryshao.springterra.service.helper.SubnetHelper;
import com.henryshao.springterra.utils.FileWriterUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TerraformCreatorService {

    public boolean generateSubnetFile(SubnetDTO[] subnetDTOs) {

        SubnetDTO[][] subnetArrays = SubnetHelper.splitSubnetsByType(subnetDTOs);
        SubnetDTO[] publicSubnets = subnetArrays[0];
        SubnetDTO[] privateSubnets = subnetArrays[1];

        StringBuilder subnetOutput = new StringBuilder();

        int publicSubnetNumber = 1;
        int privateSubnetNumber = 1;

        for (SubnetDTO subnet : publicSubnets) {
            subnetOutput.append(SubnetHelper.generatePublicSubnet(subnet, publicSubnetNumber));
            publicSubnetNumber++;
        }
        for (SubnetDTO subnet : privateSubnets) {
            subnetOutput.append(SubnetHelper.generatePrivateSubnet(subnet, privateSubnetNumber));
            privateSubnetNumber++;
        }

        String subnetFile = subnetOutput.toString();

        log.info("Generating public route table");
        if(generatePublicRouteTable(publicSubnets))
        {
            log.info("Public route generation successful");
        } else {
            log.info("Public route generation unsuccessful");
        }

        String fileName = "subnets.tf";
        log.info(String.format("Writing content to file %s", fileName));
        return FileWriterUtils.writeToFile(fileName, subnetFile);
    }

    private static boolean generatePublicRouteTable(SubnetDTO[] publicSubnets) {
        StringBuilder routeTableBuilder = new StringBuilder();

        routeTableBuilder.append(RouteTableHelper.generateRouteTable());
        routeTableBuilder.append(RouteTableHelper.generateRouteTableAssociation());
        routeTableBuilder.append(RouteTableHelper.generateLocalSubnetID(publicSubnets));

        String routeTableFile = routeTableBuilder.toString();

        String fileName = "route_table.tf";
        log.info(String.format("Writing content to file %s", fileName));
        return FileWriterUtils.writeToFile(fileName, routeTableFile);
    }


    public boolean generateInternetGatewayFile(InternetGatewayDTO internetGatewayDTO) {

        StringBuilder igwBuilder = new StringBuilder();

        igwBuilder.append("# Create an internet gateway\n");
        igwBuilder.append("resource \"aws_internet_gateway\" \"igw\" {\n");
        igwBuilder.append("  vpc_id = aws_vpc.vpc.id\n");
        igwBuilder.append("}\n");

        String igwFile = igwBuilder.toString();

        String fileName = "internet_gateway.tf";
        log.info(String.format("Writing content to file %s", fileName));
        return FileWriterUtils.writeToFile(fileName, igwFile);
    }

    public boolean generateVpcFile(VpcDTO vpcDTO) {

        StringBuilder vpcBuilder = new StringBuilder();

        vpcBuilder.append("# Create a VPC\n");
        vpcBuilder.append("resource \"aws_vpc\" \"vpc\" {\n");
        vpcBuilder.append(String.format("  cidr_block = \"%s\"\n", vpcDTO.getCidrBlock()));
        vpcBuilder.append("}\n");

        String vpcFile = vpcBuilder.toString();

        String fileName = "vpc.tf";
        log.info(String.format("Writing content to file %s", fileName));
        return FileWriterUtils.writeToFile(fileName, vpcFile);
    }


    public boolean generateProviderFile() {

        StringBuilder providerBuilder = new StringBuilder();

        providerBuilder.append(String.format("provider \"aws\" {\n"));
        providerBuilder.append(String.format("  region     = %s\n", "var.region"));
        providerBuilder.append(String.format("  access_key = %s\n", "var.access_key"));
        providerBuilder.append(String.format("  secret_key = %s\n", "var.secret_key"));
        providerBuilder.append(String.format("}\n"));

        String providerFile = providerBuilder.toString();

        String fileName = "provider.tf";
        log.info(String.format("Writing content to file %s", fileName));
        return FileWriterUtils.writeToFile(fileName, providerFile);
    }

    public boolean generateVariablesFile(VariablesDTO variablesDTO) {

        StringBuilder variablesBuilder = new StringBuilder();

        variablesBuilder.append(String.format("variable \"region\" {\n"));
        variablesBuilder.append(String.format("  default = \"%s\"\n", variablesDTO.getRegion()));
        variablesBuilder.append(String.format("}\n"));

        String variablesFile = variablesBuilder.toString();

        String fileName = "variables.tf";
        log.info(String.format("Writing content to file %s", fileName));
        return FileWriterUtils.writeToFile(fileName, variablesFile);
    }
}
