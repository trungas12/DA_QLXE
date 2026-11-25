-- ===============================================================
-- HỆ THỐNG QUẢN LÝ BÃI GỬI XE (PARKING LOT MANAGEMENT SYSTEM)
-- Cơ sở dữ liệu: DA_QLXE
-- Khôi phục và tái tạo từ tệp sao lưu: Da_QLXE
-- ===============================================================

CREATE DATABASE DA_QLXE;
GO

USE DA_QLXE;
GO

-- 1. BẢNG LOẠI XE (LoaiXe)
IF OBJECT_ID('dbo.LoaiXe', 'U') IS NOT NULL DROP TABLE dbo.LoaiXe;
CREATE TABLE dbo.LoaiXe (
    MaLoaiXe VARCHAR(10) NOT NULL,
    TenLoaiXe NVARCHAR(50) NOT NULL,
    GiaVe DECIMAL(18, 0) NOT NULL,
    CONSTRAINT PK_LoaiXe PRIMARY KEY (MaLoaiXe)
);
GO

-- 2. BẢNG THẺ XE (TheXe)
IF OBJECT_ID('dbo.TheXe', 'U') IS NOT NULL DROP TABLE dbo.TheXe;
CREATE TABLE dbo.TheXe (
    MaThe VARCHAR(10) NOT NULL,
    TrangThai NVARCHAR(20) NOT NULL CONSTRAINT DF_TheXe_TrangThai DEFAULT (N'Rảnh'),
    CONSTRAINT PK_TheXe PRIMARY KEY (MaThe)
);
GO

-- 3. BẢNG NHÂN VIÊN (NhanVien)
IF OBJECT_ID('dbo.NhanVien', 'U') IS NOT NULL DROP TABLE dbo.NhanVien;
CREATE TABLE dbo.NhanVien (
    MaNV VARCHAR(10) NOT NULL,
    TenNV NVARCHAR(50) NOT NULL,
    TenDangNhap VARCHAR(50) NOT NULL,
    MatKhau VARCHAR(50) NOT NULL,
    VaiTro NVARCHAR(20) NOT NULL CONSTRAINT DF_NhanVien_VaiTro DEFAULT (N'Nhân Viên'),
    CONSTRAINT PK_NhanVien PRIMARY KEY (MaNV),
    CONSTRAINT UQ_NhanVien_TenDangNhap UNIQUE (TenDangNhap)
);
GO

-- 4. BẢNG LƯỢT GỬI XE (LuotGuiXe)
IF OBJECT_ID('dbo.LuotGuiXe', 'U') IS NOT NULL DROP TABLE dbo.LuotGuiXe;
CREATE TABLE dbo.LuotGuiXe (
    MaLuotGui INT IDENTITY(1,1) NOT NULL,
    MaThe VARCHAR(10) NOT NULL,
    MaLoaiXe VARCHAR(10) NOT NULL,
    BienSo VARCHAR(20) NOT NULL,
    ThoiGianVao DATETIME NOT NULL CONSTRAINT DF_LuotGuiXe_ThoiGianVao DEFAULT (GETDATE()),
    ThoiGianRa DATETIME NULL,
    TienThu DECIMAL(18, 0) NOT NULL CONSTRAINT DF_LuotGuiXe_TienThu DEFAULT (0),
    TrangThai NVARCHAR(30) NOT NULL CONSTRAINT DF_LuotGuiXe_TrangThai DEFAULT (N'Trong Bãi'),
    MaNV VARCHAR(10) NULL,
    CONSTRAINT PK_LuotGuiXe PRIMARY KEY (MaLuotGui),
    CONSTRAINT FK_LuotGuiXe_MaThe FOREIGN KEY (MaThe) REFERENCES dbo.TheXe (MaThe),
    CONSTRAINT FK_LuotGuiXe_MaLoaiXe FOREIGN KEY (MaLoaiXe) REFERENCES dbo.LoaiXe (MaLoaiXe),
    CONSTRAINT FK_LuotGuiXe_MaNV FOREIGN KEY (MaNV) REFERENCES dbo.NhanVien (MaNV)
);
GO

-- ===============================================================
-- CHÈN DỮ LIỆU BAN ĐẦU (DỮ LIỆU GỐC TRONG FILE BACKUP)
-- ===============================================================

-- Dữ liệu bảng LoaiXe
INSERT INTO dbo.LoaiXe (MaLoaiXe, TenLoaiXe, GiaVe) VALUES
('XM', N'Xe máy', 5000),
('OTO', N'Ô tô con', 20000),
('XD', N'Xe đạp / Xe điện', 3000);

-- Dữ liệu bảng TheXe
INSERT INTO dbo.TheXe (MaThe, TrangThai) VALUES
('THE01', N'Đang Gửi'),
('THE02', N'Đang Gửi'),
('THE03', N'Rảnh'),
('THE04', N'Rảnh'),
('THE05', N'Rảnh');

-- Dữ liệu bảng NhanVien
INSERT INTO dbo.NhanVien (MaNV, TenNV, TenDangNhap, MatKhau, VaiTro) VALUES
('NV01', N'Nguyễn Văn Quản Lý', 'admin', 'admin123', N'Admin'),
('NV02', N'Trần Văn Trực', 'baove1', '123456', N'Nhân Viên'),
('NV03', N'Lê Thị Ca Đêm', 'baove2', '123456', N'Nhân Viên');

-- Dữ liệu bảng LuotGuiXe
SET IDENTITY_INSERT dbo.LuotGuiXe ON;
INSERT INTO dbo.LuotGuiXe (MaLuotGui, MaThe, MaLoaiXe, BienSo, ThoiGianVao, ThoiGianRa, TienThu, TrangThai, MaNV) VALUES
(1, 'THE03', 'XM', '29B1-123.45', '2024-05-10 07:30:00', '2024-05-10 11:45:00', 5000, N'Đã Thanh Toán', 'NV02'),
(2, 'THE04', 'OTO', '30A-999.88', '2024-05-10 08:15:00', '2024-05-10 17:00:00', 20000, N'Đã Thanh Toán', 'NV02'),
(3, 'THE01', 'XM', '59T1-888.88', '2024-05-11 06:45:00', NULL, 0, N'Trong Bãi', 'NV02'),
(4, 'THE02', 'OTO', '30E-567.89', '2024-05-11 09:20:00', NULL, 0, N'Trong Bãi', 'NV03');
SET IDENTITY_INSERT dbo.LuotGuiXe OFF;
GO
