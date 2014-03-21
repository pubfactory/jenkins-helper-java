package com.safaribooks.junitattachments.usage;

/**
 * simple example for the UsageExample class
 */
public class Sam {

	/**
	 * @return an html version of why
	 */
	public String getWhy(){
		return "I could not, would not, on a <i>boat</i>.<br>"
		+"I will not, <b>will not</b>, with a goat.<br>"
		+"I will not eat them in the rain.<br>"
		+"I will not eat them on a train.<br>"
		+"<blink>Not in the dark!</blink> Not in a tree!<br>"
		+"Not in a car! You let me be!<br>"
		+"I do not like them in a box.<br>"
		+"I do not like them with a fox.<br>"
		+"I will not eat them in a house.<br>"
		+"<h3>I do not like them with a mouse.</h3><br>"
		+"<h2>I do not like them here or there.</h2><br>"
		+"<h1>I do not like them ANYWHERE!</h1><br>";
	}

	public  boolean accept(String authentication){
		return authentication.contains("Try them!");
	}
}
