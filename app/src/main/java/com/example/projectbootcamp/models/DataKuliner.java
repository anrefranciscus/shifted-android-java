package com.example.projectbootcamp.models;

public class DataKuliner {
    String namaKuliner;
    String keterangan;

    public DataKuliner(String namaKuliner, String keterangan){
        this.namaKuliner = namaKuliner;
        this.keterangan = keterangan;
    }

    public String getNamaKuliner(){
        return  namaKuliner;
    }

    public void setNamaKuliner(String namaKuliner){
        this.namaKuliner = namaKuliner;
    }

    public String getKeterangan(){
        return keterangan;
    }

    public void setKeterangan(String keterangan){
        this.keterangan = keterangan;
    }
}
