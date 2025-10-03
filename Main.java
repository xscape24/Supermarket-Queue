import java.util.Random;

public class Main {
    public static void main (String[]args){

       
//Objects
        Thread thread=new Thread();
        Random random=new Random();

        System.out.println("Welcome to the Supermarket Simulator. ");
        try {
          Thread.sleep(5000);
            
        } catch (Exception e) {
            System.out.println("Error");
        }
System.out.println("Click any key to begin");


Cashier cashier=new Cashier();

CashierQueue queue=new CashierQueue();
 

 int a=5;
 int leftLimit=97, rightLimit=122; //a=97 z=122
 int nameLength=6;//length that each name should be
String newName="";
int occupiedTime, numOfCustomers=5;
Customer customer;

  while( numOfCustomers>0) { 
     occupiedTime=random.nextInt(5)+5;//in minutes

  
     customer=new Customer(newName);
 }
 System.out.println("has entered the queue");
 numOfCustomers--;
 }

 



    
    
}
