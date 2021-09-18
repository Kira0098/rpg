package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import java.util.Date;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PlayerService {

  List<Player> getPlayers(String name, String title, Race race, Profession profession, Long after,
      Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel,
      Integer maxLevel, PlayerOrder order, Integer pageNumber, Integer pageSize);

  Long getPlayersCount(String name, String title, Race race, Profession profession, Long after,
      Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel,
      Integer maxLevel);

  ResponseEntity<Player> getPlayer(String id);

  ResponseEntity<Player> deletePlayer(String id);

  ResponseEntity<Player> updatePlayer(Player requestBody, Player databasePlayer);

  ResponseEntity<Player> createPlayer(String name, String title, Race race, Profession profession,
      Date birthday, Boolean banned, Integer experience);
}
