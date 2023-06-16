package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.LocationStates;
import com.example.demo.services.CoronaVirusDataServices;
import com.example.demo.services.CoronaVirusDataServicesRepository;


@Controller
public class HomeController 
{
	  @Autowired
	CoronaVirusDataServices crnService;
	
   
	CoronaVirusDataServicesRepository repository;
	
	
	public void setCrnService(CoronaVirusDataServices crnService) {
		this.crnService = crnService;
	}


	@GetMapping("/")
	public String home(Model model)
	{
		List<LocationStates> allstates=crnService.getAllstates();
		int totalDeathsReported=allstates.stream().mapToInt(stat->stat.getLatestTotalDeaths()).sum();
		model.addAttribute("LocationStates",allstates);
		model.addAttribute("totalDeathsReported",totalDeathsReported);
		return "home";
	}
	
	@RequestMapping(path="/collectChartData",produces = ("application/json"))
	@ResponseBody
	public List<LocationStates> collectChartData(Model model)
	{ 
		System.out.println("Here view Chart Data...");
		List<LocationStates> allstates=crnService.getAllstates();
		int totalDeathsReported=allstates.stream().mapToInt(stat->stat.getLatestTotalDeaths()).sum();
		model.addAttribute("LocationStates",allstates);
		model.addAttribute("totaldeathsReported",totalDeathsReported);
		return allstates;
	}
	
	@RequestMapping(path="/collectChartData/{id}",produces= {"application/json"})
	@ResponseBody
   public Optional<LocationStates> collectChartDataByCountryID(@PathVariable("id")int countryid, Model model)
   {
	   System.out.println("Here view chart Data by Country ID...");
	   Optional<LocationStates> locationStates=repository.findById(countryid);
	   return locationStates;
   }
	
	@RequestMapping(path="/collectChartData/{name}",produces= {"application/json"})
	@ResponseBody
   public LocationStates collectChartDataByCountryName(@PathVariable("name")int countryName, Model model)
   {
		System.out.println("Here view chart Data by Country Name...");
		LocationStates locationStates=repository.findBycountry(countryName);
		return locationStates;
		
   }
	/*@RequestMapping(part="/collectChartData/country",produces={"application/json"})
	  @ResponseBody
	  public LocationStates collectChartDataByCountryName(@RequestParam("name") String countryName,Model model){
	  System.out.println("Here view Chart Data by Country Name...");
	  LocationStates locationStates=repository.findBycountry(countryName);
	  return locationStates;
	 */
	@RequestMapping(path="/collectChartData/top={count}",produces = {"application/json"})
	public List<LocationStates> collectChartDataByCountryTop(@PathVariable("count") int count,Model model)
	{
		System.out.println("Here view Chart Data by Country Name...");
		List<LocationStates> locationStates=repository.findcountryBylatestTotalDeaths(count);
		return locationStates;
		}
	
	//@RequestMapping(path="/viewChart",produces = {"application/json"})
	@RequestMapping(value="/viewChart", method=RequestMethod.GET)
	public ModelAndView viewChart()
	{
		return new ModelAndView("ViewChart").addObject("myURL",new String("http://localhost:8082/collectChartData"));
	}
	
	@GetMapping("/viewChart/{id}")
	public String viewChartByID(@PathVariable("id")int id,Model m)
	{
		m.addAttribute("id", id);
		m.addAttribute("myURL", "http://localhost:8082/collectChartData/"+id);
		return "ViewChart";
	}
	
	@GetMapping(path="/collectChartData/country={name}")
	public String viewChartByCountryName(@RequestParam String name,Model m)
	{
		//m.addAttribute("name", name);
		m.addAttribute("myURL", "http://localhost:8082/collectChartData/country?"+name);
		return "ViewChart";
	}
}