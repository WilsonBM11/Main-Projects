package dev.hotel_service.session;

import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.exceptions.ErrorCodes;

import java.util.List;
import java.util.UUID;

public class SessionContextHolder {

    //generate thread variable
    private static final ThreadLocal<Session> contextHolder = new ThreadLocal<>();

    public static void setSession(Session session) {
        if (contextHolder.get() != null) {
            throw new IllegalStateException("Session already set");
        }
        contextHolder.set(session);
    }

    public static Session getSession() {
        return contextHolder.get();
    }

    public static void clearSession() {
        contextHolder.remove();
    }

    public void validateUserAdmin(List<String> roles) {
        boolean hasAdminRole = roles.stream()
                .anyMatch(role -> role.equals("ADMIN"));
        if (!hasAdminRole){
            throw new BusinessException("The user does not have permissions", ErrorCodes.UNAUTHORIZED);
        }
    }

    public UUID getActualUser(){
        // Recuperar id del usuario
        Session session = SessionContextHolder.getSession();
        String user = session.getId();

        UUID userId;
        if (user == null || user.isEmpty()) {
            throw new BusinessException("User ID cannot be null or empty.", ErrorCodes.NULL_DATA);
        }

        try {
            userId = UUID.fromString(user);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Invalid User ID format.", ErrorCodes.INVALID_DATA);
        }

        return userId;
    }

}
