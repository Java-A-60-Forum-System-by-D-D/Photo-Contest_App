USE photo_contest;

-- Create or replace the spring_session table
DROP TABLE IF EXISTS spring_session;
CREATE TABLE spring_session (
                                PRIMARY_ID CHAR(36) NOT NULL PRIMARY KEY,
                                SESSION_ID CHAR(36) NOT NULL,
                                CREATION_TIME BIGINT NOT NULL,
                                LAST_ACCESS_TIME BIGINT NOT NULL,
                                MAX_INACTIVE_INTERVAL INT NOT NULL,
                                EXPIRY_TIME BIGINT NOT NULL,
                                PRINCIPAL_NAME VARCHAR(100) NULL,
                                CONSTRAINT SPRING_SESSION_IX1 UNIQUE (SESSION_ID)
) ROW_FORMAT=DYNAMIC;

CREATE INDEX SPRING_SESSION_IX2 ON spring_session (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON spring_session (PRINCIPAL_NAME);

-- Create or replace the spring_session_attributes table
DROP TABLE IF EXISTS spring_session_attributes;
CREATE TABLE spring_session_attributes (
                                           SESSION_PRIMARY_ID CHAR(36) NOT NULL,
                                           ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
                                           ATTRIBUTE_BYTES BLOB NOT NULL,
                                           PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
                                           CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID)
                                               REFERENCES spring_session (PRIMARY_ID) ON DELETE CASCADE
) ROW_FORMAT=DYNAMIC;

CREATE INDEX SPRING_SESSION_ATTRIBUTES_IX1 ON spring_session_attributes (SESSION_PRIMARY_ID);
