package Model;

public class Account {

    public int balance;

    public Account(int balance){
        this.balance = balance;
    }

    public int getBalance(){
        return balance;
    }

    public int setBalance(int balance){
        return this.balance = balance;
    }

    public static void pay(Account fromAccount, Account toAccount, int payAmount){

        if (fromAccount.getBalance() > payAmount) {
            System.out.println("Player has payed " + payAmount);
            fromAccount.setBalance(fromAccount.getBalance() - payAmount);
            toAccount.setBalance(toAccount.getBalance() + payAmount);
        }
        System.out.println("Insufficient funds");
    }

    public static void deposit(Account account, int amount){
        account.setBalance(account.getBalance() + amount);
    }

    public static void withdraw(Account account, int amount){
        if (account.getBalance() > amount) {
            account.setBalance(account.getBalance() - amount);
        }
        System.out.println("Insufficient funds");
    }

}
