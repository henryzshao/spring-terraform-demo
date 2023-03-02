package com.henryshao.springterra.service;

import com.henryshao.springterra.dto.*;
import com.henryshao.springterra.utils.FileWriterUtils;
import org.springframework.stereotype.Service;

@Service
public class TerraformCreatorService {

    public boolean generateSubnetFile(SubnetDTO subnetDTO) {
        String[] subnetFile = new String[4];

        // Public subnet
        StringBuilder publicSubnetBuilder = new StringBuilder();
        publicSubnetBuilder.append("resource \"aws_subnet\" \"public_subnet\" {\n");
        publicSubnetBuilder.append(String.format("  vpc_id = aws_vpc.vpc.id\n"));
        publicSubnetBuilder.append(String.format("  cidr_block = \"%s\"\n", subnetDTO.getPrivate1_cidrBlock()));
        publicSubnetBuilder.append(String.format("  availability_zone = \"%s\"\n", subnetDTO.getAvailabilityZone()));
        publicSubnetBuilder.append(String.format("  tags = {\n    Name = \"%s\"\n  }\n}\n", subnetDTO.getName()));
        subnetFile[0] = publicSubnetBuilder.toString();

        // Private subnet
        StringBuilder privateSubnetBuilder = new StringBuilder();
        privateSubnetBuilder.append("resource \"aws_subnet\" \"private_subnet\" {\n");
        privateSubnetBuilder.append(String.format("  vpc_id = aws_vpc.vpc.id\n"));
        privateSubnetBuilder.append(String.format("  cidr_block = \"%s\"\n", subnetDTO.getPublic1_cidrBlock()));
        privateSubnetBuilder.append(String.format("  availability_zone = \"%s\"\n", subnetDTO.getAvailabilityZone()));
        privateSubnetBuilder.append(String.format("  tags = {\n    Name = \"%s\"\n  }\n}\n", subnetDTO.getName()));
        subnetFile[1] = privateSubnetBuilder.toString();

        // Public route table
        StringBuilder publicRouteTableBuilder = new StringBuilder();
        publicRouteTableBuilder.append("resource \"aws_route_table\" \"public_route_table\" {\n");
        publicRouteTableBuilder.append(String.format("  vpc_id = aws_vpc.vpc.id\n"));
        publicRouteTableBuilder.append("  route {\n");
        publicRouteTableBuilder.append(String.format("    cidr_block = \"0.0.0.0/0\"\n"));
        publicRouteTableBuilder.append(String.format("    gateway_id = aws_internet_gateway.igw.id\n  }\n"));
        publicRouteTableBuilder.append(String.format("  tags = {\n    Name = \"%s\"\n  }\n}\n", subnetDTO.getName()));
        subnetFile[2] = publicRouteTableBuilder.toString();

        // Public subnet association
        StringBuilder publicSubnetAssocBuilder = new StringBuilder();
        publicSubnetAssocBuilder.append("resource \"aws_route_table_association\" \"public_subnet_association\" {\n");
        publicSubnetAssocBuilder.append(String.format("  subnet_id = aws_subnet.public_subnet.id\n"));
        publicSubnetAssocBuilder.append(String.format("  route_table_id = aws_route_table.public_route_table.id\n}\n"));
        subnetFile[3] = publicSubnetAssocBuilder.toString();

        return FileWriterUtils.writeToFile("subnets.tf", subnetFile);
    }

    public boolean generateInternetGatewayFile(InternetGatewayDTO internetGatewayDTO) {
        String[] igwFile = new String[1];

        StringBuilder igwBuilder = new StringBuilder();
        igwBuilder.append("# Create an internet gateway\n");
        igwBuilder.append("resource \"aws_internet_gateway\" \"igw\" {\n");
        igwBuilder.append("  vpc_id = aws_vpc.vpc.id\n");
        igwBuilder.append("}\n");
        igwFile[0] = igwBuilder.toString();

        return FileWriterUtils.writeToFile("internet_gateway.tf", igwFile);
    }

    public boolean generateVpcFile(VpcDTO vpcDTO) {

        StringBuilder vpcBuilder = new StringBuilder();
        vpcBuilder.append("# Create a VPC\n");
        vpcBuilder.append("resource \"aws_vpc\" \"vpc\" {\n");
        vpcBuilder.append(String.format("  cidr_block = \"%s\"\n", vpcDTO.getCidrBlock()));
        vpcBuilder.append("}\n");

        String[] vpcFile = {vpcBuilder.toString()};

        return FileWriterUtils.writeToFile("vpc.tf", vpcFile);
    }


    public boolean generateProviderFile() {
        String[] providerFile = new String[1];

        StringBuilder providerBuilder = new StringBuilder();
        providerBuilder.append(String.format("provider \"aws\" {\n"));
        providerBuilder.append(String.format("  region     = %s\n", "var.region"));
        providerBuilder.append(String.format("  access_key = %s\n", "var.access_key"));
        providerBuilder.append(String.format("  secret_key = %s\n", "var.secret_key"));
        providerBuilder.append(String.format("}\n"));

        providerFile[0] = providerBuilder.toString();

        return FileWriterUtils.writeToFile("provider.tf", providerFile);
    }

    public boolean generateVariablesFile(VariablesDTO variablesDTO) {
        String[] variablesFile = new String[1];

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

        variablesFile[0] = variablesBuilder.toString();

        return FileWriterUtils.writeToFile("variables.tf", variablesFile);
    }
}
