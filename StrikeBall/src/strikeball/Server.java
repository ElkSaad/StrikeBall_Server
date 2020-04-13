/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strikeball;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Math.random;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elkha
 */
public class Server {
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    
    public void gioco(int port) {
        
        try {
            server = new ServerSocket(port);
            System.out.println("Server avviato\n");

            System.out.println("Aspettando connessione con il client...\n");

            socket = server.accept();
            System.out.println("INIZIO GIOCO [Connessione stabilita con il client]\n");

             
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream()); 
            
            Random random = new Random();
            String ng = String.format("%04d", random.nextInt(10000)); //numero giusto
            System.out.println("\n"+ ng +"\n");
            String tentativo[];
            String n = "1234"; 
            String messaggio = ""; //del client
            int t = 4; //tentativi (aggiungere difficoltà)
            int s = 0; //strike
            int b = 0; //ball
            
            while (!messaggio.equals("fine")) {
                try {
                    while(t!=0){
                        s = 0;
                        b = 0;
                        messaggio = in.readUTF();
                        tentativo = messaggio.split(" ");
                        System.out.println(Arrays.toString(tentativo));
                        for(int i = 0; i < 4; i++){
                            for(int y = 0; y < 4; y++){
                                if(messaggio.charAt(i) == ng.charAt(i)){ //qui controllo tramite la variabile y se si tratta di uno strike in quanto controlla le posizioni
                                    s++;
                                } else if(messaggio.charAt(i) == ng.charAt(y)){ //invece qui controllo se si tratti di un ball, in quanto verifica soltanto se i numeri sono uguali
                                    b++;
                                }
                                if(s == 16){
                                    System.out.println("Hai vinto!!");
                                    out.writeUTF("Hai vinto!"); 
                                }
                            }
                        }
                        t--;
                        s=s/4;
                        System.out.println("Tentativi Rimasti: " + t + "\tStrike: " + s + "\tBall: " + b);
                        out.writeBytes("Tentativi Rimasti: " + t + "\tStrike: " + s + "\tBall: " + b + "\n");
                        if (t == 0) {
                            System.out.println("GAME OVER");
                            try {
                                server.close();
                                in.close();
                                out.close();
                            } catch (IOException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;  
                        }
                    }
                } catch (IOException i) {
                    System.out.println(i);
                }
            }
            System.out.println("Chiusura connessione...");
            
            socket.close();
            out.close();
            in.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }
}
