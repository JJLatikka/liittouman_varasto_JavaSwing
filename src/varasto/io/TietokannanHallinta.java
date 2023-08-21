package varasto.io;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.sqlite.SQLiteConfig;

public class TietokannanHallinta {

    public static void luoTaulut() {
        try {
            lauseke().execute("CREATE TABLE Säilöt (id INTEGER PRIMARY KEY, nimi TEXT UNIQUE)");
            lauseke().execute("CREATE TABLE Esineet (id INTEGER PRIMARY KEY, nimi TEXT)");
            lauseke().execute("CREATE TABLE Täyttöasteet (säilö_id INTEGER UNIQUE REFERENCES Säilöt ON DELETE CASCADE, "
                    + "tilaa BOOLEAN DEFAULT true)");
            lauseke().execute("CREATE TABLE Sijainnit (säilö_id INTEGER REFERENCES Säilöt, "
                    + "esine_id INTEGER UNIQUE REFERENCES Esineet ON DELETE CASCADE)");
        } catch (SQLException ex) {
        }
    }

    public static Map<Integer, String> indeksitJaSailot() {
        Map<Integer, String> indeksitJaSailot = new TreeMap<>();
        try {
            ResultSet r = lauseke().executeQuery("SELECT id, nimi FROM Säilöt");
            while (r.next()) {
                indeksitJaSailot.put(r.getInt(1), r.getString(2));
            }
        } catch (SQLException ex) {
        }
        return indeksitJaSailot;
    }

    public static Map<Integer, Boolean> indeksitJaTayttoasteet() {
        Map<Integer, Boolean> indeksitJaTayttoasteet = new HashMap<>();
        try {
            ResultSet r = lauseke().executeQuery("SELECT säilö_id, tilaa FROM Täyttöasteet");
            while (r.next()) {
                indeksitJaTayttoasteet.put(r.getInt(1), r.getBoolean(2));
            }
        } catch (SQLException ex) {
        }
        return indeksitJaTayttoasteet;
    }

    public static Map<Integer, String> indeksitJaSailonEsineet(int sailo_id) {
        Map<Integer, String> indeksitJaSailonEsineet = new TreeMap<>();
        try {
            ResultSet r = lauseke().executeQuery("SELECT S.esine_id, E.nimi FROM Sijainnit S, Esineet E "
                    + "WHERE S.esine_id=E.id AND S.säilö_id=" + sailo_id);
            while (r.next()) {
                indeksitJaSailonEsineet.put(r.getInt(1), r.getString(2));
            }
        } catch (SQLException ex) {
        }
        return indeksitJaSailonEsineet;
    }

    public static Map<Integer, List<String>> sailoistaLoydetytEsineet(String hakusana) {
        Map<Integer, List<String>> sailoistaLoydetytEsineet = new TreeMap<>();
        try {
            ResultSet r = lauseke().executeQuery("SELECT S.id, E.nimi FROM Säilöt S, Esineet E LEFT JOIN Sijainnit "
                    + "WHERE S.id=säilö_id AND E.id=esine_id AND E.nimi LIKE '%" + hakusana + "%'");
            while (r.next()) {
                int avain = r.getInt(1);
                sailoistaLoydetytEsineet.putIfAbsent(avain, new ArrayList<>());
                sailoistaLoydetytEsineet.get(avain).add(r.getString(2));
            }
        } catch (SQLException ex) {
        }
        return sailoistaLoydetytEsineet;
    }

    public static void lisaaSailo(String nimi) {
        try {
            lauseke().executeUpdate("INSERT INTO Säilöt (nimi) VALUES ('" + nimi + "')");
            lauseke().executeUpdate("INSERT INTO Täyttöasteet (säilö_id) VALUES ((SELECT MAX(id) FROM Säilöt))");
        } catch (SQLException ex) {
        }
    }

    public static void lisaaEsineet(int sailonIndeksi, List<String> lisattavat) {
        lisattavat.forEach(lisattava -> {
            try {
                lauseke().executeUpdate("INSERT INTO Esineet (nimi) VALUES ('" + lisattava + "')");
                lauseke().executeUpdate("INSERT INTO Sijainnit (säilö_id, esine_id) VALUES (" + sailonIndeksi
                        + ", (SELECT MAX(id) FROM Esineet))");
            } catch (SQLException ex) {
            }
        });
    }

    public static void paivitaTayttoasteet(int sailonIndeksi, Boolean uusiTayttoaste) {
        try {
            lauseke().executeUpdate("UPDATE Täyttöasteet SET tilaa=" + uusiTayttoaste + " WHERE säilö_id=" + sailonIndeksi);
        } catch (SQLException ex) {
        }
    }

    public static void tyhjennaSailo(int sailonIndeksi, Set<Integer> sailonEsineidenIndeksit) {
        sailonEsineidenIndeksit.forEach(indeksi -> {
            try {
                lauseke().executeUpdate("DELETE FROM Esineet WHERE id=" + indeksi);
            } catch (SQLException ex) {
            }
        });
        try {
            lauseke().executeUpdate("UPDATE Täyttöasteet SET tilaa=true WHERE säilö_id=" + sailonIndeksi);
        } catch (SQLException ex) {
        }
    }

    public static void poistaSailo(int sailonIndeksi) {
        try {
            lauseke().executeUpdate("DELETE FROM Säilöt WHERE id=" + sailonIndeksi);
        } catch (SQLException ex) {
        }
    }

    public static void poistaEsine(int sailonIndeksi, int esineenIndeksi) {
        try {
            lauseke().executeUpdate("DELETE FROM Esineet WHERE id=" + esineenIndeksi);
            lauseke().executeUpdate("UPDATE Täyttöasteet SET tilaa=true WHERE säilö_id=" + sailonIndeksi);
        } catch (SQLException e) {
        }
    }

    public static Statement lauseke() throws SQLException {
        return yhteys().createStatement();
    }

    public static Connection yhteys() throws SQLException {
        SQLiteConfig maarittely = new SQLiteConfig();
        maarittely.enforceForeignKeys(true);
        Connection yhteys = DriverManager.getConnection("jdbc:sqlite:Varasto.db", maarittely.toProperties());
        return yhteys;
    }

}
