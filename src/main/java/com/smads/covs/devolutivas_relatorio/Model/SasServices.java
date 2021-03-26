package com.smads.covs.devolutivas_relatorio.Model;

public class SasServices {

    private String token;
    private String firstname;
    private String attribute_4;

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

    public String getAttribute_4() {
        return attribute_4;
    }

    public void setAttribute_4(String attribute_4) {
        this.attribute_4 = attribute_4;
    }

    @Override
    public String toString() {
        return "SasServices{" +
                "token='" + token + '\'' +
                ", firstname='" + firstname + '\'' +
                ", attribute_4='" + attribute_4 + '\'' +
                '}';
    }
}
