package application.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class ApiErros {

    @Getter
    private List<String> errors;

    public ApiErros(String messagemErro){
        this.errors = Arrays.asList(messagemErro);
    }
}
