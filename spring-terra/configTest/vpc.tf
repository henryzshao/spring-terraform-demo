#create vpc
resource "aws_vpc" "prod-vpc" {
    cidr_block = "10.0.0.0/16" #use 10.0.X.X addresses
    enable_dns_support = "true" #gives you an internal domain name
    enable_dns_hostnames = "true" #gives you an internal host name
    instance_tenancy = "default"    
    
    tags = {
        Name = "prod-vpc"
    }
}

#public subnet
resource "aws_subnet" "prod-subnet-public-1" {
    vpc_id = "${aws_vpc.prod-vpc.id}"
    cidr_block = "10.0.1.0/24"
    map_public_ip_on_launch = "true" //it makes this a public subnet
    availability_zone = "us-east-1a"

    tags = {
        Name = "prod-subnet-public-1"
    }
}