terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
      version = "4.55.0"
    }
  }
}

provider "aws" {
    region = "${var.AWS_REGION}"
    access_key = "AKIA6NP254XRXEZHJB2P"
    secret_key = "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"
}