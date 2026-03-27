package kg.creativemap.api.entity;

import java.util.List;
import java.util.Map;

public enum Role {
    USER,
    FOREIGN_USER,
    ADMIN,
    CONTENT_MAKER,
    SUPER_ADMIN;

    private static final Map<Role, Integer> HIERARCHY = Map.of(
            USER, 0,
            FOREIGN_USER, 0,
            ADMIN, 1,
            CONTENT_MAKER, 2,
            SUPER_ADMIN, 3
    );

    public int level() {
        return HIERARCHY.getOrDefault(this, 0);
    }

    public boolean isAbove(Role other) {
        return this.level() > other.level();
    }

    public boolean isAboveOrEqual(Role other) {
        return this.level() >= other.level();
    }

    /**
     * Язык по умолчанию для роли.
     * FOREIGN_USER — английский, остальные — русский.
     */
    public String getDefaultLanguage() {
        return this == FOREIGN_USER ? "en" : "ru";
    }

    /**
     * Список прав (permissions) для роли.
     */
    public List<String> getPermissions() {
        return switch (this) {
            case SUPER_ADMIN -> List.of(
                    "MANAGE_USERS", "MANAGE_ROLES", "MANAGE_CONTENT", "PUBLISH_CONTENT",
                    "DELETE_CONTENT", "VIEW_ANALYTICS", "MANAGE_SETTINGS", "MODERATE_CONTENT",
                    "VIEW_AUDIT_LOG", "MANAGE_VR_TOURS", "MANAGE_EVENTS", "MANAGE_OBJECTS",
                    "VIEW_REPORTS", "EXPORT_DATA", "VIEW_CONTENT", "USE_FAVOURITES",
                    "VIEW_HISTORY", "LEAVE_REVIEW"
            );
            case CONTENT_MAKER -> List.of(
                    "MANAGE_CONTENT", "MANAGE_VR_TOURS", "MANAGE_EVENTS", "MANAGE_OBJECTS",
                    "VIEW_REPORTS", "VIEW_CONTENT", "USE_FAVOURITES", "VIEW_HISTORY", "LEAVE_REVIEW"
            );
            case ADMIN -> List.of(
                    "MANAGE_USERS", "PUBLISH_CONTENT", "DELETE_CONTENT", "VIEW_ANALYTICS",
                    "MODERATE_CONTENT", "VIEW_AUDIT_LOG", "VIEW_REPORTS", "EXPORT_DATA",
                    "VIEW_CONTENT", "USE_FAVOURITES", "VIEW_HISTORY", "LEAVE_REVIEW"
            );
            case USER, FOREIGN_USER -> List.of(
                    "VIEW_CONTENT", "USE_FAVOURITES", "VIEW_HISTORY", "LEAVE_REVIEW"
            );
        };
    }
}
