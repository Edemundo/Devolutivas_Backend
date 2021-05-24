package com.smads.covs.devolutivas_relatorio.Model;

public class SasServices {

    private String token;
    private String firstname;
    private String typology;
    private String questionGroupId;
    private String district;
    private String protection;
    private String term;
    private String position;
    private String email;
    private String completed;


    public SasServices() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getTypology() {
        return typology;
    }

    public void setTypology(String typology) {
        this.typology = typology;
    }

    public String getQuestionGroupId() {
        return questionGroupId;
    }

    public void setQuestionGroupId(String qGroupId) {
        this.questionGroupId = qGroupId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProtection() {
        return protection;
    }

    public void setProtection(String protection) {
        this.protection = protection;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getPosition(){
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getCompleted() { return completed; }

    public void setCompleted(String completed) { this.completed = completed; }

    @Override
    public String toString() {
        return "SasServices{" +
                "token='" + token + '\'' +
                ", firstname='" + firstname + '\'' +
                ", email='" + email + '\'' +
                ", typology='" + typology + '\'' +
                ", questionGroupId='" + questionGroupId + '\'' +
                ", district='" + district + '\'' +
                ", protection='" + protection + '\'' +
                ", term='" + term + '\'' +
                ", position='" + position + '\'' +
                ", completed='" + completed + '\'' +
                '}';
    }
}
