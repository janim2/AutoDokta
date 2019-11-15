const functions = require('firebase-functions');
const admin = require('firebase-admin');
const nodemailer = require('nodemailer');
const cors = require('cors')({origin: true});

admin.initializeApp (functions.config().firebase);


/**
* Here we're using Gmail to send 
*/
let transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
	address: 'smtp.gmabirdvision17@gmail.comil.com',
     	port: '587',
        user: 'papaswagger12@gmail.com',
        pass: 'codemonkey#333'
    }
});

exports.sendMail = functions.https.onRequest((req, res) => {
    cors(req, res, () => {
      
        // getting dest email by query string
        const dest = req.query.dest;

        const mailOptions = {
            from: 'Auto Dokta Team <papaswagger12@gmail.com>', // Something like: Jane Doe <janedoe@gmail.com>
            to: dest,
            subject: 'Registration Complete', // email subject
            html: `<p style="font-size: 16px;">Welcome to Auto Dokta. The best car parts sales e-shop in town.</p>
                <br />
      
            ` // email content in HTML
        };
  
       //   <img src="https://images.prod.meredith.com/product/fc8754735c8a9b4aebb786278e7265a5/1538025388228/l/rick-and-morty-pickle-rick-sticker" />
        // returning result
        return transporter.sendMail(mailOptions, (erro, info) => {
            if(erro){
                return res.send(erro.toString());
            }
            return res.send('Sended');
        });
    });    
});

//exports.sendMail = functions.https.onRequest((request, responde) => {});
