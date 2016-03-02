import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.net.httpserver.*;
import ir.ramtung.coolserver.*;

class IOCRequest extends Request{
	public IOCRequest(String symbolName, int quantity, int price, int customerID, String type){
		this.s = Symbol.getSymbol(symbolName);
		this.quantity = quantity;
		this.price = price;
		this.customerID = customerID;
		this.type = type;
	}
	public Request copyRequest(){
		return new IOCRequest(s.getName(), quantity, price, customerID, type);
	}
	public void updatePrice(){}
	public void addToList(){
		if (type.equals("sell")) 
			s.sell.add(0,this);
		else
			s.buy.add(0,this);

	}
	public String checkOrder(){
		if (type.equals("sell")) {
			if(s.buy.size()==0){
				Customer.getCustomer(getID()).addRejected(this);
				s.sell.remove(0);
				return "Order is declined";
			}
			int sumStock = 0;
			for (int i =0; i < s.buy.size() && s.buy.get(i).getPrice() >= getPrice(); i++){ 
				Customer c = Customer.getCustomer(s.buy.get(i).getID());
				if (c.getCash() < s.buy.get(i).getPrice() *s.buy.get(i).getQuant()) {
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
		}
		else{
			if (s.sell.size() == 0) {
				Customer.getCustomer(getID()).addRejected(this);
				s.buy.remove(0);
				return "Order is declined";			
			}
			int sumStock = 0;
			int sumPrice = 0;
			for (int i = 0; i < s.sell.size() && s.sell.get(i).getPrice() <= getPrice(); i++){ 
				int oldSum = sumStock;
				sumStock += s.sell.get(i).getQuant();
				if (sumStock >= getQuant()) {
					sumPrice += (getQuant() - oldSum)*getPrice();
					break;
				}
				sumPrice += s.sell.get(i).getQuant()*getPrice();
			}
			if (sumStock < getQuant() || sumPrice > Customer.getCustomer(getID()).getCash()) {
				Customer.getCustomer(getID()).addRejected(this);
				s.buy.remove(0);
				return "Order is declined";
			}

		}
		return "";
	}
}
