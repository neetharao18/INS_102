//package Caeser_cipher;

import java.util.Scanner;

class Caeser_cipher {

    public static StringBuffer encrypt(String text, int s) {
        StringBuffer ss = new StringBuffer();
        
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            
            
            if (Character.isLetter(ch)) {
               
                if (Character.isUpperCase(ch)) {
                    ch = (char) (((int)ch + s - 65) % 26 + 65);  
                    //ss.append(ch);
                } 
                else{

                    ch = (char) (((int) ch + s - 97) % 26 + 97);
                }
            } 
            else {
                
                ss.append(ch);
                continue;
            }
            
            
           ss.append(ch);
        }
        
        return ss;
    }

    public static StringBuffer decrypt(String text,int s){
        StringBuffer ss2 = new StringBuffer();
        
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            
            
            if (Character.isLetter(ch)) {
               
                if (Character.isUpperCase(ch)) {
                    ch = (char) (((int)ch - s - 65+26) % 26 + 65);  
                    //ss.append(ch);
                } 
                else{

                    ch = (char) (((int) ch - s - 97+26) % 26 + 97);
                }
            } 
            else {
                
                ss2.append(ch);
                continue;
            }
            
            
           ss2.append(ch);
        }
        
        return ss2;

    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String encrypted=" ";
        
        
        System.out.println("Enter the text to encrypt:");
        String text = sc.nextLine(); 
        System.out.println("Enter the shift value:");
        int shift = sc.nextInt();
        System.out.println("Encrypted text: " + encrypt(text, shift).toString());
        encrypted=encrypt(text, shift).toString();
        System.out.println("decrypted message is "+decrypt(encrypted,shift));
        
        
        //System.out.println("decrypted text: " + decrypt(text,shift));
        
       sc.close();
    }
}
