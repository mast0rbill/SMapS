'use strict';

const googleMaps = require('@google/maps').createClient({
    key: 'AIzaSyC6uGbj_Wog00v0xUV_UNxwZD-HKmyZgWo',
    Promise: Promise
});

const twilio = require('twilio');
const config = require('./config.json');

const MessagingResponse = twilio.twiml.MessagingResponse;

const projectId = process.env.GCLOUD_PROJECT;
const region = 'us-central1';

exports.reply = (req, res) => {
    // Validation for Twilio
    let isValid = true;
    if (process.env.NODE_ENV === 'production') {
        isValid = twilio.validateExpressRequest(req, config.TWILIO_AUTH_TOKEN, {
            url: `https://${region}-${projectId}.cloudfunctions.net/reply`
        });
    }
    if (!isValid) {
        res
            .type('text/plain')
            .status(403)
            .send('Twilio Request Validation Failed.')
            .end();
        return;
    }

    // Parse input
    let inputSplit = req.body.Body.split('/');
    let latitude = inputSplit[0];
    let longitude = inputSplit[1];
    let address = inputSplit[2];

    console.log('Receiving request. latitude='.concat(latitude, ' longitude=', longitude, 'address=', address));

    // retrieve directions
    googleMaps.directions({
        origin: latitude.concat(',', longitude),
        destination: address,
        mode: 'walking',
    }).asPromise().then((mapResp) => {
        console.log('Received response from Directions API!');

        // Generate full message
        let fullMsg = '';
        let len = mapResp.json.routes[0].legs[0].steps.length;

        for (let i = 0; i < len; i++) {
            fullMsg.concat(mapResp.json.routes[0].legs[0].steps[i].html_instructions);
            if(i != len - 1) {
                fullMsg.concat(item, '|');
            } else {
                //fullMsg.concat(item, '!');
            }
        }

        console.log('Sending response: '.concat(fullMsg));

        const textMsg = new MessagingResponse();
        textMsg.message(fullMsg);

        res
            .status(200)
            .type('text/xml')
            .end(textMsg.toString());
    }).catch((err) => {
        console.log(err);
    });
};