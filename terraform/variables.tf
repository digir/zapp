variable "region" {
  type = string
  default = "af-south-1"
}

variable "database_name" {
  type = string
  default = "zappdb"
}

variable "instance_name" {
  type = string
  default = "zappDbInstance"
}

variable "ecr_repo_name" {
  type = string
  default = "zapp"
}

variable "ecs_cluster_name" {
  type = string
  default = "zapp_ecs_cluster"
}