package com.smads.covs.devolutivas_relatorio.Model;

public class SasQuestions {

    private String question;
    private String type;
    private String questionOrder;
    private String questionId;
    private String parentQuestionId;

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

    public String getQuestionOrder() {
        return questionOrder;
    }

    public void setQuestionOrder(String questionOrder) {
        this.questionOrder = questionOrder;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getParentQuestionId() {
        return parentQuestionId;
    }

    public void setParentQuestionId(String parentQuestionId) {
        this.parentQuestionId = parentQuestionId;
    }


    @Override
    public String toString() {
        return "SasQuestions{" +
                "question='" + question + '\'' +
                ", type='" + type + '\'' +
                ", questionOrder='" + questionOrder + '\'' +
                ", questionId='" + questionId + '\'' +
                ", parentQuestionId='" + parentQuestionId + '\'' +
                '}';
    }
}
