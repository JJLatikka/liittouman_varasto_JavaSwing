package varasto.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import varasto.io.TietokannanHallinta;

public class Tietojenkasittely {

    private Map<Integer, String> tietokantaindeksitJaSailot;
    private final Map<Integer, Integer> sailojenListaJaTietokantaindeksit;
    private Map<Integer, Boolean> tietokantaindeksitJaTayttoasteet;
    private Map<Integer, String> tietokantaindeksitJaSailonEsineet;
    private final Map<Integer, Integer> sailonEsineidenListaJaTietokantaindeksit;
    private Map<Integer, List<String>> sailoistaLoydetytEsineet;

    public Tietojenkasittely() {
        this.tietokantaindeksitJaSailot = new TreeMap<>();
        this.sailojenListaJaTietokantaindeksit = new HashMap<>();
        this.tietokantaindeksitJaTayttoasteet = new HashMap<>();
        this.tietokantaindeksitJaSailonEsineet = new TreeMap<>();
        this.sailonEsineidenListaJaTietokantaindeksit = new HashMap<>();
        this.sailoistaLoydetytEsineet = new TreeMap<>();
    }

    public List<String[]> getSailot() {
        tietokantaindeksitJaSailot = TietokannanHallinta.indeksitJaSailot();
        setListaJaTietokantaindeksit(sailojenListaJaTietokantaindeksit, tietokantaindeksitJaSailot.keySet());
        tietokantaindeksitJaTayttoasteet = TietokannanHallinta.indeksitJaTayttoasteet();
        return tietokantaindeksitJaSailot.keySet().stream().map(indeksi -> {
            String tilaa = null;
            if (tietokantaindeksitJaTayttoasteet.get(indeksi)) {
                tilaa = "(tilaa)";
            }
            String[] nimiJaTilaa = {tietokantaindeksitJaSailot.get(indeksi), tilaa};
            return nimiJaTilaa;
        }).collect(Collectors.toList());
    }

    public List<String> getEsineet(int valitunSailonListaindeksi) {
        if (valitunSailonListaindeksi != -1) {
            tietokantaindeksitJaSailonEsineet = TietokannanHallinta
                    .indeksitJaSailonEsineet(getSailonTietokantaindeksi(valitunSailonListaindeksi));
            setListaJaTietokantaindeksit(sailonEsineidenListaJaTietokantaindeksit, tietokantaindeksitJaSailonEsineet.keySet());
            return tietokantaindeksitJaSailonEsineet.keySet().stream().map(i
                    -> tietokantaindeksitJaSailonEsineet.get(i)).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private void setListaJaTietokantaindeksit(Map<Integer, Integer> m, Set<Integer> keySet) {
        m.clear();
        int i = 0;
        for (int k : keySet) {
            m.put(i, k);
            i++;
        }
    }

    public Map<Integer, List<String>> getPalaute(String hakusana) {
        sailoistaLoydetytEsineet = TietokannanHallinta.sailoistaLoydetytEsineet(hakusana);
        if (hakusana.isEmpty() || sailoistaLoydetytEsineet.isEmpty()) {
            sailoistaLoydetytEsineet.put(0, null);
        }
        return sailoistaLoydetytEsineet;
    }

    public void lisaaSailo(String sailo) {
        if (!sailo.isEmpty() && !tietokantaindeksitJaSailot.values().contains(sailo)) {
            TietokannanHallinta.lisaaSailo(sailo);
        }
    }

    public void paivitaTayttoasteet(int valitunSailonListaindeksi, Boolean valittuTayttoaste) {
        TietokannanHallinta.paivitaTayttoasteet(getSailonTietokantaindeksi(valitunSailonListaindeksi), valittuTayttoaste);
    }

    public void lisaaEsineet(int valitunSailonListaindeksi, List<String> lisattavatEsineet) {
        TietokannanHallinta.lisaaEsineet(getSailonTietokantaindeksi(valitunSailonListaindeksi), lisattavatEsineet);
    }

    public void tyhjennaSailo(int valitunSailonListaindeksi) {
        TietokannanHallinta.tyhjennaSailo(getSailonTietokantaindeksi(valitunSailonListaindeksi), tietokantaindeksitJaSailonEsineet.keySet());
    }

    public void poistaSailo(int valitunSailonListaindeksi) {
        TietokannanHallinta.poistaSailo(getSailonTietokantaindeksi(valitunSailonListaindeksi));
    }

    public void poistaEsine(int valitunSailonListaindeksi, int valitunEsineenListaindeksi) {
        TietokannanHallinta.poistaEsine(getSailonTietokantaindeksi(valitunSailonListaindeksi),
                getEsineenTietokantaindeksi(valitunEsineenListaindeksi));
    }

    public Map<Integer, String> getTietokantaindeksitJaSailot() {
        return tietokantaindeksitJaSailot;
    }

    public Boolean getTilaa(int valitunSailonListaindeksi) {
        return tietokantaindeksitJaTayttoasteet.get(getSailonTietokantaindeksi(valitunSailonListaindeksi));
    }

    public String getSailonNimi(int valitunSailonListaindeksi) {
        return tietokantaindeksitJaSailot.get(getSailonTietokantaindeksi(valitunSailonListaindeksi));
    }

    public int getSailonTietokantaindeksi(int valitunSailonListaindeksi) {
        return sailojenListaJaTietokantaindeksit.get(valitunSailonListaindeksi);
    }

    public String getEsineenNimi(int valitunEsineenListaindeksi) {
        return tietokantaindeksitJaSailonEsineet.get(getEsineenTietokantaindeksi(valitunEsineenListaindeksi));
    }

    public int getEsineenTietokantaindeksi(int valitunEsineenListaindeksi) {
        return sailonEsineidenListaJaTietokantaindeksit.get(valitunEsineenListaindeksi);
    }

}
