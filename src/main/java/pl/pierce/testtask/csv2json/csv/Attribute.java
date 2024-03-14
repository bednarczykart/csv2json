package pl.pierce.testtask.csv2json.csv;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attribute {
    private String id;
    private Map<String, String> translationLabels;
    private List<Option> options;
}
