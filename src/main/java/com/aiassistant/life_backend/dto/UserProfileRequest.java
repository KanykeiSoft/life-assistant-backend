package com.aiassistant.life_backend.dto;

public class UserProfileRequest {
    private Integer age;
    private Integer sleepHours;
    private String goal;
    private String assistantStyle;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSleepHours() {
        return sleepHours;
    }

    public void setSleepHours(Integer sleepHours) {
        this.sleepHours = sleepHours;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getAssistantStyle() {
        return assistantStyle;
    }

    public void setAssistantStyle(String assistantStyle) {
        this.assistantStyle = assistantStyle;
    }


}
