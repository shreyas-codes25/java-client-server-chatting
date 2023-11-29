package applicationPackage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientFoundation {
    //Initialization of required elements
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private DataOutputStream serverOutputStream;
    private DataInputStream dataInputStream;

    //using constructor to start the client
    public ClientFoundation() {
        initialize();
        connectToServer();
    }

    //Initializing GUI of client side
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Client");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        
        JPanel messagePanel = new JPanel();
        panel.add(messagePanel, BorderLayout.SOUTH);
        messagePanel.setLayout(new BorderLayout(0, 0));

        messageField = new JTextField();
        messagePanel.add(messageField, BorderLayout.CENTER);
        messageField.setColumns(10);

        JButton sendButton = new JButton("Send");
        messagePanel.add(sendButton, BorderLayout.EAST);
        
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        

        frame.setVisible(true);
    }

    //Connecting to server and receiveing messages from server
    @SuppressWarnings("all")
    private void connectToServer() {
        Thread serverThread = new Thread(() -> {
        try {
            Socket socket = new Socket("localhost", 6666);
            serverOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            while (true) {
                String receivedMessage = dataInputStream.readUTF();
                displayMessage("Server says: " + receivedMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    });
    serverThread.setDaemon(true);
    serverThread.start();
    }

    //send messsage to the server
    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            try {

                serverOutputStream.writeUTF(message);
                serverOutputStream.flush();


                chatArea.append("You: " + message + "\n");


                messageField.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Display message to the Server
    private void displayMessage(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append(message + "\n"));
    }
}
