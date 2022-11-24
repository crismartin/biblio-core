package es.awkidev.corp.biblio.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    private String name;
    private String surname;
    private String secondSurname;
    private String nick;
    private String identity;
    private String numberMembership;

    public String getFullName() {
        return Stream.of(name, surname, secondSurname)
                .filter(StringUtils::isNoneBlank)
                .collect(Collectors.joining(" "));
    }
}
