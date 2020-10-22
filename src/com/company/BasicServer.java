package com.company;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class BasicServer extends Thread {

    private String error = "";
    private final String[] operations = { "+", "-", "*", "/" };
    private final Socket socket;
    private BufferedReader bufferedReader;

    public BasicServer(Socket socket){
        this.socket = socket;
        try {
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String []args) {

        try{
            int port = 10000;
            ServerSocket serverSocket = new ServerSocket(port);
            JOptionPane.showMessageDialog(null,"Servidor ativo na porta: "+
                    port);

            while(true){
                System.out.println("Aguardando conexão...");
                Socket connection = serverSocket.accept();
                System.out.println("Cliente conectado...");
                Thread thread = new BasicServer(connection);
                thread.start();
            }

        }catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void run(){

        try{
            OutputStream outputStream =  this.socket.getOutputStream();
            Writer outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            String expression;

            while(true)
            {
                expression = bufferedReader.readLine();
                String result = send(bufferedWriter, expression);
                if (!(result.isEmpty() | result.isBlank())) {
                    System.out.println(result);
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String send(BufferedWriter bufferedWriter, String expression) throws  IOException
    {
        String result;

        if (expression.isBlank() | expression.isEmpty()) {
            return "";
        }
        result = this.calc(expression);

        if ((this.error.isEmpty() | this.error.isBlank()) && !(result.isEmpty() | result.isBlank())) {
            bufferedWriter.write(result);
        } else {
            bufferedWriter.write(this.error);
        }
        bufferedWriter.flush();

        return result;
    }

    private String calc(String expression) throws ArithmeticException
    {
        List<String> operations = Arrays.asList(this.operations);
        if (!expression.contains(" ")) {
            this.error = "Erro na sitaxe da expressão\r\n";
            return "";
        }

        expression = expression.trim();
        String[] elements = expression.split(" ");
        if (Arrays.stream(elements).count() < 3) {
            this.error = "Erro na sitaxe da expressão\r\n";
            return "";
        }

        double numberOne = Double.parseDouble(elements[0]);
        String operation = elements[1];
        double numberTwo = Double.parseDouble(elements[2]);
        System.out.println(elements[0] + " " + elements[1] + " " + elements[2]);
        String result = "";

        if (!(operations.contains(operation))) {
            this.error = "Operador inválido\r\n";
            return "";
        }

        this.error = "";

        if (operation.equals("+")) {
            result = expression + " = " + this.plus(numberOne, numberTwo) + "\r\n";
        }

        if (operation.equals("-")) {
            result = expression + " = " + this.minus(numberOne, numberTwo) + "\r\n";
        }

        if (operation.equals("*")) {
            result = expression + " = " + this.mult(numberOne, numberTwo) + "\r\n";
        }

        if (operation.equals("/")) {
            result = expression + " = " + this.divide(numberOne, numberTwo) + "\r\n";
        }

        if (result.isEmpty()) {
            this.error = "Problema na expressão\r\n";
        }

        return result;
    }

    private double divide(double numberOne, double numberTwo) {
        if (numberTwo == Double.parseDouble("0")) {
            this.error = "Divisão por 0 não definida\r\n";
            return 0;
        }
        this.error = "";
        return numberOne / numberTwo;
    }

    private double mult(double numberOne, double numberTwo) {
        return numberOne * numberTwo;
    }

    private double minus(double numberOne, double numberTwo) {
        return numberOne - numberTwo;
    }

    private double plus(double numberOne, double numberTwo) {
        return numberOne + numberTwo;
    }

}