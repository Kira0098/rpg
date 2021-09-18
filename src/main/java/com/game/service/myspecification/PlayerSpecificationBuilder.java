package com.game.service.myspecification;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import java.util.Date;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class PlayerSpecificationBuilder implements Specification<Player> {

  private SearchCriteria criteria;

  public PlayerSpecificationBuilder(SearchCriteria criteria) {
    this.criteria = criteria;
  }

  @Override
  public Predicate toPredicate(Root<Player> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {
    if (criteria.getKey().equals("name") || criteria.getKey().equals("title")) {
      return predicateNameOrTitle(root, query, criteriaBuilder);
    } else if (criteria.getKey().equals("race")) {
      return predicateRace(root, query, criteriaBuilder);
    } else if (criteria.getKey().equals("profession")) {
      return predicateProfession(root, query, criteriaBuilder);
    } else if (criteria.getKey().equals("birthday")) {
      return predicateBirthday(root, query, criteriaBuilder);
    } else if (criteria.getKey().equals("banned")) {
      return predicateBanned(root, query, criteriaBuilder);
    } else if (criteria.getKey().equals("experience") || criteria.getKey().equals("level")){
      return predicateExperienceOrLevel(root, query, criteriaBuilder);
    }
    return null;
  }

  private Predicate predicateNameOrTitle(Root<Player> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {
    if (criteria.getFirstValue() == null) {
      return criteriaBuilder.like(
          root.get(criteria.getKey()), "%");
    } else {
      return criteriaBuilder.like(
          root.get(criteria.getKey()), "%" + criteria.getFirstValue() + "%");
    }
  }

  private Predicate predicateRace(Root<Player> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {
    if (criteria.getFirstValue() == null) {
      return criteriaBuilder.conjunction();
    } else {
      return criteriaBuilder.equal(
          root.<Race>get(criteria.getKey()), criteria.getFirstValue());
    }
  }

  private Predicate predicateProfession(Root<Player> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {
    if (criteria.getFirstValue() == null) {
      return criteriaBuilder.conjunction();
    } else {
      return criteriaBuilder.equal(
          root.<Profession>get(criteria.getKey()), criteria.getFirstValue());
    }
  }

  private Predicate predicateBirthday(Root<Player> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {
    if (criteria.getFirstValue() == null && criteria.getSecondValue() == null) {
      return criteriaBuilder.conjunction();
    } else if (criteria.getSecondValue() == null) {
      return criteriaBuilder.greaterThanOrEqualTo(
          root.<Date>get(criteria.getKey()), new Date((Long) criteria.getFirstValue()));
    } else if (criteria.getFirstValue() == null) {
      return criteriaBuilder.lessThanOrEqualTo(
          root.<Date>get(criteria.getKey()), new Date((Long) criteria.getSecondValue()));
    } else {
      return criteriaBuilder.between(
          root.<Date>get(criteria.getKey()), new Date((Long) criteria.getFirstValue()),
          new Date((Long) criteria.getSecondValue()));
    }
  }

  private Predicate predicateBanned(Root<Player> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {
    if (criteria.getFirstValue() == null) {
      return criteriaBuilder.conjunction();
    } else {
      return criteriaBuilder.equal(
          root.<Boolean>get(criteria.getKey()), criteria.getFirstValue());
    }
  }

  private Predicate predicateExperienceOrLevel(Root<Player> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {
    if (criteria.getFirstValue() == null && criteria.getSecondValue() == null) {
      return criteriaBuilder.conjunction();
    } else if (criteria.getFirstValue() == null) {
      return criteriaBuilder.lessThanOrEqualTo(
          root.<Integer>get(criteria.getKey()), (Integer) criteria.getSecondValue());
    } else if (criteria.getSecondValue() == null){
      return criteriaBuilder.greaterThanOrEqualTo(
          root.<Integer>get(criteria.getKey()), (Integer) criteria.getFirstValue());
    } else {
      return criteriaBuilder.between(
          root.<Integer>get(criteria.getKey()), (Integer) criteria.getFirstValue(), (Integer) criteria.getSecondValue());
    }
  }
}
