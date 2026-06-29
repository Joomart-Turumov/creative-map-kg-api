-- Места засеяны с явными id (V6: 1..281), но IDENTITY-счётчик не был сдвинут,
-- из-за чего GenerationType.IDENTITY генерировал id=1 → коллизия PRIMARY KEY
-- при создании места через /admin/places. Перезапускаем счётчик за пределы сид-данных.
ALTER TABLE places ALTER COLUMN id RESTART WITH 282;
