package pl.pierce.testtask.csv2json.csv;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Option {
    private String id;
    private int sortOrder;
    private Map<String, String> labelTranslations;
}
