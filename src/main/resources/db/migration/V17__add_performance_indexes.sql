-- Индексы для отзывов
CREATE INDEX IF NOT EXISTS idx_reviews_place_id ON reviews(place_id);
CREATE INDEX IF NOT EXISTS idx_reviews_user_place ON reviews(user_id, place_id);

-- Индексы для избранного
CREATE INDEX IF NOT EXISTS idx_favorites_user_id ON favorites(user_id);
CREATE INDEX IF NOT EXISTS idx_favorites_user_place ON favorites(user_id, place_id);

-- Индексы для истории просмотров
CREATE INDEX IF NOT EXISTS idx_view_history_user_id ON view_history(user_id);

-- Индексы для событий
CREATE INDEX IF NOT EXISTS idx_events_active_start ON events(active, start_date);

-- Индексы для мест
CREATE INDEX IF NOT EXISTS idx_places_type ON places(type);
CREATE INDEX IF NOT EXISTS idx_places_active ON places(active);
