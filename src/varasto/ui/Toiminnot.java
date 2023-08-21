package varasto.ui;

import java.awt.Font;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import varasto.logic.Tietojenkasittely;
import varasto.main.Koko;

public abstract class Toiminnot extends Komponentit {

    public Toiminnot(Tietojenkasittely kasittely) {
        super(kasittely);
    }

    public void toimintojenPysyvaOsaSettings() {
        toimintojenPysyvaOsa.setLayout(new BoxLayout(toimintojenPysyvaOsa, BoxLayout.Y_AXIS));
        JButton[] etsiJaLisaa = {new JButton("ETSI"), new JButton("LISÄÄ")};
        toimintojenPysyvaOsa.add(bToP(etsiJaLisaa, fontti1, new JPanel()));
        JButton[] tyhjennaJaPoista = {new JButton("TYHJENNÄ"), new JButton("POISTA")};
        toimintojenPysyvaOsa.add(bToP(tyhjennaJaPoista, fontti1, new JPanel()));
        etsiJaLisaa[0].addActionListener(a -> {
            etsittava.setText("");
            paivita(null, etsimistoiminto);
            valittuEiValittu("etsi");
        });
        etsiJaLisaa[1].addActionListener(a -> {
            if (valitunSailonListaindeksi == -1) {
                lisattava.setText("");
                paivita(null, lisaaSailo);
                valittuEiValittu("lisaaSailo");
            }
            if (valitunSailonListaindeksi != -1 && valitunEsineenListaindeksi == -1) {
                updateTilaaTaynna();
                lisattavat.forEach(c -> c.setText(""));
                paivita(null, lisaaEsineet);
                valittuEiValittu("lisaaEsineet");
            }
        });
        tyhjennaJaPoista[0].addActionListener(a -> {
            if (valitunSailonListaindeksi != -1 && valitunEsineenListaindeksi == -1 && !esineet.isEmpty()) {
                tyhjennettava.setText("Tyhjennetäänkö '" + kasittely.getSailonNimi(valitunSailonListaindeksi) + "'?");
                paivita(null, vahvistaTyhjenna);
                valittuEiValittu("tyhjennä");
            }
        });
        tyhjennaJaPoista[1].addActionListener(a -> {
            if (valitunSailonListaindeksi != -1 && esineet.isEmpty()) {
                poistettava.setText("Poistetaanko '" + kasittely.getSailonNimi(valitunSailonListaindeksi) + "'?");
                paivita(null, vahvistaPoista);
                valittuEiValittu("poistaSailo");
            }
            if (valitunEsineenListaindeksi != -1) {
                poistaEsine();
                valittuEiValittu("poistaEsine");
            }
        });
    }

    public JComponent bToP(JButton[] napit, Font f, JPanel p) {
        for (JButton b : napit) {
            p.add(komponenttiFontilla(b, f));
        }
        return p;
    }

    public void valittuEiValittu(String painettuNappi) {
        napinPainallukset.add(painettuNappi);
        if (napinPainallukset.size() == 2 && napinPainallukset.get(0).equals(napinPainallukset.get(1))) {
            napinPainallukset.clear();
            toimintojenMuuttuvaOsa.removeAll();
        }
        if (napinPainallukset.size() == 2 && !napinPainallukset.get(0).equals(napinPainallukset.get(1))) {
            napinPainallukset.clear();
            napinPainallukset.add(painettuNappi);
        }
    }

    public void updateTilaaTaynna() {
        if (kasittely.getTilaa(valitunSailonListaindeksi)) {
            tilaa.setSelected(true);
        } else {
            taynna.setSelected(true);
        }
    }

    public void etsimistoimintoSettings() {
        JButton b = new JButton("ETSI");
        lTfBToP(new JLabel("Mikä esine?"), etsittava, b, etsimistoiminto);
        b.addActionListener(a -> {
            updatePalaute();
            napinPainallukset.clear();
            paivita(null, palautteenAlusta);
        });
    }

    public void lisaaSailoSettings() {
        JButton b = new JButton("LISÄÄ");
        lTfBToP(new JLabel("Mikä säilö?"), lisattava, b, lisaaSailo);
        b.addActionListener(a -> {
            kasittely.lisaaSailo(lisattava.getText());
            updateSailolista();
            napinPainallukset.clear();
            paivita(null, null);
        });
    }

    public void lTfBToP(JLabel l, JTextField tf, JButton b, JPanel p) {
        p.add(komponenttiFontilla(l, fontti2));
        p.add(komponenttiFontilla(tfSarakkeet(tf, 1), fontti2));
        p.add(komponenttiFontilla(b, fontti2));
    }

    public JTextField tfSarakkeet(JTextField tf, int x) {
        tf.setColumns(x * (5 + Koko.Y.fontSize2() / 2));
        return tf;
    }

    public void lisaaEsineetSettings() {
        lisaaEsineet.setLayout(new BoxLayout(lisaaEsineet, BoxLayout.Y_AXIS));
        lisaaEsineet.add(lToP(new JLabel("Mitkä esineet?"), false, fontti2, new JPanel()));
        for (int n = 0; n < 22; n++) {
            JTextField tf = new JTextField();
            lisattavat.add(tfSarakkeet(tf, 2));
        }
        lisattavat.forEach(c -> lisaaEsineet.add(komponenttiFontilla(c, fontti2)));
        JPanel p = new JPanel();
        tilaaTaynna.add(tilaa);
        tilaaTaynna.add(taynna);
        JButton b = new JButton("LISÄÄ");
        p.add(komponenttiFontilla(tilaa, fontti2));
        p.add(komponenttiFontilla(taynna, fontti2));
        p.add(komponenttiFontilla(b, fontti2));
        lisaaEsineet.add(p);
        b.addActionListener(a -> {
            Boolean valittuTayttoaste = tilaa.isSelected();
            if (!Objects.equals(valittuTayttoaste, kasittely.getTilaa(valitunSailonListaindeksi))) {
                kasittely.paivitaTayttoasteet(valitunSailonListaindeksi, valittuTayttoaste);
            }
            List<String> lisattavatEsineet = lisattavat.stream().map(c -> c.getText())
                    .filter(s -> !s.isEmpty()).collect(Collectors.toList());
            if (!lisattavatEsineet.isEmpty()) {
                kasittely.lisaaEsineet(valitunSailonListaindeksi, lisattavatEsineet);
            }
            updateSailolista();
            updateEsinelista();
            napinPainallukset.clear();
            paivita(null, null);
        });
    }

    public void vahvistaTyhjennaSettings() {
        JButton[] vahvista = {new JButton("VAHVISTA")};
        pToP(tyhjennettava, vahvista, vahvistaTyhjenna);
        vahvista[0].addActionListener(a -> {
            kasittely.tyhjennaSailo(valitunSailonListaindeksi);
            updateSailolista();
            updateEsinelista();
            napinPainallukset.clear();
            paivita(null, null);
        });
    }

    public void vahvistaPoistaSettings() {
        JButton[] vahvista = {new JButton("VAHVISTA")};
        pToP(poistettava, vahvista, vahvistaPoista);
        vahvista[0].addActionListener(a -> {
            kasittely.poistaSailo(valitunSailonListaindeksi);
            valitunSailonListaindeksi = -1;
            updateSailolista();
            napinPainallukset.clear();
            paivita(null, null);
        });
    }

    public void pToP(JLabel l, JButton[] b, JPanel p) {
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(lToP(l, false, fontti2, new JPanel()));
        p.add(bToP(b, fontti2, new JPanel()));
    }

    public void poistaEsine() {
        kasittely.poistaEsine(valitunSailonListaindeksi, valitunEsineenListaindeksi);
        valitunEsineenListaindeksi = -1;
        updateSailolista();
        updateEsinelista();
        napinPainallukset.clear();
    }

}
