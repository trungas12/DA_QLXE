@echo off
chcp 65001 >nul
title BẬT TCP/IP CHO SQL SERVER

:: Kiểm tra quyền Admin
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo Đang xin quyền Administrator...
    powershell -Command "Start-Process -FilePath '%~f0' -Verb RunAs"
    exit /b
)

echo ===================================================
echo   ĐANG BẬT TCP/IP CHO SQL SERVER (MSSQL17.SQLEXPRESS)
echo ===================================================

reg add "HKLM\SOFTWARE\Microsoft\Microsoft SQL Server\MSSQL17.SQLEXPRESS\MSSQLServer\SuperSocketNetLib\Tcp" /v Enabled /t REG_DWORD /d 1 /f
reg add "HKLM\SOFTWARE\Microsoft\Microsoft SQL Server\MSSQL17.SQLEXPRESS\MSSQLServer\SuperSocketNetLib\Tcp\IPAll" /v TcpPort /t REG_SZ /d 1433 /f
reg add "HKLM\SOFTWARE\Microsoft\Microsoft SQL Server\MSSQL17.SQLEXPRESS\MSSQLServer\SuperSocketNetLib\Tcp\IPAll" /v TcpDynamicPorts /t REG_SZ /d "" /f

sc config SQLBrowser start= auto >nul 2>&1

echo.
echo Đang khởi động lại dịch vụ SQL Server...
net stop MSSQL$SQLEXPRESS
net start MSSQL$SQLEXPRESS
net start SQLBrowser

echo.
echo ===================================================
echo   ✓ ĐÃ BẬT TCP/IP VÀ CỔNG 1433 THÀNH CÔNG!
echo ===================================================
echo Bây giờ bạn có thể quay lại NetBeans để chạy phần mềm.
pause
