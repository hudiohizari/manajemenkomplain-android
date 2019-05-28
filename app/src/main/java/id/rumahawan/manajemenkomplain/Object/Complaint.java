package id.rumahawan.manajemenkomplain.Object;

public class Complaint {
    private int id;
    private String judul;
    private String deskripsi;
    private String kategori;
    private String status;
    private Long created;
    private String email;

    public Complaint(int id, String judul, String deskripsi, String kategori, String status, Long created, String email) {
        this.id = id;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        this.status = status;
        this.created = created;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
