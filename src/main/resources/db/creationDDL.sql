DROP SCHEMA IF EXISTS photo_contest;
CREATE SCHEMA photo_contest;

-- Drop the existing table if it exists, then create the new table

DROP TABLE IF EXISTS photo_contest.auth_users;
CREATE TABLE photo_contest.auth_users (
                                          total_score INT NULL,
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          dtype VARCHAR(31) NOT NULL,
                                          email VARCHAR(255) NULL,
                                          first_name VARCHAR(255),
                                          last_name VARCHAR(255),
                                          password VARCHAR(255) NULL,
                                          username VARCHAR(255) NULL,
                                          role ENUM('DICTATOR', 'ENTHUSIAST', 'JUNKIE', 'MASTER', 'ORGANIZER') NULL,
                                          CONSTRAINT UK6jqfsuvys3lan090p4mk16a5t UNIQUE (email),
                                          CONSTRAINT UKf9wqm8ya8k2x456jqotu3ihla UNIQUE (username)
);

DROP TABLE IF EXISTS photo_contest.categories;
CREATE TABLE photo_contest.categories (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          description TEXT NULL,
                                          name VARCHAR(255) NOT NULL,
                                          CONSTRAINT UKt8o6pivur7nn124jehx7cygw5 UNIQUE (name)
);

DROP TABLE IF EXISTS photo_contest.contests;
CREATE TABLE photo_contest.contests (
                                        category_id BIGINT NULL,
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        organizer_id BIGINT NULL,
                                        start_date DATETIME(6) NOT NULL,
                                        start_phase_1 DATETIME(6) NULL,
                                        start_phase_2 DATETIME(6) NULL,
                                        start_phase_3 DATETIME(6) NULL,
                                        updated_date DATETIME(6) NULL,
                                        description TEXT NULL,
                                        title VARCHAR(255) NOT NULL,
                                        phase ENUM('FINISHED', 'NOT_STARTED', 'PHASE_1', 'PHASE_2') NULL,
                                        type ENUM('INVITATIONAL', 'OPEN') NULL,
                                        CONSTRAINT UKg1nm0irnykh69s8m1fdu9fg64 UNIQUE (title),
                                        CONSTRAINT FK5o31ca9p7wy8008eo1lfmpl26 FOREIGN KEY (organizer_id) REFERENCES photo_contest.auth_users (id),
                                        CONSTRAINT FKt6oti53hlo70sv1fm1lchrnjf FOREIGN KEY (category_id) REFERENCES photo_contest.categories (id)
);

DROP TABLE IF EXISTS photo_contest.contest_participants;
CREATE TABLE photo_contest.contest_participants (
                                                    contest_id BIGINT NOT NULL,
                                                    user_id BIGINT NOT NULL,
                                                    CONSTRAINT FKfa1r516lnwwiwkuu2ltijmwo1 FOREIGN KEY (contest_id) REFERENCES photo_contest.contests (id),
                                                    CONSTRAINT FKt99f17rwgyommp1ab3c13nt33 FOREIGN KEY (user_id) REFERENCES photo_contest.auth_users (id)
);

DROP TABLE IF EXISTS photo_contest.jury_contests;
CREATE TABLE photo_contest.jury_contests (
                                             contest_id BIGINT NOT NULL,
                                             jury_id BIGINT NOT NULL,
                                             CONSTRAINT FKgykmp2e7v509xrk3u71wdm6pt FOREIGN KEY (jury_id) REFERENCES photo_contest.auth_users (id),
                                             CONSTRAINT FKt3220liusm5gmcn9979lasppx FOREIGN KEY (contest_id) REFERENCES photo_contest.contests (id)
);

DROP TABLE IF EXISTS photo_contest.notifications;
CREATE TABLE photo_contest.notifications (
                                             is_read BIT NULL,
                                             type TINYINT NULL,
                                             created_at DATETIME(6) NULL,
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             user_id BIGINT NULL,
                                             message VARCHAR(255) NULL,
                                             CONSTRAINT FKpix5d0xj2p2skfpg49ivnfk6y FOREIGN KEY (user_id) REFERENCES photo_contest.auth_users (id)
);

DROP TABLE IF EXISTS photo_contest.photo_reviews;
CREATE TABLE photo_contest.photo_reviews (
                                             category_mismatch TINYINT(1) DEFAULT 0 NOT NULL,
                                             is_reviewed TINYINT(1) DEFAULT 0 NOT NULL,
                                             score INT NOT NULL,
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             jury_id BIGINT NULL,
                                             comment TEXT NULL,
                                             CONSTRAINT FK9f5twia8exmuu39wey31lyjo6 FOREIGN KEY (jury_id) REFERENCES photo_contest.auth_users (id)
);

DROP TABLE IF EXISTS photo_contest.photo_submissions;
CREATE TABLE photo_contest.photo_submissions (
                                                 contest_id BIGINT NULL,
                                                 creator_id BIGINT NULL,
                                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                 photo_url VARCHAR(255) NOT NULL,
                                                 story TEXT NULL,
                                                 title VARCHAR(255) NOT NULL,
                                                 CONSTRAINT FK1r9j2lqrf0xw47y766jtiimdr FOREIGN KEY (contest_id) REFERENCES photo_contest.contests (id),
                                                 CONSTRAINT FK9wulpcinvi6ighjohngakmx7x FOREIGN KEY (creator_id) REFERENCES photo_contest.auth_users (id)
);

DROP TABLE IF EXISTS photo_contest.photo_submission_reviews;
CREATE TABLE photo_contest.photo_submission_reviews (
                                                        photo_review_id BIGINT NOT NULL,
                                                        photo_submission_id BIGINT NOT NULL,
                                                        CONSTRAINT FK50gmvdbrv35kenp8b304apbvq FOREIGN KEY (photo_submission_id) REFERENCES photo_contest.photo_submissions (id),
                                                        CONSTRAINT FKg3favo7adw34tvseni6xavi5y FOREIGN KEY (photo_review_id) REFERENCES photo_contest.photo_reviews (id)
);

DROP TABLE IF EXISTS photo_contest.spring_session;
CREATE TABLE photo_contest.spring_session (
                                              PRIMARY_ID CHAR(36) NOT NULL PRIMARY KEY,
                                              SESSION_ID CHAR(36) NOT NULL,
                                              CREATION_TIME BIGINT NOT NULL,
                                              LAST_ACCESS_TIME BIGINT NOT NULL,
                                              MAX_INACTIVE_INTERVAL INT NOT NULL,
                                              EXPIRY_TIME BIGINT NOT NULL,
                                              PRINCIPAL_NAME VARCHAR(100) NULL,
                                              CONSTRAINT SPRING_SESSION_IX1 UNIQUE (SESSION_ID)
) ROW_FORMAT = DYNAMIC;

-- Drop the table if it exists, and then recreate it
DROP TABLE IF EXISTS photo_contest.spring_session_attributes;

CREATE TABLE photo_contest.spring_session_attributes (
                                                         SESSION_PRIMARY_ID CHAR(36) NOT NULL,
                                                         ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
                                                         ATTRIBUTE_BYTES BLOB NOT NULL,
                                                         PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
                                                         CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES photo_contest.spring_session (PRIMARY_ID) ON DELETE CASCADE
) ROW_FORMAT = DYNAMIC;

-- Now that the table exists, drop the index if it exists, and recreate it


-- Similarly, handle the other session table
DROP TABLE IF EXISTS photo_contest.spring_session;

CREATE TABLE photo_contest.spring_session (
                                              PRIMARY_ID CHAR(36) NOT NULL PRIMARY KEY,
                                              SESSION_ID CHAR(36) NOT NULL,
                                              CREATION_TIME BIGINT NOT NULL,
                                              LAST_ACCESS_TIME BIGINT NOT NULL,
                                              MAX_INACTIVE_INTERVAL INT NOT NULL,
                                              EXPIRY_TIME BIGINT NOT NULL,
                                              PRINCIPAL_NAME VARCHAR(100) NULL,
                                              CONSTRAINT SPRING_SESSION_IX1 UNIQUE (SESSION_ID)
) ROW_FORMAT = DYNAMIC;


