package io.skysail.server.converter.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Notification {

    private String msg;
    private String type;

}
