resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }
  tags = {
    Name = "test-public-route-table"
  }
}

resource "aws_route_table_association" "public_subnet_association" {
  for_each = local.public_subnet_ids
  
  subnet_id = each.value
  route_table_id = aws_route_table.public_route_table.id
}

locals {
  public_subnet_ids = {
    public_subnet_1 = aws_subnet.public_subnet_1.id
    public_subnet_2 = aws_subnet.public_subnet_2.id
  }
}