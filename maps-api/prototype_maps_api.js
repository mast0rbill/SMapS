// https://maps.googleapis.com/maps/api/directions/json?origin=CN%20Tower&destination=Waterloo&units=metric&key=AIzaSyC6uGbj_Wog00v0xUV_UNxwZD-HKmyZgWo&mode=walking

// $ npm install @google/maps

const googleMapsClient = require('@google/maps').createClient({
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
    console.log(response.json.routes.legs.steps);
})
.catch((err) => {
    console.log(err);
});