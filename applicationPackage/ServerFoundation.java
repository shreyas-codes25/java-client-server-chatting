package applicationPackage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFoundation {
    //initializing required elements
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private DataOutputStream dataOutputStream;
    private Socket clientSocket;

    //starting server through constructor
    public ServerFoundation() {
        initialize();
        startServer();
    }

    //GUI Configuration
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Server");
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

        // Send message through 'Send' Button
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        frame.setVisible(true);
    }

    //Start Server Method to start the server port
    //And receiveing messages from client
    @SuppressWarnings("all")
    private void startServer() {
        Thread serverThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(1234);
                clientSocket = serverSocket.accept();
                if(serverSocket.isBound()){
                    displayMessage("connection successful");
                }
                dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());

                while (true) {
                    String receivedMessage = dataInputStream.readUTF();
                    displayMessage("Client says: " + receivedMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        serverThread.setDaemon(true);
        serverThread.start();
    }
    //message sending
    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            try {
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
                displayMessage("You: " + message);
                messageField.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Display data to the GUI
    private void displayMessage(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append(message + "\n"));
    }
}
