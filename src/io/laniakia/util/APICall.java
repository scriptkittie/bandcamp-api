package io.laniakia.util;

import java.util.ArrayList;
import java.util.List;

public class APICall 
{
	private String baseURL;
	private String name;
	private List<String> parameters = new ArrayList<String>();
	
	public APICall(String name) 
	{
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getBaseURL() {
		return baseURL;
	}
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
	public List<String> getParameters() {
		return parameters;
	}
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}
	
	public void addParameter(String name)
	{
		parameters.add(name);
	}

	public boolean HasParameters() 
	{
		return parameters != null && !parameters.isEmpty();
	}
}
