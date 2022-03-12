package com.github.locxter.pmdrtmr.backend.lib;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

// User class
@Entity
@Table(name = "users")
public class User {
    // Attributes
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "work_duration")
    private int workDuration;
    @Column(name = "short_break_duration")
    private int shortBreakDuration;
    @Column(name = "long_break_duration")
    private int longBreakDuration;
    @Column(name = "long_break_ratio")
    private int longBreakRatio;
    @Column(name = "caldav_address")
    private String caldavAddress;

    // Constructors
    private User() {
    }

    public User(String username, String password, int workDuration, int shortBreakDuration, int longBreakDuration, int longBreakRatio, String caldavAddress) {
        this.username = username;
        this.password = password;
        this.workDuration = workDuration;
        this.shortBreakDuration = shortBreakDuration;
        this.longBreakDuration = longBreakDuration;
        this.longBreakRatio = longBreakRatio;
        this.caldavAddress = caldavAddress;
    }

    // Getter and Setter
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getWorkDuration() {
        return workDuration;
    }

    public int getShortBreakDuration() {
        return shortBreakDuration;
    }

    public int getLongBreakDuration() {
        return longBreakDuration;
    }

    public int getLongBreakRatio() {
        return longBreakRatio;
    }

    public String getCaldavAddress() {
        return caldavAddress;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setWorkDuration(int workDuration) {
        this.workDuration = workDuration;
    }

    public void setShortBreakDuration(int shortBreakDuration) {
        this.shortBreakDuration = shortBreakDuration;
    }

    public void setLongBreakDuration(int longBreakDuration) {
        this.longBreakDuration = longBreakDuration;
    }

    public void setLongBreakRatio(int longBreakRatio) {
        this.longBreakRatio = longBreakRatio;
    }

    public void setCaldavAddress(String caldavAddress) {
        this.caldavAddress = caldavAddress;
    }
}
