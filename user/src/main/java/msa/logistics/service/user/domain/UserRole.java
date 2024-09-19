package msa.logistics.service.user.domain;

public enum UserRole {

    MASTER("MASTER"),   // 마스터 권한
    HUB_ADMIN("HUB_ADMIN"), // 허브 관리자 권한
    HUB_DELIVERY_MANAGER("HUB_DELIVERY_MANAGER"),   // 허브 배송 담당자 권한
    VENDOR_MANAGER("VENDOR_MANAGER");   // 업체 권한

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static UserRole fromString(String role) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.getAuthority().equalsIgnoreCase(role)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + role);
    }
}
