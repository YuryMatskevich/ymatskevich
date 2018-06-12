CREATE TABLE items (
  id_i SERIAL PRIMARY KEY,
  name_i VARCHAR(45) NOT NULL,
  description_i VARCHAR(45) NOT NULL,
  create_i DATE NOT NULL
);

CREATE TABLE commentss (
  id_c SERIAL PRIMARY KEY,
  comment_c TEXT NOT NULL,
  id_i INT NOT NULL,
  CONSTRAINT fk_comments_items FOREIGN KEY(id_i)
    REFERENCES items(id_i)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);