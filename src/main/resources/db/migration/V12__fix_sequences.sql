-- Fix sequences after explicit ID inserts (needed for PostgreSQL)
SELECT setval(pg_get_serial_sequence('places', 'id'), COALESCE(MAX(id), 1)) FROM places;
SELECT setval(pg_get_serial_sequence('events', 'id'), COALESCE(MAX(id), 1)) FROM events;
SELECT setval(pg_get_serial_sequence('users', 'id'), COALESCE(MAX(id), 1)) FROM users;
