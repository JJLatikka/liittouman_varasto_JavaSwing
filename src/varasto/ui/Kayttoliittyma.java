package varasto.ui;

import java.awt.BorderLayout;
import javax.swing.JFrame;

import varasto.logic.Tietojenkasittely;
import varasto.main.Koko;

public final class Kayttoliittyma extends Toiminnot {

    public JFrame paaikkuna;

    public Kayttoliittyma(Tietojenkasittely kasittely) {
        super(kasittely);
        settings();
    }

    public void settings() {
        otsikotSettings();
        sailolistaSettings();
        esinelistaSettings();
        toimintovalikkoSettings();
        palauteSettings();
        toimintojenPysyvaOsaSettings();
        etsimistoimintoSettings();
        lisaaSailoSettings();
        lisaaEsineetSettings();
        vahvistaTyhjennaSettings();
        vahvistaPoistaSettings();
        updateSailolista();
    }

    public void setPaaikkuna() {
        this.paaikkuna = new JFrame("Liittouman varasto");
        paaikkuna.setSize(Koko.X.get(), Koko.Y.get());
        paaikkuna.add(otsikot, BorderLayout.NORTH);
        paaikkuna.add(sailolistanAlusta, BorderLayout.WEST);
        paaikkuna.add(esinelistanAlusta, BorderLayout.CENTER);
        paaikkuna.add(toimintovalikonAlusta, BorderLayout.EAST);
        paaikkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        paaikkuna.setAlwaysOnTop(true);
        paaikkuna.setVisible(true);
    }

}
