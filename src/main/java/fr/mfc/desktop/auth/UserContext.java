package fr.mfc.desktop.auth;

public class UserContext {
    public enum Role {
        STAGIAIRE,
        FORMATEUR,
        ADMIN
    }

    private final Role role;
    private final String code;
    private final String nom;
    private final String prenom;
    private final String email;

    public UserContext(Role role, String code, String nom, String prenom, String email) {
        this.role = role;
        this.code = code;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public String getCode() {
        return code;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public boolean canEdit() {
        return role == Role.ADMIN;
    }

    public String displayName() {
        String full = ((prenom == null ? "" : prenom) + " " + (nom == null ? "" : nom)).trim();
        return full.isEmpty() ? (code == null ? "" : code) : full;
    }
}

