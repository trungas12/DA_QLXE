package da_qlxe;

public class LoaiXe {
    private String maLoaiXe;
    private String tenLoaiXe;
    private double giaVe;

    public LoaiXe() {}

    public LoaiXe(String maLoaiXe, String tenLoaiXe, double giaVe) {
        this.maLoaiXe = maLoaiXe;
        this.tenLoaiXe = tenLoaiXe;
        this.giaVe = giaVe;
    }

    public String getMaLoaiXe() { return maLoaiXe; }
    public void setMaLoaiXe(String maLoaiXe) { this.maLoaiXe = maLoaiXe; }

    public String getTenLoaiXe() { return tenLoaiXe; }
    public void setTenLoaiXe(String tenLoaiXe) { this.tenLoaiXe = tenLoaiXe; }

    public double getGiaVe() { return giaVe; }
    public void setGiaVe(double giaVe) { this.giaVe = giaVe; }

    @Override
    public String toString() {
        return tenLoaiXe + " - " + (long) giaVe + " đ";
    }
}
