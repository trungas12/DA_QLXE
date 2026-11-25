# Đồ Án Quản Lý Bãi Gửi Xe (DA_QLXE)

Hệ thống phần mềm Quản Lý Bãi Gửi Xe viết bằng **Java Swing (NetBeans)** kết nối cơ sở dữ liệu **Microsoft SQL Server**.

---

## 📌 Các Tính Năng Chính

Dự án gồm **4 Form giao diện (JFrame)** hoàn chỉnh:

1. **`DangNhapFrame` (Form 1 - Đăng Nhập)**:
   - Đăng nhập xác thực tài khoản nhân viên / quản lý từ SQL Server.
   - Hỗ trợ chế độ tài khoản mẫu (`admin` / `admin123`) để báo cáo, demo.

2. **`QuanLyXeFrame` (Form 2 - Quản Lý Lượt Gửi Xe)**:
   - **Thêm xe vào bãi**: Nhập biển số, chọn thẻ xe, loại xe (xe máy, ô tô, xe đạp).
   - **Sửa thông tin**: Click chọn dòng trên bảng, chỉnh sửa thông tin xe hoặc loại xe.
   - **Xóa bản ghi**: Xóa lượt gửi xe khỏi hệ thống.
   - **Xuất bãi & Tính tiền**: Tự động tính thời gian gửi, đơn giá theo loại xe và tính tiền thu.
   - **Tìm kiếm**: Tìm kiếm nhanh theo biển số xe hoặc mã thẻ xe.
   - **Thống kê**: Hiển thị số lượng xe đang trong bãi và tổng doanh thu thu được.

3. **`QuanLyNhanVienFrame` (Form 3 - Quản Lý Nhân Viên)**:
   - Thêm, sửa, xóa tài khoản nhân viên.
   - Phân quyền tài khoản (Admin / Nhân Viên).
   - Tìm kiếm nhân viên theo tên hoặc mã nhân viên.

4. **`QuanLyTheXeFrame` (Form 4 - Quản Lý Thẻ & Bảng Giá)**:
   - **Quản lý thẻ xe**: Thêm mã thẻ, thay đổi trạng thái (Rảnh, Đang gửi, Khóa/Hỏng), xóa thẻ.
   - **Bảng giá vé**: Cập nhật giá vé theo từng loại phương tiện lưu trực tiếp vào CSDL.

---

## 🛠️ Công Nghệ Sử Dụng
- **Ngôn ngữ**: Java (JDK 17+)
- **Giao diện**: Java Swing (NetBeans IDE)
- **Cơ sở dữ liệu**: Microsoft SQL Server
- **Thư viện kết nối**: `mssql-jdbc-12.8.1.jre11.jar`

---

## 🚀 Hướng Dẫn Cài Đặt & Chạy

1. **Khởi tạo cơ sở dữ liệu**:
   - Mở SQL Server Management Studio (SSMS).
   - Mở và chạy toàn bộ nội dung file script `DA_QLXE.sql`.

2. **Mở dự án trong NetBeans**:
   - Khởi động NetBeans IDE.
   - Chọn **File** -> **Open Project** -> Chọn thư mục `DA_QLXE`.
   - Nhấn phím **F6** (hoặc nút **Run Project**) để chạy chương trình.

3. **Tài khoản đăng nhập mặc định**:
   - **Tên đăng nhập**: `admin`
   - **Mật khẩu**: `admin123`
