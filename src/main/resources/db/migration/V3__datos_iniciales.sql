-- Datos iniciales.
--
-- Antes vivian en un CommandLineRunner que corria en cada arranque y decidia por
-- count() si sembrar o no. Aca quedan versionados: se aplican una sola vez, en el
-- mismo orden, en cualquier base.
--
-- Todos los INSERT son idempotentes: en produccion, donde el contenido ya existe,
-- no duplican nada.

-- ─────────────────────────────────────────────────────────────
-- Hero
-- ─────────────────────────────────────────────────────────────
INSERT INTO hero (titulo, subtitulo, texto_del_cta, url_del_cta, activo, creado_en, actualizado_en)
SELECT 'Oriente', 'El camino del bienestar', 'Conocé nuestros servicios', '#servicios', 1, NOW(6), NOW(6)
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM (SELECT 1 FROM hero LIMIT 1) AS existentes);

-- ─────────────────────────────────────────────────────────────
-- Servicios
-- ─────────────────────────────────────────────────────────────
-- slug es unico: INSERT IGNORE deja intactos los que ya estan cargados.
INSERT IGNORE INTO servicio (slug, categoria, titulo, descripcion, orden, activo, creado_en, actualizado_en) VALUES
('kinesiologia',      'KINE',     'Kinesiología',        'Rehabilitación funcional y recuperación post-lesión. Tratamientos personalizados para volver al movimiento.', 1, 1, NOW(6), NOW(6)),
('acupuntura',        'KINE',     'Acupuntura',          'Técnica milenaria de medicina tradicional china. Estimulación de puntos energéticos para aliviar el dolor, reducir el estrés y restablecer el equilibrio del organismo.', 2, 1, NOW(6), NOW(6)),
('masajes',           'KINE',     'Masajes',             'Masajes terapéuticos y de relajación profunda. Técnicas especializadas para liberar tensiones y restaurar el equilibrio.', 3, 1, NOW(6), NOW(6)),
('gimnasia-adaptada', 'KINE',     'Gimnasia Adaptada',   'Clases grupales de movimiento suave y fortalecimiento, diseñadas para todas las edades.', 4, 1, NOW(6), NOW(6)),
('estetica',          'ESTETICA', 'Medicina Estética',   'Procedimientos faciales y corporales no invasivos con resultados visibles.', 5, 1, NOW(6), NOW(6)),
('cosmiatra',         'ESTETICA', 'Cosmiatría',          'Cuidado profesional de la piel para tratar, mejorar y mantener su salud.', 6, 1, NOW(6), NOW(6)),
('bronceado',         'ESTETICA', 'Bronceado Cannelle',  'Bronceado premium de la línea Cannelle. Resultados naturales, duraderos y sin exposición solar.', 7, 1, NOW(6), NOW(6)),
('makeup',            'ESTETICA', 'Makeup',              'Maquillaje artístico y profesional para eventos, producciones y ocasiones especiales.', 8, 1, NOW(6), NOW(6)),
('depilacion',        'ESTETICA', 'Depilación Definitiva','Métodos de depilación definitiva. Soluciones para todos los fototipos y zonas del cuerpo.', 9, 1, NOW(6), NOW(6));

-- ─────────────────────────────────────────────────────────────
-- Combos
-- ─────────────────────────────────────────────────────────────
INSERT IGNORE INTO combo (slug, titulo, bajada, descripcion, etiqueta, orden, activo, creado_en, actualizado_en) VALUES
('combo-restauracion', 'Restauración Profunda', 'Cuerpo y piel en una sola visita',        'Combinamos técnica kinesiológica y cuidado estético para quienes necesitan recuperarse por dentro y verse bien por fuera.', 'Más elegido', 1, 1, NOW(6), NOW(6)),
('combo-glow',         'Oriente Glow',          'Transformación estética completa',        'El combo de imagen más completo del consultorio. Ideal para eventos, vacaciones o simplemente darte un merecido mimo.', 'Nuevo', 2, 1, NOW(6), NOW(6)),
('combo-detox',        'Detox & Bienestar',     'Reset para tu mente y tu cuerpo',         'Una experiencia de descanso activo para liberar tensiones, activar la circulación y renovar tu energía.', NULL, 3, 1, NOW(6), NOW(6)),
('combo-piel',         'Programa Piel',         'Resultados visibles desde la primera sesión', 'Protocolo intensivo de cosmiatría diseñado para tratar, unificar y luminizar la piel del rostro.', NULL, 4, 1, NOW(6), NOW(6));

-- Los items se insertan solo si el combo todavia no tiene ninguno, para no
-- duplicarlos sobre un combo que ya fue editado desde el panel.
INSERT INTO item_de_combo (combo_id, descripcion, orden, creado_en, actualizado_en)
SELECT c.id, i.descripcion, i.orden, NOW(6), NOW(6)
FROM combo c
JOIN (
    SELECT 'combo-restauracion' AS slug, 'Sesión de Kinesiología'            AS descripcion, 1 AS orden UNION ALL
    SELECT 'combo-restauracion',         'Hidratación facial profunda',            2 UNION ALL
    SELECT 'combo-restauracion',         'Masaje de cierre relajante',             3 UNION ALL
    SELECT 'combo-glow',                 'Bronceado Cannelle',                     1 UNION ALL
    SELECT 'combo-glow',                 'Sesión de Cosmiatría',                   2 UNION ALL
    SELECT 'combo-glow',                 'Maquillaje profesional',                 3 UNION ALL
    SELECT 'combo-detox',                'Masaje descontracturante',               1 UNION ALL
    SELECT 'combo-detox',                'Tratamiento corporal hidratante',        2 UNION ALL
    SELECT 'combo-detox',                'Reflexología',                           3 UNION ALL
    SELECT 'combo-piel',                 'Diagnóstico cutáneo personalizado',      1 UNION ALL
    SELECT 'combo-piel',                 'Tratamiento según tipo de piel',         2 UNION ALL
    SELECT 'combo-piel',                 'Depilación facial de precisión',         3
) i ON i.slug = c.slug
WHERE NOT EXISTS (
    SELECT 1 FROM (SELECT combo_id FROM item_de_combo) AS existentes WHERE existentes.combo_id = c.id
);

-- ─────────────────────────────────────────────────────────────
-- Nosotros y diferenciales
-- ─────────────────────────────────────────────────────────────
INSERT INTO nosotros (titulo, cuerpo, activo, creado_en, actualizado_en)
SELECT 'Sobre Oriente',
       'Un consultorio integral de kinesiología y estética personal en Resistencia, Chaco. Dos disciplinas bajo el mismo techo para acompañar el bienestar completo de cada persona.',
       1, NOW(6), NOW(6)
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM (SELECT 1 FROM nosotros LIMIT 1) AS existentes);

-- Textos que ya estaban escritos en el frontend (constants/data.ts).
INSERT INTO diferencial (nosotros_id, titulo, descripcion, orden, activo, creado_en, actualizado_en)
SELECT n.id, d.titulo, d.descripcion, d.orden, 1, NOW(6), NOW(6)
FROM (SELECT id FROM nosotros ORDER BY id LIMIT 1) n
JOIN (
    SELECT 'Equipo profesional certificado' AS titulo, 'Cada especialista cuenta con formación actualizada y experiencia comprobada en su área.' AS descripcion, 1 AS orden UNION ALL
    SELECT 'Atención personalizada',               'Escuchamos cada caso con dedicación. Los tratamientos se adaptan a cada persona, no al revés.', 2 UNION ALL
    SELECT 'Espacio pensado para vos',             'Un ambiente cuidado en cada detalle, diseñado para que el bienestar empiece desde que entrás.', 3 UNION ALL
    SELECT 'Enfoque integral',                     'Kinesiología y estética bajo el mismo techo, para abordajes completos y coordinados.', 4
) d
WHERE NOT EXISTS (SELECT 1 FROM (SELECT 1 FROM diferencial LIMIT 1) AS existentes);

-- Las estadisticas (numeros de pacientes, anos de experiencia) no se siembran:
-- son datos del negocio y los carga la administradora desde el panel. Una semilla
-- inventada aca terminaria publicada en la landing.

-- ─────────────────────────────────────────────────────────────
-- Contacto
-- ─────────────────────────────────────────────────────────────
INSERT INTO informacion_de_contacto
    (whatsapp, whatsapp_para_mostrar, instagram, direccion, url_de_maps, embed_de_maps, creado_en, actualizado_en)
SELECT '+5493625214544',
       '+54 9 362 521-4544',
       'oriente.rcia',
       'Roque Saenz Peña 555, Resistencia, Chaco',
       'https://maps.google.com/?q=Roque+Saenz+Peña+555+Resistencia+Chaco',
       'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3540.3638991284693!2d-58.99119622454141!3d-27.457928176326423!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x94450cf2a7eb2c6f%3A0x1c79dae8facf122c!2sRoque%20S%C3%A1enz%20Pe%C3%B1a%20555%2C%20H3500CEN%20Resistencia%2C%20Chaco!5e0!3m2!1ses!2sar!4v1772827508511!5m2!1ses!2sar',
       NOW(6), NOW(6)
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM (SELECT 1 FROM informacion_de_contacto LIMIT 1) AS existentes);
