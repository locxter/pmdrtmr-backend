package com.github.locxter.pmdrtmr.backend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

// Timer class
@Entity
@Table(name = "timers")
public class Timer
{
    // Attributes
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;
    @OneToOne
    @JoinColumn(name = "linked_timer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Timer linkedTimer;
    @Column(name = "is_break")
    private boolean isBreak;
    @Column(name = "description")
    private String description;
    @Column(name = "duration")
    private int duration;

    // Constructors
    private Timer()
    {
    }

    public Timer(User user, boolean isBreak, String description, int duration)
    {
        this.user = user;
        this.isBreak = isBreak;
        this.description = description;
        this.duration = duration;
    }

    public Timer(User user, Timer linkedTimer, boolean isBreak, String description, int duration)
    {
        this.user = user;
        this.linkedTimer = linkedTimer;
        this.isBreak = isBreak;
        this.description = description;
        this.duration = duration;
    }

    // Getter and Setter
    public Long getId()
    {
        return id;
    }

    public User getUser()
    {
        return user;
    }

    public Timer getLinkedTimer()
    {
        return linkedTimer;
    }

    public boolean getIsBreak()
    {
        return isBreak;
    }

    public String getDescription()
    {
        return description;
    }

    public int getDuration()
    {
        return duration;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public void setLinkedTimer(Timer linkedTimer)
    {
        this.linkedTimer = linkedTimer;
    }

    public void setIsBreak(boolean isBreak)
    {
        this.isBreak = isBreak;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setDuration(int duration)
    {
        this.duration = duration;
    }
}
