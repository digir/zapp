CREATE TABLE users (
  username VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE var_types (
  id INT NOT NULL GENERATED ALWAYS AS IDENTITY,  -- or AUTO_INCREMENT in MySQL
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (name)
);

CREATE TABLE scaffs (
  id VARCHAR(32) NOT NULL,
  parent_id VARCHAR(32) NOT NULL,
  name VARCHAR(255) NOT NULL,
  descr VARCHAR(255) NOT NULL,
  author VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (name),
  FOREIGN KEY (parent_id) REFERENCES scaffs(id),
  FOREIGN KEY (author) REFERENCES users(username)
);

CREATE TABLE substitution (
  scaff_id VARCHAR(32) NOT NULL,
  variable VARCHAR(255) NOT NULL,
  value TEXT NOT NULL,
  PRIMARY KEY (scaff_id, variable),
  FOREIGN KEY (scaff_id) REFERENCES scaffs(id)
);

CREATE TABLE insertion (
  id VARCHAR(32) NOT NULL,
  scaff_id VARCHAR(32) NOT NULL,
  filepath VARCHAR(255) NOT NULL,
  value TEXT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (scaff_id) REFERENCES scaffs(id)
);

CREATE TABLE vars (
  scaff_id VARCHAR(32) NOT NULL,
  "name" VARCHAR(255) NOT NULL,
  "type" INT NOT NULL,
  "default" VARCHAR(255) NOT NULL,
  descr VARCHAR(255) NOT NULL,
  PRIMARY KEY (scaff_id, name),
  FOREIGN KEY (scaff_id) REFERENCES scaffs(id),
  FOREIGN KEY (type) REFERENCES var_types(id)
);

CREATE TABLE tags (
  id INT NOT NULL,
  label VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (label)
);

CREATE TABLE scaff_tag (
  scaff_id VARCHAR(32) NOT NULL,
  tag_id INT NOT NULL,
  PRIMARY KEY (scaff_id, tag_id),
  FOREIGN KEY (scaff_id) REFERENCES scaffs(id),
  FOREIGN KEY (tag_id) REFERENCES tags(id)
);