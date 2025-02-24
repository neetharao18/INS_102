import java.util.Scanner;
import java.lang.*;

class playfair{
    //input the elements in the matrix
    static char[][] input_matrix(String key){
        //create the main matrix
        char[][] mainmatrix=new char[5][5];
        //create an extra array to store the key
        char[] store_key=new char[30];  
        //create the alphabets ka array
        char[] alpha=new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        //for(int k=0;k<key.length();k++){
        key=key.replace('j', 'i');
        int keyindex=0;
        int storeIndex = 0;
             for(int i=0;i<5;i++){
                 for(int j=0;j<5;j++){
                
                    if (keyindex < key.length() && storeIndex < 25 && !contains(store_key, key.charAt(keyindex))) {
                        store_key[storeIndex]=key.charAt(keyindex);
                        mainmatrix[i][j]=key.charAt(keyindex);
                        keyindex++;
                        storeIndex++;
                    }
                    
               
                }
            }
            
            for(int i=0;i<5;i++){
                for(int j=0;j<5;j++){
                    if(mainmatrix[i][j]==0){
                        for(int k=0;k<alpha.length;k++){
                             if(!contains(store_key,alpha[k])){
                                mainmatrix[i][j]=alpha[k];
                                store_key[storeIndex]=alpha[k];
                                storeIndex++;
                                break;
                             }
                        }    
                    }
               }
           }
    

            return mainmatrix;
            
           
        }
        //to find if the element is already entered into the matrix
        static boolean contains(char[] store_key, char c) {
            for (int i = 0; i < store_key.length; i++) {
                if (store_key[i] == c) {
                    return true;
                }
            }
            return false;
        }
        // to generate the cipher text
        static String generate_cipher(String plaintext,String keyword ){
            char[][] matrix=input_matrix(keyword);
            StringBuilder ciphertext=new StringBuilder();
            if(plaintext.length()%2!=0){
                plaintext=plaintext+'x';
            }
            for(int i=0;i<plaintext.length();i+=2){
                char a=plaintext.charAt(i);
                char b=plaintext.charAt(i+1);
                int[] pos1=find_pos(matrix,a);
                int[] pos2=find_pos(matrix,b);
                if(pos1[0]==pos2[0]){
                    ciphertext.append(matrix[pos1[0]][(pos1[1]+1)%5]);
                    ciphertext.append(matrix[pos2[0]][(pos2[1]+1)%5]);

                }
                else if(pos1[1]==pos2[1]){
                    ciphertext.append(matrix[(pos1[0] + 1) % 5][pos1[1]]);
                    ciphertext.append(matrix[(pos2[0] + 1) % 5][pos2[1]]);

                }
                else{
                    ciphertext.append(matrix[pos1[0]][pos2[1]]);
                    ciphertext.append(matrix[pos2[0]][pos1[1]]);
                }


            }
            return ciphertext.toString();


        }
        //to find the position of the character in the matrix
        static int[] find_pos(char matrix[][], char c){
            int[] pos=new int[2];
            for(int i=0;i<5;i++){
                for(int j=0;j<5;j++){
                         if(matrix[i][j]==c){
                            pos[0]=i;
                            pos[1]=j;
                            return pos;
                         }
                }
            }
            return pos;
            
            
        }
        static String decrypt_cipher(String ciphertext,String keyword ){
            char[][] matrix1=input_matrix(keyword);
            StringBuilder ciphertext2=new StringBuilder();
           /*  if(plaintext.length()%2!=0){
                plaintext=plaintext+'x';
            }*/
            for(int i=0;i<ciphertext.length();i+=2){
                char a=ciphertext.charAt(i);
                char b=ciphertext.charAt(i+1);
                int[] pos1=find_pos(matrix1,a);
                int[] pos2=find_pos(matrix1,b);
                if(pos1[0]==pos2[0]){
                    ciphertext2.append(matrix1[pos1[0]][(pos1[1]-1+5)%5]);
                    ciphertext2.append(matrix1[pos2[0]][(pos2[1]-1+5)%5]);

                }
                else if(pos1[1]==pos2[1]){
                    ciphertext2.append(matrix1[(pos1[0] - 1+5) % 5][pos1[1]]);
                    ciphertext2.append(matrix1[(pos2[0] - 1+5) % 5][pos2[1]]);

                }
                else{
                    ciphertext2.append(matrix1[pos1[0]][pos2[1]]);
                    ciphertext2.append(matrix1[pos2[0]][pos1[1]]);
                }


            }
            return ciphertext2.toString();


        }


    public static void main(String[] args) {
        String keyword="monarchy";
        String plaintext="instruments";
        int n=plaintext.length();
       
        /*for(int i=0;i<n;i++){
            arry[i]=plaintext.charAt(i);
            //System.out.println(arry);
        }*/
        String ciphertext=generate_cipher(plaintext, keyword);
        System.out.println("Cipher text: "+ciphertext);
        String decrypt_cipher=decrypt_cipher(ciphertext, keyword);
        System.out.println("Cipher text: "+decrypt_cipher);
        
    }
}
