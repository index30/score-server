# Tasks schema

# --- !Ups

CREATE SEQUENCE log_id_seq;
CREATE TABLE user_info (
    id integer NOT NULL DEFAULT nextval('log_id_seq'),
    name varchar(255),
    mail varchar(255),
    pass varchar(255),
    point integer,
    PRIMARY KEY(id)
	);
	
# --- !Downs
DROP TABLE user_info;
DROP SEQUENCE log_id_seq;






