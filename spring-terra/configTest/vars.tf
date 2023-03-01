variable "AWS_REGION" {    
    default = "us-east-1"
}

#amazon machine image for ec2
variable "AMI" {
    type = map(string)
    
    default = {
        us-east-1 = "ami-0ae87df03b4940655"
    }
}

variable "PUBLIC_KEY_PATH" {
    default = "virginia-region-key-pair.pub"
}

variable "PRIVATE_KEY_PATH" {
    default = "virginia-region-key-pair"
}

variable "EC2_USER" {
    default = "ubuntu"
}