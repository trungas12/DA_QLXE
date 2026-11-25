package da_qlxe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class QuanLyXeFrame extends JFrame {
  private NhanVien nvHienTai;

  // Các thành phần giao diện
  private JComboBox<TheXe> cboTheXe;
  private JComboBox<LoaiXe> cboLoaiXe;
  private JTextField txtBienSo;
  private JTextField txtTimKiem;

  private JTable tblLuotGui;
  private DefaultTableModel modelBang;

  private JLabel lblThongKeXe;
  private JLabel lblThongKeTien;
  private JLabel lblNhanVien;
  private JLabel lblTrangThaiDB;

  private DecimalFormat tienFormat = new DecimalFormat("###,###,### đ");
  private SimpleDateFormat gioFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

  // Dữ liệu nội bộ khi chạy không có SQL
  private static final List<TheXe> mockTheXe = new ArrayList<>();
  private static final List<LoaiXe> mockLoaiXe = new ArrayList<>();
  private static final List<LuotGuiXe> mockLuotGui = new ArrayList<>();
  private static int mockId = 5;

  static {
  mockTheXe.add(new TheXe("THE01", "Đang Gửi"));
  mockTheXe.add(new TheXe("THE02", "Đang Gửi"));
  mockTheXe.add(new TheXe("THE03", "Rảnh"));
  mockTheXe.add(new TheXe("THE04", "Rảnh"));
  mockTheXe.add(new TheXe("THE05", "Rảnh"));

  mockLoaiXe.add(new LoaiXe("XM", "Xe máy", 5000));
  mockLoaiXe.add(new LoaiXe("OTO", "Ô tô con", 20000));
  mockLoaiXe.add(new LoaiXe("XD", "Xe đạp / Xe điện", 3000));

  long now = System.currentTimeMillis();
  LuotGuiXe g1 = new LuotGuiXe(1, "THE03", "XM", "29B1-123.45", new Timestamp(now - 14000000), new Timestamp(now - 1000), 5000, "Đã Thanh Toán", "NV02");
  g1.setTenLoaiXe("Xe máy");
  LuotGuiXe g2 = new LuotGuiXe(2, "THE04", "OTO", "30A-999.88", new Timestamp(now - 28000000), new Timestamp(now - 2000), 20000, "Đã Thanh Toán", "NV02");
  g2.setTenLoaiXe("Ô tô con");
  LuotGuiXe g3 = new LuotGuiXe(3, "THE01", "XM", "59T1-888.88", new Timestamp(now - 10000000), null, 0, "Trong Bãi", "NV02");
  g3.setTenLoaiXe("Xe máy");
  LuotGuiXe g4 = new LuotGuiXe(4, "THE02", "OTO", "30E-567.89", new Timestamp(now - 3600000), null, 0, "Trong Bãi", "NV03");
  g4.setTenLoaiXe("Ô tô con");

  mockLuotGui.add(g1);
  mockLuotGui.add(g2);
  mockLuotGui.add(g3);
  mockLuotGui.add(g4);
  }

  public QuanLyXeFrame(NhanVien nv) {
  this.nvHienTai = nv;
  khoiTaoGiaoDien();
  taiDuLieuTheXe();
  taiDuLieuLoaiXe();
  taiBangDuLieu("");
  }

  private void khoiTaoGiaoDien() {
  setTitle("HỆ THỐNG QUẢN LÝ BÃI GỬI XE - THÊM SỬA XÓA & TÌM KIẾM");
  setSize(1080, 650);
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setLocationRelativeTo(null);

  JPanel pChinh = new JPanel(new BorderLayout(10, 10));
  pChinh.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

  // 1. THANH TIÊU ĐỀ TRÊN CÙNG
  JPanel pHeader = new JPanel(new BorderLayout());
  pHeader.setBackground(new Color(25, 118, 210));
  pHeader.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

  JLabel lblTieuDe = new JLabel("HỆ THỐNG QUẢN LÝ GỬI XE");
  lblTieuDe.setFont(new Font("Arial", Font.BOLD, 18));
  lblTieuDe.setForeground(Color.WHITE);

  String info = (nvHienTai != null) ? nvHienTai.getTenNV() + " (" + nvHienTai.getVaiTro() + ")" : "Nhân viên";
  lblNhanVien = new JLabel("NV trực: " + info + " ");
  lblNhanVien.setForeground(Color.WHITE);
  lblNhanVien.setFont(new Font("Arial", Font.PLAIN, 14));

  JButton btnQuanLyNV = new JButton("Quản Lý NV");
  btnQuanLyNV.setBackground(new Color(255, 235, 59));
  btnQuanLyNV.setForeground(Color.BLACK);
  btnQuanLyNV.setFont(new Font("Arial", Font.BOLD, 12));
  btnQuanLyNV.addActionListener(e -> {
  this.dispose();
  new QuanLyNhanVienFrame(nvHienTai).setVisible(true);
  });

  JButton btnQuanLyThe = new JButton("Thẻ & Giá Vé");
  btnQuanLyThe.setBackground(new Color(255, 235, 59));
  btnQuanLyThe.setForeground(Color.BLACK);
  btnQuanLyThe.setFont(new Font("Arial", Font.BOLD, 12));
  btnQuanLyThe.addActionListener(e -> {
  this.dispose();
  new QuanLyTheXeFrame(nvHienTai).setVisible(true);
  });

  JButton btnDangXuat = new JButton("Đăng Xuất");
  btnDangXuat.addActionListener(e -> {
  this.dispose();
  new DangNhapFrame().setVisible(true);
  });

  JPanel pUser = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
  pUser.setOpaque(false);
  pUser.add(lblNhanVien);
  pUser.add(btnQuanLyNV);
  pUser.add(btnQuanLyThe);
  pUser.add(btnDangXuat);

  pHeader.add(lblTieuDe, BorderLayout.WEST);
  pHeader.add(pUser, BorderLayout.EAST);
  pChinh.add(pHeader, BorderLayout.NORTH);

  // 2. KHU VỰC BÊN TRÁI: FORM NHẬP LIỆU & 4 NÚT CHỨC NĂNG (THÊM, SỬA, XÓA, LÀM MỚI)
  JPanel pTrai = new JPanel();
  pTrai.setLayout(new BoxLayout(pTrai, BoxLayout.Y_AXIS));
  pTrai.setPreferredSize(new Dimension(320, 0));
  pTrai.setBorder(BorderFactory.createTitledBorder("Thông Tin Lượt Gửi"));

  cboTheXe = new JComboBox<>();
  cboLoaiXe = new JComboBox<>();
  txtBienSo = new JTextField();
  txtTimKiem = new JTextField();

  // 4 Nút nghiệp vụ chuẩn đồ án: THÊM, SỬA, XÓA, LÀM MỚI
  JButton btnThem = new JButton("THÊM XE VÀO BÃI");
  btnThem.setBackground(new Color(46, 125, 50));
  btnThem.setForeground(Color.WHITE);
  btnThem.setFont(new Font("Arial", Font.BOLD, 13));

  JButton btnSua = new JButton("SỬA THÔNG TIN");
  btnSua.setBackground(new Color(255, 152, 0));
  btnSua.setForeground(Color.WHITE);
  btnSua.setFont(new Font("Arial", Font.BOLD, 13));

  JButton btnXoa = new JButton("XÓA BẢN GHI");
  btnXoa.setBackground(new Color(211, 47, 47));
  btnXoa.setForeground(Color.WHITE);
  btnXoa.setFont(new Font("Arial", Font.BOLD, 13));

  JButton btnRaBai = new JButton("XUẤT BÃI & TÍNH TIỀN");
  btnRaBai.setBackground(new Color(156, 39, 176));
  btnRaBai.setForeground(Color.WHITE);
  btnRaBai.setFont(new Font("Arial", Font.BOLD, 13));

  JButton btnLamMoi = new JButton("Làm Mới Form");

  pTrai.add(new JLabel("Mã Thẻ Xe:"));
  pTrai.add(cboTheXe);
  pTrai.add(Box.createVerticalStrut(8));

  pTrai.add(new JLabel("Loại Phương Tiện:"));
  pTrai.add(cboLoaiXe);
  pTrai.add(Box.createVerticalStrut(8));

  pTrai.add(new JLabel("Biển Số Xe (VD: 29B1-123.45):"));
  pTrai.add(txtBienSo);
  pTrai.add(Box.createVerticalStrut(15));

  // Nhóm 4 nút THÊM - SỬA - XÓA - LÀM MỚI
  JPanel pNhomNut = new JPanel(new GridLayout(2, 2, 6, 6));
  pNhomNut.add(btnThem);
  pNhomNut.add(btnSua);
  pNhomNut.add(btnXoa);
  pNhomNut.add(btnLamMoi);
  pTrai.add(pNhomNut);

  pTrai.add(Box.createVerticalStrut(10));
  btnRaBai.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
  pTrai.add(btnRaBai);

  pTrai.add(Box.createVerticalStrut(20));
  pTrai.add(new JLabel("TÌM KIẾM THEO BIỂN SỐ / THẺ:"));
  pTrai.add(txtTimKiem);
  pTrai.add(Box.createVerticalStrut(5));

  JButton btnTimKiem = new JButton("Tìm Kiếm Ngay");
  btnTimKiem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
  pTrai.add(btnTimKiem);

  pChinh.add(pTrai, BorderLayout.WEST);

  // 3. KHU VỰC BÊN PHẢI: BẢNG DỮ LIỆU
  JPanel pPhai = new JPanel(new BorderLayout(5, 5));
  pPhai.setBorder(BorderFactory.createTitledBorder("Danh Sách Lượt Gửi Xe (Click dòng để Sửa/Xóa/Tính tiền)"));

  String[] cot = {"Mã Lượt", "Mã Thẻ", "Loại Xe", "Biển Số", "Giờ Vào", "Giờ Ra", "Tiền Thu", "Trạng Thái"};
  modelBang = new DefaultTableModel(cot, 0) {
  @Override
  public boolean isCellEditable(int r, int c) { return false; }
  };
  tblLuotGui = new JTable(modelBang);
  tblLuotGui.setRowHeight(25);
  pPhai.add(new JScrollPane(tblLuotGui), BorderLayout.CENTER);

  // THANH THỐNG KÊ DƯỚI CÙNG
  JPanel pDuoi = new JPanel(new BorderLayout());
  JPanel pStatLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));

  lblThongKeXe = new JLabel("Xe trong bãi: 0 xe");
  lblThongKeXe.setFont(new Font("Arial", Font.BOLD, 14));
  lblThongKeXe.setForeground(new Color(25, 118, 210));

  lblThongKeTien = new JLabel("Tổng doanh thu: 0 đ");
  lblThongKeTien.setFont(new Font("Arial", Font.BOLD, 14));
  lblThongKeTien.setForeground(new Color(46, 125, 50));

  pStatLeft.add(lblThongKeXe);
  pStatLeft.add(lblThongKeTien);

  lblTrangThaiDB = new JLabel("Đang kiểm tra kết nối...");
  lblTrangThaiDB.setFont(new Font("Arial", Font.BOLD, 12));
  JPanel pStatRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
  pStatRight.add(lblTrangThaiDB);

  pDuoi.add(pStatLeft, BorderLayout.WEST);
  pDuoi.add(pStatRight, BorderLayout.EAST);
  pPhai.add(pDuoi, BorderLayout.SOUTH);

  pChinh.add(pPhai, BorderLayout.CENTER);
  add(pChinh);

  // KHI CLICK VÀO 1 DÒNG TRÊN BẢNG -> TỰ ĐỘNG ĐIỀN THÔNG TIN LÊN FORM ĐỂ SỬA / XÓA
  tblLuotGui.getSelectionModel().addListSelectionListener(e -> {
  int row = tblLuotGui.getSelectedRow();
  if (row >= 0) {
  String maThe = (String) modelBang.getValueAt(row, 1);
  String bienSo = (String) modelBang.getValueAt(row, 3);
  txtBienSo.setText(bienSo);

  // Chọn đúng thẻ trên combobox nếu có
  for (int i = 0; i < cboTheXe.getItemCount(); i++) {
  if (cboTheXe.getItemAt(i).getMaThe().equalsIgnoreCase(maThe)) {
  cboTheXe.setSelectedIndex(i);
  break;
  }
  }
  }
  });

  // GẮN SỰ KIỆN CHO CÁC NÚT
  btnThem.addActionListener(e -> xuLyThem());
  btnSua.addActionListener(e -> xuLySua());
  btnXoa.addActionListener(e -> xuLyXoa());
  btnRaBai.addActionListener(e -> xuLyRaBai());
  btnTimKiem.addActionListener(e -> taiBangDuLieu(txtTimKiem.getText().trim()));
  txtTimKiem.addActionListener(e -> taiBangDuLieu(txtTimKiem.getText().trim()));
  btnLamMoi.addActionListener(e -> {
  txtBienSo.setText("");
  txtTimKiem.setText("");
  tblLuotGui.clearSelection();
  taiDuLieuTheXe();
  taiBangDuLieu("");
  });
  }

  // 1. Tải thẻ xe rảnh
  private void taiDuLieuTheXe() {
  cboTheXe.removeAllItems();
  try (Connection conn = KetNoiDB.getKetNoi()) {
  if (conn != null) {
  lblTrangThaiDB.setText("ĐÃ KẾT NỐI SQL SERVER");
  lblTrangThaiDB.setForeground(new Color(46, 125, 50));
  String sql = "SELECT * FROM TheXe WHERE TrangThai = N'Rảnh'";
  PreparedStatement ps = conn.prepareStatement(sql);
  ResultSet rs = ps.executeQuery();
  while (rs.next()) {
  cboTheXe.addItem(new TheXe(rs.getString("MaThe"), rs.getString("TrangThai")));
  }
  return;
  }
  } catch (Exception ignored) {}

  lblTrangThaiDB.setText("CHƯA KẾT NỐI SQL");
  lblTrangThaiDB.setForeground(Color.RED);
  for (TheXe tx : mockTheXe) {
  if ("Rảnh".equalsIgnoreCase(tx.getTrangThai())) {
  cboTheXe.addItem(tx);
  }
  }
  }

  // 2. Tải loại xe
  private void taiDuLieuLoaiXe() {
  cboLoaiXe.removeAllItems();
  try (Connection conn = KetNoiDB.getKetNoi()) {
  if (conn != null) {
  String sql = "SELECT * FROM LoaiXe";
  PreparedStatement ps = conn.prepareStatement(sql);
  ResultSet rs = ps.executeQuery();
  while (rs.next()) {
  cboLoaiXe.addItem(new LoaiXe(rs.getString("MaLoaiXe"), rs.getString("TenLoaiXe"), rs.getDouble("GiaVe")));
  }
  return;
  }
  } catch (Exception ignored) {}

  for (LoaiXe lx : mockLoaiXe) {
  cboLoaiXe.addItem(lx);
  }
  }

  // 3. Tải bảng dữ liệu (TÌM KIẾM)
  private void taiBangDuLieu(String tuKhoa) {
  modelBang.setRowCount(0);
  int soXeTrongBai = 0;
  double tongTien = 0;

  try (Connection conn = KetNoiDB.getKetNoi()) {
  if (conn != null) {
  lblTrangThaiDB.setText("ĐÃ KẾT NỐI SQL SERVER");
  lblTrangThaiDB.setForeground(new Color(46, 125, 50));

  String sql = "SELECT g.*, lx.TenLoaiXe FROM LuotGuiXe g " +
  "LEFT JOIN LoaiXe lx ON g.MaLoaiXe = lx.MaLoaiXe " +
  "WHERE g.BienSo LIKE ? OR g.MaThe LIKE ? " +
  "ORDER BY g.MaLuotGui DESC";
  PreparedStatement ps = conn.prepareStatement(sql);
  String kw = "%" + tuKhoa + "%";
  ps.setString(1, kw);
  ps.setString(2, kw);
  ResultSet rs = ps.executeQuery();

  while (rs.next()) {
  int ma = rs.getInt("MaLuotGui");
  String the = rs.getString("MaThe");
  String loai = rs.getString("TenLoaiXe");
  String bien = rs.getString("BienSo");
  Timestamp vao = rs.getTimestamp("ThoiGianVao");
  Timestamp ra = rs.getTimestamp("ThoiGianRa");
  double tien = rs.getDouble("TienThu");
  String tt = rs.getString("TrangThai");

  if ("Trong Bãi".equals(tt)) soXeTrongBai++;
  if ("Đã Thanh Toán".equals(tt)) tongTien += tien;

  modelBang.addRow(new Object[]{
  ma, the, loai, bien,
  (vao != null ? gioFormat.format(vao) : ""),
  (ra != null ? gioFormat.format(ra) : ""),
  (tien > 0 ? tienFormat.format(tien) : "0 đ"),
  tt
  });
  }
  lblThongKeXe.setText("Xe đang trong bãi: " + soXeTrongBai + "xe");
  lblThongKeTien.setText("Tổng doanh thu: " + tienFormat.format(tongTien));
  return;
  }
  } catch (Exception ignored) {}

  // Dữ liệu nội bộ
  lblTrangThaiDB.setText("CHƯA KẾT NỐI SQL");
  lblTrangThaiDB.setForeground(Color.RED);

  for (LuotGuiXe lg : mockLuotGui) {
  boolean match = tuKhoa.isEmpty() || lg.getBienSo().toUpperCase().contains(tuKhoa.toUpperCase()) || lg.getMaThe().toUpperCase().contains(tuKhoa.toUpperCase());
  if (match) {
  if ("Trong Bãi".equals(lg.getTrangThai())) soXeTrongBai++;
  if ("Đã Thanh Toán".equals(lg.getTrangThai())) tongTien += lg.getTienThu();

  modelBang.addRow(new Object[]{
  lg.getMaLuotGui(), lg.getMaThe(), lg.getTenLoaiXe(), lg.getBienSo(),
  (lg.getThoiGianVao() != null ? gioFormat.format(lg.getThoiGianVao()) : ""),
  (lg.getThoiGianRa() != null ? gioFormat.format(lg.getThoiGianRa()) : ""),
  (lg.getTienThu() > 0 ? tienFormat.format(lg.getTienThu()) : "0 đ"),
  lg.getTrangThai()
  });
  }
  }
  lblThongKeXe.setText("Xe đang trong bãi: " + soXeTrongBai + "xe");
  lblThongKeTien.setText("Tổng doanh thu: " + tienFormat.format(tongTien));
  }

  // ==========================================
  // 4 CHỨC NĂNG CHÍNH: THÊM - SỬA - XÓA - RA BÃI
  // ==========================================

  // 1. CHỨC NĂNG THÊM: Cho xe vào bãi
  private void xuLyThem() {
  TheXe the = (TheXe) cboTheXe.getSelectedItem();
  LoaiXe loai = (LoaiXe) cboLoaiXe.getSelectedItem();
  String bienSo = txtBienSo.getText().trim().toUpperCase();

  if (the == null) {
  JOptionPane.showMessageDialog(this, "Không còn thẻ rảnh để phát cho xe!");
  return;
  }
  if (bienSo.isEmpty()) {
  JOptionPane.showMessageDialog(this, "Vui lòng nhập biển số xe!");
  txtBienSo.requestFocus();
  return;
  }

  try (Connection conn = KetNoiDB.getKetNoi()) {
  if (conn != null) {
  String sqlInsert = "INSERT INTO LuotGuiXe (MaThe, MaLoaiXe, BienSo, ThoiGianVao, TienThu, TrangThai, MaNV) " +
  "VALUES (?, ?, ?, GETDATE(), 0, N'Trong Bãi', ?)";
  PreparedStatement ps = conn.prepareStatement(sqlInsert);
  ps.setString(1, the.getMaThe());
  ps.setString(2, loai.getMaLoaiXe());
  ps.setString(3, bienSo);
  ps.setString(4, (nvHienTai != null ? nvHienTai.getMaNV() : "NV01"));
  ps.executeUpdate();

  String sqlCard = "UPDATE TheXe SET TrangThai = N'Đang Gửi' WHERE MaThe = ?";
  PreparedStatement psCard = conn.prepareStatement(sqlCard);
  psCard.setString(1, the.getMaThe());
  psCard.executeUpdate();
  } else {
  LuotGuiXe moi = new LuotGuiXe(mockId++, the.getMaThe(), loai.getMaLoaiXe(), bienSo, new Timestamp(System.currentTimeMillis()), null, 0, "Trong Bãi", "NV01");
  moi.setTenLoaiXe(loai.getTenLoaiXe());
  mockLuotGui.add(0, moi);

  for (TheXe tx : mockTheXe) {
  if (tx.getMaThe().equalsIgnoreCase(the.getMaThe())) {
  tx.setTrangThai("Đang Gửi");
  break;
  }
  }
  }

  JOptionPane.showMessageDialog(this, "THÊM THÀNH CÔNG!\nXe [" + bienSo + "] đã vào bãi, phát thẻ: " + the.getMaThe());
  txtBienSo.setText("");
  taiDuLieuTheXe();
  taiBangDuLieu("");
  } catch (Exception e) {
  JOptionPane.showMessageDialog(this, "Lỗi thêm xe: " + e.getMessage());
  }
  }

  // 2. CHỨC NĂNG SỬA: Sửa biển số hoặc loại xe của dòng đang chọn
  private void xuLySua() {
  int row = tblLuotGui.getSelectedRow();
  if (row < 0) {
  JOptionPane.showMessageDialog(this, "Vui lòng click chọn 1 dòng trên bảng để SỬA!");
  return;
  }

  int maLuot = (int) modelBang.getValueAt(row, 0);
  String bienSoMoi = txtBienSo.getText().trim().toUpperCase();
  LoaiXe loai = (LoaiXe) cboLoaiXe.getSelectedItem();

  if (bienSoMoi.isEmpty()) {
  JOptionPane.showMessageDialog(this, "Biển số xe không được để trống!");
  return;
  }

  try (Connection conn = KetNoiDB.getKetNoi()) {
  if (conn != null) {
  String sqlUpdate = "UPDATE LuotGuiXe SET BienSo = ?, MaLoaiXe = ? WHERE MaLuotGui = ?";
  PreparedStatement ps = conn.prepareStatement(sqlUpdate);
  ps.setString(1, bienSoMoi);
  ps.setString(2, loai.getMaLoaiXe());
  ps.setInt(3, maLuot);
  ps.executeUpdate();
  } else {
  for (LuotGuiXe lg : mockLuotGui) {
  if (lg.getMaLuotGui() == maLuot) {
  lg.setBienSo(bienSoMoi);
  lg.setMaLoaiXe(loai.getMaLoaiXe());
  lg.setTenLoaiXe(loai.getTenLoaiXe());
  break;
  }
  }
  }

  JOptionPane.showMessageDialog(this, "CẬP NHẬT THÀNH CÔNG!\nĐã sửa biển số thành: " + bienSoMoi);
  taiBangDuLieu("");
  } catch (Exception e) {
  JOptionPane.showMessageDialog(this, "Lỗi sửa thông tin: " + e.getMessage());
  }
  }

  // 3. CHỨC NĂNG XÓA: Xóa lượt gửi nhầm khỏi CSDL
  private void xuLyXoa() {
  int row = tblLuotGui.getSelectedRow();
  if (row < 0) {
  JOptionPane.showMessageDialog(this, "Vui lòng click chọn 1 dòng trên bảng để XÓA!");
  return;
  }

  int maLuot = (int) modelBang.getValueAt(row, 0);
  String maThe = (String) modelBang.getValueAt(row, 1);
  String bienSo = (String) modelBang.getValueAt(row, 3);
  String trangThai = (String) modelBang.getValueAt(row, 7);

  int confirm = JOptionPane.showConfirmDialog(this,
  "Bạn có chắc chắn muốn XÓA lượt gửi của xe [" + bienSo + "] (Mã lượt: " + maLuot + ") không?",
  "Xác Nhận Xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

  if (confirm != JOptionPane.YES_OPTION) return;

  try (Connection conn = KetNoiDB.getKetNoi()) {
  if (conn != null) {
  // Xóa bản ghi trong LuotGuiXe
  String sqlDelete = "DELETE FROM LuotGuiXe WHERE MaLuotGui = ?";
  PreparedStatement ps = conn.prepareStatement(sqlDelete);
  ps.setInt(1, maLuot);
  ps.executeUpdate();

  // Nếu xe đang trong bãi mà bị xóa, trả thẻ về 'Rảnh'
  if ("Trong Bãi".equals(trangThai)) {
  String sqlCard = "UPDATE TheXe SET TrangThai = N'Rảnh' WHERE MaThe = ?";
  PreparedStatement psCard = conn.prepareStatement(sqlCard);
  psCard.setString(1, maThe);
  psCard.executeUpdate();
  }
  } else {
  mockLuotGui.removeIf(lg -> lg.getMaLuotGui() == maLuot);
  if ("Trong Bãi".equals(trangThai)) {
  for (TheXe tx : mockTheXe) {
  if (tx.getMaThe().equalsIgnoreCase(maThe)) {
  tx.setTrangThai("Rảnh");
  break;
  }
  }
  }
  }

  JOptionPane.showMessageDialog(this, "ĐÃ XÓA THÀNH CÔNG lượt gửi #" + maLuot);
  txtBienSo.setText("");
  taiDuLieuTheXe();
  taiBangDuLieu("");
  } catch (Exception e) {
  JOptionPane.showMessageDialog(this, "Lỗi xóa bản ghi: " + e.getMessage());
  }
  }

  // 4. XUẤT BÃI & TÍNH TIỀN
  private void xuLyRaBai() {
  int row = tblLuotGui.getSelectedRow();
  if (row < 0) {
  JOptionPane.showMessageDialog(this, "Vui lòng click chọn 1 xe đang 'Trong Bãi' trên bảng để tính tiền!");
  return;
  }

  int maLuot = (int) modelBang.getValueAt(row, 0);
  String maThe = (String) modelBang.getValueAt(row, 1);
  String loaiXeTen = (String) modelBang.getValueAt(row, 2);
  String bienSo = (String) modelBang.getValueAt(row, 3);
  String trangThai = (String) modelBang.getValueAt(row, 7);

  if ("Đã Thanh Toán".equals(trangThai)) {
  JOptionPane.showMessageDialog(this, "Lượt gửi này đã thanh toán và ra bãi rồi!");
  return;
  }

  double giaVe = 5000;
  if (loaiXeTen != null && loaiXeTen.toLowerCase().contains("ô tô")) giaVe = 20000;
  else if (loaiXeTen != null && loaiXeTen.toLowerCase().contains("đạp")) giaVe = 3000;

  int xacNhan = JOptionPane.showConfirmDialog(this,
  "Xác nhận thanh toán cho xe [" + bienSo + "]\n" +
  "Mã thẻ: " + maThe + "\n" +
  "Loại xe: " + loaiXeTen + "\n" +
  "Số tiền cần thu: " + tienFormat.format(giaVe),
  "Thu Tiền Xuất Bãi", JOptionPane.YES_NO_OPTION);

  if (xacNhan != JOptionPane.YES_OPTION) return;

  try (Connection conn = KetNoiDB.getKetNoi()) {
  if (conn != null) {
  String sqlUpdate = "UPDATE LuotGuiXe SET ThoiGianRa = GETDATE(), TienThu = ?, TrangThai = N'Đã Thanh Toán' WHERE MaLuotGui = ?";
  PreparedStatement ps = conn.prepareStatement(sqlUpdate);
  ps.setDouble(1, giaVe);
  ps.setInt(2, maLuot);
  ps.executeUpdate();

  String sqlCard = "UPDATE TheXe SET TrangThai = N'Rảnh' WHERE MaThe = ?";
  PreparedStatement psCard = conn.prepareStatement(sqlCard);
  psCard.setString(1, maThe);
  psCard.executeUpdate();
  } else {
  for (LuotGuiXe lg : mockLuotGui) {
  if (lg.getMaLuotGui() == maLuot) {
  lg.setThoiGianRa(new Timestamp(System.currentTimeMillis()));
  lg.setTienThu(giaVe);
  lg.setTrangThai("Đã Thanh Toán");
  break;
  }
  }
  for (TheXe tx : mockTheXe) {
  if (tx.getMaThe().equalsIgnoreCase(maThe)) {
  tx.setTrangThai("Rảnh");
  break;
  }
  }
  }

  JOptionPane.showMessageDialog(this, "Thanh toán thành công! Xe [" + bienSo + "] đã ra bãi.\nThẻ [" + maThe + "] đã được thu hồi.");
  taiDuLieuTheXe();
  taiBangDuLieu("");
  } catch (Exception e) {
  JOptionPane.showMessageDialog(this, "Lỗi xuất bãi: " + e.getMessage());
  }
  }
}
