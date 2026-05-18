package com.amachi.app.vitalia.boot.config.seed;

import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.enums.BedStatusEnum;
import com.amachi.app.core.common.enums.RoomTypeEnum;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.domain.tenant.repository.TenantRepository;
import com.amachi.app.vitalia.medicalcore.common.enums.CleaningStatus;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Bed;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.DepartmentUnit;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Room;
import com.amachi.app.vitalia.medicalcore.infrastructure.repository.BedRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.repository.DepartmentUnitRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Siembra la infraestructura física del hospital: Unidades, Habitaciones y Camas.
 * Debe ejecutarse primero porque las admisiones dependen de estos recursos.
 *
 * Datos sembrados:
 *   UCI          → 2 habitaciones, 4 camas
 *   MED-INT      → 3 habitaciones, 7 camas
 *   PED          → 2 habitaciones, 4 camas
 *   CIR          → 2 habitaciones, 5 camas
 */
@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevInfrastructureSeeder {

    private static final String TENANT_CODE = "hospital-san-borja";

    private final TenantRepository         tenantRepository;
    private final DepartmentUnitRepository unitRepository;
    private final RoomRepository           roomRepository;
    private final BedRepository            bedRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    @Order(10)
    public void seed() {
        log.info("🏥 [INFRA-SEEDER] Verificando infraestructura hospitalaria...");

        Tenant tenant = tenantRepository.findByCode(TENANT_CODE).orElse(null);
        if (tenant == null) {
            log.warn("⚠️ [INFRA-SEEDER] Tenant '{}' no encontrado.", TENANT_CODE);
            return;
        }

        TenantContext.setTenantId(tenant.getId());
        TenantContext.setTenantCode(tenant.getCode());

        try {
            if (unitRepository.existsByCodeAndTenantId("UCI", tenant.getId())) {
                log.info("⏭️  [INFRA-SEEDER] Infraestructura ya existe. Omitido.");
                return;
            }

            seedUci();
            seedMedicinaInterna();
            seedPediatria();
            seedCirugia();

            log.info("✅ [INFRA-SEEDER] 4 unidades, 9 habitaciones, 20 camas creadas.");

        } catch (Exception ex) {
            log.error("❌ [INFRA-SEEDER] Error: {}", ex.getMessage(), ex);
        } finally {
            TenantContext.clear();
        }
    }

    // ── UCI ───────────────────────────────────────────────────────────────────

    private void seedUci() {
        DepartmentUnit uci = saveUnit("Unidad de Cuidados Intensivos", "UCI", "3",
                "UCI-CC-01", 8, "Atención crítica con monitoreo continuo 24/7. Ventilación mecánica.");

        Room r301 = saveRoom(uci, "301", 3, "A", RoomTypeEnum.UCI, false,
                "2 camas de aislamiento individual con ventilador.");
        Room r302 = saveRoom(uci, "302", 3, "A", RoomTypeEnum.UCI, false,
                "2 camas de monitoreo multiparamétrico.");

        saveBed(r301, "A", "UCI-301-A", BedStatusEnum.AVAILABLE,   false, "Ventilador Maquet Servo-U");
        saveBed(r301, "B", "UCI-301-B", BedStatusEnum.OCCUPIED,    true,  "Ventilador Mindray SV800 — paciente crítico");
        saveBed(r302, "A", "UCI-302-A", BedStatusEnum.AVAILABLE,   false, "Monitor Philips IntelliVue MX800");
        saveBed(r302, "B", "UCI-302-B", BedStatusEnum.MAINTENANCE, false, "En mantenimiento preventivo programado");
    }

    // ── Medicina Interna ──────────────────────────────────────────────────────

    private void seedMedicinaInterna() {
        DepartmentUnit mi = saveUnit("Medicina Interna", "MED-INT", "2",
                "MEDINT-CC-02", 24, "Hospitalización adultos: cardiología, neumología, gastroenterología.");

        Room r201 = saveRoom(mi, "201", 2, "B", RoomTypeEnum.ESTANDAR, false,
                "3 camas — Bloque B, Piso 2.");
        Room r202 = saveRoom(mi, "202", 2, "B", RoomTypeEnum.ESTANDAR, false,
                "3 camas — Bloque B, Piso 2.");
        Room r203 = saveRoom(mi, "203", 2, "B", RoomTypeEnum.SUITE, true,
                "Suite privada — seguro complementario.");

        saveBed(r201, "A", "MI-201-A", BedStatusEnum.OCCUPIED,  true,  "Paciente hospitalizado");
        saveBed(r201, "B", "MI-201-B", BedStatusEnum.AVAILABLE, false, "Disponible");
        saveBed(r201, "C", "MI-201-C", BedStatusEnum.AVAILABLE, false, "Disponible");
        saveBed(r202, "A", "MI-202-A", BedStatusEnum.OCCUPIED,  true,  "Paciente hospitalizado");
        saveBed(r202, "B", "MI-202-B", BedStatusEnum.OCCUPIED,  true,  "Paciente hospitalizado");
        saveBed(r202, "C", "MI-202-C", BedStatusEnum.AVAILABLE, false, "Disponible");
        saveBed(r203, "1", "MI-203-1", BedStatusEnum.RESERVED,  false, "Reservada — ingreso programado");
    }

    // ── Pediatría ─────────────────────────────────────────────────────────────

    private void seedPediatria() {
        DepartmentUnit ped = saveUnit("Pediatría", "PED", "1",
                "PED-CC-03", 16, "Hospitalización pediátrica 0-14 años. Neonatología.");

        Room r101 = saveRoom(ped, "101", 1, "C", RoomTypeEnum.ESTANDAR, false,
                "2 cunas/camas — acompañante permitido.");
        Room r102 = saveRoom(ped, "102", 1, "C", RoomTypeEnum.ESTANDAR, false,
                "2 cunas/camas adaptadas para pediatría.");

        saveBed(r101, "A", "PED-101-A", BedStatusEnum.OCCUPIED,  true,  "Paciente pediátrico hospitalizado");
        saveBed(r101, "B", "PED-101-B", BedStatusEnum.AVAILABLE, false, "Disponible");
        saveBed(r102, "A", "PED-102-A", BedStatusEnum.AVAILABLE, false, "Disponible");
        saveBed(r102, "B", "PED-102-B", BedStatusEnum.AVAILABLE, false, "Disponible");
    }

    // ── Cirugía ───────────────────────────────────────────────────────────────

    private void seedCirugia() {
        DepartmentUnit cir = saveUnit("Cirugía General", "CIR", "2",
                "CIR-CC-04", 12, "Pre-operatorio y recuperación post-anestésica.");

        Room rPre = saveRoom(cir, "PRE-01", 2, "D", RoomTypeEnum.PRE_OPERATORY, false,
                "2 camillas de preparación pre-operatoria.");
        Room rRec = saveRoom(cir, "REC-01", 2, "D", RoomTypeEnum.RECOVERY, false,
                "3 camas de recuperación post-anestésica.");

        saveBed(rPre, "A", "CIR-PRE-01-A", BedStatusEnum.AVAILABLE, false, "Camilla A — pre-operatorio");
        saveBed(rPre, "B", "CIR-PRE-01-B", BedStatusEnum.AVAILABLE, false, "Camilla B — pre-operatorio");
        saveBed(rRec, "A", "CIR-REC-01-A", BedStatusEnum.OCCUPIED,  true,  "Recuperación post-quirúrgica");
        saveBed(rRec, "B", "CIR-REC-01-B", BedStatusEnum.AVAILABLE, false, "Disponible");
        saveBed(rRec, "C", "CIR-REC-01-C", BedStatusEnum.AVAILABLE, false, "Disponible");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private DepartmentUnit saveUnit(String name, String code, String floor,
                                    String costCenter, int maxCapacity, String description) {
        return unitRepository.save(DepartmentUnit.builder()
                .name(name).code(code).floor(floor).costCenter(costCenter)
                .maxCapacity(maxCapacity).isClinical(true).active(true)
                .description(description).build());
    }

    private Room saveRoom(DepartmentUnit unit, String number, int blockFloor,
                          String blockCode, RoomTypeEnum type, boolean isPrivate, String description) {
        return roomRepository.save(Room.builder()
                .unit(unit).roomNumber(number).blockFloor(blockFloor).blockCode(blockCode)
                .roomType(type).privateRoom(isPrivate).cleaningStatus(CleaningStatus.CLEAN)
                .active(true).description(description).build());
    }

    private void saveBed(Room room, String bedNumber, String bedCode,
                         BedStatusEnum status, boolean isOccupied, String description) {
        bedRepository.save(Bed.builder()
                .room(room).bedNumber(bedNumber).bedCode(bedCode)
                .status(status).isOccupied(isOccupied).active(true)
                .description(description).build());
    }
}
