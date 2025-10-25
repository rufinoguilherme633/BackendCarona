const mongoose = require("mongoose");


 const  connectMongo= ()=> {
    mongoose.connect('mongodb://localhost/backendfatecnode')
    .then(()=>{
        console.log("ok conectado")       
    }).catch((err)=>{
        console.log("não foi possivel a conexao com mongo" + err);
        process.exit(1);
    });
}


module.exports = connectMongo



