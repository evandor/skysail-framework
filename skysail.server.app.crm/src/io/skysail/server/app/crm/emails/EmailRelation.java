package io.skysail.server.app.crm.emails;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailRelation {

    private String name;

    public EmailRelation(String name) {
        this.name = name;
    }

    public static List<EmailRelation> initialData() {
        List<EmailRelation> initialData = new ArrayList<>();
        initialData.add(new EmailRelation("Work"));
        initialData.add(new EmailRelation("Home"));
        return initialData;
    }
}
