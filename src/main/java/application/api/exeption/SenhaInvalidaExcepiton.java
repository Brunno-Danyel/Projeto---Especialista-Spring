package application.api.exeption;

public class SenhaInvalidaExcepiton extends RuntimeException {
    public SenhaInvalidaExcepiton() {
        super("Senha inválida");
    }
}
