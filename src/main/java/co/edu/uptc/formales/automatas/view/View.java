package co.edu.uptc.formales.automatas.view;

import java.util.Scanner;

public class View {
    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showInt(int data){
        System.out.println(data);
    }

    public String entradaString() {
        Scanner sc = new Scanner(System.in);
        String entrada = sc.nextLine();
        return entrada;
    }

    public int entradaInt() {
        Scanner sc = new Scanner(System.in);
        int entrada = sc.nextInt();
        return entrada;
    }
}
