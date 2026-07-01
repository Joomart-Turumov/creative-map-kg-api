-- Сброс сид-каталога VR-туров (см. V14__create_vr_tours.sql).
-- Убираем текущие засеянные туры, КРОМЕ «Манас Ордо», и добавляем новые.
-- Удаляем по iframe_url (стабильный идентификатор сид-записей V14): не зависит
-- от id в разных окружениях и не затрагивает туры, добавленные через админку.

DELETE FROM vr_tours WHERE iframe_url IN (
    'https://my.www3d.site/tour/kg-key',                                            -- КЭУ
    'https://my.www3d.site/tour/kg-okean-one',                                      -- ОКЕАН
    'https://my.www3d.site/tour/kg-technopark-tel996-990-111333',                   -- TECHNOPARK
    'https://my.www3d.site/tour/avangard-style-toktogula-125-tel996-997007007',     -- AVANGARD (Токтогула)
    'https://my.www3d.site/tour/avangardstyle-tokombaeva-53-tel996-997007007',      -- AVANGARD (Токомбаева)
    'https://my.www3d.site/tour/kg-royal-hotel-tel-0707876088'                      -- Royal Hotel
);

-- Тур 2: Дом-музей М.В. Фрунзе (модель хранит одно поле title/description — берём RU).
INSERT INTO vr_tours (title, description, iframe_url, address, category, region, status) VALUES
('Дом-музей М.В. Фрунзе',
 'Виртуальный тур по мемориальному дому-музею М.В. Фрунзе в Бишкеке.',
 'https://my.www3d.site/tour/kg-museum-domfrunze-frunze-street364-tel996-312660604-ru',
 'ул. Фрунзе, г. Бишкек, Кыргызстан',
 'MUSEUM', 'Бишкек', 'PUBLISHED');

-- Туры 3–4 (Google Street View) не добавляются здесь: ссылки google.com/maps/@...
-- блокируются X-Frame-Options. Будут добавлены отдельной миграцией через keyless
-- embed-ссылки (google.com/maps/embed?pb=...), которые грузятся в iframe без API-ключа.
