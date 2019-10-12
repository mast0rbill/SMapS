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
    for(let i = 0; i < steps.Count; ++i) {
        distArr[i] = steps[i].distance.text;
        instructArr[i] = steps[i].html_instructions
            .replace("</b>","").replace("<b>","");
        console.log("In " + distArr[i] + ", " + instructArr[i] + '\n');
    }
})
.catch((err) => {
    console.log(err);
});