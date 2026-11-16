/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


public class Kamar extends EntitasHotel {
    private static final long serialVersionUID = 1L;
    private int kuota;

    public Kamar(String kode, String tipeKamar, int kuota) {
        super(kode, tipeKamar);
        this.kuota = kuota;
    }

    public int getKuota() {
        return kuota;
    }

    public void kurangiKuota(int jumlah) throws KuotaTidakCukupException {
        if (jumlah <= 0) {
            throw new KuotaTidakCukupException("Jumlah pemesanan harus lebih dari 0.");
        }
        if (this.kuota < jumlah) {
            throw new KuotaTidakCukupException(
                "Kuota " + getNama() + " (" + getId() + ") tidak cukup! Diminta: " + jumlah + ", Tersisa: " + this.kuota
            );
        }
        this.kuota -= jumlah;
    }

    @Override
    public String getDeskripsi() {
        return "Kamar [" + getId() + "] " + getNama() + " (Sisa Kuota: " + kuota + ")";
    }
}
