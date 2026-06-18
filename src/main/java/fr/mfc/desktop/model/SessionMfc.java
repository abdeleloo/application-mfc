package fr.mfc.desktop.model;

public class SessionMfc {
    private String numeroSession;
    private String codeFormation;
    private String dateDebut;
    private String dateFin;
    private String lieu;
    private String salle;
    private Integer placesTotal;
    private Integer placesRestantes;

    public String getNumeroSession() {
        return numeroSession;
    }

    public void setNumeroSession(String numeroSession) {
        this.numeroSession = numeroSession;
    }

    public String getCodeFormation() {
        return codeFormation;
    }

    public void setCodeFormation(String codeFormation) {
        this.codeFormation = codeFormation;
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

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public Integer getPlacesTotal() {
        return placesTotal;
    }

    public void setPlacesTotal(Integer placesTotal) {
        this.placesTotal = placesTotal;
    }

    public Integer getPlacesRestantes() {
        return placesRestantes;
    }

    public void setPlacesRestantes(Integer placesRestantes) {
        this.placesRestantes = placesRestantes;
    }
}
