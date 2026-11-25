package da_qlxe;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DangNhapFrame extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnDangNhap, btnTaiKhoanMau;

    public DangNhapFrame() {
        setTitle("Đăng Nhập - Quản Lý Bãi Gửi Xe");
        setSize(400, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Canh giữa màn hình
        setResizable(false);

        // Khởi tạo giao diện
        JPanel p = new JPanel(new GridLayout(4, 2, 10, 15));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtUser = new JTextField("admin");
        txtPass = new JPasswordField("admin123");

        btnDangNhap = new JButton("Đăng Nhập");
        btnDangNhap.setBackground(new Color(33, 150, 243));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFont(new Font("Arial", Font.BOLD, 14));

        btnTaiKhoanMau = new JButton("Tài Khoản Mẫu");

        p.add(new JLabel("Tên Đăng Nhập:"));
        p.add(txtUser);
        p.add(new JLabel("Mật Khẩu:"));
        p.add(txtPass);
        p.add(btnTaiKhoanMau);
        p.add(btnDangNhap);

        add(p);

        // Sự kiện tự điền tài khoản mẫu
        btnTaiKhoanMau.addActionListener(e -> {
            txtUser.setText("admin");
            txtPass.setText("admin123");
        });

        // Sự kiện bấm nút Đăng Nhập
        btnDangNhap.addActionListener(e -> xuLyDangNhap());

        // Phím Enter để đăng nhập
        txtPass.addActionListener(e -> xuLyDangNhap());
    }

    private void xuLyDangNhap() {
        String u = txtUser.getText().trim();
        String p = new String(txtPass.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!");
            return;
        }

        NhanVien nv = kiemTraDangNhap(u, p);
        if (nv != null) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công! Xin chào " + nv.getTenNV() + " (" + nv.getVaiTro() + ")");
            this.dispose(); // Đóng form đăng nhập
            new QuanLyXeFrame(nv).setVisible(true); // Mở form chính
        } else {
            // Nếu không kết nối được SQL, hỗ trợ đăng nhập ngoại tuyến để tiện vấn đáp/báo cáo
            if (u.equals("admin") && p.equals("admin123")) {
                NhanVien adminFallback = new NhanVien("NV01", "Nguyễn Văn Quản Lý", "admin", "admin123", "Admin");
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công (Chế độ demo)! Xin chào Admin.");
                this.dispose();
                new QuanLyXeFrame(adminFallback).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Hàm kiểm tra trong CSDL SQL Server
    private NhanVien kiemTraDangNhap(String user, String pass) {
        NhanVien nv = null;
        try {
            Connection conn = KetNoiDB.getKetNoi();
            if (conn != null) {
                String sql = "SELECT * FROM NhanVien WHERE TenDangNhap = ? AND MatKhau = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, user);
                ps.setString(2, pass);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    nv = new NhanVien(
                            rs.getString("MaNV"),
                            rs.getString("TenNV"),
                            rs.getString("TenDangNhap"),
                            rs.getString("MatKhau"),
                            rs.getString("VaiTro")
                    );
                }
                rs.close();
                ps.close();
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("Lỗi kiểm tra đăng nhập: " + e.getMessage());
        }
        return nv;
    }

    public static void main(String[] args) {
        // Chạy chương trình từ DangNhapFrame
        SwingUtilities.invokeLater(() -> new DangNhapFrame().setVisible(true));
    }
}
