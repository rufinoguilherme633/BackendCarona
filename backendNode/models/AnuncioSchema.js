const mongoose = require("mongoose");
const { type } = require("os");


const AnuncioSchema = mongoose.Schema({
    nome_dono:{
        type: String,
        require:true
    },
    logo:{
        type: String,
        require:true
    },
    razao_social:{
         type:String,
        require:true
    },
    nome_fantasia:{
         type:String,
        require:true
    }, 
    cnpj:{
        type:String,
        require:true
    },
    ramo_empresa:{
         type:String,
        require:true
    },
    descricao_anuncio:{
         type:String,
        require:true
    },
    endereço_empresa:{
         type:String,
        require:true
    },
    endereço_dono:{
         type:String,
        require:true
    },
    contato:{
         type:String,
        require:true
    },
    email:{
         type:String,
        require:true
    },
    senha:{
         type:String,
        require:true
    },
    anuncio:{
        type:String,
        require:true
    },
    quantidade_alcance:{
        type:Number,
        require:true
    },
    quantidade_alcancados:{
        type: Number,
        require:true
    },
})



mongoose.model("anuncio",AnuncioSchema)