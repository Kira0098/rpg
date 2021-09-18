package com.game.service.myspecification;

public class SearchCriteria {

  private String key;
  private Object firstValue;
  private Object secondValue;

  public SearchCriteria(String key, Object firstValue) {
    this.key = key;
    this.firstValue = firstValue;
  }

  public SearchCriteria(String key, Object firstValue, Object secondValue) {
    this.key = key;
    this.firstValue = firstValue;
    this.secondValue = secondValue;
  }

  public String getKey() {
    return key;
  }

  public Object getFirstValue() {
    return firstValue;
  }

  public Object getSecondValue() {
    return secondValue;
  }
}
