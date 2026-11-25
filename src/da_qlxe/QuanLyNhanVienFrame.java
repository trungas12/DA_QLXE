package da_qlxe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QuanLyNhanVienFrame extends JFrame {
  private JTextField txtMaNV, txtTenNV, txtTenDangNhap, txtTimKiem;
  private JPasswordField txtMatKhau;
  private JComboBox<String> cboVaiTro;
  private JTable tblNhanVien;
  private DefaultTableModel modelBang;
  private NhanVien nvDangNhap;

  // Danh sách lưu tạm nếu mất mạng CSDL
  private static final List<NhanVien> mockList = new ArrayList<>();
  static {
  mockList.add(new NhanVien("NV01", "Nguyễn Văn Quản Lý", "admin", "admin123", "Admin"));
  mockList.add(new NhanVien("NV02", "Trần Thị Thu", "nhanvien1", "123456", "Nhân Viên"));
  mockList.add(new NhanVien("NV03", "Lê Văn Hùng", "nhanvien2", "123456", "Nhân Viên"));
  }

  public QuanLyNhanVienFrame() {
  this(new NhanVien("NV01", "Admin", "admin", "admin123", "Admin"));
  }

  public QuanLyNhanVienFrame(NhanVien nv) {
  this.nvDangNhap = nv;
  khoiTaoGiaoDien();
  taiBangNhanVien("");
  }

  private void khoiTaoGiaoDien() {
  setTitle("QUẢN LÝ NHÂN VIÊN - ĐỒ ÁN BÃI GỬI XE");
  setSize(950, 580);
  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  setLocationRelativeTo(null);

  JPanel pChinh = new JPanel(new BorderLayout(10, 10));
  pChinh.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

  // 1. HEADER
  JPanel pHeader = new JPanel(new BorderLayout());
  pHeader.setBackground(new Color(41, 128, 185));
  pHeader.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

  JLabel lblTieuDe = new JLabel("DANH MỤC QUẢN LÝ NHÂN VIÊN");
  lblTieuDe.setFont(new Font("Arial", Font.BOLD, 18));
  lblTieuDe.setForeground(Color.WHITE);

  JButton btnQuayLai = new JButton("Quay Lại Quản Lý Xe");
  btnQuayLai.addActionListener(e -> {
  this.dispose();
  new QuanLyXeFrame(nvDangNhap).setVisible(true);
  });

  pHeader.add(lblTieuDe, BorderLayout.WEST);
  pHeader.add(btnQuayLai, BorderLayout.EAST);
  pChinh.add(pHeader, BorderLayout.NORTH);

  // 2. FORM BÊN TRÁI
  JPanel pTrai = new JPanel();
  pTrai.setLayout(new BoxLayout(pTrai, BoxLayout.Y_AXIS));
  pTrai.setPreferredSize(new Dimension(320, 0));
  pTrai.setBorder(BorderFactory.createTitledBorder("Thông Tin Nhân Viên"));

  txtMaNV = new JTextField();
  txtTenNV = new JTextField();
  txtTenDangNhap = new JTextField();
  txtMatKhau = new JPasswordField();
  cboVaiTro = new JComboBox<>(new String[]{"Nhân Viên", "Admin"});
  txtTimKiem = new JTextField();

  pTrai.add(new JLabel("Mã Nhân Viên (VD: NV04):"));
  pTrai.add(txtMaNV);
  pTrai.add(Box.createVerticalStrut(8));

  pTrai.add(new JLabel("Họ Và Tên:"));
  pTrai.add(txtTenNV);
  pTrai.add(Box.createVerticalStrut(8));

  pTrai.add(new JLabel("Tên Đăng Nhập:"));
  pTrai.add(txtTenDangNhap);
  pTrai.add(Box.createVerticalStrut(8));

  pTrai.add(new JLabel("Mật Khẩu:"));
  pTrai.add(txtMatKhau);
  pTrai.add(Box.createVerticalStrut(8));

  pTrai.add(new JLabel("Vai Trò / Quyền Hạn:"));
  pTrai.add(cboVaiTro);
  pTrai.add(Box.createVerticalStrut(15));

  // 4 NÚT THÊM, SỬA, XÓA, LÀM MỚI
  JPanel pNhomNut = new JPanel(new GridLayout(2, 2, 6, 6));
  JButton btnThem = new JButton("Thêm Mới");
  btnThem.setBackground(new Color(46, 125, 50));
  btnThem.setForeground(Color.WHITE);
  btnThem.setFont(new Font("Arial", Font.BOLD, 12));

  JButton btnSua = new JButton("Sửa Đổi");
  btnSua.setBackground(new Color(255, 152, 0));
  btnSua.setForeground(Color.WHITE);
  btnSua.setFont(new Font("Arial", Font.BOLD, 12));

  JButton btnXoa = new JButton("Xóa Bỏ");
  btnXoa.setBackground(new Color(211, 47, 47));
  btnXoa.setForeground(Color.WHITE);
  btnXoa.setFont(new Font("Arial", Font.BOLD, 12));

  JButton btnLamMoi = new JButton("Làm Mới");

  pNhomNut.add(btnThem);
  pNhomNut.add(btnSua);
  pNhomNut.add(btnXoa);
  pNhomNut.add(btnLamMoi);
  pTrai.add(pNhomNut);

  pTrai.add(Box.createVerticalStrut(20));
  pTrai.add(new JLabel("Tìm Kiếm Theo Tên / Mã NV:"));
  pTrai.add(txtTimKiem);
  pTrai.add(Box.createVerticalStrut(6));

  JButton btnTimKiem = new JButton("Tìm Kiếm Ngay");
  btnTimKiem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
  pTrai.add(btnTimKiem);

  pChinh.add(pTrai, BorderLayout.WEST);

  // 3. BẢNG DỮ LIỆU BÊN PHẢI
  JPanel pPhai = new JPanel(new BorderLayout(5, 5));
  pPhai.setBorder(BorderFactory.createTitledBorder("Danh Sách Nhân Viên (Click dòng để Sửa/Xóa)"));

  String[] cot = {"Mã NV", "Họ Và Tên", "Tên Đăng Nhập", "Mật Khẩu", "Vai Trò"};
  modelBang = new DefaultTableModel(cot, 0) {
  @Override
  public boolean isCellEditable(int r, int c) { return false; }
  };
  tblNhanVien = new JTable(modelBang);
  tblNhanVien.setRowHeight(25);
  pPhai.add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);

  pChinh.add(pPhai, BorderLayout.CENTER);
  add(pChinh);

  // CLICK BẢNG TỰ ĐIỀN THÔNG TIN
  tblNhanVien.getSelectionModel().addListSelectionListener(e -> {
  int row = tblNhanVien.getSelectedRow();
  if (row >= 0) {
  txtMaNV.setText((String) modelBang.getValueAt(row, 0));
  txtTenNV.setText((String) modelBang.getValueAt(row, 1));
  txtTenDangNhap.setText((String) modelBang.getValueAt(row, 2));
  txtMatKhau.setText((String) modelBang.getValueAt(row, 3));
  String vaiTro = (String) modelBang.getValueAt(row, 4);
  cboVaiTro.setSelectedItem(vaiTro);
  txtMaNV.setEditable(false); // Không sửa khóa chính
  }
  });

  // SỰ KIỆN NÚT BẤM
  btnThem.addActionListener(e -> xuLyThem());
  btnSua.addActionListener(e -> xuLySua());
  btnXoa.addActionListener(e -> xuLyXoa());
  btnTimKiem.addActionListener(e -> taiBangNhanVien(txtTimKiem.getText().trim()));
  txtTimKiem.addActionListener(e -> taiBangNhanVien(txtTimKiem.getText().trim()));
  btnLamMoi.addActionListener(e -> lamMoiForm());
  }

  private void lamMoiForm() {
  txtMaNV.setText("");
  txtMaNV.setEditable(true);
  txtTenNV.setText("");
  txtTenDangNhap.setText("");
  txtMatKhau.setText("");
  txtTimKiem.setText("");
  cboVaiTro.setSelectedIndex(0);
  tblNhanVien.clearSelection();
  taiBangNhanVien("");
  }

  private void taiBangNhanVien(String tuKhoa) {
  modelBang.setRowCount(0);
  boolean coSQL = false;

  try {
  Connection conn = KetNoiDB.getKetNoi();
  if (conn != null) {
  String sql = "SELECT * FROM NhanVien";
  if (!tuKhoa.isEmpty()) {
  sql += "WHERE MaNV LIKE ? OR TenNV LIKE ? OR TenDangNhap LIKE ?";
  }
  sql += "ORDER BY MaNV";
  PreparedStatement ps = conn.prepareStatement(sql);
  if (!tuKhoa.isEmpty()) {
  String pattern = "%" + tuKhoa + "%";
  ps.setString(1, pattern);
  ps.setString(2, pattern);
  ps.setString(3, pattern);
  }
  ResultSet rs = ps.executeQuery();
  while (rs.next()) {
  coSQL = true;
  modelBang.addRow(new Object[]{
  rs.getString("MaNV"),
  rs.getString("TenNV"),
  rs.getString("TenDangNhap"),
  rs.getString("MatKhau"),
  rs.getString("VaiTro")
  });
  }
  rs.close();
  ps.close();
  conn.close();
  }
  } catch (Exception e) {
  System.err.println("Lỗi tải nhân viên: " + e.getMessage());
  }

  // Dự phòng dữ liệu mẫu nếu chưa có kết nối CSDL
  if (!coSQL) {
  for (NhanVien nv : mockList) {
  if (tuKhoa.isEmpty() || nv.getMaNV().toLowerCase().contains(tuKhoa.toLowerCase()) || nv.getTenNV().toLowerCase().contains(tuKhoa.toLowerCase())) {
  modelBang.addRow(new Object[]{
  nv.getMaNV(), nv.getTenNV(), nv.getTenDangNhap(), nv.getMatKhau(), nv.getVaiTro()
  });
  }
  }
  }
  }

  private void xuLyThem() {
  String ma = txtMaNV.getText().trim();
  String ten = txtTenNV.getText().trim();
  String user = txtTenDangNhap.getText().trim();
  String pass = new String(txtMatKhau.getPassword());
  String vaiTro = (String) cboVaiTro.getSelectedItem();

  if (ma.isEmpty() || ten.isEmpty() || user.isEmpty() || pass.isEmpty()) {
  JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tất cả thông tin!", "Thông Báo", JOptionPane.WARNING_MESSAGE);
  return;
  }

  try {
  Connection conn = KetNoiDB.getKetNoi();
  if (conn != null) {
  String checkSql = "SELECT COUNT(*) FROM NhanVien WHERE MaNV = ?";
  PreparedStatement psCheck = conn.prepareStatement(checkSql);
  psCheck.setString(1, ma);
  ResultSet rs = psCheck.executeQuery();
  if (rs.next() && rs.getInt(1) > 0) {
  JOptionPane.showMessageDialog(this, "Mã nhân viên " + ma + "đã tồn tại!", "Trùng Khóa Chính", JOptionPane.ERROR_MESSAGE);
  conn.close();
  return;
  }

  String insertSql = "INSERT INTO NhanVien (MaNV, TenNV, TenDangNhap, MatKhau, VaiTro) VALUES (?, ?, ?, ?, ?)";
  PreparedStatement ps = conn.prepareStatement(insertSql);
  ps.setString(1, ma);
  ps.setString(2, ten);
  ps.setString(3, user);
  ps.setString(4, pass);
  ps.setString(5, vaiTro);
  ps.executeUpdate();
  ps.close();
  conn.close();

  JOptionPane.showMessageDialog(this, "Thêm nhân viên " + ten + "thành công!");
  lamMoiForm();
  return;
  }
  } catch (Exception e) {
  JOptionPane.showMessageDialog(this, "Lỗi thêm nhân viên: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
  return;
  }

  // Mock fallback
  mockList.add(new NhanVien(ma, ten, user, pass, vaiTro));
  JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công (chế độ demo)!");
  lamMoiForm();
  }

  private void xuLySua() {
  int row = tblNhanVien.getSelectedRow();
  if (row < 0) {
  JOptionPane.showMessageDialog(this, "Vui lòng click chọn 1 nhân viên trong bảng để sửa!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
  return;
  }

  String ma = txtMaNV.getText().trim();
  String ten = txtTenNV.getText().trim();
  String user = txtTenDangNhap.getText().trim();
  String pass = new String(txtMatKhau.getPassword());
  String vaiTro = (String) cboVaiTro.getSelectedItem();

  try {
  Connection conn = KetNoiDB.getKetNoi();
  if (conn != null) {
  String updateSql = "UPDATE NhanVien SET TenNV = ?, TenDangNhap = ?, MatKhau = ?, VaiTro = ? WHERE MaNV = ?";
  PreparedStatement ps = conn.prepareStatement(updateSql);
  ps.setString(1, ten);
  ps.setString(2, user);
  ps.setString(3, pass);
  ps.setString(4, vaiTro);
  ps.setString(5, ma);
  ps.executeUpdate();
  ps.close();
  conn.close();

  JOptionPane.showMessageDialog(this, "Cập nhật nhân viên " + ma + "thành công!");
  lamMoiForm();
  return;
  }
  } catch (Exception e) {
  JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
  return;
  }

  for (NhanVien nv : mockList) {
  if (nv.getMaNV().equalsIgnoreCase(ma)) {
  nv.setTenNV(ten);
  nv.setTenDangNhap(user);
  nv.setMatKhau(pass);
  nv.setVaiTro(vaiTro);
  break;
  }
  }
  JOptionPane.showMessageDialog(this, "Cập nhật thành công (chế độ demo)!");
  lamMoiForm();
  }

  private void xuLyXoa() {
  int row = tblNhanVien.getSelectedRow();
  if (row < 0) {
  JOptionPane.showMessageDialog(this, "Vui lòng click chọn 1 nhân viên trong bảng để xóa!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
  return;
  }

  String ma = (String) modelBang.getValueAt(row, 0);
  String ten = (String) modelBang.getValueAt(row, 1);

  if (ma.equalsIgnoreCase("NV01") || ma.equalsIgnoreCase(nvDangNhap.getMaNV())) {
  JOptionPane.showMessageDialog(this, "Không thể xóa tài khoản Admin đang hoạt động!", "Bảo Vệ", JOptionPane.ERROR_MESSAGE);
  return;
  }

  int xacNhan = JOptionPane.showConfirmDialog(this,
  "Bạn có chắc chắn muốn xóa nhân viên [" + ten + "] (Mã: " + ma + ")?",
  "Xác Nhận Xóa", JOptionPane.YES_NO_OPTION);

  if (xacNhan != JOptionPane.YES_OPTION) return;

  try {
  Connection conn = KetNoiDB.getKetNoi();
  if (conn != null) {
  String delSql = "DELETE FROM NhanVien WHERE MaNV = ?";
  PreparedStatement ps = conn.prepareStatement(delSql);
  ps.setString(1, ma);
  ps.executeUpdate();
  ps.close();
  conn.close();

  JOptionPane.showMessageDialog(this, "Xóa nhân viên " + ma + "thành công!");
  lamMoiForm();
  return;
  }
  } catch (Exception e) {
  JOptionPane.showMessageDialog(this, "Không thể xóa nhân viên này (có thể đã có lượt gửi xe liên quan): " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
  return;
  }

  mockList.removeIf(nv -> nv.getMaNV().equalsIgnoreCase(ma));
  JOptionPane.showMessageDialog(this, "Đã xóa nhân viên (chế độ demo)!");
  lamMoiForm();
  }

  public static void main(String[] args) {
  SwingUtilities.invokeLater(() -> new QuanLyNhanVienFrame().setVisible(true));
  }
}
