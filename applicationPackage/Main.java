package applicationPackage;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String agrs[]){
        SwingUtilities.invokeLater(() -> {
            try {
                new ServerFoundation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    
    SwingUtilities.invokeLater(() -> {
        try {
            new ClientFoundation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    });
}
}
