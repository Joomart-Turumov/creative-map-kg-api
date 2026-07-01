-- Туры 3–4: панорамы Google Street View.
-- Ссылки google.com/maps/@... блокируются X-Frame-Options, а «Встроить карту»
-- отдаёт 2D-карту. Поэтому собираем keyless Street View embed вручную из pano_id
-- (формат !6m8!1m7!1s<PANO_ID>!2m2!1d<lat>!2d<lng>!3f<heading>!4f<pitch>!5f<zoom>) —
-- он рендерит именно панораму и не требует API-ключа.

-- Бишкек (pano 9PNxhYypUdUbJrc8IIbkhg, официальный Street View)
INSERT INTO vr_tours (title, description, iframe_url, address, category, region, status) VALUES
('Панорама 360° — Бишкек',
 'Панорамный обзор 360° в городе Бишкек (Google Street View).',
 'https://www.google.com/maps/embed?pb=!4v1700000000000!6m8!1m7!1s9PNxhYypUdUbJrc8IIbkhg!2m2!1d42.8817745!2d74.587018!3f10.07!4f0!5f0.7820865974627469',
 'г. Бишкек, Кыргызстан',
 'OTHER', 'Бишкек', 'PUBLISHED');

-- Иссык-Куль (pano CIABIhCd1utyQhyYfYs1xsoLcg3j, пользовательская фотосфера, 2e10)
INSERT INTO vr_tours (title, description, iframe_url, address, category, region, status) VALUES
('Панорама 360° — Иссык-Куль',
 'Панорамный обзор 360° в Иссык-Кульской области (Google Street View).',
 'https://www.google.com/maps/embed?pb=!4v1700000000000!6m8!1m7!1sCIABIhCd1utyQhyYfYs1xsoLcg3j!2m2!1d42.5969922!2d75.792544!3f24.59!4f0!5f0.7820865974627469',
 'Иссык-Кульская область, Кыргызстан',
 'OTHER', 'Иссык-Кульская область', 'PUBLISHED');
