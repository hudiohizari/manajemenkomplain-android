package id.rumahawan.manajemenkomplain.RecyclerView;

public class ComplaintList {
    private int komplainId;
    private String nama;
    private int drawableStatus;

    public ComplaintList(){}

    public ComplaintList(int komplainId, String nama, int drawableStatus) {
        this.komplainId = komplainId;
        this.nama = nama;
        this.drawableStatus = drawableStatus;
    }

    public int getKomplainId() {
        return komplainId;
    }

    public void setKomplainId(int komplainId) {
        this.komplainId = komplainId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    int getDrawableStatus() {
        return drawableStatus;
    }

    public void setDrawableStatus(int drawableStatus) {
        this.drawableStatus = drawableStatus;
    }
}
