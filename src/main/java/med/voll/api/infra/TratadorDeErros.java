package med.voll.api.infra;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    //Tratador de erros para EntityNotFoundException
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404(){
        //Retorna um erro 404 - notFound
        return ResponseEntity.notFound().build();
    }

}
