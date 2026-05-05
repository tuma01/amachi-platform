package com.amachi.app.core.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import com.amachi.app.core.common.enums.TenantType;

@Configuration
@ConfigurationProperties(prefix = "app.bootstrap")
@Getter
@Setter
public class AppBootstrapProperties {
    private TenantProperties tenant;
    private AdminProperties superAdmin;
    private AdminProperties tenantAdmin;

    @Getter
    @Setter
    public static class TenantProperties {
        private TenantGlobal tenantGlobal;
        private TenantLocal tenantLocal;
    }

    public interface BootstrapTenantConfig {
        String getCode();
        String getName();
        String getDescription();
        TenantType getTenantType();
        
        default String getLegalName() { return null; }
        default String getTaxId() { return null; }
        default String getMedicalLicense() { return null; }
        default AddressProperties getAddress() { return null; }
    }

    @Getter
    @Setter
    public static class TenantGlobal implements BootstrapTenantConfig {
        private String code;
        private String name;
        private String type;
        private String description;

        @Override
        public TenantType getTenantType() {
            return TenantType.GLOBAL;
        }
    }

    @Getter
    @Setter
    public static class TenantLocal implements BootstrapTenantConfig {
        private String code;
        private String name;
        private String type;
        private String legalName;
        private String taxId;
        private String medicalLicense;
        private String fallbackHeader;
        private boolean allowLocal = true;
        private String defaultDomain = "vitalia.local";
        private AddressProperties address;

        @Override
        public TenantType getTenantType() {
            return TenantType.HOSPITAL;
        }

        @Override
        public String getDescription() {
            return null;
        }
    }

    @Getter
    @Setter
    public static class AddressProperties {
        private String streetNumber;
        private String streetName;
        private String city;
        private Long stateId;
        private Long provinceId;
        private Long municipalityId;
        private Long countryId;
        private String phone;
    }

    @Getter
    @Setter
    public static class AdminProperties {
        private String email;
        private String password;
        private String firstName;
        private String lastName;
    }
}
