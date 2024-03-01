document.addEventListener("DOMContentLoaded", function() {
    const resultWrapper = document.getElementById('result-wrapper');
    const cityResult = document.getElementById('city-result');
    const cityField = document.getElementById('city');
    const favouriteCityListItems = document.getElementById('favourite-cities');
    const saveButton = document.getElementById('save-btn');
    const alertText = document.getElementById('alert-text');

    // get favourite city options
    getSavedCityOptions();

    if (cityResult.innerHTML == '') {
        resultWrapper.style.display = 'none';
    } else {
        saveButton.addEventListener("click", function() {
            // Retrieve the JSON string from local storage
            try {
                let storedCitiesJson = localStorage.getItem("cities");
                let citiesJsonObj = JSON.parse(storedCitiesJson);
                // console.log('existing: '+citiesJsonObj);
                if (Object.keys(citiesJsonObj).length !== 0 && citiesJsonObj !== null && citiesJsonObj.constructor !== Object) {
                    // check if city is already saved
                    let cityExist = 0;
                    for (let i = 0; i < citiesJsonObj.length; i++) {
                        if (citiesJsonObj[i] == cityResult.innerHTML) {
                            alertText.style.display = 'block';
                            alertText.innerHTML = cityResult.innerHTML + ' already exist.';
                            alertText.className = 'alert warning';
                            cityExist = 1;
                        }
                    }
                    
                    if (cityExist == 0) {
                        citiesJsonObj.push(cityResult.innerHTML);
                        // // Convert the array to a JSON string
                        let citiesJson = JSON.stringify(citiesJsonObj);
                        // Save the JSON string to local storage
                        localStorage.setItem("cities", citiesJson);
                        alertText.innerHTML = cityResult.innerHTML + ' is saved successfully.';
                        alertText.style.display = 'block';
                    }                    
                }
            } catch (error) {
                let newArray = [];
                newArray.push(cityResult.innerHTML);
                localStorage.setItem("cities", JSON.stringify(newArray));
                alertText.innerHTML = cityResult.innerHTML + ' is saved successfully.';
                alertText.style.display = 'block';
            }
        });
    }

    // check if a favourite city is selected
    // favouriteCityListItems.addEventListener("change", function(event) {
    //     if (favouriteCityListItems.value !== 'Select city') {
    //         // assign city field value
    //         cityField.value = event.target.value;
    //     } else {
    //         cityField.value = '';
    //     }
    // });
});

function getSavedCityOptions() {
    // Retrieve the JSON string from local storage
    try {
        let storedCitiesJson = localStorage.getItem("cities");
        console.log(storedCitiesJson);
        let citiesJsonObj = JSON.parse(storedCitiesJson);
        let favouriteCityListItems = document.getElementById('favourite-cities');
        let cityOptions = '';
        if (Object.keys(citiesJsonObj).length !== 0 && citiesJsonObj !== null && citiesJsonObj.constructor !== Object) {
            for (let i = 0; i < citiesJsonObj.length; i++) {
                cityOptions += '<p class="favourite-city-items" onclick="getFavouriteWeather('+"'"+ citiesJsonObj[i] +"'"+')">'+ citiesJsonObj[i] +'</p>';
            }
            favouriteCityListItems.innerHTML = cityOptions;
        }
    } catch (error) {
        document.getElementById('favourite-cities-wrapper').style.display = 'none';
    }
}

function getFavouriteWeather(city) {
    console.log(city);
    document.getElementById('city').value = city;
    document.getElementById('weather-submit-btn').click();
}