public class Customer{
    //Attributes
    String name;
    static int customerId=1;
    
    
    
    //Constructors
    public Customer(){
        this.name="";
        
    }
    public Customer(String name){
        this.name=name;
        this.customerId=customerId++;
      
        
    }
    
    
    
    //Methods
    
}