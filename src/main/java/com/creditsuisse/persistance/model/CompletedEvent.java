package com.creditsuisse.persistance.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@ToString
@EqualsAndHashCode
@Entity
public class CompletedEvent {
    @Id
    String id;
    boolean alert;
    long duration;
    String type;
    String host;
}
