package com.creditsuisse.service.impl.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class Event {
    String id;
    State state;
    long timestamp;
    String type;
    String host;
}
