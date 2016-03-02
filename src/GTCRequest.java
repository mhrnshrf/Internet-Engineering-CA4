import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.net.httpserver.*;
import ir.ramtung.coolserver.*;

class GTCRequest extends Request{	
	public GTCRequest(String symbolName, int quantity, int price, int customerID, String type){
		this.s = Symbol.getSymbol(symbolName);
		this.quantity = quantity;
		this.price = price;
		this.customerID = customerID;
		this.type = type;
	}
	public Request copyRequest(){
		return new GTCRequest(s.getName(), quantity, price, customerID, type);
	}
	public void updatePrice(){}
	public void addToList(){
		if (type.equals("sell")) {
			int i;
			for (i = 0; i < s.sell.size() ; i++) {
				if(price < s.sell.get(i).getPrice())
					break;
				
			}
			s.sell.add(i,this);
		}
		else{
			int i;
			for (i = 0; i < s.buy.size() ; i++) {
				if(price > s.buy.get(i).getPrice())
					break;
				
			}

			s.buy.add(i,this);
		}

	}
	public String checkOrder(){
		if (s.buy.size() == 0 || s.sell.size() == 0)
			return "Order is queued";
		return "";
	}

}
