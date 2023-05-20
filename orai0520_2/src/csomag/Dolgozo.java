
package csomag;

public class Dolgozo {

    String nev;
    int kor;
    String cim;
    double fizetes;

    public Dolgozo(String sor) {
        String[] s = sor.strip().split(";");
        this.nev = s[0];
        this.kor = Integer.parseInt(s[1]);
        this.cim = s[2];
        this.fizetes = Double.parseDouble(s[3]);
    }

    public String getNev() {
        return nev;
    }

    public int getKor() {
        return kor;
    }

    public String getCim() {
        return cim;
    }

    public double getFizetes() {
        return fizetes;
    }

    @Override
    public String toString() {
        return "Dolgozo{" + "nev=" + nev + ", kor=" + kor + ", cim=" + cim + ", fizetes=" + fizetes + '}';
    }

    
    
}
