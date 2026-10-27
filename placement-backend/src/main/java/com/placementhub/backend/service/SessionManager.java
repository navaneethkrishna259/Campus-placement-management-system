package com.placementhub.backend.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory secure token session manager.
 * Associates session tokens with authenticated users (ID, Role, Email).
 */
public class SessionManager {

    public static class SessionData {
        private final Long userId;
        private final String role; // "STUDENT" or "COMPANY"
        private final String email;

        public SessionData(Long userId, String role, String email) {
            this.userId = userId;
            this.role = role;
            this.email = email;
        }

        public Long getUserId() {
            return userId;
        }

        public String getRole() {
            return role;
        }

        public String getEmail() {
            return email;
        }
    }

    private static final Map<String, SessionData> activeSessions = new ConcurrentHashMap<>();

    public static String createSession(Long userId, String role, String email) {
        String token = UUID.randomUUID().toString();
        activeSessions.put(token, new SessionData(userId, role, email));
        return token;
    }

    public static SessionData getSession(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        return activeSessions.get(token);
    }

    public static void invalidateSession(String token) {
        if (token != null) {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7).trim();
            }
            activeSessions.remove(token);
        }
    }
}
