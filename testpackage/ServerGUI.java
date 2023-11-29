package testpackage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServerGUI extends JFrame {
    private ServerSocket serverSocket;
    private JTextArea chatArea;
    private List<DataOutputStream> outputStreams;

    private ServerGUI() {
        initUI();
        initServer();
    }

    private void initUI() {
        setTitle("Server GUI");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send");

        sendButton.addActionListener(e -> {
            String message = messageField.getText();
            sendMessageToAllClients("Server: " + message);
            messageField.setText("");
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(inputPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    serverSocket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        setVisible(true);
    }

    private void initServer() {
        outputStreams = new ArrayList<>();

        try {
            serverSocket = new ServerSocket(6666);
            chatArea.append("Server started on: " + serverSocket.getLocalSocketAddress() + "\n");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                chatArea.append("Client connected: " + clientSocket.getInetAddress() + "\n");

                DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());
                outputStreams.add(dout);

                // Start a new thread to handle the client
                Thread clientHandler = new Thread(() -> handleClient(clientSocket));
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());

            String clientInput;

            while (true) {
                clientInput = dis.readUTF();
                chatArea.append("Client says: " + clientInput + "\n");

                sendMessageToAllClients("Client " + clientSocket.getInetAddress() + ": " + clientInput);

                if (clientInput.equals("stop")) {
                    break;
                }
            }

            chatArea.append("Client disconnected: " + clientSocket.getInetAddress() + "\n");

            dis.close();
            clientSocket.close();
            outputStreams.removeIf(outputStream -> outputStream.equals(dis));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToAllClients(String message) {
        for (DataOutputStream dout : outputStreams) {
            try {
                dout.writeUTF(message);
                dout.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ServerGUI());
    }
}
