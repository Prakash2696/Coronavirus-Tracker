package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.LocationStates;

public interface CoronaVirusDataServicesRepository
{

	Optional<LocationStates> findById(int countryid);

	LocationStates findBycountry(int countryName);

	List<LocationStates> findcountryBylatestTotalDeaths(int count);

}
