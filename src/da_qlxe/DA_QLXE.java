package da_qlxe;

import javax.swing.SwingUtilities;

public class DA_QLXE {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DangNhapFrame().setVisible(true));
    }
}