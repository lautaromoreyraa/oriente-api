-- Estado del esquema tal como lo dejo el ddl-auto: update anterior.
--
-- En produccion estas tablas ya existen: con baseline-on-migrate, Flyway marca
-- esta version como aplicada sin ejecutarla. En una base vacia (local, CI) si se
-- ejecuta, para que V2 encuentre el mismo punto de partida en los dos lados.

CREATE TABLE hero_section (
    id                   BIGINT       NOT NULL AUTO_INCREMENT,
    title                VARCHAR(255) NOT NULL,
    subtitle             VARCHAR(255),
    cta_text             VARCHAR(255),
    cta_url              VARCHAR(255),
    background_image_url VARCHAR(255),
    active               BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at           DATETIME(6)  NOT NULL,
    updated_at           DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE service (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    slug          VARCHAR(255) NOT NULL,
    category      VARCHAR(255) NOT NULL,
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    icon_path     TEXT,
    display_order INT                   DEFAULT 0,
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    DATETIME(6)  NOT NULL,
    updated_at    DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_service_slug (slug)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE about_section (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    heading          VARCHAR(255) NOT NULL,
    body             TEXT,
    image_url        VARCHAR(255),
    team_images_json TEXT,
    active           BOOLEAN      NOT NULL DEFAULT TRUE,
    stat1_value      VARCHAR(20),
    stat1_label      VARCHAR(60),
    stat2_value      VARCHAR(20),
    stat2_label      VARCHAR(60),
    stat3_value      VARCHAR(20),
    stat3_label      VARCHAR(60),
    diff1_title      VARCHAR(120),
    diff1_desc       TEXT,
    diff2_title      VARCHAR(120),
    diff2_desc       TEXT,
    diff3_title      VARCHAR(120),
    diff3_desc       TEXT,
    diff4_title      VARCHAR(120),
    diff4_desc       TEXT,
    created_at       DATETIME(6)  NOT NULL,
    updated_at       DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE combo (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    slug          VARCHAR(255) NOT NULL,
    title         VARCHAR(255) NOT NULL,
    tagline       VARCHAR(255),
    description   TEXT,
    badge         VARCHAR(255),
    display_order INT                   DEFAULT 0,
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    DATETIME(6)  NOT NULL,
    updated_at    DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_combo_slug (slug)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE combo_item (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    description   VARCHAR(255) NOT NULL,
    display_order INT                   DEFAULT 0,
    combo_id      BIGINT       NOT NULL,
    created_at    DATETIME(6)  NOT NULL,
    updated_at    DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_combo_item_combo FOREIGN KEY (combo_id) REFERENCES combo (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
