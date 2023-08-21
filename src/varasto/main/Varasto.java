package varasto.main;

import java.io.File;
import varasto.io.TietokannanHallinta;
import varasto.logic.Tietojenkasittely;
import varasto.ui.Kayttoliittyma;

public class Varasto {

    private final Tietojenkasittely kasittely;
    private final Kayttoliittyma kaytto;

    public Varasto() {
        luoTietokantaTarvittaessa();
        this.kasittely = new Tietojenkasittely();
        this.kaytto = new Kayttoliittyma(kasittely);
    }

    public static void main(String[] args) {
        Varasto varasto = new Varasto();
        varasto.kaynnista();
    }
    
    
    private void kaynnista() {
        kaytto.setPaaikkuna();
    }

    private void luoTietokantaTarvittaessa() {
        if (!new File("Varasto.db").exists()) {
            TietokannanHallinta.luoTaulut();
        }
    }

}
