package id.rumahawan.manajemenkomplain.Object;

public class User {
    private String email;
    private String password;
    private String nama;
    private String jenis;

    public User(String email, String password, String nama, String jenis) {
        this.email = email;
        this.password = password;
        this.nama = nama;
        this.jenis = jenis;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}
