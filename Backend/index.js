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

function formatDirectionOutput(steps) {
    let resp = '';
    let distArr = [];
    let instructArr = [];

    let len = steps.length;

    for (let i = 0; i < len; ++i) {
        distArr[i] = steps[i].distance.text;
        instructArr[i] = steps[i].html_instructions;

        for (let j = 0; j < instructArr[i].length; ++j) {
            if (instructArr[i].charAt(j) === '<') {
                instructArr[i] = instructArr[i].substring(0, j) + instructArr[i].substring(j + 1);

                while (instructArr[i].charAt(j) !== '>') {
                    instructArr[i] = instructArr[i].substring(0, j) + instructArr[i].substring(j + 1);
                }

                instructArr[i] = instructArr[i].substring(0, j) + instructArr[i].substring(j + 1);
                j--;
            }
        }

        // Append to return
        if (i === 0)
            resp = instructArr[i] + '`';
        else if (i === len - 1) {
            resp = resp.concat('In ', distArr[i - 1], ', ', instructArr[i].substring(0, 1).toLowerCase(), instructArr[i].substring(1), '`');
            resp = resp.concat('In ', distArr[i], ', you will arrive at your destination.`');
        }
        else
            resp = resp.concat('In ', distArr[i - 1], ', ', instructArr[i].substring(0, 1).toLowerCase(), instructArr[i].substring(1), '`');
    }

    return resp;
}

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
    let split = req.body.Body.split('/');
    let latitude = split[0];
    let longitude = split[1];
    let input = split[2];

    console.log('Receiving request. ' + req.body.Body);

    // Places search
    googleMaps.placesNearby({
        radius: 2000,
        location: latitude.concat(',', longitude),
        keyword: input,
    }).asPromise().then((resp) => {
        if (resp.json.results.length === 0) {
            const textMsg = new MessagingResponse();
            textMsg.message('We could not find ' + input + ' in your area.');

            res
                .status(200)
                .type('text/xml')
                .end(textMsg.toString());
        } else {
            let address = resp.json.results[0].vicinity;
            console.log('Received response from Places API: ' + address);

            // retrieve directions
            googleMaps.directions({
                origin: latitude.concat(',', longitude),
                destination: address,
                mode: 'walking',
            }).asPromise().then((mapResp) => {
                console.log('Received response from Directions API.');

                // Generate full message
                let fullMsg = formatDirectionOutput(mapResp.json.routes[0].legs[0].steps);
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
        }
    }).catch((err) => {
        console.log(err);
    });
};