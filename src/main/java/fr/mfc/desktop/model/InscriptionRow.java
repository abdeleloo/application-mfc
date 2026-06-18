package fr.mfc.desktop.model;

public class InscriptionRow {
    private String numeroInscription;
    private String dateInscription;
    private String formation;
    private String session;
    private String dateDebut;
    private String dateFin;
    private String lieu;

    public String getNumeroInscription() {
        return numeroInscription;
    }

    public void setNumeroInscription(String numeroInscription) {
        this.numeroInscription = numeroInscription;
    }

    public String getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(String dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }
}

