import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.net.httpserver.*;
import ir.ramtung.coolserver.*;


abstract class Request{
	protected Symbol s;
	protected int quantity;
	protected int price;
	protected int customerID;
	protected String type;

	// concept of "type" is changed!!!!!!!!!!!!!!!!!!!!!

	public abstract Request copyRequest();
	public int getPrice(){return price;}
	public int getQuant(){return quantity;}
	public int getID(){return customerID;}
	public String getType(){return type;}
	public String getSymbol(){return s.getName();}
	public void setQuant(int q){quantity = q;}
	public void setPrice(int p){price = p;}
	public abstract void updatePrice();
	public abstract void addToList();
	public abstract String checkOrder();
}

