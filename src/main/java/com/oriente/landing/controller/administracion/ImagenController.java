package com.oriente.landing.controller.administracion;

import com.oriente.landing.dto.administracion.imagen.FirmaDeUploadResponse;
import com.oriente.landing.service.administracion.imagen.FirmaDeUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Firma los uploads del panel.
 *
 * El archivo no pasa por esta aplicacion: el panel lo manda directo a Cloudinary
 * con la firma que se emite aca. Asi no hay que dimensionar el servidor para el
 * peso de las imagenes, y el api_secret nunca sale del backend.
 */
@RestController
@RequestMapping("/api/admin/v1/imagenes")
public class ImagenController {

    private final FirmaDeUploadService firmaService;

    public ImagenController(FirmaDeUploadService firmaService) {
        this.firmaService = firmaService;
    }

    @PostMapping("/firma")
    public ResponseEntity<FirmaDeUploadResponse> firmar() {
        return ResponseEntity.ok(firmaService.generarFirma());
    }
}
