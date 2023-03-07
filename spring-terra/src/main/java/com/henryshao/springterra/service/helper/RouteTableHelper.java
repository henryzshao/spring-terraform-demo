package com.henryshao.springterra.service.helper;

import com.henryshao.springterra.dto.SubnetDTO;

public class RouteTableHelper {

    public static String generateRouteTable() {
        StringBuilder routeTable = new StringBuilder();

        routeTable.append("resource \"aws_route_table\" \"public_route_table\" {\n");
        routeTable.append("  vpc_id = aws_vpc.vpc.id\n");
        routeTable.append("  route {\n");
        routeTable.append("    cidr_block = \"0.0.0.0/0\"\n");
        routeTable.append("    gateway_id = aws_internet_gateway.igw.id\n");
        routeTable.append("  }\n");
        routeTable.append("  tags = {\n");
        routeTable.append("    Name = \"test-public-route-table\"\n");
        routeTable.append("  }\n");
        routeTable.append("}\n");

        return routeTable.toString();
    }

    public static String generateRouteTableAssociation() {
        StringBuilder association = new StringBuilder();

        association.append("resource \"aws_route_table_association\" \"public_subnet_association\" {\n");
        association.append("  for_each = local.public_subnet_ids\n");
        association.append("\n");
        association.append("  subnet_id = each.value\n");
        association.append("  route_table_id = aws_route_table.public_route_table.id\n");
        association.append("}\n");

        return association.toString();
    }

    public static String generateLocalSubnetID(SubnetDTO[] publicSubnets){
        StringBuilder subnetVariable = new StringBuilder();

        subnetVariable.append("locals {\n");
        subnetVariable.append("  public_subnet_ids = {\n");
        for(SubnetDTO subnet : publicSubnets) {
            subnetVariable.append(String.format("    %s = aws_subnet.%s.id\n", subnet.getResourceId(), subnet.getResourceId()));
        }
        subnetVariable.append("  }\n");
        subnetVariable.append("}\n");

        return subnetVariable.toString();
    }
}
