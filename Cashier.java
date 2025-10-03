public class Cashier {
    //Attributes
    String name;
    static int cashierId;
    boolean isOccupied;
    CashierQueue queue;
    
    
    
    //Constructors
    public Cashier(){
        
    }
    
    public Cashier (String name, int cashierId){
        this.name=name;
        this.cashierId=cashierId;
        cashierId++;
        
    }
    //Methods
    
    public void entersLine(){
        
    }
    
    
    
}
