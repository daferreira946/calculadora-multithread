package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private final JTextArea text;
    private final JTextField textMessage;
    private final JButton buttonSend;
    private final JButton buttonQuit;
    private Socket basicSocket;
    private Socket specialSocket;
    private OutputStream basicOutputStream;
    private OutputStream specialOutputStream;
    private Writer basicOutputStreamWriter;
    private Writer specialOutputStreamWriter;
    private BufferedWriter basicBufferedWriter;
    private BufferedWriter specialBufferedWriter;

    public Client() {

        JPanel panelContent = new JPanel();

        text = new JTextArea(10,20);
        text.setEditable(false);
        text.setBackground(new Color(240, 240,240));

        textMessage = new JTextField(20);

        JLabel labelResult = new JLabel("Resultado");
        JLabel labelExpression = new JLabel("Expressão");

        JTextArea options = new JTextArea();
        options.setEditable(false);
        options.setBackground(new Color(240, 240 ,240));
        options.setText("""
                Para operações básicas digite: N1 +|-|*|/ N2
                Para operações especiais digite: N1 %|^|# N2
                Sempre separado por espaços: N1 op N2
                Usar formato de decimal com . (Ex.: 2.58)
                Só uma operação por vez
                --------------------------------------------
                Operações disponíveis:
                + = Soma N1 com N2
                - = Subtrai N2 de N1
                * = Multiplica N1 e N2
                / = Divide N1 por N2
                % = N1 por cento de N2
                ^ = N1 elevado por N2
                # = N1 índice da raiz de N2""");

        buttonSend = new JButton("Enviar");
        buttonSend.setToolTipText("Enviar expressão");
        buttonQuit = new JButton("Sair");
        buttonQuit.setToolTipText("Sair da calculadora");

        buttonSend.addActionListener(this);
        buttonQuit.addActionListener(this);
        buttonSend.addKeyListener(this);
        textMessage.addKeyListener(this);

        JScrollPane scrollPane = new JScrollPane(text);
        text.setLineWrap(true);

        panelContent.add(labelResult);
        panelContent.add(scrollPane);
        panelContent.add(labelExpression);
        panelContent.add(textMessage);
        panelContent.add(buttonSend);
        panelContent.add(buttonQuit);
        panelContent.add(options);

        panelContent.setBackground(Color.LIGHT_GRAY);
        text.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
        textMessage.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));

        setContentPane(panelContent);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(350,500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws IOException{

        Client client = new Client();
        client.connect();
        client.listener();

    }

    public void connect() throws IOException{

        basicSocket = new Socket("localhost", 10000);
        basicOutputStream = basicSocket.getOutputStream();
        basicOutputStreamWriter = new OutputStreamWriter(basicOutputStream);
        basicBufferedWriter = new BufferedWriter(basicOutputStreamWriter);
        basicBufferedWriter.flush();

        specialSocket = new Socket("localhost", 20000);
        specialOutputStream = specialSocket.getOutputStream();
        specialOutputStreamWriter = new OutputStreamWriter(specialOutputStream);
        specialBufferedWriter = new BufferedWriter(specialOutputStreamWriter);
        specialBufferedWriter.flush();

    }

    private void distributor(String expression) throws IOException {

        if (expression.contains("%") | expression.contains("^") | expression.contains("#")) {
            this.text.append("Enviando para servidor de operações especiais:\r\n");
            sendSpecial(expression);
        } else if (expression.contains("+") | expression.contains("-") | expression.contains("/") | expression.contains("*")){
            this.text.append("Enviando para servidor de operações básicas:\r\n");
            sendBasic(expression);
        } else {
            text.append("Erro na sintáxe, operador inválido\r\n");
            textMessage.setText("");
        }

    }

    public void sendBasic(String message) throws IOException {

        basicBufferedWriter.write(message + "\r\n");

        basicBufferedWriter.flush();
        textMessage.setText("");

    }

    public void sendSpecial(String message) throws IOException {

        specialBufferedWriter.write(message + "\r\n");

        specialBufferedWriter.flush();
        textMessage.setText("");

    }

    public void listener() throws IOException {

        InputStream basicInputStream = basicSocket.getInputStream();
        InputStreamReader basicInputStreamReader = new InputStreamReader(basicInputStream);
        BufferedReader basicBufferedReader = new BufferedReader(basicInputStreamReader);

        InputStream specialInputStream = specialSocket.getInputStream();
        InputStreamReader specialInputStreamReader = new InputStreamReader(specialInputStream);
        BufferedReader specialBufferedReader = new BufferedReader(specialInputStreamReader);

        String message = "";

        while (!"Sair".equalsIgnoreCase(message)){

            if (basicBufferedReader.ready()) {

                message = basicBufferedReader.readLine();

                if (message.equalsIgnoreCase("sair")) {

                    text.append("Servidor caiu... \r\n");

                } else {

                    text.append(message + "\r\n");

                }
            }

            if (specialBufferedReader.ready()) {
                message = specialBufferedReader.readLine();

                if (message.equalsIgnoreCase("sair")) {

                    text.append("Servidor caiu... \r\n");

                } else {

                    text.append(message + "\r\n");

                }
            }

        }

    }

    public void quit() throws IOException {
        text.append("Desconectado");
        basicBufferedWriter.close();
        specialBufferedWriter.close();
        basicOutputStreamWriter.close();
        specialOutputStreamWriter.close();
        basicOutputStream.close();
        specialOutputStream.close();
        basicSocket.close();
        specialSocket.close();
        System.exit(0);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            if (e.getActionCommand().equals(buttonSend.getActionCommand())) {
                distributor(textMessage.getText());
            } else {
                if (e.getActionCommand().equals(buttonQuit.getActionCommand())) {
                    quit();
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                distributor(textMessage.getText());
            }catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
