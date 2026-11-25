package da_qlxe;

import java.sql.Timestamp;

public class LuotGuiXe {
    private int maLuotGui;
    private String maThe;
    private String maLoaiXe;
    private String bienSo;
    private Timestamp thoiGianVao;
    private Timestamp thoiGianRa;
    private double tienThu;
    private String trangThai; // "Trong Bãi", "Đã Thanh Toán"
    private String maNV;

    // Tên phụ hiển thị
    private String tenLoaiXe;

    public LuotGuiXe() {}

    public LuotGuiXe(int maLuotGui, String maThe, String maLoaiXe, String bienSo,
                      Timestamp thoiGianVao, Timestamp thoiGianRa, double tienThu,
                      String trangThai, String maNV) {
        this.maLuotGui = maLuotGui;
        this.maThe = maThe;
        this.maLoaiXe = maLoaiXe;
        this.bienSo = bienSo;
        this.thoiGianVao = thoiGianVao;
        this.thoiGianRa = thoiGianRa;
        this.tienThu = tienThu;
        this.trangThai = trangThai;
        this.maNV = maNV;
    }

    public int getMaLuotGui() { return maLuotGui; }
    public void setMaLuotGui(int maLuotGui) { this.maLuotGui = maLuotGui; }

    public String getMaThe() { return maThe; }
    public void setMaThe(String maThe) { this.maThe = maThe; }

    public String getMaLoaiXe() { return maLoaiXe; }
    public void setMaLoaiXe(String maLoaiXe) { this.maLoaiXe = maLoaiXe; }

    public String getBienSo() { return bienSo; }
    public void setBienSo(String bienSo) { this.bienSo = bienSo; }

    public Timestamp getThoiGianVao() { return thoiGianVao; }
    public void setThoiGianVao(Timestamp thoiGianVao) { this.thoiGianVao = thoiGianVao; }

    public Timestamp getThoiGianRa() { return thoiGianRa; }
    public void setThoiGianRa(Timestamp thoiGianRa) { this.thoiGianRa = thoiGianRa; }

    public double getTienThu() { return tienThu; }
    public void setTienThu(double tienThu) { this.tienThu = tienThu; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getTenLoaiXe() { return tenLoaiXe; }
    public void setTenLoaiXe(String tenLoaiXe) { this.tenLoaiXe = tenLoaiXe; }
}
