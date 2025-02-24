import java.util.Scanner;
import java.lang.*;
class hill_cipher {
    //to generate the key matrix
    static void generate_key(String key,int key_matrix[][]){
       // int[][] key_matrix=new int[3][3];
       
      
        int k=0;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                key_matrix[i][j]=(key.charAt(k)-65);
                k++;
            }
        }

    }
    //to generate cipher matrix in int
    static void generate_cipher(int message_vector[][], int cipher_matrix[][],int key_matrix[][] ){
        //multiply key matrx*plt matrix % 26
        for(int i=0;i<3;i++){
            for(int j=0;j<1;j++){
                cipher_matrix[i][j]=0;
                for(int k=0;k<3;k++){
                    cipher_matrix[i][j]+=key_matrix[i][k]*message_vector[k][j];
                }
                cipher_matrix[i][j]=cipher_matrix[i][j]%26;
            }
        }

    }
    //to generate the cipher text
    static void final_matrix(String plaintext,String key){
        int[][] key_matrix=new int[3][3];
        generate_key(key,key_matrix);
        if (plaintext.length() % 3 != 0) {
            plaintext += 'X';  // Add padding character to make the length a multiple of 3
        }

        int[][] message_vector=new int[3][1];
        for(int i=0;i<3;i++){
            message_vector[i][0]=(plaintext.charAt(i)-65);      
        }

        int[][] cipher_matrix=new int[3][1];
        generate_cipher(message_vector, cipher_matrix, key_matrix);

        String cipher_text="";
        for(int i=0;i<3;i++){
            cipher_text+=(char)(cipher_matrix[i][0]+65);
            
        }
        System.out.println("Ciphertext= "+cipher_text);



    }

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("enter the plain text");
        String plaintext=sc.nextLine();
        System.out.println("enter the key");
        String key=sc.nextLine();

       
        final_matrix(plaintext,key);
        

    }

    
}
