package fr.mfc.desktop.model;

public class Stagiaire {
    private String codeStagiaire;
    private String nom;
    private String prenom;
    private String email;
    private String tel;
    private String ville;
    private String societe;

    public String getCodeStagiaire() {
        return codeStagiaire;
    }

    public void setCodeStagiaire(String codeStagiaire) {
        this.codeStagiaire = codeStagiaire;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getSociete() {
        return societe;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }
}

