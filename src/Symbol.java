import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.net.httpserver.*;
import ir.ramtung.coolserver.*;
import java.lang.reflect.*;

class Symbol{
	String name;
	public ArrayList<Request> buy;
	public ArrayList<Request> sell;
	public static ArrayList<Symbol> list = new ArrayList<Symbol>();
	public Symbol(String name){
		this.name = name;
		buy = new ArrayList<Request>();
		sell = new ArrayList<Request>();
	}
	public String getName(){return name;}
	public static boolean exist(String name){
		for (int i = 0; i < list.size() ; i++) {
			if(list.get(i).name.equals(name))
				return true;
			
		}
		return false;
	}
	public static Symbol getSymbol(String name){
		for (int i = 0; i < list.size() ; i++) {
			if(list.get(i).name.equals(name))
				return list.get(i);
			
		}
		return null;
	}


	public String updateList(int id, String instrument, int price, int quantity, String type, String req){
		
		Request r = null;
		Class co = ReqContainer.loadClass("./class", type);
		if (co != null) {
			try{
				System.err.println(co.getName());
				r = (Request)co.getDeclaredConstructor(String.class, int.class, int.class, int.class, String.class).newInstance(instrument, quantity, price, id, req);

			}
			catch(Exception ex){
				
			}
		}
		else
			return "invalid type";

		
		if (req.equals("sell")) {
			if(id!=1 && quantity > Customer.getCustomer(id).stocks.get(instrument))
				return "Not enough share";
			r.addToList();
		}
		if (req.equals("buy")) {
			if(quantity*price > Customer.getCustomer(id).getCash())
				return "Not enough money";
			r.addToList();
		}
		return bargain(r);
	}
	public String bargain(Request r){
		String ans = r.checkOrder();
		if (ans.length() != 0) 
			return ans;
		StringBuilder response = new StringBuilder();
		while(sell.get(0).getPrice() <= buy.get(0).getPrice()){
			System.err.println("up 1");
			int q = buy.get(0).getQuant() - sell.get(0).getQuant();
			Request b = buy.get(0).copyRequest(); 
			Request s = sell.get(0).copyRequest(); 
			int stockNum;
			System.err.println("up 2");
			int sellerID = sell.get(0).getID();
			int buyerID = buy.get(0).getID();
			int p = buy.get(0).getPrice();
			if(q < 0){

				stockNum = buy.get(0).getQuant();
				s.setQuant(buy.get(0).getQuant());
				System.err.println("up 6");
				System.err.println("up 7");
				System.err.println("up 8");
				buy.get(0).setQuant(0);
				sell.get(0).setQuant((-1)*q);
				Customer.getCustomer(buy.get(0).getID()).update(b, "buy");
				Customer.getCustomer(sell.get(0).getID()).update(s, "sell");
				buy.remove(0);
			}
			else{

				b.setQuant(sell.get(0).getQuant());
				stockNum = sell.get(0).getQuant();
				buy.get(0).setQuant(q);
				sell.get(0).setQuant(0);
				Customer.getCustomer(buy.get(0).getID()).update(b, "buy");
				Customer.getCustomer(sell.get(0).getID()).update(s, "sell");
				sell.remove(0);
				if (q == 0)
					buy.remove(0);
			}
			System.err.println("up 3");
			// ‫‪123‬‬ ‫‪sold‬‬ ‫‪40‬‬ ‫‪shares‬‬ ‫‪o‬‬ ‫‪f‬‬ ‫‪RANA1‬‬ ‫‪@130‬‬ ‫‪to‬‬ ‫‪345‬‬
			if(response.length()!=0)
				response.append("<br>");			
			response.append(sellerID);
			response.append(" sold ");
			response.append(stockNum);
			response.append(" shares of ");
			response.append(name);
			response.append(" @");
			response.append(p);
			response.append(" to ");
			response.append(buyerID);
			System.err.println(response);


			if (buy.size() == 0 || sell.size() == 0) 
				break;
			// if (buy.size() != 0 && buy.get(0).getType().equals("MPO")) 
			// 	buy.get(0).setPrice(sell.get(0).getPrice());
			// else if (buy.size() != 0 && sell.get(0).getType().equals("MPO")) 
			// 	sell.get(0).setPrice(buy.get(0).getPrice());

			buy.get(0).updatePrice();
			sell.get(0).updatePrice();
		}
		if (response.length() == 0) 
			return "Order is queued";
		return response.toString();		
	}
}
