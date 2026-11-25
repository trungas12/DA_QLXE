package da_qlxe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class QuanLyTheXeFrame extends JFrame {
  private NhanVien nvHienTai;

  // Tab 1: Thẻ xe
  private JTextField txtMaThe;
  private JComboBox<String> cboTrangThaiThe;
  private JTable tblTheXe;
  private DefaultTableModel modelTheXe;

  // Tab 2: Giá vé
  private JTextField txtMaLoai, txtTenLoai, txtGiaVe;
  private JTable tblLoaiXe;
  private DefaultTableModel modelLoaiXe;
  private DecimalFormat tienFormat = new DecimalFormat("###,###,### đ");

  // Dữ liệu nội bộ khi không có SQL
  private static final List<TheXe> mockTheList = new ArrayList<>();
  private static final List<LoaiXe> mockLoaiList = new ArrayList<>();
  static {
  mockTheList.add(new TheXe("THE01", "Đang Gửi"));
  mockTheList.add(new TheXe("THE02", "Đang Gửi"));
  mockTheList.add(new TheXe("THE03", "Rảnh"));
  mockTheList.add(new TheXe("THE04", "Rảnh"));
  mockTheList.add(new TheXe("THE05", "Rảnh"));

  mockLoaiList.add(new LoaiXe("XM", "Xe máy", 5000));
  mockLoaiList.add(new LoaiXe("OTO", "Ô tô con", 20000));
  mockLoaiList.add(new LoaiXe("XD", "Xe đạp / Xe điện", 3000));
  }

  public QuanLyTheXeFrame() {
  this(new NhanVien("NV01", "Admin", "admin", "admin123", "Admin"));
  }

  public QuanLyTheXeFrame(NhanVien nv) {
  this.nvHienTai = nv;
  khoiTaoGiaoDien();
  taiDuLieuThe();
  taiDuLieuLoaiXe();
  }

  private void khoiTaoGiaoDien() {
  setTitle("QUẢN LÝ THẺ XE & BẢNG GIÁ VÉ");
  setSize(900, 560);
  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  setLocationRelativeTo(null);

  JPanel pChinh = new JPanel(new BorderLayout(10, 10));
  pChinh.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

  // HEADER
  JPanel pHeader = new JPanel(new BorderLayout());
  pHeader.setBackground(new Color(52, 73, 94));
  pHeader.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

  JLabel lblTieuDe = new JLabel("QUẢN LÝ THẺ XE & BẢNG GIÁ VÉ");
  lblTieuDe.setFont(new Font("Arial", Font.BOLD, 18));
  lblTieuDe.setForeground(Color.WHITE);

  JButton btnQuayLai = new JButton("Quay Lại Quản Lý Xe");
  btnQuayLai.addActionListener(e -> {
  this.dispose();
  new QuanLyXeFrame(nvHienTai).setVisible(true);
  });

  pHeader.add(lblTieuDe, BorderLayout.WEST);
  pHeader.add(btnQuayLai, BorderLayout.EAST);
  pChinh.add(pHeader, BorderLayout.NORTH);

  // TABBED PANE GỒM 2 TAB: 1) THẺ XE, 2) GIÁ VÉ
  JTabbedPane tabPane = new JTabbedPane();
  tabPane.setFont(new Font("Arial", Font.BOLD, 14));

  tabPane.addTab("Quản Lý Thẻ Xe", taoPanelTheXe());
  tabPane.addTab("Bảng Giá Vé Các Loại Xe", taoPanelLoaiXe());

  pChinh.add(tabPane, BorderLayout.CENTER);
  add(pChinh);
  }

  // ================== TAB 1: THẺ XE ==================
  private JPanel taoPanelTheXe() {
  JPanel p = new JPanel(new BorderLayout(10, 10));
  p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

  // Trái
  JPanel pTrai = new JPanel();
  pTrai.setLayout(new BoxLayout(pTrai, BoxLayout.Y_AXIS));
  pTrai.setPreferredSize(new Dimension(280, 0));
  pTrai.setBorder(BorderFactory.createTitledBorder("Chi Tiết Thẻ Xe"));

  txtMaThe = new JTextField();
  cboTrangThaiThe = new JComboBox<>(new String[]{"Rảnh", "Đang Gửi", "Khóa/Hỏng"});

  pTrai.add(new JLabel("Mã Thẻ (VD: THE06):"));
  pTrai.add(txtMaThe);
  pTrai.add(Box.createVerticalStrut(10));

  pTrai.add(new JLabel("Trạng Thái Thẻ:"));
  pTrai.add(cboTrangThaiThe);
  pTrai.add(Box.createVerticalStrut(20));

  JPanel pBtn = new JPanel(new GridLayout(2, 2, 6, 6));
  JButton btnThem = new JButton("Thêm Thẻ");
  btnThem.setBackground(new Color(46, 125, 50));
  btnThem.setForeground(Color.WHITE);

  JButton btnSua = new JButton("Đổi Trạng Thái");
  btnSua.setBackground(new Color(255, 152, 0));
  btnSua.setForeground(Color.WHITE);

  JButton btnXoa = new JButton("Xóa Thẻ");
  btnXoa.setBackground(new Color(211, 47, 47));
  btnXoa.setForeground(Color.WHITE);

  JButton btnLamMoi = new JButton("Làm Mới");

  pBtn.add(btnThem);
  pBtn.add(btnSua);
  pBtn.add(btnXoa);
  pBtn.add(btnLamMoi);
  pTrai.add(pBtn);

  p.add(pTrai, BorderLayout.WEST);

  // Bảng
  String[] cot = {"Mã Thẻ Xe", "Trạng Thái Hoạt Động"};
  modelTheXe = new DefaultTableModel(cot, 0) {
  @Override
  public boolean isCellEditable(int r, int c) { return false; }
  };
  tblTheXe = new JTable(modelTheXe);
  tblTheXe.setRowHeight(26);
  p.add(new JScrollPane(tblTheXe), BorderLayout.CENTER);

  tblTheXe.getSelectionModel().addListSelectionListener(e -> {
  int row = tblTheXe.getSelectedRow();
  if (row >= 0) {
  txtMaThe.setText((String) modelTheXe.getValueAt(row, 0));
  txtMaThe.setEditable(false);
  cboTrangThaiThe.setSelectedItem(modelTheXe.getValueAt(row, 1));
  }
  });

  btnThem.addActionListener(e -> xuLyThemThe());
  btnSua.addActionListener(e -> xuLySuaThe());
  btnXoa.addActionListener(e -> xuLyXoaThe());
  btnLamMoi.addActionListener(e -> {
  txtMaThe.setText("");
  txtMaThe.setEditable(true);
  tblTheXe.clearSelection();
  taiDuLieuThe();
  });

  return p;
  }

  private void taiDuLieuThe() {
  modelTheXe.setRowCount(0);
  boolean coSQL = false;
  try {
  Connection conn = KetNoiDB.getKetNoi();
  if (conn != null) {
  Statement st = conn.createStatement();
  ResultSet rs = st.executeQuery("SELECT * FROM TheXe ORDER BY MaThe");
  while (rs.next()) {
  coSQL = true;
  modelTheXe.addRow(new Object[]{rs.getString("MaThe"), rs.getString("TrangThai")});
  }
  rs.close();
  st.close();
  conn.close();
  }
  } catch (Exception ignored) {}

  if (!coSQL) {
  for (TheXe t : mockTheList) {
  modelTheXe.addRow(new Object[]{t.getMaThe(), t.getTrangThai()});
  }
  }
  }

  private void xuLyThemThe() {
  String ma = txtMaThe.getText().trim();
  String tt = (String) cboTrangThaiThe.getSelectedItem();
  if (ma.isEmpty()) {
  JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Thẻ Xe!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
  return;
  }

  try {
  Connection conn = KetNoiDB.getKetNoi();
  if (conn != null) {
  PreparedStatement ps = conn.prepareStatement("INSERT INTO TheXe (MaThe, TrangThai) VALUES (?, ?)");
  ps.setString(1, ma);
  ps.setString(2, tt);
  ps.executeUpdate();
  ps.close();
  conn.close();
  JOptionPane.showMessageDialog(this, "Thêm thẻ xe " + ma + "thành công!");
  txtMaThe.setText("");
  taiDuLieuThe();
  return;
  }
  } catch (Exception e) {
  JOptionPane.showMessageDialog(this, "Lỗi thêm thẻ: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
  return;
  }

  mockTheList.add(new TheXe(ma, tt));
  JOptionPane.showMessageDialog(this, "Thêm thẻ thành công (chế độ demo)!");
  taiDuLieuThe();
  }

  private void xuLySuaThe() {
  int row = tblTheXe.getSelectedRow();
  if (row < 0) {
  JOptionPane.showMessageDialog(this, "Vui lòng click chọn thẻ xe trong bảng để sửa!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
  return;
  }
  String ma = txtMaThe.getText().trim();
  String tt = (String) cboTrangThaiThe.getSelectedItem();

  try {
  Connection conn = KetNoiDB.getKetNoi();
  if (conn != null) {
  PreparedStatement ps = conn.prepareStatement("UPDATE TheXe SET TrangThai = ? WHERE MaThe = ?");
  ps.setString(1, tt);
  ps.setString(2, ma);
  ps.executeUpdate();
  ps.close();
  conn.close();
  JOptionPane.showMessageDialog(this, "Cập nhật thẻ " + ma + "thành công!");
  taiDuLieuThe();
  return;
  }
  } catch (Exception e) {
  JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
  return;
  }

  for (TheXe t : mockTheList) {
  if (t.getMaThe().equalsIgnoreCase(ma)) {
  t.setTrangThai(tt);
  break;
  }
  }
  JOptionPane.showMessageDialog(this, "Cập nhật thành công (chế độ demo)!");
  taiDuLieuThe();
  }

  private void xuLyXoaThe() {
  int row = tblTheXe.getSelectedRow();
  if (row < 0) {
  JOptionPane.showMessageDialog(this, "Vui lòng click chọn thẻ xe cần xóa!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
  return;
  }
  String ma = (String) modelTheXe.getValueAt(row, 0);

  int confirm = JOptionPane.showConfirmDialog(this, "Xóa thẻ xe " + ma + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
  if (confirm != JOptionPane.YES_OPTION) return;

  try {
  Connection conn = KetNoiDB.getKetNoi();
  if (conn != null) {
  PreparedStatement ps = conn.prepareStatement("DELETE FROM TheXe WHERE MaThe = ?");
  ps.setString(1, ma);
  ps.executeUpdate();
  ps.close();
  conn.close();
  JOptionPane.showMessageDialog(this, "Xóa thẻ " + ma + "thành công!");
  taiDuLieuThe();
  return;
  }
  } catch (Exception e) {
  JOptionPane.showMessageDialog(this, "Không thể xóa thẻ đang có dữ liệu lượt gửi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
  return;
  }

  mockTheList.removeIf(t -> t.getMaThe().equalsIgnoreCase(ma));
  JOptionPane.showMessageDialog(this, "Đã xóa thẻ (chế độ demo)!");
  taiDuLieuThe();
  }

  // ================== TAB 2: LOẠI XE & BẢNG GIÁ ==================
  private JPanel taoPanelLoaiXe() {
  JPanel p = new JPanel(new BorderLayout(10, 10));
  p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

  JPanel pTrai = new JPanel();
  pTrai.setLayout(new BoxLayout(pTrai, BoxLayout.Y_AXIS));
  pTrai.setPreferredSize(new Dimension(280, 0));
  pTrai.setBorder(BorderFactory.createTitledBorder("Cập Nhật Giá Vé"));

  txtMaLoai = new JTextField();
  txtTenLoai = new JTextField();
  txtGiaVe = new JTextField();

  pTrai.add(new JLabel("Mã Loại Xe:"));
  pTrai.add(txtMaLoai);
  pTrai.add(Box.createVerticalStrut(10));

  pTrai.add(new JLabel("Tên Loại Xe:"));
  pTrai.add(txtTenLoai);
  pTrai.add(Box.createVerticalStrut(10));

  pTrai.add(new JLabel("Giá Vé (VNĐ):"));
  pTrai.add(txtGiaVe);
  pTrai.add(Box.createVerticalStrut(20));

  JButton btnCapNhatGia = new JButton("Cập Nhật Giá Vé");
  btnCapNhatGia.setBackground(new Color(46, 125, 50));
  btnCapNhatGia.setForeground(Color.WHITE);
  btnCapNhatGia.setFont(new Font("Arial", Font.BOLD, 13));
  btnCapNhatGia.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
  pTrai.add(btnCapNhatGia);

  p.add(pTrai, BorderLayout.WEST);

  String[] cot = {"Mã Loại Xe", "Tên Loại Xe", "Giá Vé Lượt"};
  modelLoaiXe = new DefaultTableModel(cot, 0) {
  @Override
  public boolean isCellEditable(int r, int c) { return false; }
  };
  tblLoaiXe = new JTable(modelLoaiXe);
  tblLoaiXe.setRowHeight(26);
  p.add(new JScrollPane(tblLoaiXe), BorderLayout.CENTER);

  tblLoaiXe.getSelectionModel().addListSelectionListener(e -> {
  int row = tblLoaiXe.getSelectedRow();
  if (row >= 0) {
  txtMaLoai.setText((String) modelLoaiXe.getValueAt(row, 0));
  txtMaLoai.setEditable(false);
  txtTenLoai.setText((String) modelLoaiXe.getValueAt(row, 1));
  String giaStr = modelLoaiXe.getValueAt(row, 2).toString().replace("đ", "").replace(",", "").trim();
  txtGiaVe.setText(giaStr);
  }
  });

  btnCapNhatGia.addActionListener(e -> xuLyCapNhatGia());

  return p;
  }

  private void taiDuLieuLoaiXe() {
  modelLoaiXe.setRowCount(0);
  boolean coSQL = false;
  try {
  Connection conn = KetNoiDB.getKetNoi();
  if (conn != null) {
  Statement st = conn.createStatement();
  ResultSet rs = st.executeQuery("SELECT * FROM LoaiXe ORDER BY MaLoaiXe");
  while (rs.next()) {
  coSQL = true;
  modelLoaiXe.addRow(new Object[]{
  rs.getString("MaLoaiXe"),
  rs.getString("TenLoaiXe"),
  tienFormat.format(rs.getDouble("GiaVe"))
  });
  }
  rs.close();
  st.close();
  conn.close();
  }
  } catch (Exception ignored) {}

  if (!coSQL) {
  for (LoaiXe lx : mockLoaiList) {
  modelLoaiXe.addRow(new Object[]{lx.getMaLoaiXe(), lx.getTenLoaiXe(), tienFormat.format(lx.getGiaVe())});
  }
  }
  }

  private void xuLyCapNhatGia() {
  int row = tblLoaiXe.getSelectedRow();
  if (row < 0) {
  JOptionPane.showMessageDialog(this, "Vui lòng chọn loại xe trên bảng!", "Nhắc nhở", JOptionPane.WARNING_MESSAGE);
  return;
  }

  String ma = txtMaLoai.getText().trim();
  String ten = txtTenLoai.getText().trim();
  double gia;
  try {
  gia = Double.parseDouble(txtGiaVe.getText().trim());
  } catch (Exception ex) {
  JOptionPane.showMessageDialog(this, "Giá vé phải là số nguyên dương hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
  return;
  }

  try {
  Connection conn = KetNoiDB.getKetNoi();
  if (conn != null) {
  PreparedStatement ps = conn.prepareStatement("UPDATE LoaiXe SET TenLoaiXe = ?, GiaVe = ? WHERE MaLoaiXe = ?");
  ps.setString(1, ten);
  ps.setDouble(2, gia);
  ps.setString(3, ma);
  ps.executeUpdate();
  ps.close();
  conn.close();
  JOptionPane.showMessageDialog(this, "Cập nhật bảng giá thành công!");
  taiDuLieuLoaiXe();
  return;
  }
  } catch (Exception e) {
  JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + e.getMessage(), "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
  return;
  }

  for (LoaiXe lx : mockLoaiList) {
  if (lx.getMaLoaiXe().equalsIgnoreCase(ma)) {
  lx.setTenLoaiXe(ten);
  lx.setGiaVe(gia);
  break;
  }
  }
  JOptionPane.showMessageDialog(this, "Cập nhật thành công (chế độ demo)!");
  taiDuLieuLoaiXe();
  }

  public static void main(String[] args) {
  SwingUtilities.invokeLater(() -> new QuanLyTheXeFrame().setVisible(true));
  }
}
