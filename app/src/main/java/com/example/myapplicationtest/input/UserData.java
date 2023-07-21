package com.example.myapplicationtest.input;

public class UserData {
    String name;
    String password;
    String group;
    String email;
    boolean  soundButtons;
    int dailyGoal;
    boolean  sendingMessage;
    String   sendingTime;

public UserData(String name, String email,String password,  String group) {
        this.name = name;
        this.group = group;
        this.email = email;
        this.password = password;
        this.soundButtons = true;
        this.sendingMessage = true;
        this.sendingTime = "15:00";
        this.dailyGoal = 20;
        }

    public boolean isSoundButtons() {
        return soundButtons;
    }

    public int getDailyGoal() {
        return dailyGoal;
    }

    public boolean isSendingMessage() {
        return sendingMessage;
    }

    public String getSendingTime() {
        return sendingTime;
    }

    public void setSoundButtons(boolean soundButtons) {
        this.soundButtons = soundButtons;
    }

    public void setDailyGoal(int dailyGoal) {
        this.dailyGoal = dailyGoal;
    }

    public void setSendingMessage(boolean sendingMessage) {
        this.sendingMessage = sendingMessage;
    }

    public void setSendingTime(String sendingTime) {
        this.sendingTime = sendingTime;
    }

    public String getName() {
                return name;
        }

    public void setName(String name) {
                this.name = name;
        }

    public String getPassword() {
                return password;
        }

    public void setPassword(String password) {
                this.password = password;
        }

    public String getGroup() {
                return group;
        }

    public void setGroup(String group) {
                this.group = group;
        }

    public String getEmail() {
                return email;
        }

    public void setEmail(String email) {
                this.email = email;
        }

 }
