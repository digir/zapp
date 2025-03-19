resource "random_password" "password" {
  length = 16
  special = true
  override_special = "!#$&*()-=+[]{}<>:?"  # all tf special characters without '/', '@', '"', ' ' (rds requirement)
}

resource "aws_secretsmanager_secret" "zapp-secret" {
  name = "zapp-scaffold-rds-db-secret"
  force_overwrite_replica_secret = true

}

resource "aws_secretsmanager_secret_version" "s-version" {
  secret_id = aws_secretsmanager_secret.zapp-secret.id
  secret_string = <<EOF
    {
      "username": "myuser",
      "password": "${random_password.password.result}"
    }
  EOF

}