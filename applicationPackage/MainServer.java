package applicationPackage;

import javax.swing.SwingUtilities;

public class MainServer {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ServerFoundation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
