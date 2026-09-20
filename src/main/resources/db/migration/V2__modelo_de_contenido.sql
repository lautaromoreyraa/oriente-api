-- Reestructuracion del modelo de contenido.
--
-- Tres cosas pasan aca:
--   1. Tablas y columnas pasan a espanol, para que el esquema y el codigo se
--      lean igual.
--   2. Lo que estaba modelado como columnas numeradas (stat1..stat3,
--      diff1..diff4) pasa a tablas propias: se pueden agregar, reordenar y
--      desactivar sin una migracion nueva.
--   3. Entran las tablas que los requerimientos nuevos necesitan: imagenes por
--      servicio, equipo, publicaciones de Instagram y datos de contacto.

-- ─────────────────────────────────────────────────────────────
-- Hero
-- ─────────────────────────────────────────────────────────────
RENAME TABLE hero_section TO hero;

ALTER TABLE hero
    CHANGE title                titulo                    VARCHAR(255) NOT NULL,
    CHANGE subtitle             subtitulo                 VARCHAR(255),
    CHANGE cta_text             texto_del_cta             VARCHAR(255),
    CHANGE cta_url              url_del_cta               VARCHAR(255),
    CHANGE background_image_url imagen_de_fondo_url       VARCHAR(500),
    CHANGE active               activo                    BOOLEAN      NOT NULL DEFAULT TRUE,
    CHANGE created_at           creado_en                 DATETIME(6)  NOT NULL,
    CHANGE updated_at           actualizado_en            DATETIME(6)  NOT NULL,
    ADD COLUMN imagen_de_fondo_public_id VARCHAR(255) AFTER imagen_de_fondo_url,
    ADD COLUMN imagen_de_fondo_alt       VARCHAR(255) AFTER imagen_de_fondo_public_id;

-- ─────────────────────────────────────────────────────────────
-- Servicios
-- ─────────────────────────────────────────────────────────────
RENAME TABLE service TO servicio;

ALTER TABLE servicio
    CHANGE slug          slug           VARCHAR(255) NOT NULL,
    CHANGE category      categoria      VARCHAR(40)  NOT NULL,
    CHANGE title         titulo         VARCHAR(255) NOT NULL,
    CHANGE description   descripcion    TEXT,
    CHANGE display_order orden          INT                   DEFAULT 0,
    CHANGE active        activo         BOOLEAN      NOT NULL DEFAULT TRUE,
    CHANGE created_at    creado_en      DATETIME(6)  NOT NULL,
    CHANGE updated_at    actualizado_en DATETIME(6)  NOT NULL,
    ADD COLUMN descripcion_larga TEXT         AFTER descripcion,
    ADD COLUMN imagen_url        VARCHAR(500) AFTER descripcion_larga,
    ADD COLUMN imagen_public_id  VARCHAR(255) AFTER imagen_url,
    ADD COLUMN imagen_alt        VARCHAR(255) AFTER imagen_public_id;

-- icon_path guardaba un path de SVG dibujado a mano. Los servicios ahora se
-- muestran con fotos, asi que la columna no tiene destino.
ALTER TABLE servicio DROP COLUMN icon_path;

-- Carrusel de imagenes por servicio.
--
-- url_de_publicacion guarda el link al post de Instagram del que salio la foto,
-- para que la tarjeta pueda llevar al post. La imagen que se sirve es la de
-- Cloudinary y no la del CDN de Instagram: esas URLs vienen firmadas y expiran,
-- asi que hotlinkearlas rompe el carrusel a los pocos dias.
CREATE TABLE imagen_de_servicio (
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    servicio_id        BIGINT       NOT NULL,
    imagen_url         VARCHAR(500) NOT NULL,
    imagen_public_id   VARCHAR(255),
    alt                VARCHAR(255),
    url_de_publicacion VARCHAR(500),
    orden              INT          NOT NULL DEFAULT 0,
    activo             BOOLEAN      NOT NULL DEFAULT TRUE,
    creado_en          DATETIME(6)  NOT NULL,
    actualizado_en     DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    KEY ix_imagen_de_servicio_servicio (servicio_id, orden),
    CONSTRAINT fk_imagen_de_servicio_servicio
        FOREIGN KEY (servicio_id) REFERENCES servicio (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ─────────────────────────────────────────────────────────────
-- Combos
-- ─────────────────────────────────────────────────────────────
ALTER TABLE combo
    CHANGE title         titulo         VARCHAR(255) NOT NULL,
    CHANGE tagline       bajada         VARCHAR(255),
    CHANGE description   descripcion    TEXT,
    CHANGE badge         etiqueta       VARCHAR(255),
    CHANGE display_order orden          INT                   DEFAULT 0,
    CHANGE active        activo         BOOLEAN      NOT NULL DEFAULT TRUE,
    CHANGE created_at    creado_en      DATETIME(6)  NOT NULL,
    CHANGE updated_at    actualizado_en DATETIME(6)  NOT NULL;

RENAME TABLE combo_item TO item_de_combo;

ALTER TABLE item_de_combo
    CHANGE description   descripcion    VARCHAR(255) NOT NULL,
    CHANGE display_order orden          INT                   DEFAULT 0,
    CHANGE created_at    creado_en      DATETIME(6)  NOT NULL,
    CHANGE updated_at    actualizado_en DATETIME(6)  NOT NULL;

-- ─────────────────────────────────────────────────────────────
-- Nosotros, estadisticas, diferenciales y equipo
-- ─────────────────────────────────────────────────────────────
RENAME TABLE about_section TO nosotros;

ALTER TABLE nosotros
    CHANGE heading    titulo         VARCHAR(255) NOT NULL,
    CHANGE body       cuerpo         TEXT,
    CHANGE image_url  imagen_url     VARCHAR(500),
    CHANGE active     activo         BOOLEAN      NOT NULL DEFAULT TRUE,
    CHANGE created_at creado_en      DATETIME(6)  NOT NULL,
    CHANGE updated_at actualizado_en DATETIME(6)  NOT NULL,
    ADD COLUMN imagen_public_id VARCHAR(255) AFTER imagen_url,
    ADD COLUMN imagen_alt       VARCHAR(255) AFTER imagen_public_id;

CREATE TABLE estadistica (
    id             BIGINT      NOT NULL AUTO_INCREMENT,
    nosotros_id    BIGINT      NOT NULL,
    valor          VARCHAR(20) NOT NULL,
    etiqueta       VARCHAR(60) NOT NULL,
    orden          INT         NOT NULL DEFAULT 0,
    activo         BOOLEAN     NOT NULL DEFAULT TRUE,
    creado_en      DATETIME(6) NOT NULL,
    actualizado_en DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    KEY ix_estadistica_nosotros (nosotros_id, orden),
    CONSTRAINT fk_estadistica_nosotros
        FOREIGN KEY (nosotros_id) REFERENCES nosotros (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE diferencial (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    nosotros_id    BIGINT       NOT NULL,
    titulo         VARCHAR(120) NOT NULL,
    descripcion    TEXT,
    orden          INT          NOT NULL DEFAULT 0,
    activo         BOOLEAN      NOT NULL DEFAULT TRUE,
    creado_en      DATETIME(6)  NOT NULL,
    actualizado_en DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    KEY ix_diferencial_nosotros (nosotros_id, orden),
    CONSTRAINT fk_diferencial_nosotros
        FOREIGN KEY (nosotros_id) REFERENCES nosotros (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE miembro_del_equipo (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    nosotros_id      BIGINT       NOT NULL,
    nombre           VARCHAR(120) NOT NULL,
    rol              VARCHAR(120),
    imagen_url       VARCHAR(500),
    imagen_public_id VARCHAR(255),
    imagen_alt       VARCHAR(255),
    orden            INT          NOT NULL DEFAULT 0,
    activo           BOOLEAN      NOT NULL DEFAULT TRUE,
    creado_en        DATETIME(6)  NOT NULL,
    actualizado_en   DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    KEY ix_miembro_del_equipo_nosotros (nosotros_id, orden),
    CONSTRAINT fk_miembro_del_equipo_nosotros
        FOREIGN KEY (nosotros_id) REFERENCES nosotros (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- Las estadisticas y los diferenciales pasan de columnas a filas. Solo se migra
-- lo que tenga valor cargado.
INSERT INTO estadistica (nosotros_id, valor, etiqueta, orden, activo, creado_en, actualizado_en)
SELECT id, stat1_value, stat1_label, 1, 1, NOW(6), NOW(6) FROM nosotros
WHERE stat1_value IS NOT NULL AND stat1_value <> '' AND stat1_label IS NOT NULL AND stat1_label <> ''
UNION ALL
SELECT id, stat2_value, stat2_label, 2, 1, NOW(6), NOW(6) FROM nosotros
WHERE stat2_value IS NOT NULL AND stat2_value <> '' AND stat2_label IS NOT NULL AND stat2_label <> ''
UNION ALL
SELECT id, stat3_value, stat3_label, 3, 1, NOW(6), NOW(6) FROM nosotros
WHERE stat3_value IS NOT NULL AND stat3_value <> '' AND stat3_label IS NOT NULL AND stat3_label <> '';

INSERT INTO diferencial (nosotros_id, titulo, descripcion, orden, activo, creado_en, actualizado_en)
SELECT id, diff1_title, diff1_desc, 1, 1, NOW(6), NOW(6) FROM nosotros
WHERE diff1_title IS NOT NULL AND diff1_title <> ''
UNION ALL
SELECT id, diff2_title, diff2_desc, 2, 1, NOW(6), NOW(6) FROM nosotros
WHERE diff2_title IS NOT NULL AND diff2_title <> ''
UNION ALL
SELECT id, diff3_title, diff3_desc, 3, 1, NOW(6), NOW(6) FROM nosotros
WHERE diff3_title IS NOT NULL AND diff3_title <> ''
UNION ALL
SELECT id, diff4_title, diff4_desc, 4, 1, NOW(6), NOW(6) FROM nosotros
WHERE diff4_title IS NOT NULL AND diff4_title <> '';

-- team_images_json guardaba solo URLs, sin nombre ni rol, asi que no alcanza para
-- armar los miembros del equipo: hay que volver a cargarlos desde el panel. El
-- contenido crudo se guarda antes de borrar la columna para no perder las fotos.
CREATE TABLE respaldo_de_team_images_json (
    id           BIGINT      NOT NULL AUTO_INCREMENT,
    nosotros_id  BIGINT      NOT NULL,
    contenido    TEXT,
    respaldado_en DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

INSERT INTO respaldo_de_team_images_json (nosotros_id, contenido, respaldado_en)
SELECT id, team_images_json, NOW(6) FROM nosotros
WHERE team_images_json IS NOT NULL AND team_images_json <> '';

ALTER TABLE nosotros
    DROP COLUMN team_images_json,
    DROP COLUMN stat1_value, DROP COLUMN stat1_label,
    DROP COLUMN stat2_value, DROP COLUMN stat2_label,
    DROP COLUMN stat3_value, DROP COLUMN stat3_label,
    DROP COLUMN diff1_title, DROP COLUMN diff1_desc,
    DROP COLUMN diff2_title, DROP COLUMN diff2_desc,
    DROP COLUMN diff3_title, DROP COLUMN diff3_desc,
    DROP COLUMN diff4_title, DROP COLUMN diff4_desc;

-- ─────────────────────────────────────────────────────────────
-- Publicaciones de Instagram
-- ─────────────────────────────────────────────────────────────
-- Solo se guarda la URL del post: el contenido lo renderiza el embed oficial de
-- Instagram. miniatura_url es la imagen de respaldo que se muestra antes de que
-- el embed cargue, y la que queda si Instagram no responde.
CREATE TABLE publicacion_de_instagram (
    id                     BIGINT       NOT NULL AUTO_INCREMENT,
    url                    VARCHAR(500) NOT NULL,
    tipo                   VARCHAR(20)  NOT NULL DEFAULT 'POST',
    titulo                 VARCHAR(255),
    miniatura_url          VARCHAR(500),
    miniatura_public_id    VARCHAR(255),
    orden                  INT          NOT NULL DEFAULT 0,
    activo                 BOOLEAN      NOT NULL DEFAULT TRUE,
    creado_en              DATETIME(6)  NOT NULL,
    actualizado_en         DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_publicacion_de_instagram_url (url),
    KEY ix_publicacion_de_instagram_orden (orden)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ─────────────────────────────────────────────────────────────
-- Informacion de contacto
-- ─────────────────────────────────────────────────────────────
-- Estos datos vivian hardcodeados en el frontend (constants/data.ts). Con dos
-- fuentes de verdad, cambiar un telefono era un deploy.
CREATE TABLE informacion_de_contacto (
    id                   BIGINT       NOT NULL AUTO_INCREMENT,
    whatsapp             VARCHAR(30)  NOT NULL,
    whatsapp_para_mostrar VARCHAR(40),
    instagram            VARCHAR(60),
    direccion            VARCHAR(255),
    url_de_maps          VARCHAR(500),
    embed_de_maps        TEXT,
    creado_en            DATETIME(6)  NOT NULL,
    actualizado_en       DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
