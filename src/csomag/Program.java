package csomag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;

public class Program {

    private final List<String> sorok;
    private final List<Dolgozo> dolgozok;

    public Program() throws IOException {
        this.sorok = Files.readAllLines(Path.of("adatok.csv"));
        this.dolgozok = beolvas();
    }

    public static void main(String[] args) throws IOException {
        new Program().feladatok();
    }

    private void feladatok() throws IOException {
        Object eredmeny;

        eredmeny = kiKeresLegtobbet();
        System.out.printf("1. feladat:\nKi keresi a legtöbbet: %s\n\n", eredmeny);

        eredmeny = atlagFizetes();
        System.out.printf("2. feladat:\nMennyi az átlag: %.2f\n\n", eredmeny);

        eredmeny = mindenkiBudapestiE();
        System.out.printf("3. feladat:\nMindenki budapesti? %b\n\n", eredmeny);

        eredmeny = huszEvFelettiBudapesti();
        System.out.printf("4. feladat:\nVan 20 év feletti budapesti? %b\n\n", eredmeny);

        System.out.println("5. feladat:\nMilyen címek vannak eltárolva? ");

        for (String string : eltaroltCimek()) {
            System.out.println("\t" + string);
        }

        System.out.println("\n6. feladat:\nMelyik címen hányan laknak? ");
        for (Map.Entry<String, Integer> entry : melyikCimenHanyanLaknak().entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            System.out.println("\t" + key + ": " + value);
        }
        
        System.out.println("\n7. feladat:\nÍrd ki a \"nemBp.txt\" fájlba FEJLÉCCEL a nem budapestiek minden adatát!");
        nemBpAdataiFajlba();
    }

    private List<Dolgozo> beolvas() {
        List<Dolgozo> lista = new ArrayList<>();

        for (int i = 1; i < sorok.size(); i++) {
            lista.add(new Dolgozo(sorok.get(i)));
        }

        return lista;
    }

    private Dolgozo kiKeresLegtobbet() {
        int i = 1, maxIndex = 0;

        while (i < dolgozok.size()) {
            if (dolgozok.get(i).getFizetes() > dolgozok.get(maxIndex).getFizetes()) {
                maxIndex = i;
            }
            i++;
        }

        String nev = dolgozok.get(maxIndex).getNev();
        assert nev.equals("Xénia") : "1. hibás válasz";

        return dolgozok.get(maxIndex);
    }

    private double atlagFizetes() {
        int i = 0;
        double osszeg = 0;

        while (i < dolgozok.size()) {
            osszeg += dolgozok.get(i).getFizetes();
            i++;
        }

        double atlag = osszeg / dolgozok.size();
        assert atlag == 501_800: "2. hibás válasz";

        return atlag;
    }

    private boolean mindenkiBudapestiE() {
        int i = 0, N = dolgozok.size();
        boolean feltetel = false;

        while (i < N && !feltetel) {
            feltetel = !dolgozok.get(i).getCim().equals("Bp");
            i++;
        }

        boolean mind = (i >= N);
        boolean progTetel = i == 5;

        assert mind: "3. hibás válasz";
        assert progTetel : "3. hibás progTétel";

        return mind;
    }

    private boolean huszEvFelettiBudapesti() {
        int i = 0, N = dolgozok.size();
        boolean feltetel_1, feltetel_2;
        boolean feltetelek = false;

        while (i < N && !feltetelek) {
            feltetel_1 = dolgozok.get(i).getKor() > 20;
            feltetel_2 = dolgozok.get(i).getCim().equals("Bp");
            feltetelek = (feltetel_1 && feltetel_2);
            i++;
        }

        boolean van = i < N;
        boolean progTetel = i==1;

        assert van : "4. hibás válasz";
        assert progTetel : "4. hibás progTétel";

        return van;
    }

    private Set<String> eltaroltCimek() {
        Set<String> cimek = new HashSet<>();

        for (Dolgozo dolgozo : dolgozok) {
            cimek.add(dolgozo.getCim());
        }

        assert cimek.size() == 1 : "Hibás méret";
        assert cimek.contains("Bp") : "5. hibás adat";

        return cimek;
    }

    private Map<String, Integer> melyikCimenHanyanLaknak() {
        Map<String, Integer> m = new HashMap<>();

        for (Dolgozo dolgozo : dolgozok) {
            String cim = dolgozo.getCim();

            if (m.containsKey(cim)){
                int ertek = m.get(cim) + 1;
                m.put(cim, ertek);
            } else {
                m.put(cim, 1);
            }
        }

        assert m.size() == 1 : "6. hibás méret";
        assert m.containsKey("Bp") : "6. hibás kulcs";
        assert m.get("Bp") == 5 : "6. hibás érték";

        return m;
    }

    private void nemBpAdataiFajlba() throws IOException {
        List<String> nemBpLista = new ArrayList<>();
        String fejlec = "Név;Kor;Cím;Fizetés";

        nemBpLista.add(fejlec);

        for (Dolgozo dolgozo : dolgozok) {
            if (!dolgozo.getCim().equals("Bp")) {
                String nev = dolgozo.getNev();
                String kor = Integer.toString(dolgozo.getKor());
                String cim = dolgozo.getCim();
                String fizetes = Double.toString(dolgozo.getFizetes());

                nemBpLista.add(nev + ";" + kor + ";" + cim + ";" + fizetes);
            }
        }
        Files.write(Path.of("nemBp.txt"), nemBpLista);
        System.out.println("\tFájlba írva!");
    }

}
