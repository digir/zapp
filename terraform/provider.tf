terraform {

  backend "s3" {
    bucket = "zapp-scaffold-tf-state"
    dynamodb_table = "zapp-scaffold-state-lock-table"
    encrypt = true
    region = "af-south-1"
    key = "zapp/main.tfstate"
  }

  required_providers {
    aws = {
      source = "hashicorp/aws"
      version = "5.86.1"
    }

  }
}

provider "aws" {
  region = "af-south-1"
}