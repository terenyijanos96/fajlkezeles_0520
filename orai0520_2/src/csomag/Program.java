package csomag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Program {

    List<String> sorok;
    List<Dolgozo> dolgozok;
    List<String> cimek;

    public static void main(String[] args) throws IOException, ParseException {
        new Program().feladatok();
    }

    public Program() throws IOException, ParseException {
        this.sorok = Files.readAllLines(Path.of("adatok.csv"));
        this.dolgozok = dolgozok = new ArrayList<>();
        beolvas();
        this.cimek = new ArrayList<>();

    }

    private void feladatok() throws IOException, ParseException {
        Dolgozo d1 = kiKeresLegtobbet();
        System.out.printf("Ki keresi a legtöbbet: %s\n", d1);

        double atlagF = atlagFizetes();
        System.out.printf("Mennyi az átlag: %.2f\n", atlagF);

        boolean mbe = mindenkiBudapestiE();
        System.out.printf("Mindenki budapesti? %b\n", mbe);

        boolean hefb = huszEvFelettiBudapesti();
        System.out.printf("Van 20 év feletti budapesti? %b\n", hefb);

        System.out.println("Milyen címek vannak eltárolva? ");

        for (String string : eltaroltCimek()) {
            System.out.println("\t" + string);
        }

        System.out.println("Melyik címen hányan laknak? ");
        for (Map.Entry<String, Integer> entry : melyikCimenHanyanLaknak().entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            System.out.println("\t" + key + ": " + value);
        }
        
        System.out.println("Írd ki a \"nemBp.txt\" fájlba FEJLÉCCEL a nem budapestiek minden adatát!");
        nemBpAdataiFajlba();
    }

    private void beolvas() throws IOException, ParseException {
        for (int i = 1; i < sorok.size(); i++) {
            this.dolgozok.add(new Dolgozo(sorok.get(i)));
        }
    }

    private Dolgozo kiKeresLegtobbet() {
        int i = 1, maxIndex = 0;

        while (i < dolgozok.size()) {
            if (dolgozok.get(i).getFizetes() > dolgozok.get(maxIndex).getFizetes()) {
                maxIndex = i;
            }
            i++;
        }

        return dolgozok.get(maxIndex);
    }

    private double atlagFizetes() {
        int i = 0;
        double osszeg = 0;

        while (i < dolgozok.size()) {
            osszeg += dolgozok.get(i).getFizetes();
            i++;
        }

        return osszeg / dolgozok.size();
    }

    private boolean mindenkiBudapestiE() {
        int i = 0, N = dolgozok.size();
        boolean feltetel = false;

        while (i < N && !feltetel) {
            feltetel = !dolgozok.get(i).getCim().equals("Budapest");
            i++;
        }
        return !(i < N);
    }

    private boolean huszEvFelettiBudapesti() {
        int i = 0, N = dolgozok.size();
        boolean feltetelek = false;

        while (i < N && !feltetelek) {
            boolean feltetel_1 = dolgozok.get(i).getKor() > 20;
            boolean feltetel_2 = dolgozok.get(i).getCim().equals("Budapest");
            feltetelek = feltetel_1 && feltetel_2;
            i++;
        }
        return i < N;
    }

    private List<String> eltaroltCimek() {

        for (Dolgozo dolgozo : dolgozok) {
            cimek.add(dolgozo.getCim());
        }

        return cimek;
    }

    private Map<String, Integer> melyikCimenHanyanLaknak() {
        Map<String, Integer> m = new HashMap<>();
        for (String cim : cimek) {
            if (m.containsKey(cim)) {
                int ertek = m.get(cim) + 1;
                m.put(cim, ertek);
            } else {
                m.put(cim, 1);
            }

        }

        return m;
    }

    private void nemBpAdataiFajlba() throws IOException {
        List<String> nemBpLista = new ArrayList<>();
        String fejlec = "Név;Kor;Cím;Fizetés";

        nemBpLista.add(fejlec);

        for (Dolgozo dolgozo : dolgozok) {
            if (!dolgozo.getCim().equals("Budapest")) {
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
