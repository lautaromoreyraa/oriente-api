-- Video propio en las publicaciones de Instagram.
--
-- La landing dejo de usar el embed oficial: ahora muestra el contenido en grande y
-- con el diseno del sitio. Para que un reel se vea moviendose y no como una foto
-- quieta, el video se sube junto con la publicacion.
--
-- La miniatura no se va: pasa a ser el cuadro que se muestra mientras el video
-- carga, y lo que queda cuando el visitante pidio no ver cosas en movimiento.

ALTER TABLE publicacion_de_instagram
    ADD COLUMN video_url       VARCHAR(500) AFTER miniatura_public_id,
    ADD COLUMN video_public_id VARCHAR(255) AFTER video_url;
