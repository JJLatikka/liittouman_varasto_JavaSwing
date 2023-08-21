package varasto.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import varasto.logic.Tietojenkasittely;
import varasto.main.Koko;

public abstract class Komponentit {

    public final Tietojenkasittely kasittely;
    public final Font fontti1;
    public final Font fontti2;
    public final JPanel otsikot;
    public final DefaultListModel sailot;
    public final JList sailolista;
    public final JScrollPane sailolistanAlusta;
    public int valitunSailonListaindeksi;
    public final List<Integer> sailovalinnat;
    public final DefaultListModel esineet;
    public final JList esinelista;
    public final JScrollPane esinelistanAlusta;
    public int valitunEsineenListaindeksi;
    public final List<Integer> esinevalinnat;
    public final JPanel toimintovalikko;
    public final JPanel toimintojenMuuttuvaOsa;
    public final DefaultListModel palauterivit;
    public final JList palaute;
    public final JScrollPane palautteenAlusta;
    public final JPanel toimintojenPysyvaOsa;
    public final JPanel toimintovalikonAlusta;
    public final List<String> napinPainallukset;
    public final JPanel etsimistoiminto;
    public final JTextField etsittava;
    public final JPanel lisaaSailo;
    public final JTextField lisattava;
    public final JPanel lisaaEsineet;
    public final List<JTextField> lisattavat;
    public final ButtonGroup tilaaTaynna;
    public final JRadioButton tilaa;
    public final JRadioButton taynna;
    public final JPanel vahvistaTyhjenna;
    public final JLabel tyhjennettava;
    public final JPanel vahvistaPoista;
    public final JLabel poistettava;

    public Komponentit(Tietojenkasittely kasittely) {
        this.kasittely = kasittely;
        this.fontti1 = new Font("", Font.BOLD, Koko.Y.fontSize1());
        this.fontti2 = new Font("", Font.BOLD, Koko.Y.fontSize2());
        this.otsikot = new JPanel();
        this.sailot = new DefaultListModel();
        this.sailolista = new JList(sailot);
        this.sailolistanAlusta = new JScrollPane(sailolista);
        this.valitunSailonListaindeksi = -1;
        this.sailovalinnat = new ArrayList<>();
        this.esineet = new DefaultListModel();
        this.esinelista = new JList(esineet);
        this.esinelistanAlusta = new JScrollPane(esinelista);
        this.valitunEsineenListaindeksi = -1;
        this.esinevalinnat = new ArrayList<>();
        this.toimintovalikko = new JPanel();
        this.toimintojenMuuttuvaOsa = new JPanel();
        this.palauterivit = new DefaultListModel();
        this.palaute = new JList(palauterivit);
        this.palautteenAlusta = new JScrollPane(palaute);
        this.toimintojenPysyvaOsa = new JPanel();
        this.toimintovalikonAlusta = new JPanel();
        this.napinPainallukset = new ArrayList<>();
        this.etsimistoiminto = new JPanel();
        this.etsittava = new JTextField();
        this.lisaaSailo = new JPanel();
        this.lisattava = new JTextField();
        this.lisaaEsineet = new JPanel();
        this.lisattavat = new ArrayList<>();
        this.tilaaTaynna = new ButtonGroup();
        this.tilaa = new JRadioButton("tilaa");
        this.taynna = new JRadioButton("taynna");
        this.vahvistaTyhjenna = new JPanel();
        this.tyhjennettava = new JLabel();
        this.vahvistaPoista = new JPanel();
        this.poistettava = new JLabel();
    }

    public void otsikotSettings() {
        otsikot.setLayout(new BorderLayout());
        otsikot.add(setOtsikko(new JLabel("SÄILÖT"), fontti1, new JPanel()), BorderLayout.WEST);
        otsikot.add(setOtsikko(new JLabel("ESINEET"), fontti1, new JPanel()), BorderLayout.CENTER);
        otsikot.add(setOtsikko(new JLabel("TOIMINNOT"), fontti1, new JPanel()), BorderLayout.EAST);
    }

    public JComponent setOtsikko(JLabel l, Font f, JPanel p) {
        return komponenttiKoolla(lToP(l, true, f, p), Koko.X.get() / 3, 10 + Koko.Y.fontSize1());
    }

    public JComponent lToP(JLabel l, boolean keskella, Font f, JPanel p) {
        if (!keskella) {
            p.setLayout(new BorderLayout());
        }
        p.add(komponenttiFontilla(l, f));
        return p;
    }

    public JComponent komponenttiFontilla(JComponent c, Font f) {
        c.setFont(f);
        return c;
    }

    public JComponent komponenttiKoolla(JComponent c, int x, int y) {
        c.setPreferredSize(new Dimension(x, y));
        return c;
    }

    public void sailolistaSettings() {
        komponenttiKoolla(sailolistanAlusta, Koko.X.get() / 3, Koko.Y.get() - otsikot.getHeight());
        sailolista.setFixedCellHeight(10 + Koko.Y.fontSize2());
        sailolista.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel p = (JPanel) value;
            p.setBackground(isSelected ? Color.LIGHT_GRAY : sailolista.getBackground());
            return p;
        });
        sailolista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                valitunSailonListaindeksi = sailolista.getSelectedIndex();
                sailovalinnat.add(valitunSailonListaindeksi);
                if (sailovalinnat.size() == 2 && Objects.equals(sailovalinnat.get(0), sailovalinnat.get(1))) {
                    sailovalinnat.clear();
                    sailolista.getSelectionModel().clearSelection();
                    valitunSailonListaindeksi = -1;
                }
                if (sailovalinnat.size() == 2 && !Objects.equals(sailovalinnat.get(0), sailovalinnat.get(1))) {
                    sailovalinnat.clear();
                    sailovalinnat.add(valitunSailonListaindeksi);
                }
                updateEsinelista();
                napinPainallukset.clear();
                paivita(new JComponent[]{etsimistoiminto, lisaaSailo, lisaaEsineet, vahvistaTyhjenna, vahvistaPoista}, null);
            }
        });
    }

    public void updateSailolista() {
        sailot.clear();
        sailovalinnat.clear();
        strTaulukkoListToPList(kasittely.getSailot()).forEach(p -> {
            sailot.addElement(p);
        });
        if (valitunSailonListaindeksi != -1) {
            sailovalinnat.add(valitunSailonListaindeksi);
            sailolista.setSelectedIndex(valitunSailonListaindeksi);
        }
    }

    public List<JComponent> strTaulukkoListToPList(List<String[]> parit) {
        return parit.stream().map(pari -> {
            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());
            JLabel l1 = new JLabel(pari[0]);
            JLabel l2 = new JLabel(pari[1]);
            p.add(komponenttiFontilla(l1, fontti2), BorderLayout.WEST);
            p.add(komponenttiFontilla(l2, fontti2), BorderLayout.EAST);
            return p;
        }).collect(Collectors.toList());
    }

    public void esinelistaSettings() {
        komponenttiKoolla(esinelistanAlusta, Koko.X.get() / 3, Koko.Y.get() - otsikot.getHeight());
        esinelista.setFixedCellHeight(10 + Koko.Y.fontSize2());
        esinelista.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel p = (JPanel) value;
            p.setBackground(isSelected ? Color.LIGHT_GRAY : esinelista.getBackground());
            return p;
        });
        esinelista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                valitunEsineenListaindeksi = esinelista.getSelectedIndex();
                esinevalinnat.add(valitunEsineenListaindeksi);
                if (esinevalinnat.size() == 2 && Objects.equals(esinevalinnat.get(0), esinevalinnat.get(1))) {
                    esinevalinnat.clear();
                    esinelista.getSelectionModel().clearSelection();
                    valitunEsineenListaindeksi = -1;
                }
                if (esinevalinnat.size() == 2 && !Objects.equals(esinevalinnat.get(0), esinevalinnat.get(1))) {
                    esinevalinnat.clear();
                    esinevalinnat.add(valitunEsineenListaindeksi);
                }
                napinPainallukset.clear();
                paivita(new JComponent[]{etsimistoiminto, lisaaEsineet, vahvistaTyhjenna}, null);
            }
        });
    }

    public void updateEsinelista() {
        esineet.clear();
        esinevalinnat.clear();
        strListToPList(kasittely.getEsineet(valitunSailonListaindeksi)).forEach(p -> {
            esineet.addElement(p);
        });
        esinelista.getSelectionModel().clearSelection();
        valitunEsineenListaindeksi = -1;
    }

    public List<JComponent> strListToPList(List<String> list) {
        return list.stream().map(s -> {
            return lToP(new JLabel(s), false, fontti2, new JPanel());
        }).collect(Collectors.toList());
    }

    public void toimintovalikkoSettings() {
        komponenttiKoolla(toimintovalikonAlusta, Koko.X.get() / 3, Koko.Y.get() - otsikot.getHeight());
        toimintovalikonAlusta.add(toimintovalikko);
        toimintovalikko.setLayout(new BoxLayout(toimintovalikko, BoxLayout.Y_AXIS));
        toimintovalikko.add(toimintojenPysyvaOsa);
        toimintovalikko.add(toimintojenMuuttuvaOsa);
    }

    public void palauteSettings() {
        komponenttiKoolla(palautteenAlusta, Koko.X.get() / 3 - Koko.X.get() / 60,
                Koko.Y.get() - Koko.Y.get() / 5 - Koko.Y.get() / 100);
        palaute.setFixedCellHeight(10 + Koko.Y.fontSize2());
        palaute.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JPanel p = (JPanel) value;
            p.setBackground(Color.WHITE);
            return p;
        });
    }

    public void updatePalaute() {
        palauterivit.clear();
        mapToPList(kasittely.getPalaute(etsittava.getText())).forEach(p -> {
            palauterivit.addElement(p);
        });
    }

    public List<JComponent> mapToPList(Map<Integer, List<String>> tm) {
        List<JComponent> list = new ArrayList<>();
        String tab1 = "     ";
        String tab2 = "          ";
        list.add(new JPanel());
        if (tm.containsKey(0)) {
            list.add(lToP(new JLabel(tab1 + "Ei näytettävää."), false, fontti2, new JPanel()));
        } else {
            tm.keySet().forEach(k -> {
                list.add(lToP(new JLabel(tab1 + kasittely.getTietokantaindeksitJaSailot().get(k)), false, fontti2, new JPanel()));
                list.add(new JPanel());
                tm.get(k).forEach(s -> {
                    list.add(lToP(new JLabel(tab2 + s), false, fontti2, new JPanel()));
                });
                list.add(new JPanel());
                list.add(new JPanel());
            });
        }
        return list;
    }

    public void paivita(JComponent[] poistettavat, JComponent lisattava) {
        if (poistettavat != null) {
            Arrays.asList(poistettavat).forEach(c -> toimintojenMuuttuvaOsa.remove(c));
        } else {
            toimintojenMuuttuvaOsa.removeAll();
        }
        if (lisattava != null) {
            toimintojenMuuttuvaOsa.add(lisattava);
        }
        toimintojenMuuttuvaOsa.updateUI();
    }

}
