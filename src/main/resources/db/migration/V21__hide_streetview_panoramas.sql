-- Прячем панорамы Street View из публичного каталога (переводим в DRAFT).
-- Причина: keyless embed отдаёт панораму только для официального Street View,
-- а Иссык-Куль — пользовательская фотосфера (2e10), без API-ключа не встраивается.
-- Данные сохраняем — вернём в PUBLISHED, когда появится Google API-ключ или другие панорамы.

UPDATE vr_tours SET status = 'DRAFT' WHERE iframe_url IN (
    'https://www.google.com/maps/embed?pb=!4v1700000000000!6m8!1m7!1s9PNxhYypUdUbJrc8IIbkhg!2m2!1d42.8817745!2d74.587018!3f10.07!4f0!5f0.7820865974627469',
    'https://www.google.com/maps/embed?pb=!4v1700000000000!6m8!1m7!1sCIABIhCd1utyQhyYfYs1xsoLcg3j!2m2!1d42.5969922!2d75.792544!3f24.59!4f0!5f0.7820865974627469'
);
