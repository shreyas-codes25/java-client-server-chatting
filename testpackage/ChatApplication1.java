package testpackage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatApplication1 {

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private DataOutputStream dataOutputStream;
    private Socket clientSocket;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ChatApplication1();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ChatApplication1() {
        initialize();
        startServer();
    }

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

        // ActionListener for the send button
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        frame.setVisible(true);
    }

    private void startServer() {
        Thread serverThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(6666);
                System.out.println(serverSocket.getLocalSocketAddress());
                clientSocket = serverSocket.accept();
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

    private void displayMessage(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append(message + "\n"));
    }
}
