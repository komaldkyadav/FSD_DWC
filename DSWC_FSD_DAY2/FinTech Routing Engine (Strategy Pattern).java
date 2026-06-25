// PaymentStrategy.java
public interface PaymentStrategy {
    boolean processPayment(double amount);
}

// CreditCardStrategy.java
public class CreditCardStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing $" + amount + " payment via Credit Card");
        return true;
    }
}

// PayPalStrategy.java
public class PayPalStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing $" + amount + " payment via PayPal");
        return true;
    }
}

// CryptoStrategy.java
public class CryptoStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing $" + amount + " payment via Cryptocurrency");
        return true;
    }
}

// TransactionProcessor.java
public class TransactionProcessor {
    private PaymentStrategy strategy;
    
    public TransactionProcessor(PaymentStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }
    
    public boolean executeTransaction(double amount) {
        if (strategy == null) {
            System.out.println("No payment strategy set");
            return false;
        }
        return strategy.processPayment(amount);
    }
}

// CheckoutSystem.java
public class CheckoutSystem {
    public static void main(String[] args) {
        TransactionProcessor processor = new TransactionProcessor(new CreditCardStrategy());
        
        System.out.println("=== Checkout Transactions ===");
        processor.executeTransaction(150.50);
        
        processor.setPaymentStrategy(new PayPalStrategy());
        processor.executeTransaction(89.99);
        
        processor.setPaymentStrategy(new CryptoStrategy());
        processor.executeTransaction(299.75);
        
        System.out.println("\n=== Dynamic Strategy Switching ===");
        processor.setPaymentStrategy(new CreditCardStrategy());
        processor.executeTransaction(45.00);
        
        processor.setPaymentStrategy(new CryptoStrategy());
        processor.executeTransaction(1000.00);
    }
}