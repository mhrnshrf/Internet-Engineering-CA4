import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.net.httpserver.*;
import ir.ramtung.coolserver.*;

class MPORequest extends Request{
	public MPORequest(String symbolName, int quantity, int price, int customerID, String type){
		this.s = Symbol.getSymbol(symbolName);
		this.quantity = quantity;
		this.price = price;
		this.customerID = customerID;
		this.type = type;
	}
	public Request copyRequest(){
		return new MPORequest(s.getName(), quantity, price, customerID, type);
	}
	public void updatePrice(){
		if (type.equals("sell")) 
			price = s.buy.get(0).getPrice();
		else
			price = s.sell.get(0).getPrice();
	}
	public void addToList(){
		if (type.equals("sell")) 
			s.sell.add(0,this);
		else
			s.buy.add(0,this);
	}
	public String checkOrder(){
		if (type.equals("sell")) {
			if(s.buy.size()==0){
				System.err.println("if1");
				Customer.getCustomer(getID()).addRejected(this);
				s.sell.remove(0);
				return "Order is declined";
			}
			int sumStock = 0;
			for (int i =0; i < s.buy.size(); i++){ 
				Customer c = Customer.getCustomer(s.buy.get(i).getID());
				if (c.getCash() < s.buy.get(i).getPrice() *s.buy.get(i).getQuant()) {
					Customer.getCustomer(c.getID()).addRejected(s.buy.get(i));
					s.buy.remove(i);
					i--;
					continue;
				}
				sumStock += s.buy.get(i).getQuant();
			}
			if (sumStock < getQuant()) {
				Customer.getCustomer(getID()).addRejected(this);
				s.sell.remove(0);
				return "Order is declined";
			}
			updatePrice();			
		}
		else{
			if (s.sell.size() == 0) {
				System.err.println("if2");
				Customer.getCustomer(getID()).addRejected(this);
				s.buy.remove(0);
				return "Order is declined";			
			}
			int sumStock = 0;
			int sumPrice = 0;
			for (int i =0; i < s.sell.size(); i++){ 
				int oldSum = sumStock;
				sumStock += s.sell.get(i).getQuant();
				if (sumStock >= getQuant()) {
					sumPrice += (getQuant() - oldSum)*s.sell.get(i).getPrice();
					break;
				}
				sumPrice += s.sell.get(i).getQuant()*s.sell.get(i).getPrice();
			}
			if (sumStock < getQuant() || sumPrice > Customer.getCustomer(getID()).getCash()) {
				Customer.getCustomer(getID()).addRejected(this);
				s.buy.remove(0);
				return "Order is declined";
			}
			updatePrice();
			//buy.get(0).setPrice(sell.get(0).getPrice());
		}
		return "";
	}

}
