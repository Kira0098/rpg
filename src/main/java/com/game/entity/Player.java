package com.game.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "player")
public class Player implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String title;
  @Enumerated(value = EnumType.STRING)
  private Race race;
  @Enumerated(value = EnumType.STRING)
  private Profession profession;
  @Temporal(TemporalType.DATE)
  private Date birthday;
  private Boolean banned;
  private Integer experience;
  private Integer level;
  private Integer untilNextLevel;

  public Player() {
  }

  public Player(Long id, String name, String title, Race race, Profession profession,
      Integer experience, Integer level, Integer untilNextLevel, Date birthday,
      Boolean banned) {
    this.id = id;
    this.name = name;
    this.title = title;
    this.race = race;
    this.profession = profession;
    this.experience = experience;
    this.level = level;
    this.untilNextLevel = untilNextLevel;
    this.birthday = birthday;
    this.banned = banned;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Race getRace() {
    return race;
  }

  public void setRace(Race race) {
    this.race = race;
  }

  public Profession getProfession() {
    return profession;
  }

  public void setProfession(Profession profession) {
    this.profession = profession;
  }

  public Integer getExperience() {
    return experience;
  }

  public void setExperience(Integer experience) {
    this.experience = experience;
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public Integer getUntilNextLevel() {
    return untilNextLevel;
  }

  public void setUntilNextLevel(Integer untilNextLevel) {
    this.untilNextLevel = untilNextLevel;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public Boolean getBanned() {
    return banned;
  }

  public void setBanned(Boolean banned) {
    this.banned = banned;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Player player = (Player) o;
    return Objects.equals(getId(), player.getId()) &&
        Objects.equals(getName(), player.getName()) &&
        Objects.equals(getTitle(), player.getTitle()) &&
        getRace() == player.getRace() &&
        getProfession() == player.getProfession() &&
        Objects.equals(getExperience(), player.getExperience()) &&
        Objects.equals(getLevel(), player.getLevel()) &&
        Objects.equals(getUntilNextLevel(), player.getUntilNextLevel()) &&
        Objects.equals(getBirthday(), player.getBirthday()) &&
        Objects.equals(getBanned(), player.getBanned());
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(getId(), getName(), getTitle(), getRace(), getProfession(), getExperience(),
            getLevel(),
            getUntilNextLevel(), getBirthday(), getBanned());
  }

  @Override
  public String toString() {
    return "Player{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", title='" + title + '\'' +
        ", race=" + race +
        ", profession=" + profession +
        ", experience=" + experience +
        ", level=" + level +
        ", untilNextLevel=" + untilNextLevel +
        ", birthday=" + birthday +
        ", banned=" + banned +
        '}';
  }
}
