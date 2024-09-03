-- Connect to the MySQL database
USE photo_contest;

-- Insert 10 users into `auth_users`, with only two being organizers
INSERT INTO auth_users (username, password, email, first_name, last_name, role, dtype, total_score)
VALUES
    ('user1', '$2a$10$E8wyRbXxr4u7UzdQZ.DpqePn.7iTgyFXlajICHjWQ1hPa5pa6XLQy', 'user1@example.com', 'John', 'Doe', 'ORGANIZER', 'USER', 0),
    ('user2', '$2a$10$E8wyRbXxr4u7UzdQZ.DpqePn.7iTgyFXlajICHjWQ1hPa5pa6XLQy', 'user2@example.com', 'Jane', 'Smith', 'ORGANIZER', 'USER', 0),
    ('user3', '$2a$10$E8wyRbXxr4u7UzdQZ.DpqePn.7iTgyFXlajICHjWQ1hPa5pa6XLQy', 'user3@example.com', 'Alice', 'Johnson', 'JUNKIE', 'USER', 0),
    ('user4', '$2a$10$E8wyRbXxr4u7UzdQZ.DpqePn.7iTgyFXlajICHjWQ1hPa5pa6XLQy', 'user4@example.com', 'Bob', 'Brown', 'JUNKIE', 'USER', 0),
    ('user5', '$2a$10$E8wyRbXxr4u7UzdQZ.DpqePn.7iTgyFXlajICHjWQ1hPa5pa6XLQy', 'user5@example.com', 'Charlie', 'Davis', 'JUNKIE', 'USER', 0),
    ('user6', '$2a$10$E8wyRbXxr4u7UzdQZ.DpqePn.7iTgyFXlajICHjWQ1hPa5pa6XLQy', 'user6@example.com', 'David', 'Wilson', 'JUNKIE', 'USER', 0),
    ('user7', '$2a$10$E8wyRbXxr4u7UzdQZ.DpqePn.7iTgyFXlajICHjWQ1hPa5pa6XLQy', 'user7@example.com', 'Eve', 'Miller', 'JUNKIE', 'USER', 0),
    ('user8', '$2a$10$E8wyRbXxr4u7UzdQZ.DpqePn.7iTgyFXlajICHjWQ1hPa5pa6XLQy', 'user8@example.com', 'Frank', 'Moore', 'JUNKIE', 'USER', 0),
    ('user9', '$2a$10$E8wyRbXxr4u7UzdQZ.DpqePn.7iTgyFXlajICHjWQ1hPa5pa6XLQy', 'user9@example.com', 'Grace', 'Taylor', 'JUNKIE', 'USER', 0),
    ('user10', '$2a$10$E8wyRbXxr4u7UzdQZ.DpqePn.7iTgyFXlajICHjWQ1hPa5pa6XLQy', 'user10@example.com', 'Hank', 'Anderson', 'JUNKIE', 'USER', 0);

-- Insert specified categories into `categories`
INSERT INTO categories (name, description)
VALUES
    ('Nature', 'Nature photography is a wide range of photography taken outdoors and devoted to displaying natural elements such as landscapes, wildlife, plants, and close-ups of natural scenes and textures.'),
    ('Portrait', 'Showcase the essence of individuals through compelling and expressive portraits.'),
    ('Street Photography', 'Document candid moments and urban life in public spaces.'),
    ('Architecture', 'Highlight the design and aesthetics of buildings and structures, both modern and historical.'),
    ('Abstract', 'Express ideas and emotions through non-representational or unconventional imagery.'),
    ('Black and White', 'Explore the power of monochrome photography across various subjects.'),
    ('Macro', 'Reveal the intricate details of small subjects through close-up and magnified shots.');

-- Insert a contest for each category into `contests`
INSERT INTO contests (title, description, category_id, organizer_id, start_date, phase, type)
VALUES
    ('Nature Photography Contest', 'A contest for nature photos', 1, 1, NOW(), 'NOT_STARTED', 'OPEN'),
    ('Portrait Photography Contest', 'A contest for portrait photos', 2, 2, NOW(), 'NOT_STARTED', 'OPEN'),
    ('Street Photography Contest', 'A contest for street photos', 3, 1, NOW(), 'NOT_STARTED', 'OPEN'),
    ('Architecture Photography Contest', 'A contest for architecture photos', 4, 2, NOW(), 'NOT_STARTED', 'OPEN'),
    ('Abstract Photography Contest', 'A contest for abstract photos', 5, 1, NOW(), 'NOT_STARTED', 'OPEN'),
    ('Black and White Photography Contest', 'A contest for black and white photos', 6, 2, NOW(), 'NOT_STARTED', 'OPEN'),
    ('Macro Photography Contest', 'A contest for macro photos', 7, 1, NOW(), 'NOT_STARTED', 'OPEN');


INSERT INTO photo_contest.photo_submissions (contest_id, creator_id, photo_url, story, title)
VALUES
    (2, 1, 'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353363/Portrait/jurica-koletic-7YVZYZeITc8-unsplash_zgc2sv.jpg', 'A stunning portrait in natural light', 'Natural Light Portrait'),
    (2, 2, 'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353363/Portrait/kimson-doan-HD8KlyWRYYM-unsplash_tlamlb.jpg', 'A candid portrait with vibrant colors', 'Vibrant Candid Portrait'),
    (2, 3, 'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353363/Portrait/kareya-saleh-tLKOj6cNwe0-unsplash_fe7vaw.jpg', 'A beautifully composed close-up portrait', 'Close-Up Portrait'),
    (2, 4, 'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353361/Portrait/jessica-felicio-_cvwXhGqG-o-unsplash_c0imjx.jpg', 'A portrait with a soft, dreamy background', 'Dreamy Background Portrait');

INSERT INTO photo_contest.photo_submissions (contest_id, creator_id, photo_url, story, title)
VALUES
    (4, 1, 'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353368/Architecture/joel-filipe-jU9VAZDGMzs-unsplash_vh7w6h.jpg', 'A breathtaking view of modern architecture', 'Modern Architectural Marvel'),
    (4, 2, 'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353367/Architecture/izuddin-helmi-adnan-1e71PSox7m8-unsplash_vatd9x.jpg', 'A stunning example of minimalistic design', 'Minimalistic Architecture'),
    (4, 3, 'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353367/Architecture/eduard-QmU_1X9ZHXo-unsplash_uuzjiw.jpg', 'An architectural masterpiece at sunset', 'Sunset Masterpiece'),
    (4, 4, 'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353366/Architecture/daniel-burka-oR9ZisoF_NE-unsplash_auzgft.jpg', 'A unique perspective of an iconic building', 'Iconic Building Perspective');


INSERT INTO photo_contest.photo_submissions (contest_id, creator_id, photo_url, story, title)
VALUES
    (5, 1, 'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353337/Abstract/pawel-czerwinski-6lQDFGOB1iw-unsplash_yftmun.jpg', 'A captivating blend of colors and shapes', 'Abstract Harmony'),
    (5, 2, 'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353335/Abstract/pawel-czerwinski-ruJm3dBXCqw-unsplash_gxgx42.jpg', 'An exploration of abstract forms and textures', 'Textured Abstractions'),
    (5, 3, 'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353334/Abstract/bia-w-a-PO8Woh4YBD8-unsplash_ybzqlq.jpg', 'A mysterious play of light and shadow', 'Light and Shadow Abstraction');


INSERT INTO photo_contest.photo_submissions (contest_id, creator_id, photo_url, story, title)
VALUES
    (6, 1, 'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353370/Black%20and%20White/vincent-van-zalinge-I5kafgqt85w-unsplash_hoxr2f.jpg', 'A stunning capture of nature in monochrome', 'Monochrome Nature'),
    (6, 2, 'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353369/Black%20and%20White/ornella-binni-oGSxaXK-dVw-unsplash_jzfmv6.jpg', 'An elegant portrait emphasizing light and shadow', 'Elegant Shadows'),
    (6, 3, 'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353369/Black%20and%20White/hoach-le-dinh-5DJqsjAYlmk-unsplash_p34sh0.jpg', 'A quiet moment captured in black and white', 'Silent Reflection'),
    (6, 4, 'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353368/Black%20and%20White/geranimo-9yvADFNcXOc-unsplash_vvkzbo.jpg', 'An abstract pattern of light in black and white', 'Abstract Light'),
    (6, 5, 'https://res.cloudinary.com/dtwfzrl2v/image/upload/v1725353368/Black%20and%20White/esteban-amaro-hy8y0Wp_Lp0-unsplash_nrhvlc.jpg', 'A serene landscape rendered in shades of gray', 'Gray Landscape');


