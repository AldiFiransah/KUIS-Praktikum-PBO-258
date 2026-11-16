/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class SistemHotel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String namaHotel;
    private HashMap<String, Tamu> daftarTamu;
    private HashMap<String, Kamar> daftarKamar;
    private ArrayList<Transaksi> riwayatTransaksi;

    public SistemHotel(String namaHotel) {
        this.namaHotel = namaHotel;
        this.daftarTamu = new HashMap<>();
        this.daftarKamar = new HashMap<>();
        this.riwayatTransaksi = new ArrayList<>();
    }

    public void tambahTamu(String id, String nama) {
        Tamu tamu = new Tamu(id, nama);
        daftarTamu.put(id, tamu);
    }

    public void tambahKamar(String kode, String tipeKamar, int kuota) {
        Kamar kamar = new Kamar(kode, tipeKamar, kuota);
        daftarKamar.put(kode, kamar);
    }

    public void jalankanTransaksiDariFile(String namaFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(namaFile))) {
            String baris;
            int nomorBaris = 0;

            while ((baris = reader.readLine()) != null) {
                nomorBaris++;
                baris = baris.trim();
                if (baris.isEmpty()) {
                    continue;
                }

                try {
                    String[] bagian = baris.split("/");
                    if (bagian.length < 3) {
                        throw new IllegalArgumentException("Format baris tidak valid: " + baris);
                    }

                    String idTamu = bagian[0].replaceFirst("^ID-", "").trim();
                    String kodeKamar = bagian[1].replaceFirst("^KD-", "").trim();
                    int jumlah = Integer.parseInt(bagian[2].replaceFirst("^JML-", "").trim());

                    Tamu tamu = daftarTamu.get(idTamu);
                    if (tamu == null) {
                        throw new TamuTidakDitemukanException("ID Tamu '" + idTamu + "' tidak terdaftar di sistem!");
                    }

                    Kamar kamar = daftarKamar.get(kodeKamar);
                    if (kamar == null) {
                        throw new IllegalArgumentException("Kode Kamar '" + kodeKamar + "' tidak ditemukan!");
                    }

                    kamar.kurangiKuota(jumlah);

                    Transaksi trx = new Transaksi(tamu, kamar, jumlah);
                    riwayatTransaksi.add(trx);
                    System.out.println("[BARIS " + nomorBaris + " SUKSES] " + trx + " | " + kamar.getDeskripsi());

                } catch (TamuTidakDitemukanException | KuotaTidakCukupException e) {
                    System.out.println("[BARIS " + nomorBaris + " GAGAL - CUSTOM EXCEPTION] " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("[BARIS " + nomorBaris + " GAGAL - ERROR PARSING] " + e.getMessage());
                }
            }
        }
    }

    public void simpanLaporanAkhir() {
        System.out.println("\n--- MENYIMPAN LAPORAN AKHIR & SERIALIZATION ---");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("snapshot.obj"))) {
            oos.writeObject(this);
            System.out.println("[OK] Status memori berhasil diserialisasi ke file 'snapshot.obj'.");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal melakukan serialisasi: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("laporan.txt"))) {
            for (String idTamu : daftarTamu.keySet()) {
                writer.println(idTamu);
            }
            System.out.println("[OK] Daftar kode ID tamu berhasil ditulis ke 'laporan.txt'.");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menulis laporan.txt: " + e.getMessage());
        }
    }
}
