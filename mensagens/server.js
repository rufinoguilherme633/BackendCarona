const express = require("express");
const db = require("./database/dbMongodb");
const jwt = require("jsonwebtoken");
const bodyParser = require("body-parser");
const MessagemControler = require("./controller/MensagensController")
const app = express();
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

const port = 9001;


app.use("/message",  MessagemControler)


app.listen(port,()=>{

  console.log("rvidor ouvindo na porta " +port)
})