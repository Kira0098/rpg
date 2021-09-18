package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import com.game.service.myspecification.PlayerSpecificationBuilder;
import com.game.service.myspecification.SearchCriteria;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

  private PlayerRepository playerRepository;

  @Autowired
  public PlayerServiceImpl(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @Override
  public List<Player> getPlayers(String name, String title, Race race, Profession profession,
      Long after,
      Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel,
      Integer maxLevel, PlayerOrder order, Integer pageNumber, Integer pageSize) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize, Direction.ASC, order.getFieldName());
    return playerRepository.findAll(Specification
            .where(new PlayerSpecificationBuilder(
                new SearchCriteria("name", name)).and(new PlayerSpecificationBuilder(
                new SearchCriteria("title", title))).and(new PlayerSpecificationBuilder(
                new SearchCriteria("race", race))).and(new PlayerSpecificationBuilder(
                new SearchCriteria("profession", profession))).and(new PlayerSpecificationBuilder(
                new SearchCriteria("birthday", after, before))).and(new PlayerSpecificationBuilder(
                new SearchCriteria("banned", banned))).and(new PlayerSpecificationBuilder(
                new SearchCriteria("experience", minExperience, maxExperience)))
                .and(new PlayerSpecificationBuilder(
                    new SearchCriteria("level", minLevel, maxLevel)))),
        pageable).getContent();
  }

  @Override
  public Long getPlayersCount(String name, String title, Race race, Profession profession,
      Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience,
      Integer minLevel, Integer maxLevel) {
    return playerRepository.count(Specification
        .where(new PlayerSpecificationBuilder(
            new SearchCriteria("name", name)).and(new PlayerSpecificationBuilder(
            new SearchCriteria("title", title))).and(new PlayerSpecificationBuilder(
            new SearchCriteria("race", race))).and(new PlayerSpecificationBuilder(
            new SearchCriteria("profession", profession))).and(new PlayerSpecificationBuilder(
            new SearchCriteria("birthday", after, before))).and(new PlayerSpecificationBuilder(
            new SearchCriteria("banned", banned))).and(new PlayerSpecificationBuilder(
            new SearchCriteria("experience", minExperience, maxExperience)))
            .and(new PlayerSpecificationBuilder(
                new SearchCriteria("level", minLevel, maxLevel)))));
  }

  @Override
  public ResponseEntity<Player> getPlayer(String id) {
    if (validId(id)) {
      Integer val = Integer.parseInt(id);
      Optional<Player> player = playerRepository.findById(val.longValue());
      return player.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public ResponseEntity<Player> deletePlayer(String id) {
    if (validId(id)) {
      Integer val = Integer.parseInt(id);
      if (playerRepository.existsById(val.longValue())) {
        playerRepository.deleteById(val.longValue());
        return new ResponseEntity<>(HttpStatus.OK);
      }
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @Override
  public ResponseEntity<Player> updatePlayer(Player requestBody, Player databasePlayer) {
    if (requestBody.getName() != null) {
      databasePlayer.setName(requestBody.getName());
    }
    if (requestBody.getTitle() != null) {
      databasePlayer.setTitle(requestBody.getTitle());
    }
    if (requestBody.getRace() != null) {
      databasePlayer.setRace(requestBody.getRace());
    }
    if (requestBody.getProfession() != null) {
      databasePlayer.setProfession(requestBody.getProfession());
    }
    if (requestBody.getBirthday() != null) {
      if (validBirthday(requestBody.getBirthday())) {
        databasePlayer.setBirthday(requestBody.getBirthday());
      } else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }
    if (requestBody.getBanned() != null) {
      databasePlayer.setBanned(requestBody.getBanned());
    }
    if (requestBody.getExperience() != null) {
      Integer level = getLevel(requestBody.getExperience());
      if (level != null) {
        Integer untilNextLevel = getUntilNextLevel(requestBody.getExperience(), level);
        databasePlayer.setExperience(requestBody.getExperience());
        databasePlayer.setLevel(level);
        databasePlayer.setUntilNextLevel(untilNextLevel);
        Player playerResponse = playerRepository.saveAndFlush(databasePlayer);
        return new ResponseEntity<>(playerResponse, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }
    return new ResponseEntity<>(databasePlayer, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Player> createPlayer(String name, String title, Race race,
      Profession profession, Date birthday, Boolean banned, Integer experience) {
    if (name.isEmpty() || name.length() > 12 || title.length() > 30 || !validExperience(experience)
        || birthday.getTime() < 0 || !validBirthday(birthday)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    Integer level = getLevel(experience);
    Integer untilNextLevel = getUntilNextLevel(experience, level);
    Player responsePlayer = playerRepository.saveAndFlush(
        createNewPlayer(name, title, race, profession, birthday, banned, experience, level,
            untilNextLevel));
    return new ResponseEntity<>(responsePlayer, HttpStatus.OK);
  }

  private Player createNewPlayer(String name, String title, Race race, Profession profession,
      Date birthday, Boolean banned, Integer experience, Integer level, Integer untilNextLevel) {
    Player player = new Player();
    player.setName(name);
    player.setTitle(title);
    player.setRace(race);
    player.setProfession(profession);
    player.setBirthday(birthday);
    player.setBanned(banned);
    player.setExperience(experience);
    player.setLevel(level);
    player.setUntilNextLevel(untilNextLevel);
    return player;
  }

  private boolean validExperience(Integer experience) {
    return experience > 0 && experience < 10_000_000;
  }

  private boolean validId(String id) {
    try {
      int integerId = Integer.parseInt(id);
      if (integerId < 1) {
        return false;
      }
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  private Integer getLevel(Integer experience) {
    if (!validExperience(experience)) {
      return null;
    } else {
      Double level = (Math.sqrt(2500 + 200 * experience) - 50) / 100;
      return level.intValue();
    }
  }

  private Integer getUntilNextLevel(Integer experience, Integer level) {
    return 50 * (level + 1) * (level + 2) - experience;
  }

  private boolean validBirthday(Date date) {
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    return calendar.get(Calendar.YEAR) >= 2000 && calendar.get(Calendar.YEAR) <= 3000;
  }

}
