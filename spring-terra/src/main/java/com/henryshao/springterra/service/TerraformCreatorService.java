package com.henryshao.springterra.service;

import com.henryshao.springterra.dto.*;
import com.henryshao.springterra.service.helper.SubnetHelper;
import com.henryshao.springterra.utils.FileWriterUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

        generatePublicRouteTable(publicSubnets);

        return FileWriterUtils.writeToFile("subnets.tf", subnetFile);
    }

    public static String generatePublicRouteTable(SubnetDTO[] subnetDTOs) {
        StringBuilder routeTableBuilder = new StringBuilder();



        // Route table resource block
        routeTableBuilder.append("resource \"aws_route_table\" \"public_route_table\" {\n");
        routeTableBuilder.append(String.format("  vpc_id = aws_vpc.vpc.id\n"));
        routeTableBuilder.append("  route {\n");
        routeTableBuilder.append("    cidr_block = \"0.0.0.0/0\"\n");
        routeTableBuilder.append(String.format("    gateway_id = aws_internet_gateway.igw.id\n"));
        routeTableBuilder.append("  }\n");
        routeTableBuilder.append(String.format("  tags = {\n    Name = \"%s\"\n  }\n}\n", "test-public-route-table"));

        // Route table association resource block
        routeTableBuilder.append("resource \"aws_route_table_association\" \"public_subnet_association\" {\n");
        routeTableBuilder.append("  for_each = toset(var.public_subnet_ids)\n");
        routeTableBuilder.append("  subnet_id = each.value\n");
        routeTableBuilder.append("  route_table_id = aws_route_table.public_route_table.id\n");
        routeTableBuilder.append("}\n");

        return routeTableBuilder.toString();
    }


    public boolean generateInternetGatewayFile(InternetGatewayDTO internetGatewayDTO) {

        StringBuilder igwBuilder = new StringBuilder();

        igwBuilder.append("# Create an internet gateway\n");
        igwBuilder.append("resource \"aws_internet_gateway\" \"igw\" {\n");
        igwBuilder.append("  vpc_id = aws_vpc.vpc.id\n");
        igwBuilder.append("}\n");

        String igwFile = igwBuilder.toString();

        return FileWriterUtils.writeToFile("internet_gateway.tf", igwFile);
    }

    public boolean generateVpcFile(VpcDTO vpcDTO) {

        StringBuilder vpcBuilder = new StringBuilder();

        vpcBuilder.append("# Create a VPC\n");
        vpcBuilder.append("resource \"aws_vpc\" \"vpc\" {\n");
        vpcBuilder.append(String.format("  cidr_block = \"%s\"\n", vpcDTO.getCidrBlock()));
        vpcBuilder.append("}\n");

        String vpcFile = vpcBuilder.toString();

        return FileWriterUtils.writeToFile("vpc.tf", vpcFile);
    }


    public boolean generateProviderFile() {

        StringBuilder providerBuilder = new StringBuilder();

        providerBuilder.append(String.format("provider \"aws\" {\n"));
        providerBuilder.append(String.format("  region     = %s\n", "var.region"));
        providerBuilder.append(String.format("  access_key = %s\n", "var.access_key"));
        providerBuilder.append(String.format("  secret_key = %s\n", "var.secret_key"));
        providerBuilder.append(String.format("}\n"));

        String providerFile = providerBuilder.toString();

        return FileWriterUtils.writeToFile("provider.tf", providerFile);
    }

    public boolean generateVariablesFile(VariablesDTO variablesDTO) {

        StringBuilder variablesBuilder = new StringBuilder();

        variablesBuilder.append(String.format("variable \"region\" {\n"));
        variablesBuilder.append(String.format("  default = \"%s\"\n", variablesDTO.getRegion()));
        variablesBuilder.append(String.format("}\n"));
        variablesBuilder.append(String.format("\n"));
        variablesBuilder.append(String.format("variable \"access_key\" {\n"));
        variablesBuilder.append(String.format("  default = \"%s\"\n", variablesDTO.getAccessKey()));
        variablesBuilder.append(String.format("}\n"));
        variablesBuilder.append(String.format("\n"));
        variablesBuilder.append(String.format("variable \"secret_key\" {\n"));
        variablesBuilder.append(String.format("  default = \"%s\"\n", variablesDTO.getSecretKey()));
        variablesBuilder.append(String.format("}\n"));

        String variablesFile = variablesBuilder.toString();

        return FileWriterUtils.writeToFile("variables.tf", variablesFile);
    }
}
