package br.edu.uniamerica.projetomensal;
import br.edu.uniamerica.projetomensal.utils.Csv;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class CsvTest {
    @TempDir Path pasta;
    @Test void preservaAcentosAspasQuebrasEImpedeFormula() throws Exception {
        Path arquivo=pasta.resolve("teste.csv");
        Csv.salvar(arquivo,new String[]{"Nome","Observação"},List.<Object[]>of(new Object[]{"João; Silva","Uma \"visita\"\nconfirmada"},new Object[]{"=1+1",null}));
        String csv=Files.readString(arquivo);
        assertTrue(csv.startsWith("\uFEFF")); assertTrue(csv.contains("\"João; Silva\"")); assertTrue(csv.contains("Uma \"\"visita\"\"\nconfirmada")); assertTrue(csv.contains("\"'=1+1\""));
    }
}
