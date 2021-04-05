package com.smads.covs.devolutivas_relatorio.Model;

public class SasQuestions {

    private String question;
    private String type;
    private String question_order;

    public SasQuestions() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion_order() {
        return question_order;
    }

    public void setQuestion_order(String question_order) {
        this.question_order = question_order;
    }

    @Override
    public String toString() {
        return "SasQuestions{" +
                "question='" + question + '\'' +
                ", type='" + type + '\'' +
                ", question_order='" + question_order + '\'' +
                '}';
    }
}
