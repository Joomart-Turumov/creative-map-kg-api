ALTER TABLE events ADD COLUMN category VARCHAR(50) NOT NULL DEFAULT 'MUSEUM';

UPDATE events SET category = 'MUSEUM' WHERE id = 1;
UPDATE events SET category = 'FESTIVAL' WHERE id = 2;
UPDATE events SET category = 'THEATER' WHERE id = 3;
UPDATE events SET category = 'EXHIBITION' WHERE id = 4;
UPDATE events SET category = 'FESTIVAL' WHERE id = 5;
UPDATE events SET category = 'MASTERCLASS' WHERE id = 6;
