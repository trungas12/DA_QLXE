package da_qlxe;

public class TheXe {
    private String maThe;
    private String trangThai; // "Rảnh", "Đang Gửi"

    public TheXe() {}

    public TheXe(String maThe, String trangThai) {
        this.maThe = maThe;
        this.trangThai = trangThai;
    }

    public String getMaThe() { return maThe; }
    public void setMaThe(String maThe) { this.maThe = maThe; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return maThe;
    }
}
