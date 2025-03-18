resource "random_password" "password" {
  length = 16
  special = true
  override_special = "_%@"
}

resource "aws_secretsmanager_secret" "hitman-secret" {
  name = "zapp-scaffold-rds-db-secrets"
}

resource "aws_secretsmanager_secret_version" "s-version" {
  secret_id = aws_secretsmanager_secret.hitman-secret.id

  secret_string = <<EOF
    {
      "username": "myuser",
      "password": "${random_password.password.result}"
    }
  EOF

}