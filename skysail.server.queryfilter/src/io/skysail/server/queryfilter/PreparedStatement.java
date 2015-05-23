package io.skysail.server.queryfilter;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.*;

import lombok.*;

@NoArgsConstructor
@ToString
public class PreparedStatement {

    @Getter
    private StringBuilder sb = new StringBuilder();

    @Getter
    private Map<String, Object> params = new HashMap<String, Object>();

    public PreparedStatement(String str) {
        sb.append(str);
    }

    public PreparedStatement(String joinWith, List<PreparedStatement> statements) {
        if (statements.size() == 1 && "NOT".equals(joinWith)) {
            sb = new StringBuilder(joinWith).append(" (").append(statements.get(0).getSql()).append(")");
        } else {
            sb = new StringBuilder(statements.stream().map(s -> {
                return s.getSql();
            }).collect(Collectors.joining(" " + joinWith + " ")));
        }
        params = new HashMap<String, Object>();
        Stream<Entry<String, Object>> flatMap = statements.stream().map(s -> {
            return s.getParams();
        }).flatMap(m -> m.entrySet().stream());
        flatMap.forEach(m -> {
            params.put(m.getKey(), m.getValue());
        });
    }

    public StringBuilder append(String str) {
        return sb.append(str);
    }

    public String getSql() {
        return sb.toString();
    }

    public void put(String key, String value) {
        params.put(key, value);
    }

}
