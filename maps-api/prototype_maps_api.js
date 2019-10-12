// https://maps.googleapis.com/maps/api/directions/json?origin=CN%20Tower&destination=Waterloo&units=metric&key=AIzaSyC6uGbj_Wog00v0xUV_UNxwZD-HKmyZgWo&mode=walking

// $ npm install @google/maps

const googleMaps = require('@google/maps').createClient({
    key: 'AIzaSyC6uGbj_Wog00v0xUV_UNxwZD-HKmyZgWo',
    Promise: Promise
});

googleMaps.directions({
    origin: 'CN Tower',
    destination: 'Eaton Center',
    mode: 'walking',
})
.asPromise()
.then((response) => {
    const steps = response.json.routes[0].legs[0].steps;
    let distArr = [];
    let instructArr = [];
    for(let i = 0; i < steps.length; ++i) {
        distArr[i] = steps[i].distance.text;
        instructArr[i] = steps[i].html_instructions;
        for(let j = 0; j < instructArr[i].length; ++j) {
            if(instructArr[i].charAt(j) == '<') {
                instructArr[i] = instructArr[i].substring(0,j) + instructArr[i].substring(j+1);
                while(instructArr[i].charAt(j) != '>') {
                    instructArr[i] = instructArr[i].substring(0,j) + instructArr[i].substring(j+1);
                }
                instructArr[i] = instructArr[i].substring(0,j) + instructArr[i].substring(j+1);
                j--;
            }
        }
        
        //instructArr[i].replace('Destination', ' Destination');
        const ind = instructArr[i].indexOf('Destination');
        if(ind > 0)
            instructArr[i] = instructArr[i].substring(0, ind) + ' ' + instructArr[i].substring(ind);
        console.log("In " + distArr[i] + ", " + instructArr[i] + '\n');
    }
})
.catch((err) => {
    console.log(err);
});