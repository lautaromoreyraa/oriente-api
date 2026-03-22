package com.oriente.landing.config;

import com.oriente.landing.domain.entity.Combo;
import com.oriente.landing.domain.entity.ComboItem;
import com.oriente.landing.domain.entity.Service;
import com.oriente.landing.domain.enums.ServiceCategory;
import com.oriente.landing.repository.ComboRepository;
import com.oriente.landing.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ServiceRepository serviceRepository;
    private final ComboRepository comboRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeServices();
        initializeCombos();
    }

    private void initializeServices() {
        if (serviceRepository.count() > 0) {
            return;
        }

        List<Service> services = Arrays.asList(
                Service.builder()
                        .slug("kinesiologia")
                        .category(ServiceCategory.KINE)
                        .title("Kinesiología")
                        .description("Rehabilitación funcional y recuperación post-lesión. Tratamientos personalizados para volver al movimiento.")
                        .displayOrder(1)
                        .active(true)
                        .build(),
                Service.builder()
                        .slug("acupuntura")
                        .category(ServiceCategory.KINE)
                        .title("Acupuntura")
                        .description("Técnica milenaria de medicina tradicional china. Estimulación de puntos energéticos para aliviar el dolor, reducir el estrés y restablecer el equilibrio del organismo.")
                        .displayOrder(2)
                        .active(true)
                        .build(),
                Service.builder()
                        .slug("masajes")
                        .category(ServiceCategory.KINE)
                        .title("Masajes")
                        .description("Masajes terapéuticos y de relajación profunda. Técnicas especializadas para liberar tensiones y restaurar el equilibrio.")
                        .displayOrder(3)
                        .active(true)
                        .build(),
                Service.builder()
                        .slug("gimnasia-adaptada")
                        .category(ServiceCategory.KINE)
                        .title("Gimnasia Adaptada")
                        .description("Clases grupales de movimiento suave y fortalecimiento, diseñadas para todas las edades.")
                        .displayOrder(4)
                        .active(true)
                        .build(),
                Service.builder()
                        .slug("estetica")
                        .category(ServiceCategory.ESTETICA)
                        .title("Medicina Estética")
                        .description("Procedimientos faciales y corporales no invasivos con resultados visibles.")
                        .displayOrder(5)
                        .active(true)
                        .build(),
                Service.builder()
                        .slug("cosmiatra")
                        .category(ServiceCategory.ESTETICA)
                        .title("Cosmiatría")
                        .description("Cuidado profesional de la piel para tratar, mejorar y mantener su salud.")
                        .displayOrder(6)
                        .active(true)
                        .build(),
                Service.builder()
                        .slug("bronceado")
                        .category(ServiceCategory.ESTETICA)
                        .title("Bronceado Cannelle")
                        .description("Bronceado premium de la línea Cannelle. Resultados naturales, duraderos y sin exposición solar.")
                        .displayOrder(7)
                        .active(true)
                        .build(),
                Service.builder()
                        .slug("makeup")
                        .category(ServiceCategory.ESTETICA)
                        .title("Makeup")
                        .description("Maquillaje artístico y profesional para eventos, producciones y ocasiones especiales.")
                        .displayOrder(8)
                        .active(true)
                        .build(),
                Service.builder()
                        .slug("depilacion")
                        .category(ServiceCategory.ESTETICA)
                        .title("Depilación Definitiva")
                        .description("Métodos de depilación definitiva. Soluciones para todos los fototipos y zonas del cuerpo.")
                        .displayOrder(9)
                        .active(true)
                        .build()
        );

        serviceRepository.saveAll(services);
    }

    private void initializeCombos() {
        if (comboRepository.count() > 0) {
            return;
        }

        List<ComboItem> items1 = Arrays.asList(
                ComboItem.builder()
                        .description("Sesión de Kinesiología")
                        .displayOrder(1)
                        .build(),
                ComboItem.builder()
                        .description("Hidratación facial profunda")
                        .displayOrder(2)
                        .build(),
                ComboItem.builder()
                        .description("Masaje de cierre relajante")
                        .displayOrder(3)
                        .build()
        );

        Combo combo1 = Combo.builder()
                .slug("combo-restauracion")
                .title("Restauración Profunda")
                .tagline("Cuerpo y piel en una sola visita")
                .description("Combinamos técnica kinesiológica y cuidado estético para quienes necesitan recuperarse por dentro y verse bien por fuera.")
                .badge("Más elegido")
                .displayOrder(1)
                .active(true)
                .items(new ArrayList<>())
                .build();
        items1.forEach(item -> item.setCombo(combo1));
        combo1.setItems(items1);

        List<ComboItem> items2 = Arrays.asList(
                ComboItem.builder()
                        .description("Bronceado Cannelle")
                        .displayOrder(1)
                        .build(),
                ComboItem.builder()
                        .description("Sesión de Cosmiatría")
                        .displayOrder(2)
                        .build(),
                ComboItem.builder()
                        .description("Maquillaje profesional")
                        .displayOrder(3)
                        .build()
        );

        Combo combo2 = Combo.builder()
                .slug("combo-glow")
                .title("Oriente Glow")
                .tagline("Transformación estética completa")
                .description("El combo de imagen más completo del consultorio.")
                .badge("Nuevo")
                .displayOrder(2)
                .active(true)
                .items(new ArrayList<>())
                .build();
        items2.forEach(item -> item.setCombo(combo2));
        combo2.setItems(items2);

        List<ComboItem> items3 = Arrays.asList(
                ComboItem.builder()
                        .description("Masaje descontracturante")
                        .displayOrder(1)
                        .build(),
                ComboItem.builder()
                        .description("Tratamiento corporal hidratante")
                        .displayOrder(2)
                        .build(),
                ComboItem.builder()
                        .description("Reflexología")
                        .displayOrder(3)
                        .build()
        );

        Combo combo3 = Combo.builder()
                .slug("combo-detox")
                .title("Detox & Bienestar")
                .tagline("Reset para tu mente y tu cuerpo")
                .description("Una experiencia de descanso activo para liberar tensiones.")
                .displayOrder(3)
                .active(true)
                .items(new ArrayList<>())
                .build();
        items3.forEach(item -> item.setCombo(combo3));
        combo3.setItems(items3);

        List<ComboItem> items4 = Arrays.asList(
                ComboItem.builder()
                        .description("Diagnóstico cutáneo personalizado")
                        .displayOrder(1)
                        .build(),
                ComboItem.builder()
                        .description("Tratamiento según tipo de piel")
                        .displayOrder(2)
                        .build(),
                ComboItem.builder()
                        .description("Depilación facial de precisión")
                        .displayOrder(3)
                        .build()
        );

        Combo combo4 = Combo.builder()
                .slug("combo-piel")
                .title("Programa Piel")
                .tagline("Resultados visibles desde la primera sesión")
                .description("Protocolo intensivo de cosmiatría diseñado para tratar, unificar y luminizar la piel del rostro.")
                .displayOrder(4)
                .active(true)
                .items(new ArrayList<>())
                .build();
        items4.forEach(item -> item.setCombo(combo4));
        combo4.setItems(items4);

        comboRepository.saveAll(Arrays.asList(combo1, combo2, combo3, combo4));
    }
}
