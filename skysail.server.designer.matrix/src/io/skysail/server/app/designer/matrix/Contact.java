package io.skysail.server.app.designer.matrix;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.server.ext.apt.annotations.GenerateResources;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
@GenerateResources(application = "io.skysail.server.app.designer.matrix.MatrixApplication")
public class Contact implements Identifiable {

    @Id
    private String id;

    @Field
    private String title, firstName, lastName, pic, location, room, department, telephone, account, description;

//    "lastName": "Dietz",
//    "pic": "kv/Dietz_Markus.png",
//    "location": "München",
//    "room": "R1420",
//    "department": "EHS",
//    "telephone": "2633",
//    "account": "dietzma",
//    "description": "SNK, KV-SafeNet, KV-FlexNet, KV Baden-Württemberg, mobile, Cura Campus, KV-Connect"


}
