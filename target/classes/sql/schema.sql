-- Tabla que almacena los roles o autoridades de los usuarios
CREATE TABLE AUTHORITIES
(ID BIGINT NOT NULL, 
AUTHORITY VARCHAR(255) NOT NULL, 
PRIMARY KEY (ID),
CONSTRAINT UK_q0u5f2cdlshec8tlh6818bhbk UNIQUE (AUTHORITY) );

-- Tabla que almacena los usuarios
CREATE TABLE USERS
(ID BIGINT NOT NULL, 
PASSWORD VARCHAR(255) NOT NULL, 
USERNAME VARCHAR(255) NOT NULL, 
ALTER TABLE USERS ALTER COLUMN EMAIL VARCHAR(255) NULL;
ENABLED BOOLEAN NOT NULL, 
PRIMARY KEY (ID),
CONSTRAINT UK_r43af9ap4edm43mmtq01oddj6 UNIQUE (USERNAME) );

-- Tabla que almacena las publicaciones o posts
CREATE TABLE POSTS
(ID BIGINT NOT NULL, 
BODY TEXT NOT NULL,
CREATION_DATE TIMESTAMP NOT NULL, 
TITLE VARCHAR(255) NOT NULL, 
USER_ID BIGINT NOT NULL, 
IMAGE BLOB, 
PRIMARY KEY (ID),
CONSTRAINT FK5lidm6cqbc7u4xhqpxm898qme
FOREIGN KEY (USER_ID)
REFERENCES USERS );

-- Tabla que almacena los comentarios de los usuarios en los posts
CREATE TABLE COMMENTS
(ID BIGINT NOT NULL, 
BODY TEXT NOT NULL, 
CREATION_DATE TIMESTAMP NOT NULL, 
POST_ID BIGINT NOT NULL, 
USER_ID BIGINT NOT NULL, 
PRIMARY KEY (ID),
CONSTRAINT FKh4c7lvsc298whoyd4w9ta25cr
FOREIGN KEY (POST_ID)
REFERENCES POSTS, 
CONSTRAINT FK8omq0tc18jd43bu5tjh6jvraq
FOREIGN KEY (USER_ID)
REFERENCES USERS); 

-- Tabla que almacena la relación entre los usuarios y sus autoridades o roles
CREATE TABLE USERS_AUTHORITIES
(USER_ID BIGINT NOT NULL, 
AUTHORITY_ID BIGINT NOT NULL,
PRIMARY KEY (USER_ID, AUTHORITY_ID),
CONSTRAINT FKdsfxx5g8x8mnxne1fe0yxhjhq
FOREIGN KEY (AUTHORITY_ID)
REFERENCES AUTHORITIES, 
CONSTRAINT FKq3lq694rr66e6kpo2h84ad92q
FOREIGN KEY (USER_ID)
REFERENCES USERS); 
ALTER TABLE users ALTER COLUMN email varchar(255) NULL;


-- Tabla que almacena las solicitudes de amistad
CREATE TABLE FRIEND_REQUESTS
(ID BIGINT NOT NULL, 
SENDER_ID BIGINT NOT NULL, 
RECEIVER_ID BIGINT NOT NULL, 
STATUS VARCHAR(255) NOT NULL, 
CREATION_DATE TIMESTAMP NOT NULL, 
PRIMARY KEY (ID),
CONSTRAINT FK_SENDER
FOREIGN KEY (SENDER_ID)
REFERENCES USERS, 
CONSTRAINT FK_RECEIVER
FOREIGN KEY (RECEIVER_ID)
REFERENCES USERS, 
CONSTRAINT UK_REQUEST UNIQUE (SENDER_ID, RECEIVER_ID) ); 
