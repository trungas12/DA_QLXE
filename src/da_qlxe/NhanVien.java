package da_qlxe;

public class NhanVien {
    private String maNV;
    private String tenNV;
    private String tenDangNhap;
    private String matKhau;
    private String vaiTro; // "Admin" hoặc "Nhân Viên"

    public NhanVien() {}

    public NhanVien(String maNV, String tenNV, String tenDangNhap, String matKhau, String vaiTro) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
    }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
}
