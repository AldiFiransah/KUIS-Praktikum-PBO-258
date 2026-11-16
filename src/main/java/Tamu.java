/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


public class Tamu extends EntitasHotel {
    private static final long serialVersionUID = 1L;

    public Tamu(String id, String nama) {
        super(id, nama);
    }

    @Override
    public String getDeskripsi() {
        return "Tamu [" + getId() + "] - " + getNama();
    }
}