package applicationPackage;

import javax.swing.SwingUtilities;

public class MainClient {
     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ClientFoundation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
