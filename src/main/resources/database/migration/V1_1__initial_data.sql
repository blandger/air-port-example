
INSERT INTO USERS (id, username, email, password) VALUES
(1, 'admin', 'admin@gmail.com', /*'Dfks$5d*Q'*/ '$2a$12$AA0z0LFi7sF.M065NxV3ZuREtjLPZE7elrOrbjn0DZlPl3v7zwIFq'),
(2, 'guest', 'guest@gmail.com', /*'guestPwd1!'*/ '$2a$12$e6Ol9M.4Pr5Ncu9/u/CP9.YF.Kv0n3/jKDQ0UG4FTVRGBuJ752bgq')
;

ALTER SEQUENCE users_id_seq RESTART WITH 100;

INSERT INTO AIRPORTS (id, name, code, city, user_id) VALUES
(1, 'Tweed-New Haven Airport', 'KHVN', 'New Haven', 1),
(2, 'Louis Armstrong New Orleans International Airport', 'KMSY', 'New Orleans', 1),
(3, 'Newark Liberty International Airport', 'KEWR', 'Newark', 1),
(4, 'John F. Kennedy International Airport', 'KJFK', 'New York', 1),
(5, 'LaGuardia Airport', 'KLGA', 'New York', 1),
(6, 'Coastal Carolina Regional Airport', 'KEWN', 'New Bern', 1),
(7, 'Newport News/Williamsburg International Airport', 'KPHF', 'Newport News', 1),
(8, 'Abha International Airport', 'OEAB', 'Abha', 1),
(9, 'Prince Mohammad bin Abdulaziz International Airport', 'OEMA', 'Medina', 1),
(10, 'Prince Abdul Mohsin bin Abdulaziz International Airport', 'OEYN', 'Yanbu', 1)
;

ALTER SEQUENCE airports_id_seq RESTART WITH 100;