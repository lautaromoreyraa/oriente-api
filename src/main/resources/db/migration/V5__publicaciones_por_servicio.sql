-- Las publicaciones de Instagram dejan de ser una seccion propia de la landing:
-- pasan a ilustrar cada servicio, en el cuadro de su tarjeta.
--
-- Una publicacion sin servicio no tiene donde mostrarse, asi que la columna es
-- obligatoria. En produccion la tabla esta vacia; lo que haya en una base de
-- desarrollo se descarta y se vuelve a cargar desde el panel.
DELETE FROM publicacion_de_instagram;

ALTER TABLE publicacion_de_instagram
    DROP INDEX ix_publicacion_de_instagram_orden,
    ADD COLUMN servicio_id BIGINT NOT NULL AFTER id,
    ADD KEY ix_publicacion_de_instagram_servicio (servicio_id, orden),
    ADD CONSTRAINT fk_publicacion_de_instagram_servicio
        FOREIGN KEY (servicio_id) REFERENCES servicio (id) ON DELETE CASCADE;
