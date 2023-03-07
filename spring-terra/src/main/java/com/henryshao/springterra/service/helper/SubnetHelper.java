package com.henryshao.springterra.service.helper;

import com.henryshao.springterra.dto.SubnetDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubnetHelper {

    public static String generatePublicSubnet(SubnetDTO subnetDTO, int subnetNumber) {
        StringBuilder subnetBuilder = new StringBuilder();

        subnetBuilder.append("resource \"aws_subnet\" \"public_subnet_" + subnetNumber + "\" {\n");
        subnetBuilder.append(String.format("  vpc_id = aws_vpc.vpc.id\n"));
        subnetBuilder.append(String.format("  cidr_block = \"%s\"\n", subnetDTO.getCidrBlock()));
        subnetBuilder.append(String.format("  availability_zone = \"%s\"\n", subnetDTO.getAvailabilityZone()));
        subnetBuilder.append(String.format("  tags = {\n    Name = \"%s\"\n  }\n}\n", subnetDTO.getName()));

        subnetDTO.setResourceId(String.format("public_subnet_%d", subnetNumber));
        return subnetBuilder.toString();
    }

    public static String generatePrivateSubnet(SubnetDTO subnetDTO, int subnetNumber) {
        StringBuilder subnetBuilder = new StringBuilder();

        subnetBuilder.append("resource \"aws_subnet\" \"private_subnet_" + subnetNumber + "\" {\n");
        subnetBuilder.append(String.format("  vpc_id = aws_vpc.vpc.id\n"));
        subnetBuilder.append(String.format("  cidr_block = \"%s\"\n", subnetDTO.getCidrBlock()));
        subnetBuilder.append(String.format("  availability_zone = \"%s\"\n", subnetDTO.getAvailabilityZone()));
        subnetBuilder.append(String.format("  tags = {\n    Name = \"%s\"\n  }\n}\n", subnetDTO.getName()));

        subnetDTO.setResourceId(String.format("private_subnet_%d", subnetNumber));
        return subnetBuilder.toString();
    }

    public static SubnetDTO[][] splitSubnetsByType(SubnetDTO[] subnetDTOs) {
        SubnetDTO[] publicSubnets = Arrays.stream(subnetDTOs)
                .filter(subnetDTO -> subnetDTO.isPublic())
                .toArray(SubnetDTO[]::new);

        SubnetDTO[] privateSubnets = Arrays.stream(subnetDTOs)
                .filter(subnetDTO -> !subnetDTO.isPublic())
                .toArray(SubnetDTO[]::new);

        return new SubnetDTO[][] { publicSubnets, privateSubnets };
    }

//    public static String generatePublicRouteTableModule(SubnetDTO[] subnetDTOs) {
//        StringBuilder publicRouteTableBuilder = new StringBuilder();
//        publicRouteTableBuilder.append("module \"public_route_table\" {\n");
//        publicRouteTableBuilder.append("  source = \"./route_tables\"\n");
//        publicRouteTableBuilder.append("  public_subnet_ids = [\n");
//
//        for (SubnetDTO subnetDTO : subnetDTOs) {
//            if (subnetDTO.isPublic()) {
//                publicRouteTableBuilder.append(String.format("    %s,\n", subnetDTO.getResourceId()));
//            }
//        }
//
//        publicRouteTableBuilder.append("  ]\n");
//        publicRouteTableBuilder.append("}\n");
//        return publicRouteTableBuilder.toString();
//    }
}
