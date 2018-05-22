
/**
 * Write a description of class HillCipher here.
 * 
 * @author Will Luttmann 
 * @version (a version number or a date)
 */
public class HillCipher
{
    
    //for use with 3x3 array
    public static int determinant(int a[][]){
        //calculate determinant
        int det = ((a[0][0]*a[1][1]*a[2][2]) + (a[0][1]*a[1][2]*a[2][0]) + (a[0][2]*a[2][1]*a[1][0])
                 - (a[0][2]*a[1][1]*a[2][0]) - (a[1][2]*a[2][1]*a[0][0]) - (a[0][1]*a[1][0]*a[2][2]));
            
            //determinant mod 26
            det = det % 26;
            while(det < 0){
                det += 26;
            }
        
            int x[] = {1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25};
            int xinverse[] ={1, 9, 21, 15, 3, 19, 7, 23, 11, 5, 17, 25};
            
            //find determinant multiplicative inverse
            for(int i = 0; i < x.length; i++){
                if (det == x[i]){
                    det = xinverse[i];
                    break;
                }
            }
            
            //return multiplicative inverse
            return det;
           
    }
    
    
    public static int coefficient(int row, int col, int a[][]){
        //make condensed array
        int condense[][] = new int [2][2];
        int coeff;
        //initialize new array with specified row and column deleted
        int x = 0;
        for(int i = 0; i < a.length; i++){
            if(i == row)
                continue;
                
            int y = 0;
            for(int j = 0; j < a[0].length; j++){
                if (j == col)
                    continue;
                    
                condense[x][y] = a[i][j];
                y++;
            }
            
            x++;
        }
        
        int exp = row + 1 + col+ 1;
        int num= (int)Math.pow(-1,exp);
        //coeff calculation
        coeff = num * ((condense[0][0]*condense[1][1])-(condense[0][1]*condense[1][0]));
        
        return coeff;
    }
    
    public static int[][] inverseMatrix(int a[][]){
        int coefmat[][] = new int[3][3];
        int inverse[][] = new int[3][3];
        
        //initialize coefficient matrix -- transversed
        coefmat[0][0] = coefficient(0,0,a);
        coefmat[0][1] = coefficient(1,0,a);
        coefmat[0][2] = coefficient(2,0,a);
        
        coefmat[1][0] = coefficient(0,1,a);
        coefmat[1][1] = coefficient(1,1,a);
        coefmat[1][2] = coefficient(2,1,a);
        
        coefmat[2][0] = coefficient(0,2,a);
        coefmat[2][1] = coefficient(1,2,a);
        coefmat[2][2] = coefficient(2,2,a);
        
        
        //coefficient matrix*determinant %26

        for(int i =0; i < 3; i++){
            for(int j =0; j < 3; j++){
                inverse[i][j]= coefmat[i][j]*determinant(a);
                
                inverse[i][j]= inverse[i][j]%26;
                
                if(inverse[i][j]<0)
                    inverse[i][j] += 26;
            }

        }
        
       
        
        return inverse;
    }
    
    
    public static String encrypt(String plaintext, int key[][]){
        String ciphertext = "";
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        
        
        //if string is not divisible by 3, add x or xx
        if(plaintext.length() % 3 == 1){
            plaintext += "xx";
        }
        
        if(plaintext.length() % 3 == 2){
            plaintext += "x";
        }
        
        int rows = plaintext.length()/3;
        int cols = 3;

        int pnum[][] = new int [rows][cols];
        char a[] = plaintext.toCharArray();
        int cipherint [][] = new int[rows][cols];
        int count = 0;
        int sum = 0;
        
        //assign number matrix to corresponding plaintext
        for(int i = 0; i< rows; i++){
            for(int j = 0; j < cols; j++){
                pnum[i][j] = alpha.indexOf(a[count]);
                count++;
            }
        }
        
        //does matrix matrix multiplication
        for(int i = 0; i< rows; i++){
            for(int j = 0; j < cols; j++){
                for(int k = 0; k < 3; k++){
                  sum = sum + pnum[i][k] * key [k][j];  
                }
                cipherint[i][j] = sum % 26;
                sum = 0;
            }
        }
        

        //converts ciphertext nums to letters
        for(int i = 0; i< rows; i++){
            for(int j = 0; j < cols; j++){
                ciphertext += alpha.charAt(cipherint[i][j]); 
            }
        }
        
        
        return ciphertext;
    }
    
    
    public static String decrypt(String ciphertext, int keyinv[][]){
        String plaintext = "";
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        
        int rows = ciphertext.length()/3;
        int cols = 3;
        
        int ciphernums[][] = new int [rows][cols];
        char a[] = ciphertext.toCharArray();
        int plainnums [][] = new int[rows][cols];
        int count = 0;
        int sum = 0;
        
        //assign number matrix to corresponding ciphertext
        for(int i = 0; i< rows; i++){
            for(int j = 0; j < cols; j++){
                ciphernums[i][j] = alpha.indexOf(a[count]);
                count++;
            }
        }
        
        
        //does matrix matrix multiplication
        for(int i = 0; i< rows; i++){
            for(int j = 0; j < cols; j++){
                for(int k = 0; k < 3; k++){
                  sum = sum + ciphernums[i][k] * keyinv [k][j];  
                }
                plainnums[i][j] = sum % 26;
                sum = 0;
            }
        }
        
        
        //converts plaintext nums to letters
        for(int i = 0; i< rows; i++){
            for(int j = 0; j < cols; j++){
                plaintext += alpha.charAt(plainnums[i][j]); 
            }
        }
        
        return plaintext;
    }
    
    
    public static void main(){
         
        int a[][] = new int[3][3];
        int ainverse[][] = new int [3][3];
        
        a[0][0] = 4;    
        a[0][1] = 9;    
        a[0][2] = 15;    
        
        a[1][0] = 15;       
        a[1][1] = 17;       
        a[1][2] = 6;    
        
        a[2][0] = 24;   
        a[2][1] = 0;    
        a[2][2] = 17;   
        
        System.out.println("Key: ");
        for(int i =0; i < 3; i++){
            for(int j =0; j < 3; j++){
                System.out.print(a[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();
        
        ainverse = inverseMatrix(a);
       
       System.out.println("Key Inverse: ");
       for(int i =0; i < 3; i++){
            for(int j =0; j < 3; j++){
                System.out.print(ainverse[i][j] + "\t");
            }
            System.out.println();
        }
       System.out.println();
       
       
       String string1 = "paymoremoney";

       System.out.println("Plaintext: " + string1);
       
       System.out.println("Encrypting...");
       string1 = encrypt(string1, a);
       System.out.println("Ciphertext: " + string1);
       
       System.out.println("Decrypting...");
       string1 = decrypt(string1,ainverse);
       System.out.println("Plaintext: " + string1);
       System.out.println();
       System.out.println();
       
       
       int b[][] = new int[3][3];
       int binverse[][] = new int [3][3];
       
        b[0][0] = 1;
        b[0][1] = 2;
        b[0][2] = 3;
        
        b[1][0] = 4;
        b[1][1] = 5;
        b[1][2] = 6;
        
        b[2][0] = 7;
        b[2][1] = 8;
        b[2][2] = 10;
        
         System.out.println("Key: ");
        for(int i =0; i < 3; i++){
            for(int j =0; j < 3; j++){
                System.out.print(b[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();
        
       binverse = inverseMatrix(b);
       
       System.out.println("Key Inverse: ");
       for(int i =0; i < 3; i++){
            for(int j =0; j < 3; j++){
                System.out.print(binverse[i][j] + "\t");
            }
            System.out.println();
        }
       System.out.println();
       
       String string2 = "hillcipherisfuntome";
       System.out.println("Plaintext: " + string2);
       
       System.out.println("Encrypting...");
       string2 = encrypt(string2, b);
       System.out.println("Ciphertext: " + string2);
       
       System.out.println("Decrypting...");
       string2 = decrypt(string2,binverse);
       System.out.println("Plaintext: " + string2);
       System.out.println();
       System.out.println();
    }
    
    
    
}
