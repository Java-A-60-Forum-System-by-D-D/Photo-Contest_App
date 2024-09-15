-- Connect to the MySQL database
USE PHOTO_CONTEST;

SET foreign_key_checks = 0;

TRUNCATE AUTH_USERS;
TRUNCATE CATEGORIES;
TRUNCATE CONTESTS;
TRUNCATE CONTEST_PARTICIPANTS;
TRUNCATE INVITED_USERS;
TRUNCATE JURY_CONTESTS;
TRUNCATE NOTIFICATIONS;
TRUNCATE PHOTO_REVIEWS;
TRUNCATE PHOTO_SUBMISSION_REVIEWS;
TRUNCATE PHOTO_SUBMISSIONS;
TRUNCATE SPRING_SESSION;
TRUNCATE SPRING_SESSION_ATTRIBUTES;


SET foreign_key_checks = 1;

-- Populate auth_users table
-- Create temporary table to hold usernames and email prefixes
CREATE TEMPORARY TABLE TEMP_USERNAMES
(
    USERNAME     VARCHAR(255),
    EMAIL_PREFIX VARCHAR(255)
);

-- Insert sample users into temp_usernames
INSERT INTO TEMP_USERNAMES (USERNAME, EMAIL_PREFIX)
VALUES ('johnybravo', 'johnybravo'),
       ('pixelmaster', 'pixel.master'),
       ('snapdragon', 'snap.dragon'),
       ('lenslurker', 'lens.lurker'),
       ('shutterbug42', 'shutterbug42'),
       ('f_stop_fitzgerald', 'fstopfitz'),
       ('aperture_ace', 'aperture.ace'),
       ('zoominzoom', 'zoomin.zoom'),
       ('flash_gordon', 'flash.gordon'),
       ('bokeh_beast', 'bokeh.beast'),
       ('click_magnet', 'click.magnet'),
       ('iso_ninja', 'iso.ninja'),
       ('capture_captain', 'capt.capture'),
       ('shutter_swift', 'swift.shutter'),
       ('focal_phoenix', 'focal.phoenix'),
       ('lightroom_guru', 'lightroom.guru'),
       ('prime_lens_pro', 'prime.lens'),
       ('raw_magician', 'raw.magician'),
       ('exposure_expert', 'exposure.ex'),
       ('dslr_dynamo', 'dslr.dynamo');

-- Insert data into auth_users table with total_score set to 0
INSERT INTO PHOTO_CONTEST.AUTH_USERS (EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, USERNAME, ROLE, DTYPE, TOTAL_SCORE)
SELECT CONCAT(EMAIL_PREFIX, '@example.com')                                        AS EMAIL,
       CONCAT(
               UPPER(SUBSTRING(SUBSTRING_INDEX(EMAIL_PREFIX, '.', 1), 1, 1)),
               LOWER(SUBSTRING(SUBSTRING_INDEX(EMAIL_PREFIX, '.', 1), 2))
       )                                                                           AS FIRST_NAME,
       CASE
           WHEN CHAR_LENGTH(EMAIL_PREFIX) - CHAR_LENGTH(REPLACE(EMAIL_PREFIX, '.', '')) > 0
               THEN CONCAT(
                   UPPER(SUBSTRING(SUBSTRING_INDEX(EMAIL_PREFIX, '.', -1), 1, 1)),
                   LOWER(SUBSTRING(SUBSTRING_INDEX(EMAIL_PREFIX, '.', -1), 2))
                    )
           ELSE ''
           END                                                                     AS LAST_NAME,
       '$2a$10$GveUwZCoAqO1QsoICaAW9.wx7NcEmuwJYY4xx3mfKVzJfeelevVJO'              AS PASSWORD,
       USERNAME,
       ELT(1 + FLOOR(RAND() * 4), 'ORGANIZER', 'DICTATOR', 'JUNKIE', 'ENTHUSIAST') AS ROLE,
       'USER'                                                                      AS DTYPE,
       0                                                                           AS TOTAL_SCORE -- Set total_score to 0 for all users
FROM TEMP_USERNAMES
ORDER BY RAND()
LIMIT 20;

-- Drop the temporary table
DROP TEMPORARY TABLE TEMP_USERNAMES;

-- Insert specified categories into `categories`
INSERT INTO CATEGORIES (NAME, DESCRIPTION, PHOTO_URL)
VALUES ('Nature',
        'Nature photography is a wide range of photography taken outdoors and devoted to displaying natural elements such as landscapes, wildlife, plants, and close-ups of natural scenes and textures.',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353369/Nature/blake-verdoorn-cssvEZacHvQ-unsplash_kw5yj3.jpg'),
       ('Portrait', 'Showcase the essence of individuals through compelling and expressive portraits.',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353363/Portrait/jurica-koletic-7YVZYZeITc8-unsplash_zgc2sv.jpg'),
       ('Street Photography', 'Document candid moments and urban life in public spaces.',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353367/Street%20Photography/luke-stackpoole-xgXKu6uM9jY-unsplash_i1q1m1.jpg'),
       ('Architecture', 'Highlight the design and aesthetics of buildings and structures, both modern and historical.',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353368/Architecture/joel-filipe-jU9VAZDGMzs-unsplash_vh7w6h.jpg'),
       ('Abstract', 'Express ideas and emotions through non-representational or unconventional imagery.',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353337/Abstract/pawel-czerwinski-6lQDFGOB1iw-unsplash_yftmun.jpg'),
       ('Black and White', 'Explore the power of monochrome photography across various subjects.',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353370/Black%20and%20White/vincent-van-zalinge-I5kafgqt85w-unsplash_hoxr2f.jpg'),
       ('Macro', 'Reveal the intricate details of small subjects through close-up and magnified shots.',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353360/Macro/gildardo-rh-q1-dAZuhs7I-unsplash_bew8ce.jpg');

-- Insert 20 contests into the contests table with valid phase data
-- Insert 20 contests into the contests table with correct category_id and photo_url
-- Insert 20 contests into the contests table with correct category_id and photo_url
-- Need To Make some
INSERT INTO PHOTO_CONTEST.CONTESTS
(CATEGORY_ID, ORGANIZER_ID, START_DATE, START_PHASE_1, START_PHASE_2, START_PHASE_3, UPDATED_DATE, DESCRIPTION, TITLE,
 PHASE, TYPE, PHOTO_URL)
VALUES (1, 1, NOW() - INTERVAL 30 DAY, NOW() - INTERVAL 28 DAY, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 10 DAY,
        NOW() - INTERVAL 5 DAY,
        'A contest to showcase the best nature photography.', 'Nature Photography Contest', 'FINISHED', 'OPEN',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353369/Nature/blake-verdoorn-cssvEZacHvQ-unsplash_kw5yj3.jpg'),
       (2, 2, NOW() - INTERVAL 25 DAY, NOW() - INTERVAL 23 DAY, NOW() - INTERVAL 18 DAY, NOW() - INTERVAL 10 DAY,
        NOW() - INTERVAL 2 DAY,
        'Capture the essence of individuals through compelling portraits.', 'Portrait Perfection', 'FINISHED',
        'INVITATIONAL',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353363/Portrait/jurica-koletic-7YVZYZeITc8-unsplash_zgc2sv.jpg'),
       (3, 3, NOW() - INTERVAL 22 DAY, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 8 DAY,
        NOW() - INTERVAL 1 DAY,
        'Document candid moments and urban life in public spaces.', 'Street Photography Challenge', 'FINISHED', 'OPEN',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353367/Street%20Photography/luke-stackpoole-xgXKu6uM9jY-unsplash_i1q1m1.jpg'),
       (4, 4, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 18 DAY, NOW() - INTERVAL 12 DAY, NOW() - INTERVAL 5 DAY, NOW(),
        'Highlight the beauty of architectural designs.', 'Architecture Wonders', 'PHASE_2', 'INVITATIONAL',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353368/Architecture/joel-filipe-jU9VAZDGMzs-unsplash_vh7w6h.jpg'),
       (5, 5, NOW() - INTERVAL 18 DAY, NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 3 DAY, NOW(),
        'Explore abstract photography and unconventional imagery.', 'Abstract Art Photography', 'PHASE_2', 'OPEN',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353337/Abstract/pawel-czerwinski-6lQDFGOB1iw-unsplash_yftmun.jpg'),
       (7, 6, NOW() - INTERVAL 16 DAY, NOW() - INTERVAL 13 DAY, NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 1 DAY, NOW(),
        'Capture stunning macro shots of small subjects.', 'Macro Photography Contest', 'PHASE_2', 'INVITATIONAL',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353360/Macro/gildardo-rh-q1-dAZuhs7I-unsplash_bew8ce.jpg'),
       (1, 7, NOW() - INTERVAL 40 DAY, NOW() - INTERVAL 35 DAY, NOW() - INTERVAL 25 DAY, NOW() - INTERVAL 15 DAY,
        NOW() - INTERVAL 5 DAY,
        'A competition for the best nature landscapes.', 'Nature Explorer', 'FINISHED', 'OPEN',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353369/Nature/blake-verdoorn-cssvEZacHvQ-unsplash_kw5yj3.jpg'),
       (2, 8, NOW() - INTERVAL 35 DAY, NOW() - INTERVAL 30 DAY, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 10 DAY,
        NOW() - INTERVAL 3 DAY,
        'Create emotional and powerful portraits.', 'Portrait Expressions', 'FINISHED', 'INVITATIONAL',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353363/Portrait/jurica-koletic-7YVZYZeITc8-unsplash_zgc2sv.jpg'),
       (3, 9, NOW() - INTERVAL 28 DAY, NOW() - INTERVAL 25 DAY, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 10 DAY,
        NOW() - INTERVAL 2 DAY,
        'Best street moments in urban areas.', 'Urban Street Photography', 'FINISHED', 'OPEN',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353367/Street%20Photography/luke-stackpoole-xgXKu6uM9jY-unsplash_i1q1m1.jpg'),
       (4, 10, NOW() - INTERVAL 22 DAY, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 7 DAY, NOW(),
        'Capture stunning architectural designs.', 'Architectural Excellence', 'PHASE_2', 'OPEN',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353368/Architecture/joel-filipe-jU9VAZDGMzs-unsplash_vh7w6h.jpg'),
       (5, 11, NOW() - INTERVAL 30 DAY, NOW() - INTERVAL 28 DAY, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 10 DAY,
        NOW() - INTERVAL 5 DAY,
        'Express emotions through abstract photography.', 'Abstract Creations', 'FINISHED', 'INVITATIONAL',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353337/Abstract/pawel-czerwinski-6lQDFGOB1iw-unsplash_yftmun.jpg'),
       (7, 12, NOW() - INTERVAL 26 DAY, NOW() - INTERVAL 24 DAY, NOW() - INTERVAL 18 DAY, NOW() - INTERVAL 7 DAY,
        NOW() - INTERVAL 1 DAY,
        'A macro photography contest capturing intricate details.', 'Macro Focus', 'FINISHED', 'OPEN',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353360/Macro/gildardo-rh-q1-dAZuhs7I-unsplash_bew8ce.jpg'),
       (1, 13, NOW() - INTERVAL 55 DAY, NOW() - INTERVAL 50 DAY, NOW() - INTERVAL 35 DAY, NOW() - INTERVAL 20 DAY,
        NOW() - INTERVAL 5 DAY,
        'Best nature photographs from across the world.', 'Global Nature Photography', 'FINISHED', 'OPEN',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353369/Nature/blake-verdoorn-cssvEZacHvQ-unsplash_kw5yj3.jpg'),
       (2, 14, NOW() - INTERVAL 18 DAY, NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 5 DAY, NOW(),
        'Showcase the finest portrait photography.', 'Portrait Glory', 'PHASE_2', 'INVITATIONAL',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353363/Portrait/jurica-koletic-7YVZYZeITc8-unsplash_zgc2sv.jpg'),
       (3, 15, NOW() - INTERVAL 35 DAY, NOW() - INTERVAL 30 DAY, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 10 DAY,
        NOW() - INTERVAL 1 DAY,
        'Document everyday life on the streets.', 'Street Life', 'FINISHED', 'OPEN',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353367/Street%20Photography/luke-stackpoole-xgXKu6uM9jY-unsplash_i1q1m1.jpg'),
       (4, 16, NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 8 DAY, NOW() - INTERVAL 5 DAY, NOW(), NOW(),
        'Capture beautiful buildings and structures.', 'Architectural Marvels', 'PHASE_2', 'OPEN',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353368/Architecture/joel-filipe-jU9VAZDGMzs-unsplash_vh7w6h.jpg'),
       (5, 17, NOW() - INTERVAL 50 DAY, NOW() - INTERVAL 45 DAY, NOW() - INTERVAL 35 DAY, NOW() - INTERVAL 20 DAY,
        NOW() - INTERVAL 1 DAY,
        'Explore abstract photography.', 'Abstract Explorers', 'FINISHED', 'INVITATIONAL',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353337/Abstract/pawel-czerwinski-6lQDFGOB1iw-unsplash_yftmun.jpg'),
       (7, 18, NOW() - INTERVAL 12 DAY, NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 6 DAY, NOW(), NOW(),
        'Capture detailed close-ups of small objects.', 'Macro Magnificence', 'PHASE_2', 'OPEN',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353360/Macro/gildardo-rh-q1-dAZuhs7I-unsplash_bew8ce.jpg'),
       (1, 19, NOW() - INTERVAL 60 DAY, NOW() - INTERVAL 55 DAY, NOW() - INTERVAL 40 DAY, NOW() - INTERVAL 30 DAY,
        NOW() - INTERVAL 5 DAY,
        'A nature contest focused on wildlife.', 'Wildlife Photography Contest', 'FINISHED', 'OPEN',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353369/Nature/blake-verdoorn-cssvEZacHvQ-unsplash_kw5yj3.jpg'),
       (2, 20, NOW() - INTERVAL 30 DAY, NOW() - INTERVAL 28 DAY, NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 10 DAY,
        NOW() - INTERVAL 1 DAY,
        'A portrait photography contest for beginners.', 'Portrait Novice Challenge', 'FINISHED', 'INVITATIONAL',
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353363/Portrait/jurica-koletic-7YVZYZeITc8-unsplash_zgc2sv.jpg');



INSERT INTO PHOTO_CONTEST.PHOTO_SUBMISSIONS (CONTEST_ID, CREATOR_ID, PHOTO_URL, STORY, TITLE)
VALUES (2, 1,
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353363/Portrait/jurica-koletic-7YVZYZeITc8-unsplash_zgc2sv.jpg',
        'A stunning portrait in natural light', 'Natural Light Portrait'),
       (2, 2,
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353363/Portrait/kimson-doan-HD8KlyWRYYM-unsplash_tlamlb.jpg',
        'A candid portrait with vibrant colors', 'Vibrant Candid Portrait'),
       (2, 3,
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353363/Portrait/kareya-saleh-tLKOj6cNwe0-unsplash_fe7vaw.jpg',
        'A beautifully composed close-up portrait', 'Close-Up Portrait'),
       (2, 4,
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353361/Portrait/jessica-felicio-_cvwXhGqG-o-unsplash_c0imjx.jpg',
        'A portrait with a soft, dreamy background', 'Dreamy Background Portrait');

INSERT INTO PHOTO_CONTEST.PHOTO_SUBMISSIONS (CONTEST_ID, CREATOR_ID, PHOTO_URL, STORY, TITLE)
VALUES (4, 1,
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353368/Architecture/joel-filipe-jU9VAZDGMzs-unsplash_vh7w6h.jpg',
        'A breathtaking view of modern architecture', 'Modern Architectural Marvel'),
       (4, 2,
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353367/Architecture/izuddin-helmi-adnan-1e71PSox7m8-unsplash_vatd9x.jpg',
        'A stunning example of minimalistic design', 'Minimalistic Architecture'),
       (4, 3,
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353367/Architecture/eduard-QmU_1X9ZHXo-unsplash_uuzjiw.jpg',
        'An architectural masterpiece at sunset', 'Sunset Masterpiece'),
       (4, 4,
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353366/Architecture/daniel-burka-oR9ZisoF_NE-unsplash_auzgft.jpg',
        'A unique perspective of an iconic building', 'Iconic Building Perspective');


INSERT INTO PHOTO_CONTEST.PHOTO_SUBMISSIONS (CONTEST_ID, CREATOR_ID, PHOTO_URL, STORY, TITLE)
VALUES (5, 1,
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353337/Abstract/pawel-czerwinski-6lQDFGOB1iw-unsplash_yftmun.jpg',
        'A captivating blend of colors and shapes', 'Abstract Harmony'),
       (5, 2,
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353335/Abstract/pawel-czerwinski-ruJm3dBXCqw-unsplash_gxgx42.jpg',
        'An exploration of abstract forms and textures', 'Textured Abstractions'),
       (5, 3,
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353334/Abstract/bia-w-a-PO8Woh4YBD8-unsplash_ybzqlq.jpg',
        'A mysterious play of light and shadow', 'Light and Shadow Abstraction');


INSERT INTO PHOTO_CONTEST.PHOTO_SUBMISSIONS (CONTEST_ID, CREATOR_ID, PHOTO_URL, STORY, TITLE)
VALUES (6, 1,
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353370/Black%20and%20White/vincent-van-zalinge-I5kafgqt85w-unsplash_hoxr2f.jpg',
        'A stunning capture of nature in monochrome', 'Monochrome Nature'),
       (6, 2,
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353369/Black%20and%20White/ornella-binni-oGSxaXK-dVw-unsplash_jzfmv6.jpg',
        'An elegant portrait emphasizing light and shadow', 'Elegant Shadows'),
       (6, 3,
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353369/Black%20and%20White/hoach-le-dinh-5DJqsjAYlmk-unsplash_p34sh0.jpg',
        'A quiet moment captured in black and white', 'Silent Reflection'),
       (6, 4,
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353368/Black%20and%20White/geranimo-9yvADFNcXOc-unsplash_vvkzbo.jpg',
        'An abstract pattern of light in black and white', 'Abstract Light'),
       (6, 5,
        'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353368/Black%20and%20White/esteban-amaro-hy8y0Wp_Lp0-unsplash_nrhvlc.jpg',
        'A serene landscape rendered in shades of gray', 'Gray Landscape');


