package com.placementhub.student.util;

import java.util.prefs.Preferences;

public class CredentialStorage {
    private static final String KEY_REMEMBERED_EMAIL = "student_remembered_email";
    private static final Preferences prefs = Preferences.userNodeForPackage(CredentialStorage.class);

    public static void saveEmail(String email) {
        prefs.put(KEY_REMEMBERED_EMAIL, email);
    }

    public static String getSavedEmail() {
        return prefs.get(KEY_REMEMBERED_EMAIL, "");
    }

    public static void clearEmail() {
        prefs.remove(KEY_REMEMBERED_EMAIL);
    }
}
